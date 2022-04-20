package me.av306.xenon.util.config;

import me.av306.xenon.Xenon;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class ConfigurationManager
{
    // it's the feature's job to parse the setting string
    public HashMap<String, String> settings = new HashMap<>();
    
    private final File configFile;

    public ConfigurationManager( String pathToConfigFile )
    {
        this.configFile = new File( pathToConfigFile );
        
        checkConfigFile();
    }

    public void checkConfigFile() {}

    public void readConfigs( File configFile )
    {
        try
        {
            // read into the field
            readConfigsInternal( configFile );
        }
        catch ( IOException e )
        {
            // should not happen,
            // if it does, just die
            e.printStackTrace();
            throw new IllegalStateException(
                "Looks like the config file checks failed, and this is very very bad!!! Please make a bug report!"
            );
        }
    }
    
    private void readConfigsInternal( File configFile ) throws IOException
    {
        Scanner configFileScanner = new Scanner( configFile ); // IOException here
        while ( configFileScanner.hasNextLine() )
        {
            String line = configFileScanner.next();
            if ( line.startsWith( "#" ) ) continue; // skip comments
            else
            {
                String[] config = line.strip().split( "=" );
                // e.g. {"waila.updatefreq", "3"}
                // then WAILA parses "3" to 3 (short)
                settings.put( config[0], config[1] );
            }
        }
    }
}
