package me.av306.xenon.feature;

import me.av306.xenon.Xenon;
import me.av306.xenon.util.text.TextFactory;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

/**
 * Base class for all features. You shouldn't need to touch this,
 * just extend it and implement its methods.
 * NOTE: This *could* work by just using the subclass name field
 * to hide the superclass name field,
 * but let's try this way first.
 */
public abstract class IFeature
{
	/**
	 * The Feature's DISPLAY name.
	 */
	protected String name = "IFeature";
	public String getName() { return this.name; }
	public void setName( String name ) { this.name = name; }
	
  	//public boolean isEnabled = false;

	/**
	 * The key the Feature is bound to.
	 */
	protected int key;

	/**
	 * The `KeyBinding` instance of the Feature.
	 */
	protected KeyBinding keyBinding;

	/**
	 * Sets whether this Feature should be hidden in FeatureList.
	 */
	public boolean hide = false;

	/**
	 * +-------------+
	 * | Contructors |
	 * +-------------+
	 */

	/**
	 * Recommended constructor to call in a subclass.
	 * @param name: The Feature's display name
	 * @param aliases: Aliases for the feature in CP. Should NOT contain the original name
	 */
	protected IFeature( String name, String... aliases )
	{
		this( name, GLFW.GLFW_KEY_UNKNOWN );

		// register aliases
		for ( String alias : aliases )
			Xenon.INSTANCE.featureRegistry.put( alias.toLowerCase(), this );
	}

	/**
	 * Constructor with display name, aliases and pre-bound key.
	 * @param name: Display name
	 * @param key: GLFW keycode to bind to
	 * @param aliases: CommandProcessor aliases
	 */
	protected IFeature( String name, int key, String... aliases )
	{
		this( name, key );

		// register aliases
		for ( String alias : aliases )
			Xenon.INSTANCE.featureRegistry.put( alias.toLowerCase(), this );
	}

	/**
	 * Name-only constructor.
	 * @param name: Display name
	 */
	protected IFeature( String name )
	{
		this( name, GLFW.GLFW_KEY_UNKNOWN );
	}

	// random ones

	/**
	 * Constructor with only display name and pre-bound key.
	 * @param name: Display name
	 * @param key: GLFW keycode to bind to
	 */
	protected IFeature( String name, int key )
	{ 
		this.name = name;

		this.key = key;

		// use the name passed in because some features change their name (e.g. timer)
		this.keyBinding = new KeyBinding(
				"key.xenon." + name.toLowerCase().replaceAll( " ", "" ),
				InputUtil.Type.KEYSYM,
				this.key,
				"category.xenon.features"
		); // don't construct statically because name might not have been set

		//KeyEvent.EVENT.register( this::onKeyboardKey );

		// register our keybind
		KeyBindingHelper.registerKeyBinding( this.keyBinding );

		// register our event
		ClientTickEvents.END_CLIENT_TICK.register(
				client -> this.keyEvent()
		);

		// register our display name in the registry
		Xenon.INSTANCE.featureRegistry.put(
				name.replaceAll( " ", "" ).toLowerCase(),
				this
		);
	}

	/*protected ActionResult onKeyboardKey( long window, int key, int scanCode, int action, int modifiers )
	{
		if ( action == GLFW.GLFW_PRESS && key == this.key )
			enable();

		return ActionResult.PASS;
	}*/

	/**
	 * Method called when the key event is registered.
	 * Override this for advanced behaviour (see ZoomFeature)
	 */
	protected void keyEvent()
	{
		if ( this.keyBinding.wasPressed() )
			this.enable();
	}

	/**
	 * Method to enable a Feature. Used to hide shared logic.
	 * Only override for advanced behaviour. Use onEnable() instead.
	 */
	public void enable()
	{
		// this will cause the feature to only be enabled once per session
		// but it is a failsafe in ITF
		//if ( isEnabled ) return;
			
		//this.isEnabled = true;

		Xenon.INSTANCE.log( this.getName() + " enabled!" );

		//Xenon.INSTANCE.featureManager.getEnabledFeatures().add( this.name );

		onEnable();
	}

	// this goes in IToggleableFeature instead
	/*public void disable()
	{
		//if ( !isEnabled ) return;
			
		isEnabled = false;

		//Xenon.INSTANCE.featureManager.getEnabledFeatures().remove( name );
		
		onDisable();
	}*/

	/**
	 * Abstract method that should contain logic for the feature.
	 */
  	protected abstract void onEnable();

	/**
	 * Called when a config change is requested in CP.
	 * Used to hide config change logic.
	 * @param config: The name of the config
	 * @param value: The value to set teh config to
	 */
	public void parseConfigChange( String config, String value )
	{
		// success flag
        boolean result;
		
		try
		{
			// attempt to change the config
            result = this.onRequestConfigChange( config, value );
		}
		catch ( NumberFormatException nfe )
		{
			// Most features will need to parse a number from a string.
			// if anything goes wrong, catch it here
			result = false;
		}

		// respond based on success falag
		if ( result )
		{
			Xenon.INSTANCE.sendInfoMessage(
					TextFactory.createTranslatable(
							"text.xenon.ifeature.configchange.success",
							this.name,
							config,
							value
					)
			);
			Xenon.INSTANCE.config.save();
		}
		else
		{
			Xenon.INSTANCE.sendInfoMessage(
					TextFactory.createTranslatable(
							"text.xenon.ifeature.configchange.invalidconfig",
							this.name,
							config,
							value
					)
			);
		}
	}

	/**
	 * Overrideable method that should contain logic for config changes.
	 * NOTE: Catch ALL exceptions EXCEPT NumberFormatExceptions in here.
	 * @param config: The name of the config
	 * @param value: The value to set the config to
	 */
	protected boolean onRequestConfigChange( String config, String value ) { return false; }

	/**
	 * Method that hides execution logic.
	 * See CP for an example of this.
	 * @param action: An array containing the command.
	 */
    public void executeAction( String[] action )
	{
        boolean result = this.onRequestExecuteAction( action );

		if ( result )
		{
			Xenon.INSTANCE.sendInfoMessage(
                TextFactory.createTranslatable(
                    "text.xenon.ifeature.executeaction.success",
					this.name,
					Arrays.toString( action )
				)
			);
		}
		else
		{
			Xenon.INSTANCE.sendInfoMessage(
				TextFactory.createTranslatable(
					"text.xenon.ifeature.executeaction.fail",
					this.name,
					Arrays.toString( action )
				).formatted( Xenon.INSTANCE.ERROR_FORMAT )
			);
		}
	}

	/**
	 * Overridable method containing execution logic.
	 * @param action: An array containing the command.
	 */
    protected boolean onRequestExecuteAction( String[] action ) { return true; }

	/**
	 * Method to retrieve help text for a feature.
	 */
	public Text getHelpText( String[] args )
	{
		// TODO: add formatting
		return TextFactory.createLiteral( "Whoops! This Feature doesn't have any documentation :(" );
	}
}
