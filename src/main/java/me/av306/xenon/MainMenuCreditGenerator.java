package me.av306.xenon;

public class MainMenuCreditGenerator
{
	// Hooray for multiline string literals!
	public static final String MMC_JSON = """
	{
  		"main_menu": {
    		"top_left": [
    
    		],
    		"top_right": [
    
    		],
    		"bottom_left": [
    			"Xenon %s"
    		],
    		"bottom_right": [
    			
    		],
    		"mod_blacklist": [

    		]
  		},
  		"pause_menu": {
    		"top_left": [
    
    		],
    		"top_right": [
    	
    		],
    		"bottom_left": [
    
    		],
    		"bottom_right": [
    
    		],
    		"mod_blacklist": [

    		]
  		}
	}
	""";
	
	// Using the MMC API would be a lot easier,
	// but I don't want to force people to download that too as a dependency
	// Maybe I'll bundle it in the jar
	public static void generateJson()
	{
		// Grab the MMC config file
		File configFile = FabricLoader.getInstance()
				.getConfigDir()
				.resolve( "main-menu-credits.json" )
				.toFile();

		if ( !configFile.createNewFile() )
		{
			// Config file doesn't exist yet, create one with our own text
			try ( BufferedWriter writer = new BufferedWriter( new FileWriter( configFile ) ) )
			{
				writer.write(
						String.format(
								MMC_JSON,
								Xenon.INSTANCE.getVersion();
						)
				);
			catch ( IOException e )
			{
				Xenon.INSTANCE.LOGGER.error( "An exception occured while writing the MMC config file!" );
				e.printStackTrace();
			}
		}
		else
		{
			// Append to existing file???
			// Configs???
			// ?????????????????
			Xenon.INSTANCE.LOGGER.warn( "MMC config file already exists, not writing" );
		}
	}
}