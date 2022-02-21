package me.av306.xenon.feature.template;

import me.av306.xenon.Xenon;

public abstract class IFeature
{
  public boolean isEnabled = false;
	
	public String name = "IFeature";
	public void setName( String name ) { this.name = name; }

	public IFeature() { this.setName( name ); }

	public void init()
	{
		// initialises the keybind by registering its name.
		Xenon.INSTANCE.featureManager.getFeatureMap().put( name, this );
	}
	
	public void enable()
	{
		if ( isEnabled ) return;
			
		isEnabled = true;

		Xenon.INSTANCE.featureManager.getEnabledFeatures().add( name );

		onEnable();
	}

	public void disable()
	{
		if ( !isEnabled ) return;
			
		isEnabled = false;

		Xenon.INSTANCE.featureManager.getEnabledFeatures().remove( name );
		
		onDisable();
	}

  public abstract void onEnable();
  public abstract void onDisable();
}
