package me.av306.xenon;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;


public class ClientInitializer implements ClientModInitializer
{
    private boolean initialized = false;


    @Override
    public void onInitializeClient() {
        Xenon.INSTANCE.LOGGER.info("Initialising!");

        Xenon.INSTANCE.debug = true;

        if (initialized)
            throw new RuntimeException("Oh no! Xenon tried to initialise twice and this is very bad!!!");
        else Xenon.INSTANCE.initialise();

        initialized = true;
    }
}
