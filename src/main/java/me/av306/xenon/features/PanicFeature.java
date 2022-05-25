package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IFeature;
import me.av306.xenon.feature.IToggleableFeature;

public final class PanicFeature extends IFeature
{
    public PanicFeature()
  {
    super( "Panic" );
  }

    @Override
    protected void onEnable()
    {
        Xenon.INSTANCE.enabledFeatures.forEach(
                IToggleableFeature::disable
        );
    }
}