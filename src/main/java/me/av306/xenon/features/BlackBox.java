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

	private ConcurrentLinkedQueue<LoggingData> dataQueue = new ConcurrentLinkedQueue<>();

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool( 1 );
	private ScheduledFuture<?> trackingLogTimerFuture = null;
	private ScheduledFuture<?> dataWritierThread = null;

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

		this.dataWritierThread = scheduler.scheduleAtFixedRate(
				this::writeDataToFile,
				0, 5,
				TimeUnit.MINUTES
		);
	}

	private void writeDataToFile()
	{
		// ConcurrentLinkedQueue has a weakly consistent iterator
		for ( LoggingData data : this.dataQueue )
		{
			if ( data instanceof PlayerStateData )
			{

			}
			else if ( data instanceof EntityAttackPlayerData )
			{

			}
			else
			{

			}
		}
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
			PlayerStateData data = new PlayerStateData();

			// Log dimension name
			data.setDimension( Xenon.INSTANCE.client.world.getDimensionKey().getValue() );

			// Log position
			data.setPosition( Xenon.INSTANCE.client.player.getPos() );

			// Log velocity
			data.setVelocity( Xenon.INSTANCE.client.player.getVelocity() );

			// Log rotation
			data.setRotation( Xenon.INSTANCE.client.player.getRotationVector() );

			// Log stats
			data.setHealth( Xenon.INSTANCE.client.player.getHealth() );

			data.setHunger( Xenon.INSTANCE.client.player.getHungerManager().getFoodLevel() );

			data.setExhaustion( Xenon.INSTANCE.client.player.getHungerManager().getExhaustion() );

			data.setSaturation( Xenon.INSTANCE.client.player.getHungerManager().getSaturationLevel() );

			// Log armour
			data.setArmour( Xenon.INSTANCE.client.player.getArmorItems() );

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

	private static interface LoggingData {}

	private static class PlayerStateData implements LoggingData
	{
		private Identifier dimension;
		private Vec3d position;
		private Vec3d rotation;
		private Vec3d velocity;
		private float health;
		private int hunger;
		private float exhaustion;
		private float saturation;
		private ItemStack[] armour;
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

		public ItemStack[] getArmour() {
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

	private static class PlayerAttackEntityData implements LoggingData
	{
		private PlayerStateData stateData;
	}

	private static class EntityAttackPlayerData implements LoggingData
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
