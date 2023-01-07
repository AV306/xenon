package me.av306.xenon;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class PreLaunchInitializer implements PreLaunchEntrypoint
{
    private boolean initialized = false;
    @Override
    public void onPreLaunch()
    {
        Xenon.INSTANCE.LOGGER.info( "Beginning pre-launch initialisation!" );
        if ( initialized )
            throw new RuntimeException( "Oh no! Xenon tried to perform pre-launch initialisation twice and this is very, very bad!!!" );
        else Xenon.INSTANCE.preLaunchInitialise();
    }
}
