package me.av306.xenon.feature;

import me.av306.xenon.Xenon;
import me.av306.xenon.util.text.TextFactory;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;
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
	protected String name = "IFeature";
	public String getName() { return this.name; }
	public void setName( String name ) { this.name = name; }
	
  	public boolean isEnabled = false;

	protected int key;

	protected KeyBinding keyBinding;

	// generalised constructors (can't be called anyway)
	protected IFeature( String name, int key )
	{ 
		this.name = Objects.requireNonNull( name );

		this.key = key;

		this.keyBinding = new KeyBinding(
				"key.xenon." + this.name.toLowerCase(),
				InputUtil.Type.KEYSYM,
				this.key,
				"category.xenon.features"
		); // don't construct statically because name might not have been set

		//KeyEvent.AFTER_VIGNETTE.register( this::onKeyboardKey );

		// register our keybind
		KeyBindingHelper.registerKeyBinding( this.keyBinding );

		// register our event
		ClientTickEvents.END_CLIENT_TICK.register(
				client -> this.registerKeyEvent()
		);

		// register our name in the registry
		// (the DISPLAY name, not our actual class name)
		Xenon.INSTANCE.featureRegistry.put( this.getName().toLowerCase(), this );
	}

	protected IFeature( String name )
	{
		this( name, GLFW.GLFW_KEY_UNKNOWN );
	}

	/*protected ActionResult onKeyboardKey( long window, int key, int scanCode, int action, int modifiers )
	{
		if ( action == GLFW.GLFW_PRESS && key == this.key )
			enable();

		return ActionResult.PASS;
	}*/

	protected void registerKeyEvent()
	{
		if ( this.keyBinding.wasPressed() )
			this.enable();
	}

	protected IFeature() { this( "IFeature" ); }
	
	public void enable()
	{
		//if ( isEnabled ) return;
			
		isEnabled = true;

		//Xenon.INSTANCE.featureManager.getEnabledFeatures().add( this.name );

		onEnable();
	}

	// this is not even going to be used, but it's here now.
	// deal with it :shrug:
	/*public void disable()
	{
		//if ( !isEnabled ) return;
			
		isEnabled = false;

		//Xenon.INSTANCE.featureManager.getEnabledFeatures().remove( name );
		
		onDisable();
	}*/

  	protected abstract void onEnable();

  	public void parseConfigChange( String config, String value )
	{
        boolean result;
		
		try
		{
            result = this.onConfigChange( config, value );
		}
		catch ( NumberFormatException nfe )
		{
			result = false;
		}

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

    // TODO: rename to "onRequestConfigChange"
	protected boolean onConfigChange( String config, String value ) { return false; }

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
				)
			);
		}
	}

    protected boolean onRequestExecuteAction( String[] action ) { return true; }
	//public abstract void onDisable();
}
