package me.av306.xenon.mixin;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin( GameOptions.class )
public interface GameOptionsAccessor
{
    @Accessor
    SimpleOption<Double> getGamma();

    @Accessor( "gamma" )
    void setGamma( SimpleOption<Double> gamma );
}
