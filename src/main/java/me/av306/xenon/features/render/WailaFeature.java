package me.av306.xenon.features.render;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.render.WailaGroup;
import me.av306.xenon.event.RenderInGameHudEvent;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.util.color.ColorUtil;
import me.av306.xenon.util.text.TextFactory;
import me.av306.xenon.util.text.TextUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class WailaFeature extends IToggleableFeature
{
	// Krystal's suggestion :D
	// There will always only be one translatable text, 
	// so we can construct it at class level (?)
	private Text dataText =  TextFactory.createLiteral( "Internal Error :(" ); // fallback

	private short ticks = 0;

	//private int interval = WailaGroup.interval;
	//public short getLimit() { return limit; }
	//public static void setLimit( short limit ) { limit = limit; }

	public WailaFeature()
	{
		super( "WAILA" );

		// register event
		RenderInGameHudEvent.AFTER_VIGNETTE.register( this::onInGameHudRender );
	}	

	private ActionResult onInGameHudRender( DrawContext drawContext, float tickDelta )
	{
		if ( !this.isEnabled ) return ActionResult.PASS;

		final int interval = WailaGroup.interval;

		ticks++;

		// get centre crosshair target
		// seems to work fine for javier
		HitResult hit = Xenon.INSTANCE.client.player.raycast( WailaGroup.maxDistance, tickDelta, true );

		// actual waila logic
		if ( ticks >= interval )
		{
			this.createDataText( hit );
			ticks = 0;
		}

		// display block data (should go every frame otherwise it will flash)
		this.drawDataText( drawContext, dataText );

		return ActionResult.PASS;
	}

	private void createDataText( HitResult hit )
	{
		switch ( hit.getType() )
		{
			case MISS ->
			{
				// nothing near enough :)
				dataText = TextFactory.createEmpty();
			}

			case BLOCK ->
			{
				// looking at a block, now what block is it?
				// TODO: add more details, multiline it
				// Let's try this. Is it faster?
				BlockHitResult blockHit = (BlockHitResult) hit;
				BlockPos blockPos = blockHit.getBlockPos();
				BlockState blockState = Xenon.INSTANCE.client.world.getBlockState( blockPos );
				Block block = blockState.getBlock(); // finally.

				//Block block = Xenon.INSTANCE.client.world.getBlockState(((BlockHitResult) hit).getBlockPos()).getBlock();
        		// The sharp-eyed may notice that we're just sending over the block hardness, 
				// not the actual break time (which depends on the tool).
				// This is intentional; I didn't implement the break time calculations
				// because the chances that someone will even *use* Xenon are so astronomically small,
				// and there are so many other *good* WAILA mods,
		    	// so I figured there was no point.
				this.dataText = TextFactory.createTranslatable(
						"text.xenon.waila.blocktype",
						block.getName(),
						block.getHardness(),
						block.getBlastResistance()
				);
			}

			case ENTITY ->
			{
				// looking at a entity, now who/what is it?
				//EntityHitResult entityHit = (EntityHitResult) hit;
				//Entity entity = entityHit.getEntity();
				Entity entity = ((EntityHitResult) hit).getEntity();

				// if not a mob/player, discard
				if (!(entity instanceof LivingEntity livingEntity)) break;

				// don't need to figure out the exact entity type
				EntityType<?> type = livingEntity.getType();

				// get health
				float health = livingEntity.getHealth();

				// now draw text!!! :D
				this.dataText = TextFactory.createTranslatable(
						"text.xenon.waila.entityhealth",
						type.getName(),
						health
				);
			}
		}
	}

	private void drawDataText( DrawContext drawContext, Text text )
	{
		TextUtil.drawPositionedText( drawContext, text, WailaGroup.position, 0, WailaGroup.yOffset, false, ColorUtil.RED );
	}
	
	@Override
	public void onEnable() {}

	@Override
	public void onDisable() {}
}