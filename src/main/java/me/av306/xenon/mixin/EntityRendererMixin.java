package me.av306.xenon.mixin;

import me.av306.xenon.event.EntityRendererEvents;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( EntityRenderer.class )
public class EntityRendererMixin<T extends Entity>
{
    @Inject(
            method = "hasLabel(Lnet/minecraft/entity/Entity;)Z",
            at = @At( "HEAD" ),
            cancellable = true
    )
    private void onGetHasLabel( T entity, CallbackInfoReturnable<Boolean> cir )
    {
        // This only works on dropped entities, living entities use MobEntityRenderer which overrides this method
        // You can use this to make dropped items have names. Hmmm...
        ActionResult result = EntityRendererEvents.GET_HAS_LABEL.invoker().onGetHasLabel( entity );
        if ( result == ActionResult.SUCCESS )
            cir.setReturnValue( true );

        else if ( result == ActionResult.FAIL )
            cir.setReturnValue( false );
    }

    @ModifyVariable(
            argsOnly = true,
            method = "renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "LOAD",
                    ordinal = 1
            )
    )
    private Text modifyLabelText( Text text, T entity, Text textOther, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light )
    {
        //if ( !HealthDisplayFeature.getInstance().getIsEnabled() ) return text1;

        // Do the thing to give features a chance to set the event data
        EntityRendererEvents.GET_LABEL_TEXT.invoker().onGetLabelText( entity, text );

        Text textOverride = EntityRendererEvents.EventData.LABEL_TEXT_OVERRIDE;

        return EntityRendererEvents.EventData.SHOULD_OVERRIDE_LABEL_TEXT && textOverride != null ? EntityRendererEvents.EventData.LABEL_TEXT_OVERRIDE : text;
    }
}
