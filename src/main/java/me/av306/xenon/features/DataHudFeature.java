package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.callback.InGameHudRenderCallback;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.util.General;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;

public class DataHudFeature extends IToggleableFeature
{
    public DataHudFeature()
    {
        // set name
        super( "DataHUD" );

        // register listener
        InGameHudRenderCallback.EVENT.register( this::onInGameHudRender );
    }

    private ActionResult onInGameHudRender( MatrixStack matrices, float tickDelta )
    {
        Xenon.INSTANCE.log( "render" );
        TextRenderer textRenderer = Xenon.INSTANCE.client.inGameHud.getTextRenderer();

        Window window = Xenon.INSTANCE.client.getWindow();

        textRenderer.draw(  matrices, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", 0, window.getHeight() - 10, 0 );

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
