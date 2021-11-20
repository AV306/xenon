package me.av306.xenon;

import me.av306.xenon.features.FullBrightFeature;
import me.av306.xenon.features.TestToggleableFeature;
import me.av306.xenon.features.TestUpdatableFeature;
import me.av306.xenon.features.interfaces.IToggleableFeature;
import me.av306.xenon.features.interfaces.IUpdatableFeature;
import me.av306.xenon.keybindutils.XenonKeybind;
import me.av306.xenon.keybindutils.XenonKeybindManager;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import javax.swing.text.JTextComponent;

public enum Xenon
{
    INSTANCE;

    public boolean debug = true;

    public XenonKeybindManager keybindManager = new XenonKeybindManager();

    public final Logger LOGGER = LogManager.getLogger( "xenon" );

    public MinecraftClient CLIENT;

    public KeyBinding fullBrightKey;


    public void initialise()
    {
        // set client
        CLIENT = MinecraftClient.getInstance();

        // register keybinds
        log( "Registering FullBright key!" );
        keybindManager.register(
                new XenonKeybind<IToggleableFeature>(
                        fullBrightKey =  KeyBindingHelper.registerKeyBinding(
                                new KeyBinding(
                                        "key.xenon.togglefullbright",
                                        InputUtil.Type.KEYSYM,
                                        GLFW.GLFW_KEY_G,
                                        "category.xenon.features"
                                )
                        ),

                        new FullBrightFeature()
                )
        );


        /*
        if ( debug ) LOGGER.info( "Registering TestUpdatableFeature key!" );
        keybindManager.register(
                new XenonKeybind<IUpdatableFeature>(
                        fullBrightKey =  KeyBindingHelper.registerKeyBinding(
                                new KeyBinding(
                                        "key.xenon.testupdatablefeature",
                                        InputUtil.Type.KEYSYM,
                                        GLFW.GLFW_KEY_H,
                                        "category.xenon.features"
                                )
                        ),

                        new TestUpdatableFeature()
                )
        );
        */
    }



    public void log( String msg ) { if ( debug ) LOGGER.info( msg ); }
}
