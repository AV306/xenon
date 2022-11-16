package me.av306.xenon.features.render;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.RenderInGameHudEvent;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.util.ScreenPosition;
import me.av306.xenon.util.color.ColorUtil;
import me.av306.xenon.util.text.TextFactory;
import me.av306.xenon.util.text.TextUtil;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;

public class DataHudFeature extends IToggleableFeature
{
    public DataHudFeature()
    {
        super( "DataHUD" );

        RenderInGameHudEvent.AFTER_VIGNETTE.register( this::onInGameHudRender );
    }

    private ActionResult onInGameHudRender( MatrixStack matrixStack, float tickDelta )
    {
        if ( this.isEnabled )
        {
            TextUtil.drawPositionedText(
                    matrixStack,
                    TextFactory.createLiteral( Xenon.INSTANCE.client.fpsDebugString ),
                    ScreenPosition.TOP_LEFT,
                    0, 0,
                    false,
                    ColorUtil.WHITE
            );
        }

        return ActionResult.PASS;
    }

    @Override
    protected void onEnable() {}

    @Override
    protected void onDisable() {}
}
