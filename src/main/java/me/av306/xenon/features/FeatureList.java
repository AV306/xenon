package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.callback.InGameHudRenderCallback;
import me.av306.xenon.feature.IFeature;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.util.ScreenPosition;
import me.av306.xenon.util.color.ColorUtil;
import me.av306.xenon.util.text.TextUtil;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;

import java.util.ArrayList;

public class FeatureList extends IToggleableFeature
{
		private static boolean shouldShowVersion = true;

    private static ScreenPosition position = ScreenPosition.TOP_RIGHT;

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

		Text versionText = new TranslatableText( "text.xenon.version", Xenon.INSTANCE.getVersion() );

        ArrayList<Text> nameTexts = new ArrayList<>();

        //textRenderer.drawWithShadow( matrices, versionText, 5, 5, General.rgbToInt(0, 255, 0) );

        // write feature names

        TextUtil.drawPositionedText( matrices, versionText, position, 0, 0, false, ColorUtil.GREEN );
			
		    for ( IFeature feature : Xenon.INSTANCE.enabledFeatures )
		    {
			    // hide FeatureList itself
				if ( !(feature instanceof FeatureList) )
				{
				    LiteralText nameText = new LiteralText( feature.getName() );
				    nameTexts.add( nameText );
				}
			}

		    // remember to leave space for the version text!
            TextUtil.drawPositionedMultiLineText( matrices, nameTexts.toArray( Text[]::new ), position, 0, 12, false, ColorUtil.WHITE );
			
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
