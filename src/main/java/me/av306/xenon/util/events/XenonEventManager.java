package me.av306.xenon.util.events;

import me.av306.xenon.util.events.eventobjects.ChatHudAddMessageEvent;
import me.av306.xenon.util.events.listeners.ChatHudAddMessageEventListener;

import java.util.ArrayList;

public class XenonEventManager
{
    public ArrayList<ChatHudAddMessageEventListener> chatHudAddMessageEventListeners = new ArrayList<>();


    public void receiveEvent( ChatHudAddMessageEvent event )
    {
        for ( ChatHudAddMessageEventListener listener : chatHudAddMessageEventListeners )
        {
            listener.fire( event );
        }
    }
}
