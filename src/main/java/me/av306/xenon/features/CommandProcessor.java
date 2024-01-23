package me.av306.xenon.features;

import com.mojang.blaze3d.platform.GlStateManager;
import me.av306.xenon.Xenon;
import me.av306.xenon.command.Command;
import me.av306.xenon.config.feature.CommandProcessorGroup;
import me.av306.xenon.event.ChatOutputEvent;
import me.av306.xenon.event.MinecraftClientEvents;
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
        this.setPersistent( true ); // Don't disable on world exit

        ChatOutputEvent.EVENT.register( this::onChatHudAddMessage );

        // Re-enable on entering world
        MinecraftClientEvents.JOIN_WORLD.register( world ->
        {
            if ( CommandProcessorGroup.reEnableOnWorldEnter )
                this.enable();
            return ActionResult.PASS;
        } );

        this.enable();
    }

    // Fun fact: the very first version of this
    // would respond to *any* chat message that was a valid command,
    // allowing you to control someone else's client.
    // :P

    // TODO: make QuickChat and MQC messages pass through this
    private ActionResult onChatHudAddMessage( String text )
    {
        // Check if message starts with prefix
        if ( !text.startsWith( CommandProcessorGroup.prefix ) ) return ActionResult.PASS;

        // Check if CP is enabled
        if ( !this.isEnabled )
        {
            // Should we warn the user that CP is disabled?
            if ( CommandProcessorGroup.warn )
            {
                this.sendErrorMessage( "text.xenon.commandprocessor.disabled" );

                return ActionResult.FAIL;
            }
            else return ActionResult.PASS;
        }

        // Report command
       Xenon.INSTANCE.sendInfoMessage( "text.xenon.commandprocessor.report", text );

        // Remove prefix
        String[] cmd = deserialiseCommand( CommandProcessorGroup.prefix, text );


        // Long enough?
        if ( cmd.length < 1 ) return ActionResult.PASS;

        // Command probably valid, sort its components out

        String name = cmd[0];

        String[] args = Arrays.copyOfRange( cmd, 1, cmd.length );

        if ( !this.handleStandaloneCommand( name, args ) && !this.handleFeatureCommand( name, args ) )
            this.sendErrorMessage( "text.xenon.commandprocesor.unresolvable", name );

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
        Command cmd = Xenon.INSTANCE.commandRegistry.get( targetId );
        if ( cmd == null ) return false;
        cmd.execute( args );
        return true;
    }

    /**
     * Feature-command handler
     * @param targetId: Target ID
     * @param args: Command keyword + args
     * @return False ONLY if the target could not be resolved
     */
    private boolean handleFeatureCommand( String targetId, String[] args )
    {
        IFeature feature = Xenon.INSTANCE.featureRegistry.get( targetId );
        if ( feature == null ) return false;

        if ( args.length < 1 )
        {
           this.sendErrorMessage( "text.xenon.commandprocesor.missing.action" );
            return true;
        }

        switch( args[0] )
        {
            case "e", "enable", "on" -> feature.enable();

            case "d", "disable", "off" ->
            {
                try
                {
                    IToggleableFeature itf = (IToggleableFeature) feature;
                    itf.disable();
                }
                catch ( ClassCastException cce )
                {
                    this.sendErrorMessage( "text.xenon.commandprocessor.invalid.command.featurenottoggleable" );
                }
            }

            case "set" ->
            {
                if ( args.length < 3 ) this.sendErrorMessage( "text.xenon.commandprocessor.missing.args", 3, args.length );
                else feature.requestConfigChange( args[1], args[2] );
            }

            default -> feature.requestExecuteAction( args );
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
    public Text getHelpText( String arg )
    {
        // example: !cp help listf
        // args[0]

        // 23-01-2024: I have no clue what this does, also I have no memory of making this but it seems neat
        MutableText helpText = TextFactory.createLiteral( "[CommandProcessor] " );

        switch( arg )
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

