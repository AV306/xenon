package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.features.interfaces.IFeature;
import net.minecraft.text.Text;

import java.io.File;

public class TakePanoramaFeature extends IFeature
{
		private static int resolution = 1024;
		public static int getResolution() { return resolution; }
		public static void setResolution( int res ) { resolution = res; }
	
    @Override
    public void onEnable()
    {
        Xenon.INSTANCE.client.takePanorama( Xenon.INSTANCE.client.runDirectory, 1024, 1024 );

        //Xenon.INSTANCE.client.player.sendMessage( text, false );
    }

    @Override
    public void onDisable() {}
}
