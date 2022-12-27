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

        // TODO: Rewrite

        // Could it be a command (starts with prefix and isn't *just* the prefix)?
        if ( !text.startsWith( CommandProcessorGroup.prefix ) &&
                !text.equalsIgnoreCase( CommandProcessorGroup.prefix )
        ) return ActionResult.PASS;

        String[] cmd = text.replaceAll( CommandProcessorGroup.prefix, "" )
                .split( " " );

        // Pad command array
        if ( cmd.length < 5 ) cmd = Arrays.copyOfRange( cmd, 0, 4 );

        if ( !handleStandaloneCommand( cmd ) && ! handleFeatureCommand( cmd ) )
            Xenon.INSTANCE.sendErrorMessage( "Could not resolve target!" );



        return ActionResult.FAIL;
    }



    private boolean handleStandaloneCommand( String[] cmd )
    {

        try
        {
            Command target = Xenon.INSTANCE.commandRegistry.get( cmd[0] );
            target.execute( Arrays.copyOfRange( cmd, 1, cmd.length ) );
        }
        catch ( NullPointerException npe ) { return false; }

        return true;
    }

    private boolean handleFeatureCommand( String[] cmd )
    {
        // FC handling has *many* error messages,
        /// so the `return false` is only used for the "cannot resolve target" scenario
        if ( !Xenon.INSTANCE.featureRegistry.containsKey( cmd[0] ) ) return false;

        IFeature target = Xenon.INSTANCE.featureRegistry.get( cmd[0] );

        switch ( cmd[1] )
        {
            case "e", "enable", "on" -> target.enable();

            case "d", "disable", "off" ->
            {
                try
                { ((IToggleableFeature) target).disable(); }
                catch ( ClassCastException cce )
                { Xenon.INSTANCE.sendErrorMessage( "Non-toggleable feature!" ); }
            }

            case "set" ->
            {
                if ( cmd[2] == null || cmd[3] == null )
                {
                    Xenon.INSTANCE.sendErrorMessage( "Not enough arguments for config change!" );
                    return true;
                }

                target.requestConfigChange( cmd[2], cmd[3] );
            }

            case "execute", "exec", "ex" ->
            {
                if ( cmd.length < 3 )
                {
                    Xenon.INSTANCE.sendErrorMessage( "Not enough arguments for action execution!" );
                    return true;
                }

                target.requestExecuteAction( Arrays.copyOfRange( cmd, 2, cmd.length ) );
            }

            case null -> Xenon.INSTANCE.sendErrorMessage( "Missing action!" );

            default -> Xenon.INSTANCE.sendErrorMessage(
                    String.format( "Unknown action %s!", cmd[1] )
            );
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

        MutableText helpText = MutableText.of( TextContent.EMPTY ).append( "[CommandProcessor] ");

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

