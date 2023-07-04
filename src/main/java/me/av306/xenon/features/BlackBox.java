package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.BlackBoxGroup;
import me.av306.xenon.event.MinecraftClientEvents;
import me.av306.xenon.feature.IToggleableFeature;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.util.ActionResult;
import org.joml.Vector3i;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.*;

public class BlackBox extends IToggleableFeature
{
	private File logFile = null;
	private OutputStreamWriter logFileWriter = null;
	private int index = 0;

	private ConcurrentLinkedQueue<LoggingData> dataQueue = new ConcurrentLinkedQueue<>();

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool( 1 );
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
		this.createLogFile();

		// Write starting data
		this.dataQueue.add( this.createWorldData() );

		// Register 5-minute tracking log timer (working!!!)
		this.trackingLogTimerFuture = scheduler.scheduleAtFixedRate(
			() -> this.dataQueue.add( this.createPlayerStateData() ),
			0, BlackBoxGroup.trackingLogInterval,
			TimeUnit.MINUTES
		);

		this.dataWriterThread = scheduler.scheduleAtFixedRate(
				this::writeDataToFile,
				0, BlackBoxGroup.writeInterval,
				TimeUnit.MINUTES
		);
	}

	private void writeDataToFile()
	{
		try
		{
			if ( BlackBoxGroup.writeEntireQueue )
			{
				// Write the entire queue
					for ( LoggingData data : this.dataQueue )
						this.logFileWriter.append( this.parseData( data ) );
			}
			else
			{
				// Write little bits
				for ( var i = 0; i < BlackBoxGroup.bufferSize; i++ )
				{
					LoggingData data = this.dataQueue.poll();
					if ( data == null ) break; // Break out if we've reached the end of the queue

					this.logFileWriter.append( this.parseData( data ) );
				}
			}
		}
		catch ( IOException ioe )
		{
			Xenon.INSTANCE.sendErrorMessage( "text.xenon.blabkbox.ioexception.write" );
			ioe.printStackTrace();
		}
	}

	@Override
	protected void onDisable()
	{
		this.cleanup();
	}

	private String parseData( LoggingData data )
	{
		StringBuilder builder = new StringBuilder( data.getTime() ).append( '\n' );

		if ( data instanceof PlayerStateData playerStateData )
		{
			// TODO: Finish
			builder.append( playerStateData.getDimension().toString() ).append( '\n' );
			//builder.append( playerStateData.get)
		}
		else if ( data instanceof WorldData )
		{

		}// TODO: Implement other cases
		else
		{
			return "Unknown data type";
		}
		return "todo";
	}


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

		return data;
	}

	private PlayerStateData createPlayerStateData()
	{	
		PlayerStateData data = new PlayerStateData();

		// Log dimension name
		data.dimension = Xenon.INSTANCE.client.world.getDimensionKey().getValue();
		// Log position
		data.position = Xenon.INSTANCE.client.player.getPos();
		// Log velocity
		data.velocity = Xenon.INSTANCE.client.player.getVelocity();
		// Log rotation
		data.rotation = Xenon.INSTANCE.client.player.getRotationVector();
		// Log stats
		data.health = Xenon.INSTANCE.client.player.getHealth();
		data.hunger = Xenon.INSTANCE.client.player.getHungerManager().getFoodLevel();
		data.exhaustion = Xenon.INSTANCE.client.player.getHungerManager().getExhaustion();
		data.saturation = Xenon.INSTANCE.client.player.getHungerManager().getSaturationLevel();
		// Log armour
		data.armour = Xenon.INSTANCE.client.player.getArmorItems();
		// Log items
		data.mainHandItem = Xenon.INSTANCE.client.player.getMainHandStack();
		data.offHandItem = Xenon.INSTANCE.client.player.getOffHandStack();

		return data;
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

		// Cancel data writer
		// TODO: How do we make it finish logging everything?

		// Clean up files gracefully
		try { this.writeData( "BlackBox logging stopped." ); logFileWriter.close(); }
		catch ( IOException e ) { Xenon.INSTANCE.sendErrorMessage( "text.xenon.blackbox.ioexception.disposal" ); }

		this.logFile = null;
		this.logFileWriter = null;
		index++;
	}

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

	private static class WorldData extends LoggingData
	{
		// TODO: Encapsulate
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
