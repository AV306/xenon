package me.av306.xenon.features.interfaces;

public abstract class IFeature
{
    public static boolean isEnabled = false;
		public static String name;

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
