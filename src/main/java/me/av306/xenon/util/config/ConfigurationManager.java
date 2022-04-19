package me.av306.xenon.util.config;

import me.av306.xenon.Xenon;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class ConfigurationManager
{
    private HashMap<String, ConfigEntry> settings = new HashMap<>();

    public ConfigurationManager() {}

    public void initialiseConfigFile() {}

    public HashMap<String, ConfigEntry> readConfigs( File configFile )
    {
        try
        {
            Scanner configFileScanner = new Scanner( configFile );
            while ( configFileScanner.hasNextLine() )
            {
                String line = configFileScanner.next();
                if ( line.startsWith( "#" ) ) continue;
                //else
            }
        }
        catch ( IOException e )
        {
            Xenon.INSTANCE.LOGGER.warn( "IOException thrown while reading configs!" );
        }

        return this.settings;
    }

    public ConfigEntry getConfig( String name )
    {
        return this.settings.get( name );
    }
}