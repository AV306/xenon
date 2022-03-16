package me.av306.xenon.feature;

import me.av306.xenon.Xenon;

import java.util.Objects;

/**
 * Base class for all features. You shouldn't need to touch this,
 * just extend it and implement its methods.
 * NOTE: THis *could* work by just using the subclass name
 * to hide the superclass name,
 * but let's try this way first.
 */
public abstract class IFeature
{
	protected String name = "IFeature";
	public String getName() { return this.name; }
	
  	public boolean isEnabled = false;

	// generalised constructors (can't be called anyway)
	protected IFeature( String name ) { this.name = Objects.requireNonNull( name ); }
	protected IFeature() { this( "IFeature" ); }
	
	public void init()
	{
		// initialises the keybind by registering its name.
		// Called only when the feature is REGISTERED,
		// NOT on construction. 
		// If you're confused; don't worry -- I am, too.
		//Xenon.INSTANCE.featureManager.getFeatureMap().put( this.name, this );
		// TODO: Implement

		//onInit();
	}
	
	public void enable()
	{
		if ( isEnabled ) return;
			
		isEnabled = true;

		//Xenon.INSTANCE.featureManager.getEnabledFeatures().add( this.name );

		onEnable();
	}

	// this is not even going to be used, but it's here now.
	// deal with it :shrug:
	public void disable()
	{
		if ( !isEnabled ) return;
			
		isEnabled = false;

		//Xenon.INSTANCE.featureManager.getEnabledFeatures().remove( name );
		
		onDisable();
	}

	//public abstract void onInit();
  	public abstract void onEnable();
  	public abstract void onDisable();
}
