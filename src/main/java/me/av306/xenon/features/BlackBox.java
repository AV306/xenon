package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.BlackBoxGroup;
import me.av306.xenon.event.MinecraftClientEvents;
import me.av306.xenon.feature.IToggleableFeature;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.util.ActionResult;
import org.joml.Vector3i;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.*;

public class BlackBox extends IToggleableFeature
{
	private File logFile = null;
	private OutputStreamWriter logFileWriter = null;
	private int index = 0;

	private ConcurrentLinkedQueue<LoggingData> dataQueue = new ConcurrentLinkedQueue<>();

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool( 2 );
	private ScheduledFuture<?> trackingLogTimerFuture = null;
	private ScheduledFuture<?> dataWriterThread = null;

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
		Xenon.INSTANCE.LOGGER.info( "BlackBox creating log file" );
		this.createLogFile();

		// Write starting data
		this.dataQueue.add( this.createWorldData() );

		// Register 5-minute tracking log timer
		this.trackingLogTimerFuture = scheduler.scheduleWithFixedDelay(
				() -> this.dataQueue.add( this.createPlayerStateData() ),
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
				this.logFileWriter.append( this.parseData( data ) );
				this.dataQueue.remove( data );
			}

			this.logFileWriter.flush();
		}
		catch ( IOException ioe )
		{
			Xenon.INSTANCE.sendErrorMessage( "text.xenon.blabkbox.exception.write" );
			ioe.printStackTrace();
		}
	}

	@Override
	protected void onDisable()
	{
		this.cleanup();
	}

	/**
	 * Serialise the data contained in a data object into a string.
	 * @param data: The data object
	 * @return The serialised data
	 */
	private String parseData( LoggingData data )
	{
		// Begin string with timestamp
		StringBuilder builder = new StringBuilder( "[" )
				.append( data.getTime() ).append( "] " );

		// TODO: Implement other data types
		if ( data instanceof PlayerStateData playerStateData )
		{
			builder.append( "BEGIN PLAYER STATE DATA ====================\n" );

			builder.append( "Dimension: " ) .append( playerStateData.dimension.toString() ).append( '\n' );
			builder.append( "Position: " ).append( playerStateData.position.toString() ).append( '\n' );
			builder.append( "Rotation: " ).append( playerStateData.rotation.toString() ).append( '\n' );
			builder.append( "Velocity: " ).append( playerStateData.velocity.toString() ).append( '\n' );
			builder.append( "Health: " ).append( playerStateData.health ).append( '\n' );
			builder.append( "Hunger: " ).append( playerStateData.hunger ).append( '\n' );
			builder.append( "Saturation: " ).append( playerStateData.saturation ).append( '\n' );
			builder.append( "Exhaustion: " ).append( playerStateData.exhaustion ).append( '\n' );

			builder.append( "Armour:\n" );
			for ( ItemStack stack : playerStateData.armour )
			{
				builder.append( "\tName: " ).append( stack.getItem().toString() ).append( '\n' );
				builder.append( "\tDamage: " ).append( stack.getDamage() ).append( '\n' );
			}

			builder.append( "Main hand item:\n" );
			builder.append( "\tName: " ).append( playerStateData.mainHandItem.getName().toString() ).append( '\n' );
			builder.append( "\tDamage: " ).append( playerStateData.mainHandItem.getDamage() ).append( '\n' );

			builder.append( "Off hand item:\n" );
			builder.append( "\tName: " ).append( playerStateData.offHandItem.getName().toString() ).append( '\n' );
			builder.append( "\tDamage: " ).append( playerStateData.offHandItem.getDamage() ).append( '\n' );
			
			builder.append( "END PLAYER STATE DATA ====================\n" );

			return builder.toString();
		}
		else if ( data instanceof WorldData worldData )
		{
			builder.append( "BEGIN WORLD DATA ====================\n" );

			builder.append( "World type: " ).append( worldData.isClient ? "Local" : "Server" ).append( '\n' );
			builder.append( "Difficulty: " ).append( worldData.difficulty.getName() );
			if ( worldData.isDifficultyLocked ) builder.append( " (Hardcore) " );
			if ( worldData.isDifficultyLocked ) builder.append( " (Locked)" );

			builder.append( '\n' );

			builder.append( "Spawn position: " ).append( worldData.spawnPos.toString() ).append( '\n' );
			builder.append( "Spawn angle: " ).append( worldData.spawnAngle ).append( '\n' );
			builder.append( "Spawn time: " ).append( worldData.time ).append( '\n' );

			builder.append( "END WORLD DATA ====================\n" );

			return builder.toString();
		}
		else
		{
			return "Unknown data type";
		}
	}

	/**
	 * Create world data
	 * @return The build WorldData object
	 */
	private WorldData createWorldData()
	{
		WorldData data = new WorldData();

		ClientWorld clientWorld = Xenon.INSTANCE.client.world;
		ClientWorld.Properties props = clientWorld.getLevelProperties();

		data.isClient = clientWorld.isClient();
		data.difficulty = props.getDifficulty();
		data.isHardcore = props.isHardcore();
		data.isDifficultyLocked = props.isDifficultyLocked();
		data.spawnPos.set( props.getSpawnX(), props.getSpawnY(), props.getSpawnZ() );
		data.spawnAngle = props.getSpawnAngle();
		data.time = props.getTime();

		Xenon.INSTANCE.LOGGER.info( "BlackBox created world data" );

		return data;
	}

	/**
	 * Create player state data
	 * @return The built PlayerStateData object
	 */
	private PlayerStateData createPlayerStateData()
	{	
		PlayerStateData data = new PlayerStateData();

		net.minecraft.client.network.ClientPlayerEntity player = Xenon.INSTANCE.client.player;

		// Log dimension name
		data.dimension = Xenon.INSTANCE.client.world.getDimensionKey().getValue();
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
					.append( "BlackBox logging started. Stay safe!\n" )
		}
		catch ( IOException ioe )
		{
			Xenon.INSTANCE.sendErrorMessage( "text.xenon.blackbox.exception.creation" );
			ioe.printStackTrace();

            // Try to clean up resources
			try
			{
				this.logFile.delete();
				this.logFileWriter.close();
			}
			// Will probably throw NPE if the IOE was thrown before the creation
			// of either the file or the writer
			catch ( NullPointerException npe ) {}
				
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
			Xenon.INSTANCE.sendErrorMessage( "text.xenon.blackbox.exception.write" );
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
		catch ( Exception e ) { e.printStackTrace(); Xenon.INSTANCE.sendErrorMessage( "text.xenon.blackbox.exception.disposal" ); }
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
	}

	/**
	 * World data class
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
			super();
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

		/*public PlayerStateData(
				Identifier dimension,
				Vec3d position,
				Vec3d rotation,
				Vec3d velocity,
				float health,
				int hunger,
				float exhaustion,
				float saturation,
				ItemStack[] armour,
				ItemStack mainHandItem,
				ItemStack offHandItem
		)
		{
			this.dimension = dimension;
			this.position = position;
			this.rotation = rotation;
			this.velocity = velocity;
			this.health = health;
			this.hunger = hunger;
			this.exhaustion = exhaustion;
			this.saturation = saturation;
			this.armour = armour;
			this.mainHandItem = mainHandItem;
			this.offHandItem = offHandItem;
		}*/

		public PlayerStateData()
		{
			super();
		}
		/*
		public Identifier getDimension() {
			return dimension;
		}

		public void setDimension(Identifier dimension) {
			this.dimension = dimension;
		}

		public Vec3d getPosition() {
			return position;
		}

		public void setPosition(Vec3d position) {
			this.position = position;
		}

		public Vec3d getRotation() {
			return rotation;
		}

		public void setRotation(Vec3d rotation) {
			this.rotation = rotation;
		}

		public Vec3d getVelocity() {
			return velocity;
		}

		public void setVelocity(Vec3d velocity) {
			this.velocity = velocity;
		}

		public float getHealth() {
			return health;
		}

		public void setHealth(float health) {
			this.health = health;
		}

		public int getHunger() {
			return hunger;
		}

		public void setHunger(int hunger) {
			this.hunger = hunger;
		}

		public float getExhaustion() {
			return exhaustion;
		}

		public void setExhaustion(float exhaustion) {
			this.exhaustion = exhaustion;
		}

		public float getSaturation() {
			return saturation;
		}

		public void setSaturation(float saturation) {
			this.saturation = saturation;
		}

		public Iterable<ItemStack> getArmour() {
			return armour;
		}

		public void setArmour(Iterable<ItemStack> armour) {
			this.armour = armour;
		}

		public ItemStack getMainHandItem() {
			return mainHandItem;
		}

		public void setMainHandItem(ItemStack mainHandItem) {
			this.mainHandItem = mainHandItem;
		}

		public ItemStack getOffHandItem() {
			return offHandItem;
		}

		public void setOffHandItem(ItemStack offHandItem) {
			this.offHandItem = offHandItem;
		}
	*/
	}

	private static class PlayerAttackEntityData extends LoggingData
	{
		private PlayerStateData stateData;
	}

	private static class EntityAttackPlayerData extends LoggingData
	{
		private DamageSource source;
		private PlayerStateData stateData;

		public DamageSource getSource() {
			return source;
		}

		public void setSource(DamageSource source) {
			this.source = source;
		}

		public PlayerStateData getStateData() {
			return stateData;
		}

		public void setStateData(PlayerStateData stateData) {
			this.stateData = stateData;
		}
	}
}
