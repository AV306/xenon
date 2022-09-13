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

        // First, check if it's a possible command
        // Pass if it isn't
        if ( !text.startsWith( CommandProcessorGroup.prefix ) )
            return ActionResult.PASS;

        // Ok, should be a command,
        // now remove the prefix and split
        String[] possibleCommand = this.deserialiseCommand(
            CommandProcessorGroup.prefix,
            text
        );

        //Xenon.INSTANCE.LOGGER.info( Arrays.toString( possibleCommand ) );
        // Tell the player what they sent
        Xenon.INSTANCE.sendInfoMessage( TextFactory.createLiteral( "> " ).append( text ) );

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
                case "enable", "on", "e" ->
                        // User wants to enable a feature
                        feature.enable();

                case "disable", "off", "d" ->
                        // User wants to disable a feature
                        // Only for ITF
                        ((IToggleableFeature) feature).disable(); // cce

                case "toggle", "t" ->
                        // User wants to toggle an ITF
                        ((IToggleableFeature) feature).toggle();

                case "exec", "execute", "ex", "run", "do" ->
                        feature.requestExecuteAction( args );


                case "set", "s" ->
                {
                    // User wants to change a config,
                    // this is delegated to the feature.
                    String attrib = possibleCommand[2];
                    String value = possibleCommand[3]; // oobe
                    feature.requestConfigChange( attrib, value );
                }

                case "help", "h", "?" ->
                {
                    // User doesn't know how to use the feature
                    // FIXME: This feels like it causes more confusion
                    // than it resolves.
                    Xenon.INSTANCE.sendInfoMessage(
                        feature.getHelpText( args )
                    );
                }

                default ->
                {
                    /*
                     * We go straight to the actual logic
                     * and skip any error messages.
                     * This is because the user themselves
                     * did not clarify what they're trying to do,
                     * so it's actually a toss-up for Xenon.
                     * I just decided to be nice and make it more convenient for users.
                     * 
                     * E.g. A hypothetical feature "f" with a config "congif"
                     * and a command "reset".
                     * 
                     * The user can change the config like so:
                     * !f congif set 2.0f OR !f congif 2.0f
                     * 
                     * The use can execute the command like so:
                     * !f exec reset OR !f reset
                     * 
                     * This reduces typing and makes it more convenient.
                     * 
                     * Xenon will try to "guess" what the user intended to do,
                     * by the advanced technique of simply trying both of them.
                     * This can cause issues where a command accepts an argument,
                     * but has the same name as a config.
                     * Generally, no one does that, so it's probably fine.
                     * We skip the error message because it might be a bit disconcerting
                     * for the user to see an "Invalid config" error
                     * when they only intended to execute a command,
                     * and vice versa.
                     *
                     * EDIT: Due to the fact that implementing the above
                     * will only transfer the headache to me
                     * and make it a million times worse,
                     * the encapsulating function (with error messages)
                     * is still used.
                     */
                    // Same here
                    feature.requestExecuteAction( args );
                    
                    /*
                     * We can be a little smarter about this
                     * and only try the config change if there is more than one argument.
                     * This avoids another "Not enough arguments" error message.
                     */
                    if ( possibleCommand.length >= 3 )
                        feature.requestConfigChange(
                            possibleCommand[1],
                            possibleCommand[2]
                        );
                }
            }
        } 
        catch ( ArrayIndexOutOfBoundsException oobe )
        {
            // Tried to access smth outside the arg array
            // probably not enough args
            Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocessor.invalidcommand.notenoughargs" );

            // don't print stacktrace here, because this is gonna happen pretty often
            Xenon.INSTANCE.LOGGER.warn( oobe );
        }
        catch ( ClassCastException cce )
        {
            // Almost definitely tried to cast a Feature to a ToggleableFeature
            Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocessor.invalidcommand.featurenottoggleable" );
            
            cce.printStackTrace(); // print stacktrace, this shouldn't happen
        }
        catch ( NullPointerException npe )
        {
            // Somewhere, someone tried to access a non-existent feature
            // aka me trying to find who asked
            Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocessor.invalidcommand.invalidfeature" );
            Xenon.INSTANCE.LOGGER.warn( npe );
        }
        catch ( Exception e )
        {
            // Oops!
            Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocessor.exception" );
            e.printStackTrace();
        }
        
        // Cancel the message event
        return ActionResult.FAIL;
    }

    /**
     * A helper method to deserialise a command into an array.
     *
     * @param prefixChar: The prefix character
     * @param command: The command string to be formatted
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

