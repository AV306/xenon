package me.av306.xenon.features.movement;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.KeyEvent;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.mixin.MouseAccessor;
import org.lwjgl.glfw.GLFW;

import net.minecraft.util.ActionResult;

public final class FullKeyboardFeature extends IToggleableFeature
{
    public FullKeyboardFeature()
    {
        super( "FullKeyboard", "fullkey", "fullkb" );

        KeyEvent.EVENT.register( this::onKey );
    }

    protected ActionResult onKey(
        long window,
        int keycode,
        int scancode,
        int action,
        int modifiers
    )
    {
        if ( !this.isEnabled ) return ActionResult.PASS;
        double f = Xenon.INSTANCE.client.options.getMouseSensitivity().getValue() * 32d;
        switch ( keycode )
        {
            case GLFW.GLFW_KEY_UP -> ((MouseAccessor) Xenon.INSTANCE.client.mouse).setCursorDeltaY( -f );
            case GLFW.GLFW_KEY_DOWN -> ((MouseAccessor) Xenon.INSTANCE.client.mouse).setCursorDeltaY( f );
            case GLFW.GLFW_KEY_LEFT -> ((MouseAccessor) Xenon.INSTANCE.client.mouse).setCursorDeltaX( -f );
            case GLFW.GLFW_KEY_RIGHT -> ((MouseAccessor) Xenon.INSTANCE.client.mouse).setCursorDeltaX( f );
            default -> {}
        }

        return ActionResult.PASS;
    }

    @Override
    protected void onEnable()
    {

    }

    @Override
    protected void onDisable()
    {

    }
}