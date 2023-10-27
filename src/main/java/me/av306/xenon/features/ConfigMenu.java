package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IFeature;
import me.lortseam.completeconfig.gui.ConfigScreenBuilder;
import me.lortseam.completeconfig.gui.cloth.ClothConfigScreenBuilder;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.gui.screen.Screen;

public class ConfigMenu extends IFeature
{
    public ConfigMenu()
    {
        super( "ConfigMenu", "config", "cm" );

        //KeyEvent.AFTER_VIGNETTE.register( this::onKey );
    }

    /*protected ActionResult onKey( long window, int key, int scanCode, int action, int modifiers )
    {
        //if ( Xenon.INSTANCE.client.currentScreen instanceof XenonScreen )

        return ActionResult.PASS;
    }*/

    @Override
    protected void onEnable()
    {
        // Catch any exception when initialising the config menu
        // because it's annoying when I test stuff and the game just crashes
        // Configs are saved automatically :D
        try
        {
            // build a new one every time we open
            ConfigScreenBuilder<AbstractConfigListEntry<?>> screenBuilder = new ClothConfigScreenBuilder(
                    () -> ConfigBuilder.create()
                    .transparentBackground()
            );
            Screen configScreen = screenBuilder.build( Xenon.INSTANCE.client.currentScreen, Xenon.INSTANCE.config );
            Xenon.INSTANCE.client.setScreen( configScreen );
        }
        catch ( UnsupportedOperationException unsupported )
        {
            this.sendErrorMessage( "text.xenon.configmenu.unsupportedoperation" );
            unsupported.printStackTrace();
        }
        catch ( Exception e )
        {
            this.sendErrorMessage( "text.xenon.configmenu.unknownexception" );
            e.printStackTrace();
        }
        catch ( Error error )
        {
            error.printStackTrace();
            this.sendErrorMessage( "text.xenon.configmenu.unknownerror" );
        }
    }
}
