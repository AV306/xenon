package me.av306.xenon;

import me.av306.xenon.config.FeatureConfigGroup;
import me.av306.xenon.feature.*;
import me.av306.xenon.features.*;
import me.av306.xenon.features.chat.MultiQuickChatFeature;
import me.av306.xenon.features.chat.QuickChatFeature;
import me.av306.xenon.features.chat.ShareLocationFeature;
import me.av306.xenon.features.fovchallenge.DecreaseFovFeature;
import me.av306.xenon.features.fovchallenge.IncreaseFovFeature;
import me.av306.xenon.features.movement.CreativeFlightFeature;
import me.av306.xenon.features.movement.TimerFeature;
import me.av306.xenon.features.render.*;
import me.av306.xenon.mixin.MinecraftClientAccessor;
import me.av306.xenon.util.text.TextFactory;
import me.lortseam.completeconfig.data.Config;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public enum Xenon
{
    INSTANCE;

    public final String MODID = "xenon";
	
    public final boolean debug = true;

    public Config config = new Config( this.MODID, new FeatureConfigGroup() );
	
    public final Logger LOGGER = LogManager.getLogger( this.MODID );

    public MinecraftClient client;
    public MinecraftClientAccessor clientAccessor;

    public final Formatting SUCCESS_FORMAT = Formatting.GREEN;
    public final Formatting MESSAGE_FORMAT = Formatting.AQUA;
    public final Formatting WARNING_FORMAT = Formatting.YELLOW;
    public final Formatting ERROR_FORMAT = Formatting.RED;

    // this is most likely going to be used
    // to resolve a feature by its name (e.g. CommandProcessor),
    // so I put it in this order.
    public HashMap<String, IFeature> featureRegistry = new HashMap<>();
    public ArrayList<IToggleableFeature> enabledFeatures = new ArrayList<>();

    private String version;
    public String getVersion() { return version; }
    
    public ModContainer modContainer;

    private final Text namePrefix = TextFactory.createLiteral( "[Xenon] " )
            .formatted( this.MESSAGE_FORMAT );

    //private boolean updateAvailable = false;
    //public boolean getUpdateAvailable() { return updateAvailable; }

    public void preLaunchInitialise()
    {
        // Load our configs BEFORE the game loads.
        // This is mainly because of the LazyDFU integration,
        // where the DFU "optimisations" are executed BEFORE our initialisation.
        // This can be here because it does not depend on MC.
        this.config.load();
    }

    public void initialise()
    {
        // Read our version data
        readVersionData();
		
        // set client and its accessor
        this.client = MinecraftClient.getInstance();
        this.clientAccessor = (MinecraftClientAccessor) this.client;
			
        // register features
        //new FlightFeature();
        //new HighJumpFeature();
        //new JetpackFeature();
        //new JumpBoostFeature();
        //new NoFallDamageFeature();
        new AustralianModeFeature();
        new CommandProcessor();
        new ConfigMenu();
        new CreativeFlightFeature();
        new FeatureList();
        new FullBrightFeature();
        new MultiQuickChatFeature();
        new ProximityRadarFeature();
        new QuickChatFeature();
        new RedReticleFeature();
        new ShareLocationFeature();
        new TakePanoramaFeature();
        new TimerFeature();
        new WailaFeature();
        new ZoomFeature();

        //new IncreaseFovFeature();
        //new DecreaseFovFeature();
    }

    // FIXME: usage of this is highly inconsistent
    public void log( String msg ) { if ( debug ) LOGGER.info( msg ); }

    private void readVersionData()
    {
        // set version

        // assert that we're actually loaded
        // If this ever fails, please, send me the logs.
        assert FabricLoader.getInstance().getModContainer( "xenon" ).isPresent();
        this.modContainer = FabricLoader.getInstance().getModContainer( "xenon" ).get();

        this.version = modContainer.getMetadata().getVersion().getFriendlyString();
    }




    /*
     * +-----------------+
     * | Utility methods |
     * +-----------------+
     */
    public void sendInfoMessage( String key )
    {
        Text finalText = namePrefix.copy()
                .append( TextFactory.createTranslatable( key ) );
        this.client.player.sendMessage( finalText, false );
    }

    public void sendInfoMessage( Text text )
    {
        Text finalText = namePrefix.copy()
                .append( text );
        this.client.player.sendMessage( finalText, false );
    }

    public void sendErrorMessage( String key )
    {
        // TODO: impl parameter packs?
        Text finalText = namePrefix.copy()
                .append(
                        TextFactory.createTranslatable( key )
                        .formatted( this.ERROR_FORMAT )
                );
        this.client.player.sendMessage(finalText, false);
    }

    public void sendErrorMessage( Text text )
    {
        Text finalText = namePrefix.copy()
                .append( text/*.formatted( this.ERROR_FORMAT )*/ );
        this.client.player.sendMessage( finalText, false );
    }
}
