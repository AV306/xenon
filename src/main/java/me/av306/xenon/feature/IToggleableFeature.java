package me.av306.xenon.feature;

import me.av306.xenon.Xenon;
import me.av306.xenon.util.text.TextFactory;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

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
        this( name, GLFW.GLFW_KEY_UNKNOWN );
    }

    protected IToggleableFeature( String name, String... aliases )
    {
        this( name, GLFW.GLFW_KEY_UNKNOWN, aliases );
    }

    protected IToggleableFeature( String name, int key )
    {
        super( name, key );
		
		this.enabledText = TextFactory.createTranslatable( "text.xenon.itoggleablefeature.enabled", name )
                .formatted( Xenon.INSTANCE.SUCCESS_FORMAT );
        this.disabledText = TextFactory.createTranslatable( "text.xenon.itoggleablefeature.disabled", name )
                .formatted( Xenon.INSTANCE.SUCCESS_FORMAT );

        this.commandBuilder.then( literal( "disable" ).executes( context -> { this.disable(); return 1; } ) );
        this.commandBuilder.then( literal( "d" ).executes( context -> { this.disable(); return 1; } ) );
        this.commandBuilder.then( literal( "toggle" ).executes( context -> { this.toggle(); return 1; } ) );
        this.commandBuilder.then( literal( "t" ).executes( context -> { this.toggle(); return 1; } ) );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register( this.commandBuilder )
        );
    }

    protected IToggleableFeature( String name, int key, String... aliases )
    {
        super( name, key, aliases );

		this.enabledText = TextFactory.createTranslatable( "text.xenon.itoggleablefeature.enabled", name )
                .formatted( Xenon.INSTANCE.SUCCESS_FORMAT );
        this.disabledText = TextFactory.createTranslatable( "text.xenon.itoggleablefeature.disabled", name )
                .formatted( Xenon.INSTANCE.SUCCESS_FORMAT );

        // We're registering the things like thrice, but it works, so I'm not complaining
        this.commandBuilder.then( literal( "disable" ).executes( context -> { this.disable(); return 1; } ) );
        this.commandBuilder.then( literal( "d" ).executes( context -> { this.disable(); return 1; } ) );
        this.commandBuilder.then( literal( "toggle" ).executes( context -> { this.toggle(); return 1; } ) );
        this.commandBuilder.then( literal( "t" ).executes( context -> { this.toggle(); return 1; } ) );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register( this.commandBuilder )
        );
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
        {
            if ( this.forceDisabled )
                Xenon.INSTANCE.sendErrorMessage( "text.xenon.ifeature.blocked", this.name );
        
            else this.toggle();
        }
    }

    @Override
    public void enable()
    {
        if ( this.isEnabled ) return; // safety

        this.isEnabled = true;

        Xenon.INSTANCE.LOGGER.info( "{} enabled!", this.getName() );

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
