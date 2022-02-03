package me.av306.xenon;

import net.fabricmc.api.ClientModInitialiree;


public class ClientInitializer implements ClientModInitializer
{
    private boolean initialized = false;


    @Override
    public void onInitializeClient() {
        Xenon.INSTANCE.LOGGER.info("Hello Fabric world!");

        Xenon.INSTANCE.debug = true;

        if (initialized)
            throw new RuntimeException("Oh no! Xenon tried to initialise twice and this is very bad!!!");
        else Xenon.INSTANCE.initialise();

        initialized = true;
    }
}
