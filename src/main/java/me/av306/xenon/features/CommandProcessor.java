package me.av306.xenon.features;

import me.av306.xenon.Xenon;
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

        this.hide = true;

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
            String keyword = possibleCommand[1]; // oobe

            // Cooy over the args after the keyword
            // e.g. [cp, exec, timer, enable] -> [timer, enable]
            String[] args = Arrays.copyOfRange( possibleCommand, 2, possibleCommand.length );

            IFeature feature = Xenon.INSTANCE.featureRegistry.get( featureTargeted ); // npe

            switch ( keyword )
            {
                case "enable", "on", "e" -> feature.enable(); // User wants to enable a feature

                case "disable", "off", "d" -> ((IToggleableFeature) feature).disable(); // cce; user wants to disable a feature

                case "exec", "execute", "ex", "run", "do" ->
                {
                    // pattern matching fun!
                    // copy over the components after the "exec"
                    // e.g. [commandprocessor, exec, timer, enable] -> [timer, enable]
                    feature.executeAction( args );
                }

                case "set", "s" ->
                {
                    // User wants to change a config,
                    // this is delegated to the feature.
                    String attrib = possibleCommand[2];
                    String value = possibleCommand[3]; // oobe
                    feature.parseConfigChange( attrib, value );
                }

                case "help", "h", "?" ->
                {
                    // user doesn't know how to use this
                    Xenon.INSTANCE.sendInfoMessage(
                        feature.getHelpText( args )
                    );
                }

                case "toggle", "t" ->
                {
                    ((IToggleableFeature) feature).toggle();
                }

                default ->
                {
                    // User probably wants to change a config
                    // or list the arguments
                    if ( possibleCommand.length == 1 )
                    {
                        // no arguments at all, list possible keywords
                        Xenon.INSTANCE.client.sendInfoMessage(
                            "text.xenon.commandprocessor.possiblekeywords"
                        );
                    }
                    else
                    {
                        String attrib = possibleCommand[1];
                        String value = possibleCommand[2]; // oobe
                        feature.parseConfigChange( attrib, value );
                    }
                }
            }
        } 
        catch ( ArrayIndexOutOfBoundsException oobe )
        {
            // Tried to access smth outside the arg array
            // probably not enough args
            Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocessor.invalidcommand.notenoughargs" );
        }
        catch ( ClassCastException cce )
        {
            // Almost definitely tried to cast a Feature to a ToggleableFeature
            Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocessor.invalidcommand.featurenottoggleable" );
        }
        catch ( NullPointerException npe )
        {
            // Somewhere, someone tried to access a non-existent feature
            // aka me trying to find who asked
            Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocessor.invalidcommand.invalidfeature" );
        }
        catch ( Exception e )
        {
            // Oops!
            Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocessor.exception" );
        }
        
        // Cancel the message event.
        return ActionResult.FAIL;
    }

    /**
     * A helper method to deserialise a command into an array.
     *
     * @param prexixChar: The prefix character
     * @param command: The command string to be formatted
     */
    private String[] deserialiseCommand( char prefixChar, String command )
    {
        return command
                .toLowerCase()
                .replace( prefixChar, ' ' ) // remove prefix
                .trim() // remove leading and trailing spaces
                .split( " " ); // split
    }

    /**
     * Helper method to serialise a command array into a command string.
     * @param prefixChar: The prefix character
     * @param command: The command array
     */
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

        // the builder will return a string with an extra space at the end
        return builder.toString().trim();
    }

    @Override
    protected void onEnable() {}

    @Override
    protected void onDisable() {}

    @Override
    protected boolean onRequestConfigChange(String config, String value )
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

    @Override
    public Text getHelpText( String[] args )
    {
        // example: !cp help listf
        // args[0]

        MutableText helpText = MutableText.of( TextContent.EMPTY ).append( "[CommandProcessor] ");
        switch( args[0] )
        {
            case "listf", "listfeatures", "features", "lf" ->
            {
                helpText.append( "Registered aliases:\n" )
                        .append(
                            Arrays.toString(
                                Xenon.INSTANCE.featureRegistry.keySet()
                                    .toArray( new String[]{} )
                            )
                        );
            }

            case "configs", "listconfigs" ->
            {
                helpText.append( "Possible configs:\n" )
                        .append(
                            "prefix - change the prefix of the command"
                        );
            }
        }

        helpText.formatted( Xenon.INSTANCE.MESSAGE_FORMAT );
        
        return helpText;
    }
}

