package me.av306.xenon.config.feature;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.ConfigEntries;
import net.minecraft.client.util.InputUtil;

import java.util.HashMap;

@ConfigEntries( includeAll = true )
public class MultiQuickChatGroup implements XenonConfigGroup
{
    // a GLFW int keycode is mapped to each message
    @ConfigEntries.Exclude
    public static HashMap<InputUtil.Key, String> messages = new HashMap<>();
}
