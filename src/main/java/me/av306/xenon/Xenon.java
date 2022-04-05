package me.av306.xenon;

import me.av306.xenon.feature.*;
import me.av306.xenon.features.*;
import me.av306.xenon.keybind.*;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.impl.resource.loader.FabricModResourcePack;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.fabricmc.loader.impl.ModContainerImpl;
import net.fabricmc.loader.impl.discovery.ModCandidate;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.CallbackI;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public enum Xenon
{
    INSTANCE;

    public boolean debug = true;

    public XenonKeybindManager keybindManager = new XenonKeybindManager();
	
    public final Logger LOGGER = LogManager.getLogger( "xenon" );

    public MinecraftClient client;

    public final Formatting SUCCESS_FORMAT = Formatting.GREEN;
    //public final Formatting WARNING_FORMAT = Formatting.YELLOW;
    //public final Formatting ERROR_FORMAT = Formatting.RED;

    public ArrayList<IFeature> enabledFeatures = new ArrayList<>();

    private String version;
    public String getVersion() { return version; }
    //public void setVersion( String version ) { this.version = version; }

    @Nullable
    private ModContainer modContainer;

    //private boolean updateAvailable = false;
    //public boolean getUpdateAvailable() { return updateAvailable; }

    public void initialise()
    {
        // check folders
        // TODO: implement

        // set version & TODO: impl check for update
        assert FabricLoader.getInstance().getModContainer( "xenon" ).isPresent();
        this.modContainer = FabricLoader.getInstance().getModContainer( "xenon" ).get();

        this.version = modContainer.getMetadata().getVersion().getFriendlyString();

        // set client
        this.client = MinecraftClient.getInstance();


        // register keybinds
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


        log( "Registering OptionsGui key!" );
        keybindManager.register(
                new XenonKeybind<IFeature>(
                        "key.xenon.options",
                        InputUtil.Type.KEYSYM,
                        -1,
                        "category.xenon.features",
                        new XenonOptionsGuiFeature()
                )
        );


        log( "Registering NoFireOverlay key!" );
        keybindManager.register(
                new XenonKeybind<IToggleableFeature>(
                        "key.xenon.nofireoverlay",
                        InputUtil.Type.KEYSYM,
                        -1,
                        "category.xenon.features",
				        new NoFireOverlayFeature()
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

    }


    public void log( String msg ) { if ( debug ) LOGGER.info( msg ); }

    /*
    private void checkDataFolder()
    {
        File dataFolder = new File( this.client.runDirectory.getAbsolutePath() + File.separator + "xenon" + File.separator );

        if ( !dataFolder.exists() )
            dataFolder.mkdir();
    }
    */
}
