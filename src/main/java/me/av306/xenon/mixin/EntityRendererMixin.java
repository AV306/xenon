package me.av306.xenon.mixin;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.MinecraftClientEvents;
import me.av306.xenon.features.render.HealthDisplayFeature;
import me.av306.xenon.util.text.TextFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.minecraft.client.WindowEventHandler;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
        // You can use this to make dropped items have names. Hmmm...
        // TODO: Make this a feature :D
        cir.setReturnValue( entity instanceof LivingEntity && HealthDisplayFeature.getInstance().getIsEnabled() );
    }

    /*@Inject(
            method = "render(Lnet/minecraft/entity/Entity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At( "HEAD" ),
            cancellable = true
    )
    private void onRender( T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci )
    {

    }*/

    @ModifyVariable(
            argsOnly = true,
            method = "renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "LOAD",
                    ordinal = 1
            )
    )
    private Text modifyLabelText( Text text1, T entity, Text text2, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light )
    {
        if ( !HealthDisplayFeature.getInstance().getIsEnabled() ) return text1;

        try
        {
            LivingEntity livingEntity = (LivingEntity) entity;

            Text healthText = TextFactory.createLiteral( String.format( " §c(%f♥)§r", livingEntity.getHealth() ) );

            if ( livingEntity.hasCustomName() )
                text1 = text1.copy().append( healthText );
            else return healthText;
        }
        catch ( ClassCastException ignored ) {}

        return text1;
    }
}
