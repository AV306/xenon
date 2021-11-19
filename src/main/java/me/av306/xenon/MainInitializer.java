package me.av306.xenon;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainInitializer implements ModInitializer
{
    public final Logger LOGGER = LogManager.getLogger( "xenon" );

    @Override
    public void onInitialize()
    {
        LOGGER.info( "testing" );
    }
}
