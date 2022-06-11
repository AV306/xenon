package me.av306.xenon.mixin;


import me.av306.xenon.Xenon;
import me.av306.xenon.mixinterface.SimpleOptionAccessor;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;
import java.util.function.Consumer;

@Mixin( SimpleOption.class )
public abstract class SimpleOptionMixin<T> implements SimpleOptionAccessor<T>
{
    @Shadow
    T value;

    @Shadow
    @Final
    private Consumer<T> changeCallback;


    // Ugh.
    @Override
    public void forceSetValue(T newValue)
    {
        // This is basically what akready exists in setValue,
        // minus the validation.
        // Why, Mojang? Why?
        // First the text system, and now this!
        // Why must you do this to us?
        // I mean, I get why you would do this (and the text system too),
        // but it makes life really difficult.
        // Well, what's done is done, and this is how we have to do it now.
        if( !Xenon.INSTANCE.client.isRunning() )
        {
            value = newValue;
            return;
        }

        if( !Objects.equals( value, newValue ) )
        {
            value = newValue;
            changeCallback.accept( value) ;
        }
    }
}
