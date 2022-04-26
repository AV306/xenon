package me.av306.xenon;

import me.av306.xenon.feature.IFeature;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.features.*;
import me.av306.xenon.keybind.XenonKeybind;
import me.av306.xenon.keybind.XenonKeybindManager;
import me.av306.xenon.config.CoreConfigGroup;
import me.av306.xenon.config.FeatureConfigGroup;
import me.lortseam.completeconfig.data.Config;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public enum Xenon
{
    INSTANCE;

    public final String MODID = "xenon";
	
    public boolean debug = true;

    public XenonKeybindManager keybindManager = new XenonKeybindManager();

    public Config config = new Config( this.MODID, new CoreConfigGroup(), new FeatureConfigGroup() );
	
    public final Logger LOGGER = LogManager.getLogger( this.MODID );

    public MinecraftClient client;

    public final Formatting SUCCESS_FORMAT = Formatting.GREEN;
    //public final Formatting WARNING_FORMAT = Formatting.YELLOW;
    //public final Formatting ERROR_FORMAT = Formatting.RED;

    public ArrayList<IFeature> enabledFeatures = new ArrayList<>();

    //public File configFile = FabricLoader.getInstance().getConfigDir().resolve( "xenon_config.txt" ).toFile();

    private String version;
    public String getVersion() { return version; }
    //public void setVersion( String version ) { this.version = version; }

    @Nullable
    private ModContainer modContainer;

    //private boolean updateAvailable = false;
    //public boolean getUpdateAvailable() { return updateAvailable; }

    public void initialise()
    {
			
        readVersionData();

				// load configs
				config.load();
			
        // set client
        this.client = MinecraftClient.getInstance();
			
        // register keybinds
        // TODO: make it less clunky
		// but how???
        log( "Registering FullBright key!" );
        keybindManager.register(
                new XenonKeybind<IToggleableFeature>(
                        "key.xenon.fullbright",
						InputUtil.Type.KEYSYM,
                        -1,
                        "category.xenon.features",
						new FullBrightFeature()
                )
        );

        log( "Registering AutoReply key!" );
        keybindManager.register(
                new XenonKeybind<IFeature>(
                        "key.xenon.autoreply",
                        InputUtil.Type.KEYSYM,
                        -1,
						"category.xenon.features",
                        new AutoReplyFeature()
                )
        );

        log( "Registering TakePanorama key!" );
        keybindManager.register(
                new XenonKeybind<IFeature>(
                        "key.xenon.takepanorama",
                        InputUtil.Type.KEYSYM,
                        -1,
                        "category.xenon.features",
                        new TakePanoramaFeature()
                )
        );

        log( "Registering ShareLocation key!" );
            keybindManager.register(
			        new XenonKeybind<IFeature>(
					        "key.xenon.sharelocation",
							InputUtil.Type.KEYSYM,
							-1,
							"category.xenon.features",
							new ShareLocationFeature()
					)
		);

        log( "Registering FeatureList key!" );
        keybindManager.register(
                new XenonKeybind<IFeature>(
                        "key.xenon.featurelist",
                        InputUtil.Type.KEYSYM,
                        -1,
                        "category.xenon.features",
                        new FeatureList()
                )
        );

        log( "Registering WAILA key!" );
        keybindManager.register(
                new XenonKeybind<IFeature>(
                        "key.xenon.waila",
                        InputUtil.Type.KEYSYM,
                        -1,
                        "category.xenon.features",
                        new WailaFeature()
                )
        );

        log( "Registering DataHUD key!" );
        keybindManager.register(
                new XenonKeybind<IToggleableFeature>(
                        "key.xenon.datahud",
                        InputUtil.Type.KEYSYM,
                        -1,
                        "category.xenon.features",
                        new DataHudFeature()
                )
        );

    }


    public void log( String msg ) { if ( debug ) LOGGER.info( msg ); }

    /*
    private void checkDataFolder() // should never throw
    {
        Scanner configFileScanner;

        try
        {
            configFile.createNewFile();

            configFileScanner = new Scanner( configFile );

            while ( configFileScanner.hasNextLine() )
            {
                String configuration = configFileScanner.nextLine().strip();
                IFeature targeted = configuration.split( "\\." )[0].;
            }
        }
        catch ( IOException e ) { e.printStackTrace(); }
    }
    */

    private void readVersionData()
    {
        // set version & TODO: impl check for update
        assert FabricLoader.getInstance().getModContainer( "xenon" ).isPresent();
        modContainer = FabricLoader.getInstance().getModContainer( "xenon" ).get();

        version = modContainer.getMetadata().getVersion().getFriendlyString();
    }
}
