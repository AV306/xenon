package me.av306.xenon.mixin;

import me.av306.xenon.config.GeneralConfigGroup;
import me.av306.xenon.event.ChatOutputEvent;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( ChatScreen.class )
public class ChatScreenMixin extends Screen
{
	@Shadow
	protected TextFieldWidget chatField;

	@Shadow
	public String normalize( String t ) { return null; }

	protected ChatScreenMixin( Text title )
	{
		super( title );
	}

	@Inject(
			at = @At( "TAIL" ),
			method = "init()V"
	)
	protected void onInit( CallbackInfo ci )
	{
		if ( GeneralConfigGroup.infiniteChatLength ) chatField.setMaxLength( Integer.MAX_VALUE );
	}

	// Trust
	@Inject(
			at = @At( "HEAD" ),
			method = "sendMessage(Ljava/lang/String;Z)V",
			cancellable = true
	)
	public void onSendMessage( String message, boolean addToHistory, CallbackInfo ci )
	{
		if ( (message = normalize(message)).isEmpty() ) return;

		ActionResult result = ChatOutputEvent.EVENT.invoker().interact( message );

		if ( result == ActionResult.FAIL )
		{
			ci.cancel();
			return;
		}
	}
}
