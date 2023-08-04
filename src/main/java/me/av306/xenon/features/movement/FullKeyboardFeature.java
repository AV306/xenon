package me.av306.xenon.features.movement;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.movement.FullKeyboardGroup;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.mixin.MouseAccessor;
import me.av306.xenon.mixinterface.IMouse;
import me.av306.xenon.util.KeybindUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public final class FullKeyboardFeature extends IToggleableFeature
{
    private final KeyBinding upKey;
    private final KeyBinding downKey;
    private final KeyBinding leftKey;
    private final KeyBinding rightKey;
    private final KeyBinding virtualLeftMouseKey;
    private final KeyBinding virtualRightMouseKey;

    public FullKeyboardFeature()
    {
        super( "FullKeyboard", "fullkey", "fullkb", "fkb" );

        // Register extra keys
        this.virtualLeftMouseKey = KeybindUtil.registerKeybind(
            "fullkeyboard.virtualLeftMouseKey",
            GLFW.GLFW_KEY_COMMA,
            this.category
        );

        this.virtualRightMouseKey = KeybindUtil.registerKeybind(
            "fullkeyboard.virtualRightMouseKey",
            GLFW.GLFW_KEY_PERIOD,
            this.category
        );

        this.upKey = KeybindUtil.registerKeybind(
            "fullkeyboard.upKey",
            GLFW.GLFW_KEY_UP,
            this.category
        );

        this.downKey = KeybindUtil.registerKeybind(
            "fullkeyboard.downKey",
            GLFW.GLFW_KEY_DOWN,
            this.category
        );

        this.leftKey = KeybindUtil.registerKeybind(
            "fullkeyboard.leftKey",
            GLFW.GLFW_KEY_LEFT,
            this.category
        );

        this.rightKey = KeybindUtil.registerKeybind(
            "fullkeyboard.rightKey",
            GLFW.GLFW_KEY_RIGHT,
            this.category
        );
        
        // Register double-bindings
        ClientTickEvents.START_CLIENT_TICK.register( (client) -> this.onEndTick() );
    }

    private void onEndTick()
    {
        if ( !this.isEnabled ) return;

        // FIXME: virtual attack key only works for block breaking, not attacking
        // Best guess is attack is a wasPressed() call, but block breaking is an isPressed()
        Xenon.INSTANCE.client.options.attackKey.setPressed( this.virtualLeftMouseKey.isPressed() );
        Xenon.INSTANCE.client.options.useKey.setPressed( this.virtualRightMouseKey.isPressed() );
        
        double fac = Xenon.INSTANCE.client.options.getMouseSensitivity().getValue() * FullKeyboardGroup.sensitivity;

        // The cursor will be unlocked when in a GUI or chat, so when this happens
        // we need to modify the cursor position directly
        // Movement stuff
        if ( Xenon.INSTANCE.client.mouse.isCursorLocked() )
            // Gameplay; use deltas
            this.modifyMouseDelta( fac, FullKeyboardGroup.acceleration );
        else
            // This works, but doesn't update the actual cursor position
            this.modifyMousePos( fac );
    }

    /**
     * Modify the mouse delta, so that the arrow keys control look angle.
     * Note: Mouse deltas are only used when the cursor is locked; i.e. when "actually playing"
     * @param f: Delta of the delta
     * @param accelerate: Make the delta change at an increasing rate. Not sure if this actually does anything
     */
    private void modifyMouseDelta( double f, boolean accelerate )
    {
        if ( Xenon.INSTANCE.client.options.sprintKey.isPressed() )
        {
            // FLick view
            // TODO: How does vanilla rotate the player?
            //if ( this.upKey.wasPressed() ) 
            //if ( this.downKey.wasPressed() ) 
            //if ( this.leftKey.wasPressed() )
            //Xenon.INSTANCE.client.player.sendMessage( me.av306.xenon.util.text.TextFactory.createLiteral( String.valueOf( this.rightKey.wasPressed() ) ) );
            //if ( this.rightKey.wasPressed() ) ((MouseAccessor) Xenon.INSTANCE.client.mouse).setCursorDeltaX( 100 );
        }
        else if ( !accelerate )
        {
            // Linear movement
            if ( this.upKey.isPressed() ) ((MouseAccessor) Xenon.INSTANCE.client.mouse).setCursorDeltaY( -f );
            if ( this.downKey.isPressed() ) ((MouseAccessor) Xenon.INSTANCE.client.mouse).setCursorDeltaY( f );
            if ( this.leftKey.isPressed() ) ((MouseAccessor) Xenon.INSTANCE.client.mouse).setCursorDeltaX( -f );
            if ( this.rightKey.isPressed() ) ((MouseAccessor) Xenon.INSTANCE.client.mouse).setCursorDeltaX( f );
        }
        else
        {
            if ( this.upKey.isPressed() ) ((IMouse) Xenon.INSTANCE.client.mouse).accelerateDeltaY( -f );
            if ( this.downKey.isPressed() ) ((IMouse) Xenon.INSTANCE.client.mouse).accelerateDeltaY( f );
            if ( this.leftKey.isPressed() ) ((IMouse) Xenon.INSTANCE.client.mouse).accelerateDeltaX( -f );
            if ( this.rightKey.isPressed() ) ((IMouse) Xenon.INSTANCE.client.mouse).accelerateDeltaX( f );
        }
    }

    /**
     * Modify the actual mouse position.
     * Note: Only used when the cursor is unlocked; i.e. anytime you can see the cursor, like in GUIs and chat
     * @param keycode: GLFW keycode
     * @param f: Delta delta
     */
    private void modifyMousePos( double f )
    {
        // Linear movement
        if ( this.upKey.isPressed() ) ((IMouse) Xenon.INSTANCE.client.mouse).changeY( -f );
        if ( this.downKey.isPressed() ) ((IMouse) Xenon.INSTANCE.client.mouse).changeY( f );
        if ( this.leftKey.isPressed() ) ((IMouse) Xenon.INSTANCE.client.mouse).changeX( -f );
        if ( this.rightKey.isPressed() ) ((IMouse) Xenon.INSTANCE.client.mouse).changeX( f );
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