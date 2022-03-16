package me.av306.xenon;

import me.av306.xenon.feature.*;
import me.av306.xenon.features.*;
import me.av306.xenon.keybind.*;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.impl.resource.loader.FabricModResourcePack;
import net.fabricmc.loader.impl.ModContainerImpl;
import net.fabricmc.loader.impl.discovery.ModCandidate;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    public final Formatting WARNING_FORMAT = Formatting.YELLOW;

    public final Formatting ERROR_FORMAT = Formatting.RED;

    public ArrayList<IFeature> enabledFeatures = new ArrayList<>();

    private String version;
    public String getVersion() { return version; }
    public void setVersion( String version ) { this.version = version; }

    public void initialise()
    {
        // check folders
        // TODO: implement

        // set version & TODO: impl check for update
        version = "3.0.0.alpha.5+1.18.2";

        // set client
        this.client = MinecraftClient.getInstance();


        // register keybinds
        log( "Registering FullBright key!" );
        keybindManager.register(
                new XenonKeybind<IToggleableFeature>(
                        "key.xenon.fullbright",
						InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_G,
                        "category.xenon.features",
						new FullBrightFeature()
                )
        );



        log( "Registering AutoReply key!" );
        keybindManager.register(
                new XenonKeybind<IFeature>(
                        "key.xenon.autoreply",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_H,
						"category.xenon.features",
                        new AutoReplyFeature()
                )
        );


        log( "Registering OptionsGui key!" );
        keybindManager.register(
                new XenonKeybind<IFeature>(
                        "key.xenon.options",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_N,
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

        log( "Registering DataHUD key!" );
        keybindManager.register(
                new XenonKeybind<IFeature>(
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
    private void checkDataFolder()
    {
        File dataFolder = new File( this.client.runDirectory.getAbsolutePath() + File.separator + "xenon" + File.separator );

        if ( !dataFolder.exists() )
            dataFolder.mkdir();
    }
    */
}
