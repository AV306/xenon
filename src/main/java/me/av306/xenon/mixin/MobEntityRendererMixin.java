package me.av306.xenon.mixin;

import me.av306.xenon.event.MobEntityRendererEvents;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;

import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( MobEntityRenderer.class )
public class MobEntityRendererMixin<T extends MobEntity, M extends EntityModel<T>>
{
    @Inject(
        method = "hasLabel(Lnet/minecraft/entity/mob/MobEntity;)Z",
        at = @At( "HEAD" ),
        cancellable = true
    )
    private void onGetHasLabel( T entity, CallbackInfoReturnable<Boolean> cir )
    {
        // Don't block name tags when disabled
        //if ( HealthDisplayFeature.getInstance().shouldForceEntityNameplate() )
            //cir.setReturnValue( true );

        if ( MobEntityRendererEvents.RENDER_LABEL_TEXT.invoker().onGetHasLabel( entity ) == ActionResult.FAIL )
            cir.setReturnValue( true );
    }
}
