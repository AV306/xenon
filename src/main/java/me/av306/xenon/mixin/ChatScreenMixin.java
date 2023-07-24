package me.av306.xenon.mixin;

import me.av306.xenon.config.GeneralConfigGroup;
import me.av306.xenon.event.ChatOutputEvent;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( ChatScreen.class )
public class ChatScreenMixin extends Screen
{
	@Shadow
	protected TextFieldWidget chatField;

	@Shadow
	public String normalize( String t ) { return null; }

	protected ChatScreenMixin (Text title )
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
			method = "sendMessage(Ljava/lang/String;Z)Z",
			cancellable = true
	)
	public void onSendMessage( String message, boolean addToHistory, CallbackInfoReturnable<Boolean> cir )
	{
		if( (message = normalize(message)).isEmpty() ) return;

		ActionResult result = ChatOutputEvent.EVENT.invoker().interact( message );

		if ( result == ActionResult.FAIL )
		{
			cir.setReturnValue( true );
			return;
		}

		// Unlike Wurst, this doesn't let you change the message.
		// You shouldn't need to do that anyway.
	}
}
