package me.av306.xenon.feature;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.av306.xenon.Xenon;
import me.av306.xenon.util.text.TextFactory;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

/**
 * Base class for all features, to be extended by other feature types and feature implementations.
 * NOTE: This *could* work by just using the subclass name field
 * to hide the superclass name field,
 * but let's try this way first.
 * EDIT: this way works pretty nicely!
 */
public abstract class IFeature
{
	/**
	 * The Feature's display name, i.e. the name to be displayed in FeatureList
	 */
	protected String name = "IFeature";
	public String getName() { return this.name; }
	public void setName( String name ) { this.name = name; }

	/**
 	 * The category for the feature in the keybinds page
   	 * Doesn't do anything outside the keybind registration yet.
   	 */
	protected String category = "features";
	public String getCategory() { return this.category; }

	/**
	 * Whether the server wishes to force-disable the feature
	 */
	protected boolean forceDisabled = false;
	public boolean isForceDisabled() { return this.forceDisabled; }
	public void setForceDisabled( boolean b ) { this.forceDisabled = b; }
	

	/**
	 * The key the Feature is bound to
	 */
	protected int key;

	/**
	 * The `KeyBinding` instance of the Feature
	 * It is advised not to set this directly; use one of the constructors instead.
	 */
	protected KeyBinding keyBinding;
	public KeyBinding getKeyBinding() { return this.keyBinding; }
	public void setKeyBinding( KeyBinding keybinding ) { this.keyBinding = keybinding; }

	/**
	 * Sets whether this Feature should be hidden in FeatureList
	 */
	private boolean hide = false;
	public void setShouldHide( boolean shouldHide ) { this.hide = shouldHide; }
	public boolean getShouldHide() { return this.hide; }

	protected LiteralCommandNode<FabricClientCommandSource> commandNode;
	protected final LiteralArgumentBuilder<FabricClientCommandSource> commandBuilder;

	/**
	 * Constructor that initialises a feature with the given display name, aliases and no default key
	 * @param name: The Feature's display name
	 * @param aliases: Aliases for the feature in CP. <i>Technically can</i>, but should not, contain the name in the first argument
	 * @see #IFeature(String, int, String...)
	 */
	protected IFeature( String name, String... aliases )
	{
		this( name, GLFW.GLFW_KEY_UNKNOWN, aliases );
	}

	/**
	 * Constructor that initialises a feature with a display name, aliases and pre-bound key
	 * @param name: Display name
	 * @param key: GLFW keycode to bind to
	 * @param aliases: CommandProcessor aliases
	 * @see #IFeature(String, int)
	 */
	protected IFeature( String name, int key, String... aliases )
	{
		this( name, key );

		// register aliases
		for ( String alias : aliases )
		{
			Xenon.INSTANCE.featureRegistry.put( alias.toLowerCase(), this );

			// Register aliases as Brigadier command redirects
			ClientCommandRegistrationCallback.EVENT.register( (dispatcher, registryAccess) ->
					dispatcher.register(
							ClientCommandManager.literal( alias )
									.executes( context -> { this.toggleOrElseEnable(); return 1; } )
									.redirect( this.commandNode )
					)
			);
		}
	}

	/**
	 * Constructor that initialises a feature with a display name, no aliases and no default key
	 * @param name: Display name
	 * @see #IFeature(String, int)
	 */
	protected IFeature( String name )
	{
		this( name, GLFW.GLFW_KEY_UNKNOWN );
	}


	/**
	 * Initialises a feature with a display name and default key
	 * @param name: Display name
	 * @param key: GLFW keycode to bind to
	 */
	protected IFeature( String name, int key )
	{
		// Set fields
		this.name = name;
		this.key = key;

		// Use the name passed in for keybind
		// because some features change their name (e.g. timer)
		this.keyBinding = new KeyBinding(
				"key.xenon." + name.toLowerCase().replaceAll( " ", "" ),
				InputUtil.Type.KEYSYM,
				key,
				"category.xenon." + this.category
		);

		// Register keybind and key event
		KeyBindingHelper.registerKeyBinding( this.keyBinding );
		ClientTickEvents.END_CLIENT_TICK.register( client -> this.keyEvent() );

		// Register display name in CP registry
		String formattedName = name.replaceAll( " ", "" ).toLowerCase();
		Xenon.INSTANCE.featureRegistry.put( formattedName, this );

		// Register a Brigadier command (native minecraft client command)
		// FIXME: this causes issues with commands accepting more than one string argument
		this.commandBuilder = ClientCommandManager.literal( formattedName )
				.executes( context -> { this.toggleOrElseEnable(); return 1; } );

		this.commandBuilder.then( ClientCommandManager.literal( "enable" ).executes( context -> { this.enable(); return 1; } ) );
		this.commandBuilder.then( ClientCommandManager.literal( "e" ).executes( context -> { this.enable(); return 1; } ) ); // Enable alias
		
		this.commandBuilder.then(
				ClientCommandManager.literal( "help" )
						.executes( context ->
						{
						this.sendInfoMessage( this.getHelpText( null ) );
						return 1;
						} )
						.then( ClientCommandManager.argument( "keyword", StringArgumentType.word() )
								.executes( context ->
								{
									this.sendInfoMessage( this.getHelpText( StringArgumentType.getString( context, "keyword" ) ) );
									return 1;
								} )
						)
		);

		// Holy fk I DID IT
		this.commandBuilder.then(
			ClientCommandManager.literal( "set" )
					.then(
							ClientCommandManager.argument( "config", StringArgumentType.string() )
									.then( ClientCommandManager.argument( "value", StringArgumentType.string() )
											.executes( context ->
											{
												this.requestConfigChange(
														StringArgumentType.getString( context, "config" ),
														StringArgumentType.getString( context, "value" )
												);
												return 1;
											} )
									)
					)
		);

		// Register command
		ClientCommandRegistrationCallback.EVENT.register(
				(dispatcher, registryAccess) -> this.commandNode = dispatcher.register( this.commandBuilder )
		);
	}

