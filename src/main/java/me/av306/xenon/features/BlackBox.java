package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.BlackBoxGroup;
import me.av306.xenon.event.MinecraftClientEvents;
import me.av306.xenon.feature.IToggleableFeature;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.util.ActionResult;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.*;

public class BlackBox extends IToggleableFeature
{
	private File logFile = null;
	private OutputStreamWriter logFileWriter = null;
	private int index = 0;
	private boolean isGameThreadWriting = false;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool( 1 );
	private ScheduledFuture<?> trackingLogTimerFuture = null;

	public BlackBox()
	{
		super( "BlackBox" );

		//this.setShouldHide( BlackBoxGroup.showInFeatureList );
		MinecraftClientEvents.JOIN_WORLD_TAIL.register( world ->
		{
			if ( BlackBoxGroup.reEnableOnWorldEnter ) this.enable();

			return ActionResult.PASS;
		} );

		// Register logging hooks
	}

	@Override
	protected void onEnable()
	{
		this.setShouldHide( !BlackBoxGroup.showInFeatureList );
		// Open a new log file and output stream
		this.createLogFile();

		// Write starting data
		this.writeWorldData();

		// Register 5-minute tracking log timer (working!!!)
		this.trackingLogTimerFuture = scheduler.scheduleAtFixedRate(
			this::writeTrackingData,
			0, BlackBoxGroup.trackingLogInterval,
			TimeUnit.MINUTES
		);
	}

	@Override
	protected void onDisable()
	{
		this.cleanup();
	}

	private void writeWorldData()
	{
		try
		{
			this.writeData( "BEGIN CONNECTION DATA BLOCK ====================" );

			ClientWorld clientWorld = Xenon.INSTANCE.client.world;
			ClientWorld.Properties props = clientWorld.getLevelProperties();

			if ( clientWorld.isClient() )
			{
				this.logFileWriter.append( "Local server" ).append( '\n' );
			}

			this.logFileWriter.append( "Difficulty: " ).append( props.getDifficulty().getName() );
			if ( props.isHardcore() ) this.logFileWriter.append( "(Hardcore)" );
			if ( props.isDifficultyLocked() ) this.logFileWriter.append( "(Locked)" );
			this.logFileWriter.append( '\n' );

			this.logFileWriter.append( "Spawn:\n" );
			this.logFileWriter.append( "\tSpawn position: (" )
					.append( String.valueOf( props.getSpawnX() ) ).append( ", " )
					.append( String.valueOf( props.getSpawnY() ) ).append( ", " )
					.append( String.valueOf( props.getSpawnZ() ) ).append( ')' )
					.append( '\n' );

			this.writeData( "END CONNECTION DATA BLOCK ====================" );
		}
		catch ( IOException ioe )
		{
			ioe.printStackTrace();
			Xenon.INSTANCE.sendErrorMessage( "text.xenon.blackbox.ioexception.write" );
		}
	}

	private void writeTrackingData()
	{	
		// Spin until game thread is no longer writing
		//while ( this.isGameThreadWriting ) {}
		try
		{
			this.writeData( "BEGIN TRACKING DATA BLOCK ====================" );	

			// Log dimension name
			DimensionType dim = Xenon.INSTANCE.client.world.getDimension();
			// Heuristically determine the dimension (can't get name string :()
			if ( dim.bedWorks() )
			{
				this.logFileWriter.append( "Bed works; probably Overworld" );
			}
			else if ( dim.hasCeiling() )
			{
				this.logFileWriter.append( "Bed explodes and has ceiling; probably Nether" );
			}
			else this.logFileWriter.append( "Bed explodes but no ceiling; probably the End" );	

			this.logFileWriter.append( '\n' );	

			// Log position
			this.logFileWriter.append( "Position: " )
					.append( Xenon.INSTANCE.client.player.getPos().toString() )
					.append( '\n' );	

			// Log velocity
			this.logFileWriter.append( "Velocity: " )
					.append( Xenon.INSTANCE.client.player.getVelocity().toString() )
					.append( '\n' );	

			// Log rotation
			this.logFileWriter.append( "Rotation: " )
					.append( Xenon.INSTANCE.client.player.getRotationVector().toString() )
					.append( '\n' );	

			// Log stats
			this.logFileWriter.append( "Health: " )
					.append( String.valueOf( Xenon.INSTANCE.client.player.getHealth() ) )
					.append( '\n' );	

			this.logFileWriter.append( "Position: " )
					.append( Xenon.INSTANCE.client.player.getPos().toString() )
					.append( '\n' );	

			this.logFileWriter.append( "Hunger: " )
					.append( String.valueOf( Xenon.INSTANCE.client.player.getHungerManager().getFoodLevel() ) )
					.append( '\n' );	

			this.logFileWriter.append( "Exhaustion: " )
					.append( String.valueOf( Xenon.INSTANCE.client.player.getHungerManager().getExhaustion() ) )
					.append( '\n' );	

			this.logFileWriter.append( "Saturation: " )
					.append( String.valueOf( Xenon.INSTANCE.client.player.getHungerManager().getSaturationLevel() ) )
					.append( '\n' );	

			// Log armour
			this.logFileWriter.append( "Equipped armour:\n" );
			for ( ItemStack stack : Xenon.INSTANCE.client.player.getArmorItems() )
			{
				this.logFileWriter.append( "\tName: " ).append( stack.getName().getString() ).append( '\n' );
				this.logFileWriter.append( "\tDamage: " ).append( String.valueOf( stack.getDamage() ) ).append( '\n' );
			}	

			this.logFileWriter.append( '\n' );

			// Log item
			this.logFileWriter.append( "Main hand item:\n" );
			ItemStack mhs = Xenon.INSTANCE.client.player.getMainHandStack();
			this.logFileWriter.append( "\tName: " ).append( mhs.getName().getString() ).append( '\n' );
			this.logFileWriter.append( "\tDamage: " ).append( String.valueOf( mhs.getDamage() ) ).append( '\n' );

			this.logFileWriter.append( '\n' );

			this.logFileWriter.append( "Off-hand item:\n" );
			ItemStack ohs = Xenon.INSTANCE.client.player.getOffHandStack();
			this.logFileWriter.append( "\tName: " ).append( ohs.getName().getString() ).append( '\n' );
			this.logFileWriter.append( "\tDamage: " ).append( String.valueOf( ohs.getDamage() ) ).append( "\n\n" );	

			this.writeData( "END TRACKING DATA BLOCK ====================" );

			this.logFileWriter.flush();
		}
		catch ( IOException ioe )
		{
			ioe.printStackTrace();
			Xenon.INSTANCE.sendErrorMessage( "text.xenon.blackbox.ioexception.write" );
		}
	}

