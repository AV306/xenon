/*package me.av306.xenon.features;

import me.av306.xenon.event.callback.ChatHudAddMessageCallback;
import me.av306.xenon.feature.IToggleableFeature;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public class CommandProcessorFeature extends IToggleableFeature
{
    public CommandProcessorFeature()
    {
        super( "CommandProcessor" );
        ChatHudAddMessageCallback.AFTER_VIGNETTE.register( this::onChatHudAddMessage );
    }

    private ActionResult onChatHudAddMessage( Text message )
    {
        if ( this.isEnabled && message.getString().startsWith( "!" ) )
        {
            // is a command && is enabled, now does it exist?
            String[] command = message.getString()
                    .toLowerCase()
                    .replace( '!', ' ' )
                    .strip()
                    .split( " " );
        }

        return ActionResult.PASS;
    }

    @Override
    public void onEnable() { }

    @Override
    public void onDisable() { }
}
*/
