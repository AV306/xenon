package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IFeature;
import me.lortseam.completeconfig.data.Config;
import me.lortseam.completeconfig.gui.ConfigScreenBuilder;
import me.lortseam.completeconfig.gui.cloth.ClothConfigScreenBuilder;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.gui.screen.Screen;

public class ConfigMenu extends IFeature
{
    public ConfigMenu()
    {
        super( "ConfigMenu" );
    }

    @Override
    public void onEnable()
    {
        // build a new one every time we open
        ConfigScreenBuilder screenBuilder = new ClothConfigScreenBuilder(
                () -> ConfigBuilder.create()
                .transparentBackground()
        );
        Screen configScreen = screenBuilder.build( Xenon.INSTANCE.client.currentScreen, Xenon.INSTANCE.config );
        Xenon.INSTANCE.client.setScreen( configScreen ); // TODO: make it save on exit screen
    }

    @Override
    public void onDisable()
    {

    }
}
