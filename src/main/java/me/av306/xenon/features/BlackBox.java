package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.BlackBoxGroup;
import me.av306.xenon.event.ClientPlayNetworkHandlerEvents;
import me.av306.xenon.event.MinecraftClientEvents;
import me.av306.xenon.feature.IToggleableFeature;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.util.ActionResult;
import org.joml.Vector3i;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.*;

// what the fck is all this spaghetti doing
public class BlackBox extends IToggleableFeature
{
	/**
	 * Reference to the log file
	 */
	private File logFile = null;

	/**
	 * Log file writer
	 */
	private OutputStreamWriter logFileWriter = null;

	/**
	 * The index of the log file, 1 (one) greater than the number of logfiles already created
	 * (will be appended to the end of the filename)
	 */
	private int index = 0;

	/**
	 * Logging data queue, thread-safe
	 */
	private final ConcurrentLinkedQueue<LoggingData> dataQueue = new ConcurrentLinkedQueue<>();

	/**
	 * Concurrent thread scheduler
	 */
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool( 2 );

	/**
	 * Timer future for the tracking logger (the one that logs your position and stuff periodically)
	 */
	private ScheduledFuture<?> trackingLogTimerFuture = null;

	/**
	 * Timer future for the data writer
	 */
	private ScheduledFuture<?> dataWriterThread = null;

	public BlackBox()
	{
		super( "BlackBox" );

		// Register the event that enabled BB when you join a world
		MinecraftClientEvents.JOIN_WORLD_TAIL.register( world ->
		{
			if ( BlackBoxGroup.reEnableOnWorldEnter ) this.enable();
			return ActionResult.PASS;
		} );

		// Register logging hooks

		// Player attack event hook
		ClientPlayNetworkHandlerEvents.ENTITY_DAMAGED.register( this::onPlayerDamaged );
	}

	private ActionResult onPlayerDamaged( EntityDamageS2CPacket packet )
	{
		// Construct event data
		if ( !this.isEnabled ) return ActionResult.PASS;
		
		this.dataQueue.add( new EntityAttackPlayerData( packet ) );
		return ActionResult.PASS;
	}

	@Override
	protected void onEnable()
	{
		this.setShouldHide( !BlackBoxGroup.showInFeatureList );

		// Open a new log file and output stream
		Xenon.INSTANCE.LOGGER.info( "BlackBox creating log file" );
		this.createLogFile();

		// Write starting data
		this.dataQueue.add( new WorldData() );

		// Register 5-minute tracking log timer
		this.trackingLogTimerFuture = scheduler.scheduleWithFixedDelay(
				() -> this.dataQueue.add( new PlayerStateData() ),
				1, BlackBoxGroup.trackingLogInterval,
				TimeUnit.MINUTES
		);

		this.dataWriterThread = scheduler.scheduleWithFixedDelay(
				this::writeDataToFile,
				2, BlackBoxGroup.writeInterval,
				TimeUnit.MINUTES
		);
	}

	/**
	 * Write data from the queue to the log file
	 */
	private void writeDataToFile()
	{
		// FIXME: Comment out when not needed (.size() O(N), not efficient)
		Xenon.INSTANCE.LOGGER.info( "{} entries in data queue", this.dataQueue.size() );
		try
		{
			// Write the entire queue
			Xenon.INSTANCE.LOGGER.info( "BlackBox writing entire data queue" );
			for ( LoggingData data : this.dataQueue )
			{
				this.logFileWriter.append( data.toString() );
				this.dataQueue.remove( data );
			}

			this.logFileWriter.flush();
		}
		catch ( IOException ioe )
		{
			this.sendErrorMessage( "text.xenon.blackbox.exception.write" );
			ioe.printStackTrace();
		}
	}

	@Override
	protected void onDisable()
	{
		this.cleanup();
	}

