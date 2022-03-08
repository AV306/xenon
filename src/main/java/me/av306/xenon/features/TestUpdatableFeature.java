package me.av306.xenon.feature;


import me.av306.xenon.Xenon;
import me.av306.xenon.feature.template.IUpdatableFeature;


public class TestUpdatableFeature extends IUpdatableFeature
{


    // gets used when the key is pressed
    @Override
    public void onEnable()
    {
        Xenon.INSTANCE.LOGGER.info( "Started!" );
    }


    @Override
    public void onDisable()
    {
        Xenon.INSTANCE.LOGGER.info( "Stopped!" );
    }


    @Override
    public void onUpdate()
    {
        //if ( isEnabled ) Xenon.INSTANCE.LOGGER.info( "Updated!" );

    }
}