	/*protected ActionResult onKeyboardKey( long window, int key, int scanCode, int action, int modifiers )
	{
		if ( action == GLFW.GLFW_PRESS && key == this.key )
			enable();

		return ActionResult.PASS;
	}*/

	/**
	 * Key event callback (called when the feature key is pressed)
	 * Override this for advanced behaviour (see ZoomFeature)
	 */
	protected void keyEvent()
	{
		if ( this.keyBinding.wasPressed() )
			if ( this.forceDisabled )
				// Server wishes to opt out of this feature
				this.sendErrorMessage( "text.xenon.ifeature.forcedisabled" );
			else this.enable();
	}

	protected void toggleOrElseEnable()
	{
		if ( this instanceof IToggleableFeature itf ) itf.toggle();
		else this.enable();
	}

	/**
	 * Method to enable a Feature "nicely", with stuff like the "enabled!" message
	 * Only override for advanced behaviour. Overridde onEnable() instead
	 */
	public void enable()
	{
		// this will cause the feature to only be enabled once per session
		// but it is a failsafe in ITF
		//if ( isEnabled ) return;
			
		//this.isEnabled = true;

		Xenon.INSTANCE.LOGGER.info( this.getName() + " enabled!" );

		//Xenon.INSTANCE.featureManager.getEnabledFeatures().add( this.name );

		onEnable();
	}

	/**
	 * Internal feature "enable" callback; called whenn the feature is enabled
	 */
  	protected abstract void onEnable();

	/**
	 * Config change method, called when a feature change is requested in CP
	 * Used to hide config change logic.
	 * @param config: The name of the config
	 * @param value: The value to set the config to
	 */
	// FIXME: I don't think we should catch the NFE here
	public void requestConfigChange( String config, String value )
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

		// respond based on success flag
		if ( result )
		{
			this.sendInfoMessage(
					"text.xenon.ifeature.configchange.success",
					config,
					value
			);
			Xenon.INSTANCE.config.save();
		}
		else
		{
			this.sendInfoMessage(
					"text.xenon.ifeature.configchange.invalidconfig",
					config,
					value
			);
		}
	}

	/**
	 * Internal config change method, for the actual change of the config value
	 * NOTE: Catch ALL exceptions EXCEPT NumberFormatExceptions in here.
	 * @param config: The name of the config
	 * @param value: The value to set the config to
	 */
	protected boolean onRequestConfigChange( String config, String value ) { return false; }

	/**
	 * Method that requests a feature to execute an action
	 * See CP for an example of this.
	 * @param action: An array containing the command.
	 */
    	public void requestExecuteAction( String[] action )
	{
        	boolean result = this.onRequestExecuteAction( action );

		if ( result )
		{
			this.sendInfoMessage(
					"text.xenon.ifeature.executeaction.success",
					Arrays.toString( action )
			);
		}
		else
		{
			this.sendErrorMessage(
					"text.xenon.ifeature.executeaction.fail",
					Arrays.toString( action )
			);
		}
	}

	/**
	 * Internal method to handle execution of acttions
	 * @param action: An array containing the command.
	 */
    	protected boolean onRequestExecuteAction( String[] action ) { return true; }

	/**
	 * Method to retrieve help text for a feature.
	 */
	public Text getHelpText( String argument )
	{
		// TODO: add formatting
		return TextFactory.createLiteral( "Whoops! This Feature doesn't have any documentation :(" );
	}


	protected void sendInfoMessage( Text text )
	{
		Text message = Xenon.INSTANCE.getNamePrefixCopy().append(
				TextFactory.createTranslatable(
						"text.xenon.message",
						this.name,
						text
				)
		);

		try
		{
			Xenon.INSTANCE.client.player.sendMessage( message );
		}
		catch ( NullPointerException ignored ) {}
	}

	protected void sendInfoMessage( String key, Object... args )
	{
		Text message = Xenon.INSTANCE.getNamePrefixCopy().append(
				TextFactory.createTranslatable(
						"text.xenon.message",
						this.name,
						TextFactory.createTranslatable( key, args )
				)
		);

		try
		{
			Xenon.INSTANCE.client.player.sendMessage( message );
		}
		catch ( NullPointerException ignored ) {}
	}

	protected void sendErrorMessage( String key, Object... args )
	{
		Text message = Xenon.INSTANCE.getNamePrefixCopy().append(
				TextFactory.createTranslatable(
						"text.xenon.message",
						this.name,
						TextFactory.createTranslatable( key, args ).formatted( Xenon.INSTANCE.ERROR_FORMAT )
				)
		);

		try
		{
			Xenon.INSTANCE.client.player.sendMessage( message );
		}
		catch ( NullPointerException ignored ) {}
	}
}
