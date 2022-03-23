package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.callback.InGameHudRenderCallback;
import me.av306.xenon.feature.IFeature;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.util.color.ColorUtil;
import me.av306.xenon.util.ScreenPosition;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;

import java.util.concurrent.TransferQueue;

public class FeatureList extends IToggleableFeature
{
    public ScreenPosition position = ScreenPosition.TOP_RIGHT;

    public FeatureList()
    {
        // set name
        super( "FeatureList" );

		// start enabled by default
		this.isEnabled = true;

        // register listener
        InGameHudRenderCallback.EVENT.register( this::onInGameHudRender );
    }

    private ActionResult onInGameHudRender( MatrixStack matrices, float tickDelta )
    {
        if ( !this.isEnabled ) return ActionResult.PASS;

        TextRenderer textRenderer = Xenon.INSTANCE.client.inGameHud.getTextRenderer();

        Window window = Xenon.INSTANCE.client.getWindow();

        String versionString = "Xenon " + Xenon.INSTANCE.getVersion();

        Text updateText = new TranslatableText( Xenon.INSTANCE.getUpdateAvailable() ? "text.xenon.updateavailable" : "" );
        Text versionText = new TranslatableText( "text.xenon.versionText", updateText );



 
        //textRenderer.drawWithShadow( matrices, versionText, 5, 5, General.rgbToInt(0, 255, 0) );

        // write feature names

        int y;
        switch ( this.position )
        {
            case TOP_LEFT:
                textRenderer.drawWithShadow( matrices, versionText, 5, 5, ColorUtil.rgbToInt(0, 255, 0) );

                y = 5 + 10 + 2;
                for ( IFeature feature : Xenon.INSTANCE.enabledFeatures )
                {
                    textRenderer.drawWithShadow( matrices, feature.getName(), 5, y, ColorUtil.rgbToInt( 255, 255, 255 ) );

                    y += 12;
                }
                break;

            case TOP_RIGHT:
                textRenderer.drawWithShadow( matrices, versionText, window.getScaledWidth() - textRenderer.getWidth( versionString ) - 5, 5, TextUtil.rgbToInt(0, 255, 0) );

                y = 5 + 10 + 2;
                for ( IFeature feature : Xenon.INSTANCE.enabledFeatures )
                {
                    int x = window.getScaledWidth() - textRenderer.getWidth( feature.getName() ) - 5;
                    textRenderer.drawWithShadow( matrices, feature.getName(), x, y, ColorUtil.rgbToInt( 255, 255, 255 ) );

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
