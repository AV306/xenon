package me.av306.xenon.features;

import com.mojang.blaze3d.platform.GlStateManager;
import me.av306.xenon.Xenon;
import me.av306.xenon.command.Command;
import me.av306.xenon.config.feature.CommandProcessorGroup;
import me.av306.xenon.event.ChatOutputEvent;
import me.av306.xenon.feature.IFeature;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.util.text.TextFactory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.util.ActionResult;

import java.util.Arrays;

public class CommandProcessor extends IToggleableFeature
{
    public CommandProcessor()
    {
        super( "CommandProcessor", "cp", "cmd" );

        this.setShouldHide( true );

        ChatOutputEvent.EVENT.register( this::onChatHudAddMessage );

        this.enable();
    }

    // Fun fact: the very first version of this
    // would respond to *any* chat message that was a valid command,
    // allowing you to control someone else's client.
    // :P

    // TODO: make QuickChat and MQC messages pass through this
    private ActionResult onChatHudAddMessage( String text )
    {
        // Check if CP is enabled
        if ( !this.isEnabled )
        {
            // Should we warn the user that CP is disabled?
            if ( CommandProcessorGroup.warn )
            {
                Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocessor.disabled" );

                return ActionResult.FAIL;
            }
            else return ActionResult.PASS;
        }

        // Reject if it doesn't start with the prefix or is just the prefix character alone
        if ( !text.startsWith( CommandProcessorGroup.prefix ) &&
                !text.equalsIgnoreCase( CommandProcessorGroup.prefix )
        ) return ActionResult.PASS;

        // Possible command, process :D

        // Process command into array
        // Ok the code here is all kinds of messed up
        String[] cmd = text.replaceAll( CommandProcessorGroup.prefix, "" ).split( " " );

        String target = cmd[0]; // Target will always be removed from command
        try
        {
            cmd = Arrays.copyOfRange( cmd, 1, cmd.length ); // Remove target by copying everything after it
        }
        catch ( ArrayIndexOutOfBoundsException | IllegalArgumentException e )
        {
            // These exceptions are thrown if the command only has length 1; i.e. missing action
            cmd = new String[]{ "" };
        }

        /*
            [ Command format specification ]
            length: 1+
            elements:
            0.  Target ID
            1.  Action keyword (FC), arg (SC)
            2.  Config name (FC, if action is "set"), arg (SC / FC if action is "exec")
            3.  Config value (FC, if action is "set"), arg (SC / FC if action is "exec")
            4+. Argumants (SC / FC if action is "exec")
        */


        if ( !handleStandaloneCommand( target, cmd ) )
        {
            // Not an SC
            // Pad argument array to be at least long enough for a config change (3)
            if ( cmd.length < 3 ) cmd = Arrays.copyOfRange( cmd, 0, 3 );
            if ( !handleFeatureCommand( target, cmd ) ) // Not an FC either
                Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocesor.unresolvable", target );
        }

        return ActionResult.FAIL;
    }


    /**
     * Attempt to execute the command as a standalone command
     * @param targetId: The target ID
     * @param args: The rest of the command
     * @return Whether the command was resolved
     */
    private boolean handleStandaloneCommand( String targetId, String[] args )
    {
        try
        {
            Command target = Xenon.INSTANCE.commandRegistry.get( targetId );
            target.execute( args );
        }
        catch ( NullPointerException npe )
        {
            return false;
        }

        return true;
    }