	/**
	 * Create a world data object
	 * @return The build WorldData object
	 */
	private WorldData _createWorldData()
	{
		WorldData data = new WorldData();

		ClientWorld clientWorld = Xenon.INSTANCE.client.world;
		ClientWorld.Properties props = clientWorld.getLevelProperties();

		data.isClient = clientWorld.isClient();
		data.difficulty = props.getDifficulty();
		data.isHardcore = props.isHardcore();
		data.isDifficultyLocked = props.isDifficultyLocked();
		data.spawnPos.set( props.getSpawnPos().getX(), props.getSpawnPos().getY(), props.getSpawnPos().getZ() );
		data.spawnAngle = props.getSpawnAngle();
		data.time = props.getTime();

		Xenon.INSTANCE.LOGGER.info( "BlackBox created world data" );

		return data;
	}

	/**
	 * Create player state data (not used directly, handles in constructor)
	 * @return The built PlayerStateData object
	 */
	private PlayerStateData _createPlayerStateData()
	{	
		PlayerStateData data = new PlayerStateData();

		net.minecraft.client.network.ClientPlayerEntity player = Xenon.INSTANCE.client.player;

		// Log dimension name
		// Something changed in Minecraft from 1.20.4 to .6; idk if this works
		// Guess we'll find out in ShareLocation
		data.dimension = RegistryKeys.toDimensionKey( Xenon.INSTANCE.client.world.getRegistryKey() ).getValue();
		// Log position
		data.position = player.getPos();
		// Log velocity
		data.velocity = player.getVelocity();
		// Log rotation
		data.rotation = player.getRotationVector();
		// Log stats
		data.health = player.getHealth();
		data.hunger = player.getHungerManager().getFoodLevel();
		data.exhaustion = player.getHungerManager().getExhaustion();
		data.saturation = player.getHungerManager().getSaturationLevel();
		// Log armour
		data.armour = player.getArmorItems();
		// Log items
		data.mainHandItem = player.getMainHandStack();
		data.offHandItem = player.getOffHandStack();

		Xenon.INSTANCE.LOGGER.info( "BlackBox created player state data" );

		return data;
	}

	/**
	 * Create the log file
	 */
	private void createLogFile()
	{
		try
		{
			String basePath = FabricLoader.getInstance().getGameDir().toString() + File.separator +
			        LocalDateTime.now().toString().replace( ":", "." ) + "-";

			//Xenon.INSTANCE.LOGGER.info( gameDir + File.separator + timestamp + this.index + ".log" );

			this.logFile = new File( basePath + this.index + ".data" );
			while ( !this.logFile.createNewFile() )
			{
				this.index++;
				this.logFile = new File( basePath + this.index + ".data" );
			}

			this.logFileWriter = new OutputStreamWriter( new FileOutputStream( this.logFile ) );

			this.logFileWriter.append( '[' ).append( LocalTime.now().toString() ).append( "] " )
					.append( "BlackBox logging started. Stay safe!\n" );
		}
		catch ( IOException ioe )
		{
			this.sendErrorMessage( "text.xenon.blackbox.exception.creation" );
			ioe.printStackTrace();

            // Try to clean up resources
			try
			{
				this.logFile.delete();
				this.logFileWriter.close();
			}
			// Will probably throw NPE if the IOE was thrown before the creation
			// of either the file or the writer
			catch ( Exception npe ) {} // FIXME AAAAAAAAAAAA
				
			this.logFile = null;
			this.logFileWriter = null;
		}
	}

