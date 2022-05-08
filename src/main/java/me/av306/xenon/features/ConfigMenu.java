package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.callback.KeyboardKeyCallback;
import me.av306.xenon.feature.IFeature;
import me.lortseam.completeconfig.data.Config;
import me.lortseam.completeconfig.gui.ConfigScreenBuilder;
import me.lortseam.completeconfig.gui.cloth.ClothConfigScreenBuilder;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.fabricmc.fabric.mixin.screen.ScreenAccessor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ActionResult;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class ConfigMenu extends IFeature
{
    public ConfigMenu()
    {
        super( "ConfigMenu" );

        //KeyboardKeyCallback.AFTER_VIGNETTE.register( this::onKey );
    }

    protected ActionResult onKey( long window, int key, int scanCode, int action, int modifiers )
    {
        //if ( Xenon.INSTANCE.client.currentScreen instanceof XenonScreen )

        return ActionResult.PASS;
    }

    @Override
    protected void onEnable()
    {
        // build a new one every time we open
        ConfigScreenBuilder screenBuilder = new ClothConfigScreenBuilder(
                () -> ConfigBuilder.create()
                .transparentBackground()
        );
        Screen configScreen = screenBuilder.build( Xenon.INSTANCE.client.currentScreen, Xenon.INSTANCE.config );
        Xenon.INSTANCE.client.setScreen( configScreen ); // TODO: make it save on exit screen
    }
}
