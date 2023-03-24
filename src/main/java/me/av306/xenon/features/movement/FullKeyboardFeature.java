package me.av306.xenon.features.movement;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.FullKeyboardGroup;
import me.av306.xenon.event.KeyEvent;
import me.av306.xenon.event.MouseEvents;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.mixin.MouseAccessor;
import me.av306.xenon.mixinterface.IMouse;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import org.lwjgl.glfw.GLFW;

import net.minecraft.util.ActionResult;
import org.lwjgl.opengl.GL;

import java.awt.*;
import java.awt.event.InputEvent;

public final class FullKeyboardFeature extends IToggleableFeature
{
    private final KeyBinding virtualLeftMouseKey;
    private final KeyBinding virtualRightMouseKey;

    private final Robot robot;
    public FullKeyboardFeature()
    {
        super( "FullKeyboard", "fullkey", "fullkb" );

        KeyEvent.EVENT.register( this::onKey );

        // Register extra keys
        this.virtualLeftMouseKey = new KeyBinding(
            "key.xenon.fullkeyboard.virtualLeftMouseKey",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.xenon." + this.category
        );
        KeyBindingHelper.registerKeyBinding( this.virtualLeftMouseKey );

        this.virtualRightMouseKey = new KeyBinding(
                "key.xenon.fullkeyboard.virtualRightMouseKey",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "category.xenon." + this.category
        );
        KeyBindingHelper.registerKeyBinding( this.virtualRightMouseKey );

        Robot robot1;
        try
        {
            robot1 = new Robot();
        }
        catch ( AWTException e )
        {
            Xenon.INSTANCE.LOGGER.warn( "Could not create AWT Robot!" );
            robot1 = null;
        }
        this.robot = robot1;
    }

    private ActionResult onKey(
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

        // Movement stuff
        if ( mouseLocked )
            // Gameplay; use deltas
            this.modifyMouseDelta( keycode, fac, FullKeyboardGroup.acceleration );
        else
            // This works, but doesn't update the actual cursor position
            this.modifyMousePos( keycode, fac );

        if ( this.robot == null ) return ActionResult.PASS; // Could not create Robot, don't try to use it

        // Mouse stuff
        if ( keycode == GLFW.GLFW_KEY_COMMA )
            // Simulate left mouse
            this.robot.mousePress( InputEvent.BUTTON1_DOWN_MASK );


        if ( this.virtualRightMouseKey.matchesKey( keycode, scancode ) )
            // Simulate left mouse
            this.robot.mousePress( InputEvent.BUTTON2_DOWN_MASK );


        return ActionResult.PASS;
    }

    /**
     * Modify the mouse delta, so that the arrow keys control look angle.
     * Note: Mouse deltas are only used when the cursor is locked; i.e. when "actually playing"
     * @param keycode: GLFW keycode
     * @param f: Delta delta :)
     * @param accelerate: Make the delta change at an increasing rate. Not sure if this actually does anything
     */
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

    /**
     * Modify the actual mouse position.
     * Note: Only used when the cursor is unlocked; i.e. anytime you can see the cursor, like in GUIs and chat
     * @param keycode: GLFW keycode
     * @param f: Delta delta
     */
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