	private void createLogFile()
	{
		try
		{
			String datestamp = LocalDate.now().toString();
			String gameDir = FabricLoader.getInstance().getGameDir().toString();

			//Xenon.INSTANCE.LOGGER.info( gameDir + File.separator + timestamp + this.index + ".log" );

			this.logFile = new File( gameDir + File.separator + datestamp + "-" + this.index + ".data" );
			this.logFile.createNewFile();

			this.logFileWriter = new OutputStreamWriter( new FileOutputStream( this.logFile ) );
		}
		catch ( IOException ioe )
		{
			Xenon.INSTANCE.sendErrorMessage( "text.xenon.blackbox.ioexception.creation" );
			ioe.printStackTrace();
			this.logFile = null;
			this.logFileWriter = null;
			this.disable();
		}

		try
		{
			this.logFileWriter.append( '[' ).append( LocalTime.now().toString() ).append( "] " )
					.append( "BlackBox logging started. Stay safe!\n" );
		}
		catch ( IOException ioe )
		{
			Xenon.INSTANCE.sendErrorMessage( "text.xenon.blackbox.ioexception.creation" );
			ioe.printStackTrace();
			this.disable();
		}
	}

	private void writeDataSafe( String... datas )
	{
		try
		{
			this.logFileWriter.append( '[' )
					.append( LocalTime.now().toString() )
					.append( "] " );

			for ( String data : datas ) this.logFileWriter.append( data );

			this.logFileWriter.append( '\n' );
		}
		catch ( IOException ioe )
		{
			ioe.printStackTrace();
			Xenon.INSTANCE.sendErrorMessage( "text.xenon.blackbox.ioexception.write" );
		}
		catch ( NullPointerException npe )
		{
			Xenon.INSTANCE.LOGGER.warn( "BlackBox write attempted but logfile was null!" );
		}
	}

	private void writeData( String... datas ) throws IOException
	{
		try
		{
			this.logFileWriter.append( '[' )
					.append( LocalTime.now().toString() )
					.append( "] " );

			for ( String data : datas ) this.logFileWriter.append( data );

			this.logFileWriter.append( '\n' );
		}
		catch ( NullPointerException npe )
		{
			Xenon.INSTANCE.LOGGER.warn( "BlackBox write attempted but logfile was null!" );
		}
	}

	private void cleanup()
	{
		if ( this.logFile == null || this.logFileWriter == null || this.trackingLogTimerFuture == null )
			return;

		// Cancel tracking log task
		this.trackingLogTimerFuture.cancel( false );
		this.trackingLogTimerFuture = null;

		// Clean up files gracefully
		try { this.writeData( "BlackBox logging stopped." ); logFileWriter.close(); }
		catch ( IOException e ) { Xenon.INSTANCE.sendErrorMessage( "text.xenon.blackbox.ioexception.disposal" ); }

		this.logFile = null;
		this.logFileWriter = null;
		index++;
	}
}
