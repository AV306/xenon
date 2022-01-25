package me.av306.xenon;

import me.av306.xenon.features.*;
import me.av306.xenon.features.interfaces.*;
import me.av306.xenon.keybinds.*;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public enum Xenon
{
    INSTANCE;

    public boolean debug = true;

    public XenonFeatureManager featureManager = new XenonFeatureManager();
	
    public final Logger LOGGER = LogManager.getLogger( "xenon" );

    public MinecraftClient client;

    public void initialise()
    {
        // set client
        this.client = MinecraftClient.getInstance();


        // register keybinds
        log( "Registering FullBright key!" );
        featureManager.register(
                new XenonKeybind<IToggleableFeature>(
                        "key.xenon.togglefullbright",
						InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_G,
                        "category.xenon.features",
						new FullBrightFeature()
                )
        );



        log( "Registering AutoReply key!" );
        featureManager.register(
                new XenonKeybind<IFeature>(
                        "key.xenon.autoreply",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_H,
						"category.xenon.features",
                        new AutoReplyFeature()
                )
        );


        log( "Registering OptionsGui key!" );
        featureManager.register(
                new XenonKeybind<IFeature>(
                        "key.xenon.options",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_N,
                        "category.xenon.features",
                        new XenonOptionsGuiFeature()
                )
        );


        log( "Registering NoFireOverlay key!" );
        featureManager.register(
                new XenonKeybind<IToggleableFeature>(
                        "key.xenon.nofireoverlay",
                        InputUtil.Type.KEYSYM,
                        -1,
                        "category.xenon.features",
				        new NoFireOverlayFeature()
                )
        );


        log( "Registering TakePanorama key!" );
        featureManager.register(
                new XenonKeybind<IFeature>(
                        "key.xenon.takepanorama",
                        InputUtil.Type.KEYSYM,
                        -1,
                        "category.xenon.features",
                        new TakePanoramaFeature()
                )
        );

    }



    public void log( String msg ) { if ( debug ) LOGGER.info( msg ); }
}
