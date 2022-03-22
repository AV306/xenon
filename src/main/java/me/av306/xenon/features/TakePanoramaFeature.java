package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IFeature;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.io.File;

public class TakePanoramaFeature extends IFeature
{
	
    private static int resolution = 512;
    public static int getResolution() { return resolution; }
    public static void setResolution( int res ) { resolution = res; }

	public TakePanoramaFeature() { super( "TakePanoramaFeature" ); } // Not strictly needed
	
    @Override
    public void onEnable()
    {
        File runDir = Xenon.INSTANCE.client.runDirectory;
        File panoramaFile = new File( runDir, "screenshots" );

        // A little bit of info:
        // `runDirectory` refers to the root of the gamedir,
        // and through a bunch of nested func calls,
        // it saves in the screenshots subdir.
        // So we pass the root gamedir.
        Xenon.INSTANCE.client.takePanorama( runDir, getResolution(), getResolution() );

        Text linkToPanoramas = new LiteralText( panoramaFile.getName() + File.pathSeparator + "panorama0.png" )
                .formatted( Formatting.UNDERLINE )
                .styled(
                        style -> style.withClickEvent(
                                new ClickEvent( ClickEvent.Action.OPEN_FILE, panoramaFile.getAbsolutePath() )
                        )
                );

        Text msg = new TranslatableText( "text.xenon.takepanorama.success", linkToPanoramas )
                .formatted( Xenon.INSTANCE.SUCCESS_FORMAT );

        Xenon.INSTANCE.client.player.sendMessage(
                msg,
                false
        );
    }

    @Override
    public void onDisable() {}
}
