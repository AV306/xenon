package me.av306.xenon.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;

public class KeybindUtil
{
    public static KeyBinding registerKeybind( String translation, int key, String category )
    {
        KeyBinding kb = new KeyBinding(
                "key.xenon." + translation,
                InputUtil.Type.KEYSYM,
                key,
                "category.xenon." + category
        );
        KeyBindingHelper.registerKeyBinding( kb );

        return kb;
    }
}