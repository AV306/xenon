package me.av306.xenon.mixin;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.MinecraftClientEvents;
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

@Mixin( EntityRenderer.class )
public class EntityRendererMixin<T extends Entity>
{
    @ModifyVariable(
            argsOnly = true,
            method = "renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "LOAD",
                    ordinal = 1
            ),
            print = true
    )
    private Text modifyLabelText( Text text1, T entity, Text text2, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light )
    {
        Xenon.INSTANCE.LOGGER.info( String.valueOf( text1.equals( text2 ) ) );
        try

        {
            LivingEntity livingEntity = (LivingEntity) entity;

            text1 = text1.copy().append( TextFactory.createLiteral( "(%f health)", String.valueOf( livingEntity.getHealth() ) ) );
        }
        catch ( ClassCastException cce ) { Xenon.INSTANCE.LOGGER.info( "Skipped non-living entity" ); }

        return text1;
    }

    /*@Redirect( // Fck this won't work because `text` is a local var, not a field
            method = "renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "FIELD", // Target the `text` field
                    target = "Lnet/minecraft/client/render/entity/EntityRenderer;text:Lnet/minecraft/text/Text;",
                    opcode = Opcodes.GETFIELD // Target the field access
            )
    )
    private Text getLabelText( EntityRenderer owner )
    {
        //Xenon.INSTANCE.LOGGER.info( text.toString() );

        try
        {
            LivingEntity livingEntity = (LivingEntity) entity;

            text = text.copy().append( TextFactory.createLiteral( "(%f health)", String.valueOf( livingEntity.getHealth() ) ) );
        }
        catch ( ClassCastException cce ) { Xenon.INSTANCE.LOGGER.info( "Skipped non-living entity" ); }

        return TextFactory.createLiteral( "AAAAAAAAAAAAAAAAA" );
    }*/
}
