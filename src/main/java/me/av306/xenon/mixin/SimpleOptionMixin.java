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
        // Why, Mojang? Why?
        // First the text system, and now this!
        // Why must you do this to us?
        // I mean, I get why you would do this (and the text system too),
        // but it makes life really difficult.
        // Well, what's done is done, and this is how we have to do it now.


        // I just realised something...
        // Microsoft adding the chat reports thing to Minecraft is very similar
        // to how they changed Minecraft's text and options system.

        // All 3 changes add something,
        // usually validation (2/3 of them) to MC,
        // that makes life harder for people
        // (SimpleOption, the need for anti-chatreport mods),
        // but have a semi-legitimate reason for being added
        // ("option validation" (kind of a halfhearted attempt ngl), idk "organisation"?, "player safety" or something)
        if( !Xenon.INSTANCE.client.isRunning() )
        {
            // if we're somehow not running, just change it
            value = newValue;
            return;
        }

        if( !Objects.equals( value, newValue ) )
        {
            value = newValue;
            changeCallback.accept( value );
        }
    }
}
