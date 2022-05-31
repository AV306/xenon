package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.CommandProcessorGroup;
import me.av306.xenon.event.ChatOutputEvent;
import me.av306.xenon.feature.IFeature;
import me.av306.xenon.feature.IToggleableFeature;
import net.minecraft.util.ActionResult;

public class CommandProcessorFeature extends IToggleableFeature
{
    public CommandProcessorFeature()
    {
        super( "CommandProcessor" );

        ChatOutputEvent.EVENT.register( this::onChatHudAddMessage );
    }

    private ActionResult onChatHudAddMessage( String text )
    {
        if ( !this.isEnabled ) return ActionResult.PASS;

        //Xenon.INSTANCE.sendInfoMessage( Xenon.INSTANCE.featureNames.toString() );
        //Xenon.INSTANCE.LOGGER.info( "message sent" );

        // example command: !timer speed 2.0f
        // [prefix][featurename] [config] [value]

        String textSent = text.toLowerCase().trim();
        String prefixString = String.valueOf( CommandProcessorGroup.prefix );

        //Xenon.INSTANCE.LOGGER.info( textSent );

        // is it a command?
        if ( !textSent.startsWith( prefixString ) )
            //Xenon.INSTANCE.LOGGER.info( "no prefix" );
            return ActionResult.PASS;

        // should be a command,
        // now remove the prefix
        String[] possibleCommand = textSent
                .replace( CommandProcessorGroup.prefix, ' ' )
                .strip()
                .split( " " );

        // is it long enough to at least toggle a feature?
        if ( possibleCommand.length < 2 )
            return ActionResult.PASS;

        // is it targeting a valid feature?
        String featureName = possibleCommand[0];
        if ( !Xenon.INSTANCE.featureRegistry.containsKey( featureName ) )
            //Xenon.INSTANCE.LOGGER.info( "invalid feature" );
            return ActionResult.PASS;

        // is it enabling or disabling the feature? (special)
        IFeature feature = Xenon.INSTANCE.featureRegistry
                .get( featureName );
        String config = possibleCommand[1];

        switch ( config )
        {
            case "enable" -> feature.enable();

            case "disable" ->
            {
                try
                {
                    ((IToggleableFeature) feature).disable();
                }
                catch ( ClassCastException e )
                {
                    Xenon.INSTANCE.sendErrorMessage( "text.xenon.commandprocessor.classcastexception" );
                }
            }

            default ->
            {
                // is it long enough?
                if ( possibleCommand.length != 3 )
                    return ActionResult.PASS;

                // is it targeting a valid config?
                // Delegated to the feature.
                String value = possibleCommand[2];

                feature.parseConfigChange( config, value );
            }
        }

        return ActionResult.FAIL;
    }

    @Override
    protected void onEnable() {}

    @Override
    protected void onDisable() {}

    @Override
    public void parseConfigChange(String config, String value) {}
}