    /**
     * Feature-command handler
     * @param targetId: Target ID
     * @param args: Command keyword + args
     * @return False ONLY if the target could not be resolved
     */
    private boolean handleFeatureCommand( String targetId, String[] cmd )
    {
        // FC handling has *many* error messages,
        // so the `return false` is only used for the "cannot resolve target" scenario
        if ( !Xenon.INSTANCE.featureRegistry.containsKey( targetId ) ) return false;

        IFeature target = Xenon.INSTANCE.featureRegistry.get( targetId );

        if ( cmd[0] == null ) Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocesor.missing.action" );
        else switch ( cmd[0] /* action keyword */ )
        {
            case "e", "enable", "on" -> target.enable();

            case "d", "disable", "off" ->
            {
                try
                { ((IToggleableFeature) target).disable(); }
                catch ( ClassCastException cce )
                { Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocesor.invalid.command.featurenottoggleable" ); }
            }

            case "set" ->
            {
                if ( cmd[1] == null || cmd[2] == null )
                {
                    Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocessor.missing.args.null" );
                    return true;
                }

                target.requestConfigChange( cmd[1], cmd[2] );
            }

            /*case "execute", "exec", "ex" ->
            {
                if ( cmd.length < 3 )
                {
                    Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocessor.missing.args", cmd.length - 1, 2 );
                    return true;
                }

                target.requestExecuteAction( Arrays.copyOfRange( cmd, 2, cmd.length ) );
            }*/

            // Java 20 only
            //case null -> Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocesor.missing.action" );

            /*default -> Xenon.INSTANCE.sendErrorMessage(
                    "text.xenon.commandprocesor.unknown", "action", cmd[0]
            );*/
            
            // Pass unknown action to feature
            default ->
            {
                if ( cmd[0] != null ) target.requestExecuteAction( cmd );
                else Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocesor.missing.action" );
            }
        }

        return true;
    }


    /**
     * A helper method to deserialise a command into an array.
     *
     * @param prefixChar: The prefix character
     * @param command: The command string to be formatted
     * @return The array of components in the command
     */
    public static String[] deserialiseCommand( char prefixChar, String command )
    {
        // FIXME: This may cause very weird errors if the prefix is a space or control character.
        return command
                .toLowerCase()
                .replace( prefixChar, ' ' ) // replace prefix with a space
                .trim() // remove leading space (from above) and trailing spaces (if any)
                .split( " " ); // split
    }

    public static String[] deserialiseCommand( String prefixStr, String command )
    {
        return deserialiseCommand( prefixStringToChar( prefixStr ), command );
    }

    /**
     * Helper method to serialise a command array into a command string.
     * @param prefixChar: The prefix character
     * @param command: The command array
     */
    public static String serialiseCommand( char prefixChar, String[] command )
    {
        String prefixString = String.valueOf( prefixChar );
        
        return serialiseCommand( prefixString, command );
    }

    public static String serialiseCommand( String prefixStr, String[] command )
    {
        StringBuilder builder = new StringBuilder()
                .append( prefixStringToChar( prefixStr ) );

        for ( String component : command )
            builder.append( component ).append( " " );

        // the builder will return a string with an extra space at the end
        return builder.toString().trim();
    }

    public static char prefixStringToChar( String prefixStr )
    {
        char prefixChar;
        try
        {
            // Grab the first character out of the string
            prefixChar = prefixStr.toCharArray()[0];
        }
        catch( ArrayIndexOutOfBoundsException oobe )
        {
            oobe.printStackTrace();
            Xenon.INSTANCE.LOGGER.warn( "Prefix silently corrected to '!'" );
            prefixChar = '!';
        }

        return prefixChar;
    }



    @Override
    protected void onEnable() {}

    @Override
    protected void onDisable() {}

    @Override
    protected boolean onRequestConfigChange( String config, String value )
    {
        boolean result = config.contains( "prefix" );
        if ( result ) CommandProcessorGroup.prefix = value;

        return result;
    }

    @Override
    protected boolean onRequestExecuteAction( String[] action )
    {
        // This is a bit useless, just for testing
        // test command: !commandprocessor exec timer set speed 2f
        this.onChatHudAddMessage(
            serialiseCommand(
                CommandProcessorGroup.prefix,
                action
            )
        );

        return true;
    }

    @Override
    public Text getHelpText( String[] args )
    {
        // example: !cp help listf
        // args[0]

        MutableText helpText = MutableText.of( TextContent.EMPTY ).append( "[CommandProcessor] " );

        if ( args.length < 1 )
        {
            // Whoops! No help arguments were passed in.
            // Change the args variable to point to a new String[]
            // with exactly one help argument
            // so that it will proceed to the default branch without an NPE.
            args = new String[]{ " " };
        }

        switch( args[0] )
        {
            case "listf", "listfeatures", "features", "lf", "aliases" ->
            {
                helpText.append( "Registered aliases:\n" )
                        .append(
                            Arrays.toString(
                                Xenon.INSTANCE.featureRegistry.keySet()
                                    .toArray( String[]::new )
                            )
                        );
            }

            case "configs", "listconfigs", "listcfg" ->
            {
                helpText.append( "Possible configs:\n" )
                        .append( "prefix - change the prefix of the command" );
            }

            default ->
            {
                // Invalid help argument,
                // list out possible help arguments
                helpText.append( "Possible help arguments:\n" )
                        .append( "listf, listfeatures, features, lf, aliases,\n" )
                        .append( "configs, listconfigs, listcfg" );
            }
        }

        helpText.formatted( Xenon.INSTANCE.MESSAGE_FORMAT );
        
        return helpText;
    }
}

