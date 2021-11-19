package me.av306.xenon.keybindutils;


import me.av306.xenon.features.interfaces.IFeature;
import net.minecraft.client.option.KeyBinding;


/**
 * POD class for Xenon keybinds.
 */
public class XenonKeybind<T extends IFeature>
{
    public KeyBinding keybind;

    public T feature;


    public XenonKeybind( KeyBinding keybind, T feature )
    {
        this.keybind = keybind;
        this.feature = feature;
    }
}