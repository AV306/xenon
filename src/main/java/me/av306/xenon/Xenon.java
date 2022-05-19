package me.av306.xenon;

import me.av306.xenon.config.FeatureConfigGroup;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.features.*;
import me.lortseam.completeconfig.data.Config;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public enum Xenon
{
    INSTANCE;

    public final String MODID = "xenon";
	
    public boolean debug = true;

    public Config config = new Config( this.MODID, new FeatureConfigGroup() );
	
    public final Logger LOGGER = LogManager.getLogger( this.MODID );

    public MinecraftClient client;

    public final Formatting SUCCESS_FORMAT = Formatting.GREEN;
    //public final Formatting WARNING_FORMAT = Formatting.YELLOW;
    //public final Formatting ERROR_FORMAT = Formatting.RED;

    public ArrayList<IToggleableFeature> enabledFeatures = new ArrayList<>();

    private String version;
    public String getVersion() { return version; }
    //public void setVersion( String version ) { this.version = version; }

    private ModContainer modContainer;

    //private boolean updateAvailable = false;
    //public boolean getUpdateAvailable() { return updateAvailable; }

    public void initialise()
    {
        readVersionData();

	    // load configs
	    this.config.load();
			
        // set client
        this.client = MinecraftClient.getInstance();
			
        // register features
        new ConfigMenu();
        new FastBreakFeature();
        new FeatureList();
        new FullBrightFeature();
        new ProximityRadarFeature();
        new QuickChatFeature();
        new ShareLocationFeature();
        new TimerFeature();
        new WailaFeature();
    }


    public void log( String msg ) { if ( debug ) LOGGER.info( msg ); }

    private void readVersionData()
    {
        // set version & TODO: impl check for update
        assert FabricLoader.getInstance().getModContainer( "xenon" ).isPresent();
        modContainer = FabricLoader.getInstance().getModContainer( "xenon" ).get();

        version = modContainer.getMetadata().getVersion().getFriendlyString();
    }

    public void sendInfoMessage( String key )
    {
        Text finalText = new LiteralText( "[Xenon] " ).formatted( Formatting.AQUA )
            .append( new TranslatableText( key ) );
        this.client.player.sendMessage( finalText, false );
    }

    public void sendInfoMessage( Text text )
    {
        Text finalText = new LiteralText( "[Xenon] " ).formatted( Formatting.AQUA )
            .append( text );
        this.client.player.sendMessage( finalText, false );
    }
}
