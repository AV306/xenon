package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.TimerGroup;
import me.av306.xenon.event.BeginRenderTickEvent;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.mixin.MinecraftClientAccessor;
import me.av306.xenon.mixin.RenderTickCounterAccessor;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;

/**
 * Our first "grey" feature!
 *
 * A client-sided TPS booster to speed up basically everything.
 */
public class TimerFeature extends IToggleableFeature
{
    public TimerFeature()
    {
        super( "Timer" );

        BeginRenderTickEvent.EVENT.register( this::onBeginRenderTick );
    }

    private ActionResult onBeginRenderTick( long timeMillis )
    {
        if ( this.isEnabled )
        {
            // ducktyping!!!
            RenderTickCounter renderTickCounter = ((MinecraftClientAccessor) Xenon.INSTANCE.client).getRenderTickCounter();

            float lastFrameDurationLocal = ((RenderTickCounterAccessor) renderTickCounter).getLastFrameDuration();

            lastFrameDurationLocal *= TimerGroup.timerSpeed;
            ((RenderTickCounterAccessor) renderTickCounter).setLastFrameDuration( lastFrameDurationLocal );

            this.setName( "Timer (" + TimerGroup.timerSpeed + ")" );
        }

        return ActionResult.PASS;
    }

    @Override
    protected void onEnable()
    {
        this.setName( "Timer (" + TimerGroup.timerSpeed + ")" );
    }

    @Override
    protected void onDisable()
    {

    }

    @Override
    public void parseConfigChange( String config, String value )
    {
        //Xenon.INSTANCE.LOGGER.info( config + " " + value );

        if ( !config.contains( "speed" ) )
        {
            Xenon.INSTANCE.sendErrorMessage(
                    new TranslatableText(
                            "text.xenon.commandprocessor.configchange.invalidconfig",
                            this.name,
                            config,
                            value
                    )
            );

            return;
        }

        TimerGroup.timerSpeed = Float.parseFloat( value );

        Xenon.INSTANCE.sendInfoMessage(
                new TranslatableText(
                        "text.xenon.commandprocessor.configchange.success",
                        this.name,
                        config,
                        value
                )
        );
    }
}
