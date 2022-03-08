package me.av306.xenon.feature;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IFeature;
import net.minecraft.text.TranslatableText;

public class TakePanoramaFeature extends IFeature
{
	
    private static int resolution = 1024;
    public static int getResolution() { return resolution; }
    public static void setResolution( int res ) { resolution = res; }

		//public TakePanoramaFeature() { super( "TakePanoramaFeature" ); } // Not strictly needed
	
    @Override
    public void onEnable()
    {
        Xenon.INSTANCE.client.takePanorama( Xenon.INSTANCE.client.runDirectory, 1024, 1024 );

        Xenon.INSTANCE.client.player.sendMessage(
                new TranslatableText( "message.xenon.panoramasuccess" ),
                false
        );
    }

    @Override
    public void onDisable() {}
}
