package me.av306.xenon;

import me.av306.xenon.features.AutoReplyFeature;
import me.av306.xenon.features.FullBrightFeature;
import me.av306.xenon.features.TestUpdatableFeature;
import me.av306.xenon.features.interfaces.IFeature;
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



        log( "Registering AutoResponse key!" );
        keybindManager.register(
                new XenonKeybind<IFeature>(
                        fullBrightKey =  KeyBindingHelper.registerKeyBinding(
                                new KeyBinding(
                                        "key.xenon.autoresponse",
                                        InputUtil.Type.KEYSYM,
                                        GLFW.GLFW_KEY_H,
                                        "category.xenon.features"
                                )
                        ),

                        new AutoReplyFeature()
                )
        );

    }



    public void log( String msg ) { if ( debug ) LOGGER.info( msg ); }
}
