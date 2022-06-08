package me.av306.xenon;

import me.av306.xenon.config.FeatureConfigGroup;
import me.av306.xenon.feature.IFeature;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.features.*;
import me.lortseam.completeconfig.data.Config;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;

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

    // this is most likely going to be used to
    // resolve a feature by its name (e.g. CommandProcessor),
    // so I put it in this order.
    public HashMap<String, IFeature> featureRegistry = new HashMap<>();
    public ArrayList<IToggleableFeature> enabledFeatures = new ArrayList<>();

    private String version;
    public String getVersion() { return version; }
    //public void setVersion( String version ) { this.version = version; }

    public ModContainer modContainer;

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
        new CommandProcessor();
        new ConfigMenu();
        //new ExtraReachFeature(); // FIXME: desyncs
        new FastBreakFeature();
        new FeatureList();
        new FullBrightFeature();
        new HighJumpFeature();
        new PanicFeature();
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
        Text finalText = new LiteralTextContent( "[Xenon] " ).formatted( Formatting.AQUA )
            .append( new TranslatableTextContent( key ) );
        this.client.player.sendMessage( finalText, false );
    }

    public void sendInfoMessage( Text text )
    {
        Text finalText = new LiteralTextContent( "[Xenon] " ).formatted( Formatting.AQUA )
            .append( text );
        this.client.player.sendMessage( finalText, false );
    }

    public void sendErrorMessage( String key )
    {
        Text finalText = new LiteralTextContent( "[Xenon] " ).formatted( Formatting.RED )
                .append( new TranslatableTextContent( key ) );
        this.client.player.sendMessage( finalText, false );
    }

    public void sendErrorMessage( Text text )
    {
        Text finalText = new LiteralTextContent( "[Xenon] " ).formatted( Formatting.RED )
                .append( text );
        this.client.player.sendMessage( finalText, false );
    }
}
