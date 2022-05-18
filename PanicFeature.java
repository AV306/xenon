package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IFeature;

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
      (feature) ->
      {
        feature.disable();
      }
    );
  }
}