package me.av306.xenon.features.movement;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.movement.TimerGroup;
import me.av306.xenon.event.BeginRenderTickEvent;
import me.av306.xenon.event.ScrollInHotbarEvent;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.mixin.MinecraftClientAccessor;
import me.av306.xenon.mixin.RenderTickCounterAccessor;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.MathHelper;

import java.util.Timer;

/**
 * A client-sided TPS booster to speed up basically everything.
 */
public class TimerFeature extends IToggleableFeature
{
    public TimerFeature()
    {
        super( "Timer" );

        BeginRenderTickEvent.EVENT.register( this::onBeginRenderTick );

        ScrollInHotbarEvent.EVENT.register( this::onScrollInHotbar );
    }

    private ActionResult onScrollInHotbar( double amount )
    {
        if ( this.isEnabled && Xenon.INSTANCE.modifierKey.isPressed() && this.keyBinding.isPressed() )
        {
            TimerGroup.timerSpeed += ((int) amount) * TimerGroup.adjustmentInterval;
            if ( TimerGroup.timerSpeed < 0 ) TimerGroup.timerSpeed = 0; // Negative values break everything
            return ActionResult.FAIL;
        }

        return ActionResult.PASS;
    }

    @Override
    protected void keyEvent()
    {
        super.keyEvent();
    }

    @Override
    public String getName()
    {
        // when the name is retrieved, update it and return
        this.setName( "Timer (" + TimerGroup.timerSpeed + ")" );

        return this.name;
    }

    private ActionResult onBeginRenderTick( long timeMillis )
    {
        if ( this.isEnabled )
        {
            RenderTickCounter renderTickCounter = ((MinecraftClientAccessor) Xenon.INSTANCE.client).getRenderTickCounter();

            // Get the duration of the last frame
            float lastFrameDurationLocal = ((RenderTickCounterAccessor) renderTickCounter).getLastFrameDuration();

            // Make it seem longer than it was to force Minecraft to try to run everything faster
            lastFrameDurationLocal *= TimerGroup.timerSpeed;
            ((RenderTickCounterAccessor) renderTickCounter).setLastFrameDuration( lastFrameDurationLocal );
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
    public boolean onRequestConfigChange(String config, String value )
    {
        boolean result = config.contains( "speed" );

        if ( result ) TimerGroup.timerSpeed = Float.parseFloat( value );

        return result;
    }
}
