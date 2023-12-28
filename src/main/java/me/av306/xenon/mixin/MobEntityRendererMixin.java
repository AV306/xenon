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
        // Decide whether to force nameplates, block them or do nothing
        ActionResult result = MobEntityRendererEvents.GET_HAS_LABEL.invoker().onGetHasLabel( entity );

        if ( result == ActionResult.SUCCESS )
            cir.setReturnValue( true );
        else if ( result == ActionResult.FAIL )
            cir.setReturnValue( false );
    }
}
