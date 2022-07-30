package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.CommandProcessorGroup;
import me.av306.xenon.event.ChatOutputEvent;
import me.av306.xenon.feature.IFeature;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.util.text.TextFactory;
import net.minecraft.util.ActionResult;

import java.util.Arrays;

public class CommandProcessor extends IToggleableFeature
{
    public CommandProcessor()
    {
        super( "CommandProcessor", "cp", "cmd" );

        ChatOutputEvent.EVENT.register( this::onChatHudAddMessage );

        this.enable();
    }

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

        // Some bookkeeping for the prefix
        // because CompleteConfig doesn't like chars
        char prefixChar;
        try
        {
             prefixChar = CommandProcessorGroup.prefix.toCharArray()[0];
        }
        catch ( ArrayIndexOutOfBoundsException e )
        {
            Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocessor.invalidprefix" );
            prefixChar = '!';
        }

        // First, check if it's a possible command
        if ( !text.startsWith( CommandProcessorGroup.prefix ) )
            return ActionResult.PASS;

        // example command: !timer speed 2.0f
        // advanced: !commandprocessor execute timer set speed 3f
        // [prefix][featurename] [config] [value]

        // Ok, should be a command,
        // now remove the prefix and split
        String[] possibleCommand = this.deserialiseCommand( prefixChar, text );

        //Xenon.INSTANCE.LOGGER.info( Arrays.toString( possibleCommand ) );
        // Tell the player what they sent
        Xenon.INSTANCE.sendInfoMessage(
                TextFactory.createLiteral( "> " )
                        .append( text )
        );

        try
        {
            // Now we try to parse the command
            // should have length > 2
            String featureTargeted = possibleCommand[0];
            String command = possibleCommand[1]; // oobe

            IFeature feature = Xenon.INSTANCE.featureRegistry.get( featureTargeted ); // npe

            switch ( command )
            {
                case "enable", "on", "e" -> feature.enable(); // User wants to enable a feature

                case "disable", "off", "d" -> ((IToggleableFeature) feature).disable(); // cce; user wants to disable a feature

                case "exec", "execute", "ex", "run" ->
                {
                    // pattern matching fun!
                    // copy over the components after the "exec"
                    // e.g. [commandprocessor, exec, timer, enable] -> [timer, enable]
                    String[] action = Arrays.copyOfRange( possibleCommand, 2, possibleCommand.length );
                    this.executeAction( action );
                }

                case "set" ->
                {
                    // User wants to change a config,
                    // this is delegated to the feature.
                    String attrib = possibleCommand[2];
                    String value = possibleCommand[3]; // oobe
                    feature.parseConfigChange( attrib, value );
                }

                default ->
                {
                    // User probably wants to change a config
                    String attrib = possibleCommand[1];
                    String value = possibleCommand[2]; // oobe
                    feature.parseConfigChange( attrib, value );
                }
            }
        }
        catch ( ArrayIndexOutOfBoundsException oobe )
        {
            Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocessor.invalidcommand.notenoughargs" );
        }
        catch ( ClassCastException cce )
        {
            Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocessor.invalidcommand.featurenottoggleable" );
        }
        catch ( NullPointerException npe )
        {
            Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocessor.invalidcommand.invalidfeature" );
            npe.printStackTrace(); // debug
        }
        catch ( Exception e )
        {
            Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocessor.exception" );
        }

        return ActionResult.FAIL;
    }

    private String[] deserialiseCommand( char prefixChar, String command )
    {
        return command
                .toLowerCase()
                .replace( prefixChar, ' ' ) // remove prefix
                .trim() // remove leading and trailing spaces
                .split( " " ); // split
    }

    public String serialiseCommand( char prefixChar, String[] command )
    {
        String prefixString = String.valueOf( prefixChar );
        
        return serialiseCommand( prefixString, command );
    }

    public String serialiseCommand( String prefixStr, String[] command )
    {
        StringBuilder builder = new StringBuilder( prefixStr );

        for ( String component : command )
            builder.append( component ).append( " " );

        // the builder wll return a string with an extra space at the end
        return builder.toString().trim();
    }

    @Override
    protected void onEnable() {}

    @Override
    protected void onDisable() {}

    @Override
    protected boolean onConfigChange( String config, String value )
    {
        boolean result = config.contains( "prefix" );
        if ( result ) CommandProcessorGroup.prefix = value;

        return result;
    }

    @Override
    protected boolean onRequestExecuteAction( String[] action )
    {
        // test command: !commandprocessor exec timer set speed 2f
        this.onChatHudAddMessage(
            this.serialiseCommand(
                CommandProcessorGroup.prefix,
                action
            )
        );

        return true;
    }
}

