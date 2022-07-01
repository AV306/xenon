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
        super( "CommandProcessor" );

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
        // [prefix][featurename] [config] [value]

        // Ok, should be a command,
        // now remove the prefix and split
        String[] possibleCommand = text
                .toLowerCase()
                .replace( prefixChar, ' ' )
                .trim()
                .split( " " );

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
                case "enable", "on" -> feature.enable(); // User wants to enable a feature

                case "disable", "off" -> ((IToggleableFeature) feature).disable(); // cce; user wants to disable a feature

                case "set" ->
                {
                    // User probably wants to change a config, 
                    // this is delegated to the feature.
                    String attrib = possibleCommand[2];
                    String value = possibleCommand[3]; // oobe
                    feature.parseConfigChange( attrib, value );
                }

                case "exec", "execute", "run" ->
                {
                    // pattern matching fun!
                    String action = Arrays.copyOfRange( possibleCommand, 1, possibleCommand.length );
                    this.executeAction( action );
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
        }

        return ActionResult.FAIL;
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
}

