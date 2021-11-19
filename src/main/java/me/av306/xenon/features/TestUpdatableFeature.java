package me.av306.xenon.features;


import me.av306.xenon.Xenon;
import me.av306.xenon.features.interfaces.IUpdatableFeature;


public class TestUpdatableFeature implements IUpdatableFeature
{
    public static boolean isEnabled = false;


    // gets used when the key is pressed
    @Override
    public void onEnable()
    {
        isEnabled = true;
        Xenon.INSTANCE.LOGGER.info( "Started!" );
    }


    @Override
    public void onDisable()
    {
        isEnabled = false;
        Xenon.INSTANCE.LOGGER.info( "Stopped!" );
    }


    @Override
    public void onUpdate()
    {
        //if ( isEnabled ) Xenon.INSTANCE.LOGGER.info( "Updated!" );

    }


    @Override
    public void toggle()
    {
        if ( isEnabled ) onDisable();
        else onEnable();
    }
}
