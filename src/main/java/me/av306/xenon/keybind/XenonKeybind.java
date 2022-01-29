package me.av306.xenon.keybind;

import me.av306.xenon.feature.template.IFeature;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil.Type;


/**
 * POD class for Xenon keybinds.
 */
public class XenonKeybind<T extends IFeature>
{
    public KeyBinding keybind;

    public T feature;


    public XenonKeybind( String translation, Type type, int key, String category, T feature )
    {
      this.keybind = KeyBindingHelper.registerKeyBinding(
				new KeyBinding( translation, type, key, category )
			);
       this.feature = feature;
    }
}