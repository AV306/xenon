package me.av306.xenon;

import me.av306.xenon.features.*;
import me.av306.xenon.features.interfaces.*;
import me.av306.xenon.util.keybinds.XenonKeybind;
import me.av306.xenon.util.keybinds.XenonKeybindManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;


public enum Xenon
{
    INSTANCE;

    public boolean debug = true;

    public XenonKeybindManager keybindManager = new XenonKeybindManager();
	
    public final Logger LOGGER = LogManager.getLogger( "xenon" );

    public MinecraftClient CLIENT;


    public void initialise()
    {
        // set client
        CLIENT = MinecraftClient.getInstance();


        // register keybinds
        log( "Registering FullBright key!" );
        keybindManager.register(
          new XenonKeybind<IToggleableFeature>(
            "key.xenon.togglefullbright",
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

				/*
        log( "Registering NoFireOverlay key!" );
        keybindManager.register(
          new XenonKeybind<IToggleableFeature>(
						"key.xenon.nofireoverlay",
            InputUtil.Type.KEYSYM,
            -1,
            "category.xenon.features"
						new NoFireOverlayFeature()
        );
				*/

    }



    public void log( String msg ) { if ( debug ) LOGGER.info( msg ); }
}
