package me.av306.xenon.config;

import me.av306.xenon.Xenon;
import org.apache.commons.io.FileUtils;

import java.io.*;
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
            Xenon.INSTANCE.LOGGER.warn( "Could not ensure the config file exists!" );
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
                }
            });
        }
        catch ( IOException e )
        {
            // doesn't matter if this throws,
            // it will just leave the HashMap empty,
            // and the Features will just set their default configs.
            Xenon.INSTANCE.LOGGER.warn( "An error occurred while loading configs! Features will use default settings." );
        }
    }

    /*private void loadConfigsAlt()
    {
        // alternative implementation of loadConfigs() but with Scanner.
        // don't use
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
					if ( config.length == 2 )
					{
                    	Xenon.INSTANCE.LOGGER.info( "Read configuration: " + line );
                    	settings.put( config[0], config[1] );
					}
					else Xenon.INSTANCE.LOGGER.warn(
					        "Invalid configuration: \"" + line + "\" - associated Feature will use default."
                    );
                }
            }

            Xenon.INSTANCE.LOGGER.info( "Done reading configurations!" );
        }
        catch ( FileNotFoundException e )
        {
            Xenon.INSTANCE.LOGGER.warn( "Config file absent! Features will use default settings." );
        }
    }*/



    public void updateConfig( String key, String newValue )
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
            Xenon.INSTANCE.LOGGER.warn( "An error occurred while saving configs! Restoring previous settings." );
					  FileUtils.copyFile( backupConfigFile, this.configFile );
					  backupConfigFile.delete();
        }
	}
}