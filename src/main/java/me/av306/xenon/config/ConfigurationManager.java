package me.av306.xenon.config;

import me.av306.xenon.Xenon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class ConfigurationManager {
    // it's the feature's job to parse the setting string
    public HashMap<String, String> settings = new HashMap<>();

    private final File configFile;

    public ConfigurationManager(File configFile) {
        Xenon.INSTANCE.LOGGER.info( "Now loading config file..." );
        this.configFile = configFile;

        try
        {
            this.configFile.createNewFile(); // ensure that the file exists
        }
        catch ( IOException e )
        {
            Xenon.INSTANCE.LOGGER.warn("Could not ensure the config file exists!");
            e.printStackTrace();
        }

        loadConfigs();
    }

    private void loadConfigs()
    {
        // doesn't matter if this throws,
        // it will just leave the HashMap empty,
        // and the Features will just set their default configs.
        try ( Scanner configFileScanner = new Scanner( this.configFile ); )
        {
            while ( configFileScanner.hasNextLine() )
            {
                String line = configFileScanner.nextLine().strip();
                if ( !line.startsWith( "#" ) && !line.isBlank() ) // skip comments
                {
                    String[] config = line.strip().split( "=" );
                    // e.g. {"waila.updatefreq", "3"}
                    // then WAILA parses "3" to 3 (short)
                    Xenon.INSTANCE.LOGGER.info( "Read configuration: " + line );
                    settings.put( config[0], config[1] );
                }
            }

            Xenon.INSTANCE.LOGGER.info( "Done reading configurations!" );
        }
        catch ( FileNotFoundException e )
        {
            Xenon.INSTANCE.LOGGER.warn( "Config file absent! Features will use default settings." );
        }
    }

    public void updateConfig(String key, String newValue)
    {
        this.settings.put(key, newValue);

        // we'll only overwrite the affected line
        // Iterate over the file till we find the correct line
        //while ( this.configFileS)
        // TODO: actually implement
    }
}