package me.av306.xenon.config.feature.chat;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.ConfigEntries;

@ConfigEntries( includeAll = true )
public class MultiQuickChatGroup implements XenonConfigGroup
{
    // a GLFW int keycode is mapped to each message

    // Map method
    /*@ConfigEntries.Exclude
    public static HashMap<InputUtil.Key, String> messageMap = new HashMap<>();

    // Array method
    @ConfigEntries.Exclude
    public static ArrayList<String> messageArray = new ArrayList<>();*/

    // hardcode message
    public static String message0 = "";
    public static String message1 = "";
    public static String message2 = "";
    public static String message3 = "";
    public static String message4 = "";
    public static String message5 = "";
    public static String message6 = "";
    public static String message7 = "";
    public static String message8 = "";
    public static String message9 = "";
}
