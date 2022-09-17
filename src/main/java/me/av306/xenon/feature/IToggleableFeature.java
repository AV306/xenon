package me.av306.xenon.feature;

import me.av306.xenon.Xenon;
import me.av306.xenon.util.text.TextFactory;
import net.minecraft.text.Text;

public abstract class IToggleableFeature extends IFeature
{
    protected Text enabledText;
    protected Text disabledText;

    protected boolean isEnabled = false;

    //protected int key = GLFW.GLFW_KEY_UNKNOWN;

    //protected static IToggleableFeature instance;

	protected IToggleableFeature( String name )
    {
        super( name );

        this.enabledText = TextFactory.createLiteral( name + " ENABLED!" )
                .formatted( Xenon.INSTANCE.SUCCESS_FORMAT );
        this.disabledText = TextFactory.createLiteral( name + " DISABLED!" )
                .formatted( Xenon.INSTANCE.SUCCESS_FORMAT );
    }

    protected IToggleableFeature( String name, String... aliases )
    {
        super( name, aliases );

        this.enabledText = TextFactory.createLiteral( name + " ENABLED!" )
                .formatted( Xenon.INSTANCE.SUCCESS_FORMAT );
        this.disabledText = TextFactory.createLiteral( name + " DISABLED!" )
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
    protected void keyEvent()
    {
        if ( this.keyBinding.wasPressed() )
            this.toggle();
    }

    @Override
    public void enable()
    {
        if ( this.isEnabled ) return; // safety

        this.isEnabled = true;

        Xenon.INSTANCE.LOGGER.info( this.getName() + " enabled!" );

        Xenon.INSTANCE.enabledFeatures.add( this );

        try
        {
            Xenon.INSTANCE.client.player.sendMessage( this.enabledText, true );
        }
        catch ( NullPointerException ignored ) {} // Features that start enabled will try to send this message and fail

        onEnable();
    }

    public void disable()
    {
        if ( !this.isEnabled ) return; // safety catch

        this.isEnabled = false;

        Xenon.INSTANCE.LOGGER.info( this.getName() + " disabled!" );

        Xenon.INSTANCE.enabledFeatures.remove( this );

        Xenon.INSTANCE.client.player.sendMessage( this.disabledText, true );

        onDisable();
    }
    
    protected abstract void onDisable();

    public void toggle()
    {
        if ( this.isEnabled ) disable();
        else enable();
    }
}