	/**
	 * Write data to the log file with a try/catch inside
	 * @param datas
	 */
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
			this.sendErrorMessage( "text.xenon.blackbox.exception.write" );
		}
		catch ( NullPointerException npe )
		{
			Xenon.INSTANCE.LOGGER.warn( "BlackBox write attempted but logfile was null!" );
		}
	}

	/**
	 * '/
	 * ''
	 */
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

	/**
	 * Clean up the log file and writer stream
	 */
	private void cleanup()
	{
		Xenon.INSTANCE.LOGGER.info( "BlackBox beginning cleanup" );

		// Return if anything is null (initialisation not complete)
		if ( this.logFile == null || this.logFileWriter == null || this.trackingLogTimerFuture == null || this.dataWriterThread == null )
			return;
		
		this.index++;

		// Cancel log tasks
		this.trackingLogTimerFuture.cancel( false );
		this.trackingLogTimerFuture = null;

		try
		{
			this.dataWriterThread.cancel( false ); // Cancel data writer thread

			int entriesLeft = this.dataQueue.size();
			Xenon.INSTANCE.LOGGER.info( "{} entries left in data queue", entriesLeft );
			if ( entriesLeft > 0 ) this.writeDataToFile(); // Flush what's left

			this.writeDataSafe( "Blackbox logging stopped. " );
			this.logFileWriter.flush();
		}
		catch ( Exception e ) { e.printStackTrace(); this.sendErrorMessage( "text.xenon.blackbox.exception.disposal" ); }
		finally
		{
			this.dataWriterThread = null;

			// Clean up files
			

			this.logFile = null;
			this.logFileWriter = null;
		}
	}



	/**
	 * Base class for log data, stores the time of creation
	 */
	private static abstract class LoggingData
	{
		private String timestamp;
		
		protected LoggingData()
		{
			// Record the time of creation
			this.timestamp = LocalTime.now().toString();
		}

		public String getTime() { return this.timestamp; }

		public abstract String toString();
	}

	/**
	 * World data
	 * <br>
	 * Records stuff about the world/server
	 */
	private static class WorldData extends LoggingData
	{
		private boolean isClient;
		private Difficulty difficulty;
		private boolean isHardcore;
		private boolean isDifficultyLocked;
		private Vector3i spawnPos = new Vector3i();
		private float spawnAngle;
		private long time;


		public WorldData()
		{
			// Rember time of creation
			super();

			ClientWorld clientWorld = Xenon.INSTANCE.client.world;
			ClientWorld.Properties props = clientWorld.getLevelProperties();

			this.isClient = clientWorld.isClient();
			this.difficulty = props.getDifficulty();
			this.isHardcore = props.isHardcore();
			this.isDifficultyLocked = props.isDifficultyLocked();
			this.spawnPos.set( props.getSpawnPos().getX(), props.getSpawnPos().getY(), props.getSpawnPos().getZ() );
			this.spawnAngle = props.getSpawnAngle();
			this.time = props.getTime();

			Xenon.INSTANCE.LOGGER.info( "BlackBox created world data" );
		}

		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder( "[" )
				.append( this.getTime() ).append( "] " );

			builder.append( "BEGIN WORLD DATA ====================\n" );

			builder.append( "World type: " ).append( this.isClient ? "Local" : "Server" ).append( '\n' );
			builder.append( "Difficulty: " ).append( this.difficulty.getName() );
			if ( this.isHardcore ) builder.append( " (Hardcore) " );
			if ( this.isDifficultyLocked ) builder.append( " (Locked)" );

			builder.append( '\n' );

			builder.append( "Spawn position: " ).append( this.spawnPos.toString() ).append( '\n' );
			builder.append( "Spawn angle: " ).append( this.spawnAngle ).append( '\n' );
			builder.append( "Spawn time: " ).append( this.time ).append( '\n' );

			builder.append( "END WORLD DATA ====================\n" );

			return builder.toString();
		}
	}

	/**
	 * Player state data (aka tracking data)
	 * <br>
	 * Records pretty much everything about the player at a given instant
	 */
	private static class PlayerStateData extends LoggingData
	{
		private Identifier dimension;
		private Vec3d position;
		private Vec3d rotation;
		private Vec3d velocity;
		private float health;
		private int hunger;
		private float exhaustion;
		private float saturation;
		private Iterable<ItemStack> armour;
		private ItemStack mainHandItem;
		private ItemStack offHandItem;

		public PlayerStateData()
		{
			// Remember time of creation
			super();

			net.minecraft.client.network.ClientPlayerEntity player = Xenon.INSTANCE.client.player;

			// Log dimension name
			this.dimension = RegistryKeys.toDimensionKey( Xenon.INSTANCE.client.world.getRegistryKey() ).getValue();
			// Log position
			this.position = player.getPos();
			// Log velocity
			this.velocity = player.getVelocity();
			// Log rotation
			this.rotation = player.getRotationVector();
			// Log stats
			this.health = player.getHealth();
			this.hunger = player.getHungerManager().getFoodLevel();
			this.exhaustion = player.getHungerManager().getExhaustion();
			this.saturation = player.getHungerManager().getSaturationLevel();
			// Log armour
			this.armour = player.getArmorItems();
			// Log items
			this.mainHandItem = player.getMainHandStack();
			this.offHandItem = player.getOffHandStack();

			Xenon.INSTANCE.LOGGER.info( "BlackBox created player state data" );
		}
		
		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder( "[" )
				.append( this.getTime() ).append( "] " );

			builder.append( "BEGIN PLAYER STATE DATA ====================\n" );
			builder.append( "Dimension: " ) .append( this.dimension.toString() ).append( '\n' );
			builder.append( "Position: " ).append( this.position.toString() ).append( '\n' );
			builder.append( "Rotation: " ).append( this.rotation.toString() ).append( '\n' );
			builder.append( "Velocity: " ).append( this.velocity.toString() ).append( '\n' );
			builder.append( "Health: " ).append( this.health ).append( '\n' );
			builder.append( "Hunger: " ).append( this.hunger ).append( '\n' );
			builder.append( "Saturation: " ).append( this.saturation ).append( '\n' );
			builder.append( "Exhaustion: " ).append( this.exhaustion ).append( '\n' );	
			builder.append( "Armour:\n" );
			for ( ItemStack stack : this.armour )
			{
				builder.append( "\tName: " ).append( stack.getItem().toString() ).append( '\n' );
				builder.append( "\tDamage: " ).append( stack.getDamage() ).append( '\n' );
			}	
			builder.append( "Main hand item:\n" );
			builder.append( "\tName: " ).append( this.mainHandItem.getName().toString() ).append( '\n' );
			builder.append( "\tDamage: " ).append( this.mainHandItem.getDamage() ).append( '\n' );
			builder.append( "Off hand item:\n" );
			builder.append( "\tName: " ).append( this.offHandItem.getName().toString() ).append( '\n' );
			builder.append( "\tDamage: " ).append( this.offHandItem.getDamage() ).append( '\n' );				
			builder.append( "END PLAYER STATE DATA ====================\n" );	
			return builder.toString();
		}
	}

	private static class PlayerAttackEntityData extends LoggingData
	{
		private PlayerStateData stateData;

		@Override
		public String toString() { return ""; }
	}

	private static class EntityAttackPlayerData extends LoggingData
	{
		private DamageSource source;
		private PlayerStateData stateData;

		public EntityAttackPlayerData( EntityDamageS2CPacket packet )
		{
			// Remember time of creation
			super();

			// Grab tracking data
			this.stateData = new PlayerStateData();

			this.source = packet.createDamageSource( Xenon.INSTANCE.client.world );
		}

		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder( "[" )
				.append( this.getTime() ).append( "] " );

			builder.append( "BEGIN ENTITY ATTACK PLAYER DATA ====================\n" );


			builder.append( this.source.getType().toString() ).append( '\n' );

			net.minecraft.entity.Entity attacker = this.source.getAttacker();
			if ( attacker != null )
			{
				builder.append( "Attacker:\n" );
				builder.append( "\tPosition: " ).append( this.source.getPosition().toString() ).append( '\n' );
				builder.append( "\tName: " ).append( attacker.getDisplayName() );

				if ( attacker instanceof LivingEntity livingAttacker )
				{
					// Is player?
					if ( attacker instanceof PlayerEntity playerAttacker) builder.append( " (Player)\n" );

					// Health
					builder.append( "\tHealth: " ).append( livingAttacker.getHealth() );
				}

				builder.append( '\n' );
			}

			builder.append( this.stateData.toString() );

			builder.append( "END ENTITY ATTACK PLAYER DATA ====================\n" );
			return builder.toString();
		}
	}
}
