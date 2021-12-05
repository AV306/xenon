package me.av306.xenon.util.events.eventobjects;


import me.av306.xenon.util.events.eventobjects.Event;
import net.minecraft.text.Text;

public class ChatHudAddMessageEvent extends Event
{
    public Text text;

    public int chatLineId;


    public ChatHudAddMessageEvent( Text text, int chatLineId )
    {
        this.text = text;
        this.chatLineId = chatLineId;
    }
}
