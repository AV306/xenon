package me.av306.xenon.feature;

import me.av306.xenon.Xenon;
import me.av306.xenon.util.text.TextFactory;
import net.minecraft.text.Text;

public abstract class IToggleableFeature extends IFeature
{
	/**
 	* Text to display when the feature is enabled. Should not change after initialisation,
  	*/
    protected final Text enabledText;
	
	/**
 	 * Text to display when the feature is disabled. Should not change after initialisation,
     */
    protected final Text disabledText;

	/**
 	 * Whether the feature is enabled.
     */
    protected boolean isEnabled = false;

    /**
	 * Whether this feature should be disabled on exit
	 */
	private boolean persistent = false;
	public void setPersistent( boolean persistent ) { this.persistent = persistent; }
	public boolean isPersistent() { return this.persistent; }

    //protected int key = GLFW.GLFW_KEY_UNKNOWN;

    //protected static IToggleableFeature instance;

	protected IToggleableFeature( String name )
    {
        super( name );

        this.enabledText = TextFactory.createTranslatable( "text.xenon.itoggleablefeature.enabled", name )
                .formatted( Xenon.INSTANCE.SUCCESS_FORMAT );
        this.disabledText = TextFactory.createTranslatable( "text.xenon.itoggleablefeature.disabled", name )
                .formatted( Xenon.INSTANCE.SUCCESS_FORMAT );
    }

    protected IToggleableFeature( String name, String... aliases )
    {
        super( name, aliases );

        this.enabledText = TextFactory.createTranslatable( "text.xenon.itoggleablefeature.enabled", name )
                .formatted( Xenon.INSTANCE.SUCCESS_FORMAT );
        this.disabledText = TextFactory.createTranslatable( "text.xenon.itoggleablefeature.disabled", name )
                .formatted( Xenon.INSTANCE.SUCCESS_FORMAT );
    }

    protected IToggleableFeature( String name, int key )
    {
        super( name, key );
		
		this.enabledText = TextFactory.createTranslatable( "text.xenon.itoggleablefeature.enabled", name )
                .formatted( Xenon.INSTANCE.SUCCESS_FORMAT );
        this.disabledText = TextFactory.createTranslatable( "text.xenon.itoggleablefeature.disabled", name )
                .formatted( Xenon.INSTANCE.SUCCESS_FORMAT );
    }

    protected IToggleableFeature( String name, int key, String... aliases )
    {
        super( name, key, aliases );

		this.enabledText = TextFactory.createTranslatable( "text.xenon.itoggleablefeature.enabled", name )
                .formatted( Xenon.INSTANCE.SUCCESS_FORMAT );
        this.disabledText = TextFactory.createLiteral( "text.xenon.itoggleablefeature.disabled", name )
                .formatted( Xenon.INSTANCE.SUCCESS_FORMAT );
    }

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
        if ( this.keyBinding.wasPressed() && !Xenon.INSTANCE.modifierKey.isPressed() )
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

        try
        {
            Xenon.INSTANCE.client.player.sendMessage( this.disabledText, true );
        }
        catch ( NullPointerException ignored ) {}

        onDisable();
    }
    
    protected abstract void onDisable();

    public void toggle()
    {
        if ( this.isEnabled ) disable();
        else enable();
    }
}
