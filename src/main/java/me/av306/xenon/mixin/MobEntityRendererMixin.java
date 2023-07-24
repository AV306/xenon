package me.av306.xenon.mixin;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.MinecraftClientEvents;
import me.av306.xenon.features.render.HealthDisplayFeature;
import me.av306.xenon.util.text.TextFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;

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
        cir.setReturnValue( HealthDisplayFeature.getInstance().shouldForceEntityNameplate() );
    }
}
