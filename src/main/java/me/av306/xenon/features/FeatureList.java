package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.FeatureListGroup;
import me.av306.xenon.event.RenderInGameHudEvent;
import me.av306.xenon.feature.IFeature;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.util.ScreenPosition;
import me.av306.xenon.util.color.ColorUtil;
import me.av306.xenon.util.text.TextFactory;
import me.av306.xenon.util.text.TextUtil;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import java.util.ArrayList;

public final class FeatureList extends IToggleableFeature
{
    private final Text versionText;

    public FeatureList()
    {
        // set name
        super( "FeatureList" );

		// start enabled by default
        this.enable();
		
        // version data should alr be initialised at this time
        this.versionText = Xenon.INSTANCE.getUpdateAvailable() ?
                TextFactory.createTranslatable( "text.xenon.version.updateavailable", Xenon.INSTANCE.getVersion(), Xenon.INSTANCE.getLatestVersion() ) :
                TextFactory.createTranslatable( "text.xenon.version", Xenon.INSTANCE.getVersion() );

        // register listener
        RenderInGameHudEvent.AFTER_VIGNETTE.register( this::onInGameHudRender );
    }

    // TODO: profile
    private ActionResult onInGameHudRender( MatrixStack matrices, float tickDelta )
    {
        if ( !this.isEnabled ) return ActionResult.PASS;

        final boolean shouldShowVersion = FeatureListGroup.showVersion;
        final ScreenPosition position = FeatureListGroup.position;

        TextRenderer textRenderer = Xenon.INSTANCE.client.inGameHud.getTextRenderer();
        Window window = Xenon.INSTANCE.client.getWindow();

        ArrayList<Text> nameTexts = new ArrayList<>();

        // write feature names

        if ( shouldShowVersion )
            TextUtil.drawPositionedText(
                    matrices,
                    this.versionText,
                    position,
                    0, 0,
                    FeatureListGroup.shadow,
                    Xenon.INSTANCE.SUCCESS_FORMAT
            );
			
        for ( IFeature feature : Xenon.INSTANCE.enabledFeatures )
        {
            // hide FeatureList itself
		    if ( !(feature instanceof FeatureList) )
		  	{
                Text nameText = TextFactory.createLiteral( feature.getName() );
                nameTexts.add( nameText );
			}
        }

        // remember to leave space for the version text!
        TextUtil.drawPositionedMultiLineText(
                matrices,
                nameTexts.toArray( Text[]::new ),
                position,
                0, 12,
                FeatureListGroup.shadow,
                ColorUtil.WHITE
        );

        return ActionResult.PASS;
    }

    @Override
    protected void onEnable() {}

    @Override
    protected void onDisable() {}
}
