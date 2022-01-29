package me.av306.xenon.feature.template;

public abstract class IFeature
{
    public boolean isEnabled = false;
	public String name;
	public void setName( String name ) { this.name = name; }

	public void enable()
	{
		if ( isEnabled ) return;
			
		isEnabled = true;

		onEnable();
	}

	public void disable()
	{
		if ( !isEnabled ) return;
			
		isEnabled = false;

		onDisable();
	}
	
    public abstract void onEnable();
    public abstract void onDisable();
}
