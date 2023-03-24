package me.av306.xenon.features.movement;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.FullKeyboardGroup;
import me.av306.xenon.event.KeyEvent;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.mixin.MouseAccessor;
import me.av306.xenon.mixinterface.IMouse;
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
        
        double fac = Xenon.INSTANCE.client.options.getMouseSensitivity().getValue() * FullKeyboardGroup.sensitivity;

        // The cursor will be unlocked when in a GUI or chat, so when this happens
        // we need to modify the cursor position directly
        boolean mouseLocked = Xenon.INSTANCE.client.mouse.isCursorLocked();

        if ( mouseLocked )
        {
            // Gameplay; use deltas
            this.modifyMouseDelta( keycode, fac, FullKeyboardGroup.acceleration );
        }
        else
        {
            this.modifyMousePos( keycode, fac );
        }

        return ActionResult.PASS;
    }

    private void modifyMouseDelta( int keycode, double f, boolean accelerate )
    {
        if ( !accelerate )
        {
            // Linear movement
            switch ( keycode )
            {
                case GLFW.GLFW_KEY_UP -> ((MouseAccessor) Xenon.INSTANCE.client.mouse).setCursorDeltaY( -f );
                case GLFW.GLFW_KEY_DOWN -> ((MouseAccessor) Xenon.INSTANCE.client.mouse).setCursorDeltaY( f );
                case GLFW.GLFW_KEY_LEFT -> ((MouseAccessor) Xenon.INSTANCE.client.mouse).setCursorDeltaX( -f );
                case GLFW.GLFW_KEY_RIGHT -> ((MouseAccessor) Xenon.INSTANCE.client.mouse).setCursorDeltaX( f );
                default -> {}
            }
        }
        else
        {
            switch ( keycode )
            {
                case GLFW.GLFW_KEY_UP -> ((IMouse) Xenon.INSTANCE.client.mouse).accelerateDeltaY( -f );
                case GLFW.GLFW_KEY_DOWN -> ((IMouse) Xenon.INSTANCE.client.mouse).accelerateDeltaY( f );
                case GLFW.GLFW_KEY_LEFT -> ((IMouse) Xenon.INSTANCE.client.mouse).accelerateDeltaX( -f );
                case GLFW.GLFW_KEY_RIGHT -> ((IMouse) Xenon.INSTANCE.client.mouse).accelerateDeltaX( f );
                default -> {}
            }
        }
    }

    private void modifyMousePos( int keycode, double f )
    {
        // Linear movement
        switch ( keycode )
        {
            case GLFW.GLFW_KEY_UP -> ((IMouse) Xenon.INSTANCE.client.mouse).changeY( -f );
            case GLFW.GLFW_KEY_DOWN -> ((IMouse) Xenon.INSTANCE.client.mouse).changeY( f );
            case GLFW.GLFW_KEY_LEFT -> ((IMouse) Xenon.INSTANCE.client.mouse).changeX( -f );
            case GLFW.GLFW_KEY_RIGHT -> ((IMouse) Xenon.INSTANCE.client.mouse).changeX( f );
            default -> {}
        }
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