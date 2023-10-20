package me.av306.xenon;

import me.av306.xenon.command.Command;
import me.av306.xenon.commands.*;
import me.av306.xenon.config.FeatureConfigGroup;
import me.av306.xenon.config.GeneralConfigGroup;
import me.av306.xenon.config.feature.movement.FullKeyboardGroup;
import me.av306.xenon.event.ClientWorldEvents;
import me.av306.xenon.feature.*;
import me.av306.xenon.features.*;
import me.av306.xenon.features.chat.*;
import me.av306.xenon.features.movement.*;
import me.av306.xenon.features.render.*;
import me.av306.xenon.mixin.MinecraftClientAccessor;
import me.av306.xenon.util.KeybindUtil;
import me.av306.xenon.util.text.TextFactory;
import me.lortseam.completeconfig.data.Config;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public enum Xenon
{
    INSTANCE;

    public final String MODID = "xenon";
	
    public final boolean debug = true;

    public Config config = new Config( this.MODID, new FeatureConfigGroup() );
	
    public final Logger LOGGER = LoggerFactory.getLogger( this.MODID );

    public MinecraftClient client;
    public MinecraftClientAccessor clientAccessor;

    public final Formatting SUCCESS_FORMAT = Formatting.GREEN;
    public final Formatting MESSAGE_FORMAT = Formatting.AQUA;
    public final Formatting WARNING_FORMAT = Formatting.YELLOW;
    public final Formatting ERROR_FORMAT = Formatting.RED;

    // This is most likely going to be used to resolve a feature
    // by its name (e.g. CommandProcessor),
    // so I put it in this order.
    public final HashMap<String, IFeature> featureRegistry = new HashMap<>();
    public final ArrayList<IToggleableFeature> enabledFeatures = new ArrayList<>();

    public HashMap<String, Command> commandRegistry = new HashMap<>();

    private String versionString;

    public String getVersion() { return versionString; }
    
    public ModContainer modContainer;

    public KeyBinding modifierKey;

    /**
     * This field `namePrefix` should contain "[Xenon] " (note whitespace),
     * and be formatted with the message format.
     */
    private final Text namePrefix = TextFactory.createLiteral( "[Xenon] " )
            .formatted( this.MESSAGE_FORMAT );

    public MutableText getNamePrefixCopy() { return namePrefix.copy(); }

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

        FabricLoader loader = FabricLoader.getInstance();

        // Check for CompleteConfig
        if ( loader.isModLoaded( "completeconfig-base" ) )
            Xenon.INSTANCE.LOGGER.warn( "CompleteConfig not detected! Some features will not work properly." );
		
        // set client and its accessor
        this.client = MinecraftClient.getInstance();
        this.clientAccessor = (MinecraftClientAccessor) this.client;

        ClientWorldEvents.DISCONNECT.register( this::disableAllFeatures );
			
        // register features
        initCommands();
        initFeatures();

        // Register config screen with ModMenu if present
        /*if ( FabricLoader.getInstance().isModLoaded( "cloth-config" ) )
            ConfigScreenBuilder.setMain( this.MODID, new ClothConfigScreenBuilder() );*/
    }

    private void initCommands()
    {
        new DebugCrashCommand();
        new HelpCommand();
        //new BindCommand();
        new AliasCommand();
        new RegisterCommand();
    }

    private void initFeatures()
    {
        this.modifierKey = KeybindUtil.registerKeybind(
                "modifier",
                GLFW.GLFW_KEY_LEFT_ALT,
                "features"
        );

        new AustralianModeFeature();
        new CommandProcessor();
        new ConfigMenu();
        new FeatureList();
        new FullBrightFeature();
        if ( FullKeyboardGroup.enable ) new FullKeyboardFeature();
        new HealthDisplayFeature();
        new MultiQuickChatFeature();
        new ProximityRadarFeature();
        new QuickChatFeature();
        new RedReticleFeature();
        new ShareLocationFeature();
        new TakePanoramaFeature();
        if ( GeneralConfigGroup.enableTimer ) new TimerFeature();
        new WailaFeature();
        new ZoomFeature();
        new BlackBox();
    }


    public void disableAllFeatures()
    {
        // FIXME: This feels very inefficient
        Xenon.INSTANCE.LOGGER.info( "Exiting world, disabling all features" );
        ArrayList<IToggleableFeature> enabledFeatures_copy = new ArrayList<>( this.enabledFeatures );
        for ( IToggleableFeature feature : enabledFeatures_copy )
            if ( !feature.isPersistent() ) feature.disable(); // Don't disable if it's persistent (like CP)

        // Remove restrictions
        for ( IFeature feature : this.featureRegistry.values() )
            feature.setForceDisabled( false );
    }

    // Mod Menu handles update checks for us :)
    private void readVersionData()
    {
        assert FabricLoader.getInstance().getModContainer( "xenon" ).isPresent();
        this.modContainer = FabricLoader.getInstance().getModContainer( "xenon" ).get();
        // Get version string
        Version ver = modContainer.getMetadata().getVersion();
        this.versionString = ver.getFriendlyString();
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
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }

    public void sendInfoMessage( String key, Object... args )
    {
        Text finalText = namePrefix.copy()
                .append( TextFactory.createTranslatable( key, args ) );
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }

    /*public void sendInfoMessage( Text text )
    {
        Text finalText = namePrefix.copy()
                .append( text );
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }

    public void sendWarningMessage( String key )
    {
        Text finalText = namePrefix.copy()
                .append( TextFactory.createTranslatable( key ).formatted( this.MESSAGE_FORMAT ) );
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }

    public void sendWarningMessage( Text text )
    {
        Text finalText = namePrefix.copy()
                .append(
                        text.copyContentOnly()
                                .formatted( this.MESSAGE_FORMAT )
                );
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }

    public void sendErrorMessage( String key )
    {
        // TODO: impl parameter packs?
        Text finalText = namePrefix.copy()
                .append(
                        TextFactory.createTranslatable( key )
                        .formatted( this.ERROR_FORMAT )
                );
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }

    public void sendErrorMessage( String key, Object... args )
    {
        Text finalText = namePrefix.copy()
                .append(
                        TextFactory.createTranslatable( key, args )
                                .formatted( this.ERROR_FORMAT )
                );

        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }

    // FIXME
    public void sendErrorMessage( Text text )
    {
        Text finalText = namePrefix.copy()
                .append(
                        text.copyContentOnly()
                                .formatted( this.ERROR_FORMAT )
                );
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }*/
}
