package me.av306.xenon.features.chat;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.QuickChatGroup;
import me,av306.xenon.event.KeyEvent;
import me.av306.xenon.feature.IFeature;
import me.av306.xenon.util.text.TextFactory;

public class MultiQuickChatFeature extends IFeature
{
	public MultiQuickChatFeature()
	{
		super( "MultiQuickChat" );

        KeyEvent.EVENT.register( this::onKey );
    }

    @Override
    protected keyEvent() {}

    protected ActionResult onKeyboardKey(
        long window,
        int key,
        int scancode,
        int action,
        int modifiers
    )
	{
		if ( this.keyBinding.isPressed() && action == GLFW.GLFW_PRESS )
        {
            // key was pressed while holding our key
            // TODO: Find a way to add optional configs in Cloth Config
            // Well, CC has support for Map...

            String message = MultiQuickChatGroup.messages.get( InputUtil.fromKeyCode( key, scancode ) );

            try
            {
                Xenon.INSTANCE.sendChatMessage( message );
            }
            catch ( NullPointerException ignored ) {}
        }

        Xenon.INSTANCE.client.sendChatMessage( message );

        return ActionResult.PASS;
	}
	
    @Override
    public void onEnable() {}
}
