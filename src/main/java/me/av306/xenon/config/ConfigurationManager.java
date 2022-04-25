/*
package me.av306.xenon.config;

import me.av306.xenon.Xenon;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class ConfigurationManager {
    // it's the feature's job to parse the setting string
	  // and also to put something in the setting string if it's invalid.
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
            Xenon.INSTANCE.LOGGER.warn( "Could not ensure the config file exists! This may cause errors while reading configs." );
            e.printStackTrace();
        }

        loadConfigs();
    }

    private void loadConfigs()
    {
        // load all the configs in the config file into a HashMap.
        try ( BufferedReader reader = new BufferedReader( new FileReader( this.configFile ) ) )
        {
            // iterate over each line in the file
            reader.lines().forEach( line ->
            {
                line = line.strip(); // strip whitespace
                if ( !line.startsWith( "#") && !line.isBlank() ) // skip newlines and comments
                {
                    String[] config = line.split( "=" ); // split config declaration

                    if ( config.length == 2 )
                    {
                        Xenon.INSTANCE.log( "Read configuration: " + line );
                        settings.put( config[0], config[1] );
                    }
                    else Xenon.INSTANCE.LOGGER.warn(
                            "Invalid configuration: \"" + line + "\" - associated Feature will use default."
                    ); // invalid thing like "foo.bar=" or "f====o======o.bar-lol+get'rekt"
									  // if the config line is invalid, it eill not be loaded into the HashMap.
									  // It's the feature's job to handle the null value returned
									  // if the config it wants to read is invalid,
									  // and to put a valid default config into the HashMap.
                }
            });
        }
        catch ( IOException e )
        {
				  	// May be thrown if createNewFile() above throws.
            // It doesn't matter if this throws,
            // it will just leave the HashMap empty,
            // and the Features will just set their default configs.
            Xenon.INSTANCE.LOGGER.warn( "An error occurred while loading configs! Features will use default settings." );
        }
    }

    public void saveConfigs()
    {
        // When we're about to exit,
        // we'll iterate over the file in the same way as we read it
        // so as not to just trample all the comments.
        // Then, we'll edit each config definition line.
				// NOTE: this one is *doubly* expensive as we have to parse it AND write to it.
				// It's stupid, but that's how it goes.

        // first, backup the config file
        File backupConfigFile = new File( this.configFile.getParent() + File.separator + "xenon_config_backup.congif" );

        try ( BufferedReader reader = new BufferedReader( new FileReader( this.configFile ) );
						 PrintWriter writer  = new PrintWriter( new BufferedWriter( new FileWriter( this.configFile, false ) ) ) )
        {
            // backup our config
            FileUtils.copyFile( this.configFile, backupConfigFile );

            // create a StringBuilder to store out new config file text
					  StringBuilder configTextBuilder = new StringBuilder();
					
            // iterate over each line in the file
            reader.lines().forEach( line ->
            {
                // strip so we know there will be no newlines
							  line = line.strip();
							
                if ( !line.startsWith( "#" ) && !line.isBlank() ) // skip newlines and comments
                {
									  // reconstruct the config statement and set line
								  	String config = line.split( "=" )[0];
										line = config + "=" + this.settings.get( config );
										// e.g. "autoreply.message" + "=" + "asdf" => "autoreply.message=asdf"
                }

							  configTextBuilder.append( line ).append( "\n" );
					
            });
					
					  // now we bulldoze everything in the config file.
					  // FIXME: this is bound to break
					  writer.print( configTextBuilder.toString() );
        }
        catch ( IOException e )
        {
            Xenon.INSTANCE.LOGGER.warn( "An error occurred while saving configs!" );
						Xenon.INSTANCE.LOGGER.warn( "Please navigate to the Xenon config")
	}
}
*/