package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.callback.InGameHudRenderCallback;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.util.ScreenPosition;
import me.av306.xenon.util.color.ColorUtil;
import me.av306.xenon.util.text.TextUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.ActionResult;
import org.lwjgl.opengl.GL15;

public class DataHudFeature extends IToggleableFeature
{
    public DataHudFeature()
    {
        super( "DataHUD" );

        InGameHudRenderCallback.EVENT.register( this::onInGameHudRender );
    }

    private ActionResult onInGameHudRender( MatrixStack matrixStack, float tickDelta )
    {
        if ( this.isEnabled )
        {
            GL15.glDisable( GL15.GL_BLEND );

            TextUtil.drawPositionedText(
                matrixStack,
                new LiteralText( Xenon.INSTANCE.client.fpsDebugString ),
                ScreenPosition.TOP_LEFT,
                0, 0,
                false,
                ColorUtil.WHITE
            );

            GL15.glEnable( GL15.GL_BLEND );
        }

        return ActionResult.PASS;
    }

    @Override
    public void onEnable()
    {

    }

    @Override
    public void onDisable()
    {

    }
}
