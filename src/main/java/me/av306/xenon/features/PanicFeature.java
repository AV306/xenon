package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IFeature;
import me.av306.xenon.feature.IToggleableFeature;

import java.util.ArrayList;
import java.util.List;

public final class PanicFeature extends IFeature
{
    public PanicFeature()
  {
    super( "Panic" );
  }

    @Override
    protected void onEnable()
    {
        // copy the list
        List<IToggleableFeature> list = new ArrayList<>( Xenon.INSTANCE.enabledFeatures );

        // iterate over the enabled ITFs in the copy
        // and disable them
        list.forEach(
                IToggleableFeature::disable
        );

        // destroy the list
    }
}