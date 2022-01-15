package me.av306.xenon.features;


import me.av306.xenon.Xenon;
import me.av306.xenon.features.interfaces.IUpdatableFeature;


public class ProximityRadarFeature implements IUpdatableFeature
{
	private static final String NAME = "ProximityRadar";
	public static String getName() { return NAME; }
	
	private static boolean isEnabled = false;


    // gets used when the key is pressed
    @Override
    public void onEnable()
    {
      isEnabled = true;
    }


    @Override
    public void onDisable()
    {
        isEnabled = false;
    }


    @Override
    public void onUpdate()
    {
				
    }


    @Override
    public void toggle()
    {
        if ( isEnabled ) onDisable();
        else onEnable();

				Xenon.INSTANCE.CLIENT.player.sendMessage(
        	new LiteralText(
          		NAME + " " + (isEnabled ? "ENABLED" : "DISABLED") + "!"
        	),
        	true
      	);
    }
}
