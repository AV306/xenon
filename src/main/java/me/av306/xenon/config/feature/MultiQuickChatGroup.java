package me.av306.xenon.config.feature;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;
import net.minecraft.client.util.InputUtil.Key;

@ConfigEntries( includeAll = true )
public class MultiQuickChatGroup implements XenonConfigGroup
{
    // a GLFW int keycode is mapped to each message
    public static Map<InputUtil.Key, String> messages = new HashMap<>();
}
