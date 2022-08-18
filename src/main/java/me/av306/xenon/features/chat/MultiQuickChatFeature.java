package me.av306.xenon.features.chat;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.MultiQuickChatGroup;
import me.av306.xenon.event.KeyEvent;
import me.av306.xenon.feature.IFeature;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.ActionResult;
import org.lwjgl.glfw.GLFW;

public class MultiQuickChatFeature extends IFeature
{
	public MultiQuickChatFeature()
	{
		super( "MultiQuickChat" );

        KeyEvent.EVENT.register( this::onKey );

        //MultiQuickChatGroup.messagesMap.put( InputUtil.fromKeyCode( InputUtil.GLFW_KEY_1, -1 ), "asdfsdaf" );
    }

    @Override
    protected void keyEvent() {}

    protected ActionResult onKey(
        long window,
        int keycode,
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
            // Ok, it doesn't work.

            // Map method
            /*String message = MultiQuickChatGroup.messageMap.get( InputUtil.fromKeyCode( key, scancode ) );

            try
            {
                assert Xenon.INSTANCE.client.player != null;
                Xenon.INSTANCE.client.player.sendChatMessage( message );
            }
            catch ( NullPointerException ignored ) {}*/

            // Array method
            /*int key = keycode - 48;
            String message = MultiQuickChatGroup.messageArray.get( key );*/

            // Hardcode method
            String message;
            switch ( key )
            {
                case GLFW.GLFW_KEY_0 -> { message = MultiQuickChatGroup.message0; }
                case GLFW.GLFW_KEY_1 -> { message = MultiQuickChatGroup.message1; }
                case GLFW.GLFW_KEY_2 -> { message = MultiQuickChatGroup.message2; }
                case GLFW.GLFW_KEY_3 -> { message = MultiQuickChatGroup.message3; }
                case GLFW.GLFW_KEY_4 -> { message = MultiQuickChatGroup.message4; }
                case GLFW.GLFW_KEY_5 -> { message = MultiQuickChatGroup.message5; }
                case GLFW.GLFW_KEY_6 -> { message = MultiQuickChatGroup.message6; }
                case GLFW.GLFW_KEY_7 -> { message = MultiQuickChatGroup.message7; }
                case GLFW.GLFW_KEY_8 -> { message = MultiQuickChatGroup.message8; }
                case GLFW.GLFW_KEY_9 -> { message = MultiQuickChatGroup.message9; }
                default -> {}
            }

            // send the message
            try
            {
                // throws NPE if message is null
                Xenon.INSTANCE.client.player.sendChatMessage( message );

                // Cancel keypress if message was present and sent successfully
                // Will not be reached if message was not sent
                return ActionResult.FAIL;
            }
            catch ( NullPointerException ignored ) {}
        }

        return ActionResult.PASS;
	}
	
    @Override
    public void onEnable() {}
}
