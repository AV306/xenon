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
        // FIXME: this crashes MC, need to deobfuscate stacktrace
    
        // copy the list through copy-construction
        // because disabling the features will mutate the list,
        // causing errors
        List<IToggleableFeature> enabledFeatures_copy = new ArrayList<>( Xenon.INSTANCE.enabledFeatures );

        // iterate over the enabled ITFs in the copy
        // and disable them
        enabledFeatures_copy.forEach(
                IToggleableFeature::disable
        );
    }
}
