package me.av306.xenon.config;

import me.av306.xenon.Xenon;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class ConfigurationManager
{
    // it's the feature's job to parse the setting string
    public HashMap<String, String> settings = new HashMap<>();
    
    private final File configFile;

    public ConfigurationManager( File configFile )
    {
        this.configFile = configFile;
			  this.configFile.createNewFile(); // ensure that the file exists

			  loadConfigs();
		}

    public void loadConfigs()
    {
        try
				{
			    readConfigsInternal();
				}
			  catch ( FileNotFoundException e ) { e.printStackTrace(); }
    }
    
    private void readConfigsInternal( File configFile ) throws FileNotFoundException
    {
        // if the file is blank, it will just not put anything in the HM
			
        try
				{
					// doesn't matter if this throws,
					// it will just leave the HashMap empty,
					// and the Features will just set their default configs.
					Scanner configFileScanner = new Scanner( configFile );
					
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
			
	  	    Xenon.INSTANCE.client.LOGGER.info( "Done reading configurations!" );
				}
			  catch ( FileNotFoundException e )
				{
					Xenon.INSTANCE.LOGGER.warn( "Config file Scanner threw while initialising! Features will use default configs!" );
				}
			  finally
				{
					configFileScanner.close();
				}
    }

	  public void updateConfig( String key, String newValue )
	  {
      this.settings.put( key, newValue );

			// we'll only overwrite the affected line
			// Iterate over the file till we find the correct line
			//while ( this.configFileS)
	  }
}
