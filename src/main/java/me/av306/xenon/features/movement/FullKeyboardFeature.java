package me.av306.xenon.features.movement;

import me.av306.xenon.event.KeyEvent;
import me.av306.xenon.feature.IToggleableFeature;

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
    //if ( !this.isEnabled ) return ActionResult.PASS;

        switch ( keycode )
        {
            case GLFW.GLFW_KEY_UP -> ((MouseAccessor) Xenon.INSTANCE.client.mouse).changeY( 5 );
            case GLFW.GLFW_KEY_DOWN -> ((MouseAccessor) Xenon.INSTANCE.client.mouse).changeY( -5 );
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