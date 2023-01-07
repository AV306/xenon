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
    public void forceSetValue( T newValue )
    {
        // This is basically what already exists in setValue,
        // minus the validation.

        // [11-9-2022 Removed dramatic monologue]
        if ( !Xenon.INSTANCE.client.isRunning() )
        {
            // if we're somehow not running, just change it
            value = newValue;
            return;
        }

        if ( !Objects.equals( value, newValue ) )
        {
            value = newValue;
            changeCallback.accept( value );
        }
    }
}
