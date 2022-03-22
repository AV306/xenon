package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.callback.InGameHudRenderCallback;
import me.av306.xenon.feature.IFeature;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.util.General;
import me.av306.xenon.util.Position;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;

public class FeatureList extends IToggleableFeature
{
    public Position position = Position.TOP_RIGHT;

    public DataHudFeature()
    {
        // set name
        super( "DataHUD" );

        // register listener
        InGameHudRenderCallback.EVENT.register( this::onInGameHudRender );
    }

    private ActionResult onInGameHudRender( MatrixStack matrices, float tickDelta )
    {
        if ( this.isEnabled ) return ActionResult.PASS; // on by default

        TextRenderer textRenderer = Xenon.INSTANCE.client.inGameHud.getTextRenderer();

        Window window = Xenon.INSTANCE.client.getWindow();

        String versionString = "Xenon " + Xenon.INSTANCE.getVersion();

        //textRenderer.drawWithShadow( matrices, versionString, 5, 5, General.rgbToInt(0, 255, 0) );

        // write feature names

        int y;
        switch ( this.position )
        {
            case TOP_LEFT:
                textRenderer.drawWithShadow( matrices, versionString, 5, 5, General.rgbToInt(0, 255, 0) );

                y = 5 + 10 + 2;
                for ( IFeature feature : Xenon.INSTANCE.enabledFeatures )
                {
                    textRenderer.drawWithShadow( matrices, feature.getName(), 5, y, General.rgbToInt( 255, 255, 255 ) );

                    y += 12;
                }
                break;

            case TOP_RIGHT:
                textRenderer.drawWithShadow( matrices, versionString, window.getScaledWidth() - textRenderer.getWidth( versionString ) - 5, 5, General.rgbToInt(0, 255, 0) );

                y = 5 + 10 + 2;
                for ( IFeature feature : Xenon.INSTANCE.enabledFeatures )
                {
                    int x = window.getScaledWidth() - textRenderer.getWidth( feature.getName() ) - 5;
                    textRenderer.drawWithShadow( matrices, feature.getName(), x, y, General.rgbToInt( 255, 255, 255 ) );

                    y += 12;
                }
                break;

            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
                break;
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
