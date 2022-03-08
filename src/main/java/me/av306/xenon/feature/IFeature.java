package me.av306.xenon.feature;

import me.av306.xenon.Xenon;

import java.util.Objects;

/**
 * Base class for all features. You shouldn't need to touch this,
 * just extend it and implement its methods.
 */
public abstract class IFeature
{
	private String name = "IFeature";
	//public String getName() { return this.name; }
	
  public boolean isEnabled = false;

	// generalised constructors (can't be called anyway)
	protected IFeature( String name ) { this.name = Objects.requireNonNull( name ); }
	protected IFeature() { this( "IFeature" ); }
	
	public void init()
	{
		// initialises the keybind by registering its name.
		//Xenon.INSTANCE.featureManager.getFeatureMap().put( this.name, this );
		// TODO: Implement
	}
	
	public void enable()
	{
		if ( isEnabled ) return;
			
		isEnabled = true;

		//Xenon.INSTANCE.featureManager.getEnabledFeatures().add( this.name );

		onEnable();
	}

	public void disable()
	{
		if ( !isEnabled ) return;
			
		isEnabled = false;

		//Xenon.INSTANCE.featureManager.getEnabledFeatures().remove( name );
		
		onDisable();
	}

  public abstract void onEnable();
  public abstract void onDisable();
}
