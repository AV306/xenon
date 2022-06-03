package me.av306.xenon.feature;

import me.av306.xenon.Xenon;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public abstract class IToggleableFeature extends IFeature
{
    protected Text enabledText;
    protected Text disabledText;

    //protected int key = GLFW.GLFW_KEY_UNKNOWN;

    //protected static IToggleableFeature instance;

	protected IToggleableFeature( String name )
    {
        super( name );

        this.enabledText = new LiteralText( this.name + " ENABLED!" )
                .formatted( Xenon.INSTANCE.SUCCESS_FORMAT );
        this.disabledText = new LiteralText( this.name + " DISABLED!" )
                .formatted( Xenon.INSTANCE.SUCCESS_FORMAT );
    }

    /*public static IToggleableFeature getInstance()
    {
        return instance;
    }*/

    /*@Override
    protected ActionResult onKeyboardKey( long window, int key, int scanCode, int action, int modifiers )
    {
        if ( action == GLFW.GLFW_PRESS && key == this.key )
            toggle();

        return ActionResult.PASS;
    }*/

    @Override
    protected void registerKeyEvent()
    {
        if ( this.keyBinding.wasPressed() )
            this.toggle();
    }

    @Override
    public void enable()
    {
        if ( isEnabled ) return; // safety

        isEnabled = true;

        Xenon.INSTANCE.enabledFeatures.add( this );

        try
        {
            Xenon.INSTANCE.client.player.sendMessage( this.enabledText, true );
        }
        catch ( NullPointerException ignored ) {}

        onEnable();
    }

    public void disable()
    {
        if ( !isEnabled ) return;

        isEnabled = false;

        Xenon.INSTANCE.enabledFeatures.remove( this );

        Xenon.INSTANCE.client.player.sendMessage( this.disabledText, true );

        onDisable();
    }

    private void toggle()
    {
        if ( isEnabled ) disable();
        else enable();
    }

    protected abstract void onDisable();
}
