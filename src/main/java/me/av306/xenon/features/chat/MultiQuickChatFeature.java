package me.av306.xenon.features.chat;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.MultiQuickChatGroup;
import me.av306.xenon.event.KeyEvent;
import me.av306.xenon.feature.IFeature;
import net.minecraft.util.ActionResult;
import org.lwjgl.glfw.GLFW;

public class MultiQuickChatFeature extends IFeature
{
	public MultiQuickChatFeature()
	{
		super( "MultiQuickChat" );

        KeyEvent.EVENT.register( this::onKey );
    }

    @Override
    protected void keyEvent() {}

    protected ActionResult onKey(
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

            String message = MultiQuickChatGroup.messages.get( /*InputUtil.fromKeyCode( key, scancode )*/key );

            try
            {
                assert Xenon.INSTANCE.client.player != null;
                Xenon.INSTANCE.client.player.sendChatMessage( message );
            }
            catch ( NullPointerException ignored ) {}
        }

        return ActionResult.PASS;
	}
	
    @Override
    public void onEnable() {}
}
