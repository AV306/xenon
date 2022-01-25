package me.av306.xenon.features.interfaces;

public abstract class IFeature
{
    public boolean isEnabled = false;
		public String name;

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
