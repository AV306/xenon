package me.av306.xenon.mixin;

import me.av306.xenon.features.render.HealthDisplayFeature;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin( EntityRenderer.class )
public class EntityRendererMixin<T extends Entity>
{
    /*@Inject(
            method = "hasLabel(Lnet/minecraft/entity/Entity;)Z",
            at = @At( "HEAD" ),
            cancellable = true
    )
    private void onGetHasLabel( T entity, CallbackInfoReturnable<Boolean> cir )
    {
        // This only works on dropped entities, living entities use MobEntityRenderer which overrides this method
        // You can use this to make dropped items have names. Hmmm...
        // TODO: Make this a feature :D
        cir.setReturnValue( false );
    }*/

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

        return HealthDisplayFeature.getInstance().modifyEntityLabelText( entity, text );
    }
}
