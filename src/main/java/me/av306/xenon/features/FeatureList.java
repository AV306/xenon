package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.FeatureListGroup;
import me.av306.xenon.event.RenderInGameHudEvent;
import me.av306.xenon.event.MinecraftClientEvents;
import me.av306.xenon.feature.IFeature;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.util.render.ScreenPosition;
import me.av306.xenon.util.color.ColorUtil;
import me.av306.xenon.util.text.TextFactory;
import me.av306.xenon.util.text.TextUtil;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
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

        // Hide FL from the list and don't disable it on exit
        this.setShouldHide( true );
        this.setPersistent( true );

		// start enabled by default
        this.enable();
		
        // version data should alr be initialised at this time
        this.versionText = TextFactory.createTranslatable(
                "text.xenon.version", Xenon.INSTANCE.getVersion()
        );

        // register render listener
        RenderInGameHudEvent.AFTER_VIGNETTE.register( this::onInGameHudRender );

        MinecraftClientEvents.JOIN_WORLD.register( world ->
        {
            if ( FeatureListGroup.reEnableOnWorldEnter )
                this.enable();
            return ActionResult.PASS;
        } );
    }

    private ActionResult onInGameHudRender( DrawContext drawContext, float tickDelta )
    {
        if ( !this.isEnabled ) return ActionResult.PASS;

        final boolean shouldShowVersion = FeatureListGroup.showVersion;
        final ScreenPosition position = FeatureListGroup.position;

        // Initialise an empty AL that will contain the feature names
        ArrayList<Text> nameTexts = new ArrayList<>();

        // Now, begin drawing text

        // should we draw the version name?
        if ( shouldShowVersion )
            TextUtil.drawPositionedText(
                    drawContext,
                    this.versionText,
                    position,
                    0, 0,
                    FeatureListGroup.shadow,
                    Xenon.INSTANCE.SUCCESS_FORMAT
            );
			
        // Place the feature names to be drawn in an AL
        // then convert it into a normal array.
        // THis is done because the logic for drawing text on
        // multiple lines at a given ScreenPosition
        // is hidden in TextUtil.drawPositionedMultiLineText,
        // and we don't need to reinvent the motor car here.
        for ( IFeature feature : Xenon.INSTANCE.enabledFeatures )
        {
            // hide FeatureList itself and Debbuger
		    if ( !feature.getShouldHide() )
		  	{
                Text nameText = TextFactory.createLiteral( feature.getName() );
                nameTexts.add( nameText );
			}
        }

        // Begin drawing the feature names from the array.
        // remember to leave space for the version text!
        TextUtil.drawPositionedMultiLineText(
                drawContext,
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
