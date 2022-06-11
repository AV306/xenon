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

    @Override
    public void forceSetValue(T newValue)
    {
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
