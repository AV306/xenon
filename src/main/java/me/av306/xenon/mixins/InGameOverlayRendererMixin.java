package me.av306.xenon.mixins;

import me.av306.xenon.Xenon;

import me.av306.xenon.features.NoOverlayFeature;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin( InGameOverlayRenderer.class )
public class InGameOverlayRendererMixin
{
    ///*
    @Inject(
            at = {@At("HEAD")},
            method = {
                    "renderOverlays(Lnet/minecraft/client/MinecraftClient; Lnet/minecraft/client/util/math/MatrixStack;)V"
            }
    )
    public static void onRenderOverlays( MinecraftClient client, MatrixStack matrixStack, CallbackInfo ci )
    {
        //else
        {
            Mouse mouse = Xenon.INSTANCE.CLIENT.mouse;
            Overlay overlay = new Overlay()
            {
                @Override
                public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
                {
                    drawCenteredTextWithShadow(
                            matrices,
                            Xenon.INSTANCE.CLIENT.textRenderer,
                            (OrderedText) new LiteralText( "asfddddddd" ),
                            Xenon.INSTANCE.CLIENT.currentScreen.width / 2,
                            Xenon.INSTANCE.CLIENT.currentScreen.height / 2,
                            0
                    );

                    matrices.push();
                }
            };

            Xenon.INSTANCE.CLIENT.setOverlay( overlay );

            overlay.render( matrixStack, (int) mouse.getX(), (int) mouse.getY(), 0f );
        }
    }
    //*/


    ///*
    @Inject(
            at = {@At("HEAD")},
            method = {
                    "renderFireOverlay(Lnet/minecraft/client/MinecraftClient; Lnet/minecraft/client/util/math/MatrixStack;)V"
            },
            cancellable = true
    )
    private static void onRenderFireOverlay( MinecraftClient client, MatrixStack matrixStack, CallbackInfo ci )
    {
        Xenon.INSTANCE.LOGGER.info( "u on fire" );
        if ( NoOverlayFeature.isEnabled ) ci.cancel();
    }
    //*/


    /*
    @Inject(
            at = {@At("HEAD")},
            method = {
                    "renderUnderwaterOverlay(Lnet/minecraft/client/MinecraftClient; Lnet/minecraft/client/util/math/MatrixStack;)V"
            },
            cancellable = true
    )
    private static void onRenderUnderwaterOverlay( MinecraftClient minecraftClient, MatrixStack matrixStack, CallbackInfo ci )
    {
        if ( NoOverlayFeature.isEnabled ) ci.cancel();
    }
    */
}