package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.WailaGroup;
import me.av306.xenon.event.RenderInGameHudEvent;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.util.color.ColorUtil;
import me.av306.xenon.util.text.TextUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
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
	private Text dataText = new LiteralTextContent( "Internal Error :(" ); // fallback

	private short ticks = 0;

	//private int interval = WailaGroup.interval;
	//public short getLimit() { return limit; }
	//public static void setLimit( short limit ) { limit = limit; }

	public WailaFeature()
	{
		super("WAILA"); 

		// register event
		RenderInGameHudEvent.AFTER_VIGNETTE.register( this::onInGameHudRender );
	}	

	private ActionResult onInGameHudRender( MatrixStack matrices, float tickDelta )
	{
		if ( !this.isEnabled ) return ActionResult.PASS;

		final int interval = WailaGroup.interval;

		ticks++;

		// get centre crosshair target
		// seems to work fine for javier
		HitResult hit = Xenon.INSTANCE.client.crosshairTarget;

		// actual waila logic
		if ( ticks >= interval )
		{
			this.createDataText( hit );
			ticks = 0;
		}

		// display block data (should go e ery frame otherwise it will flash)
		this.drawDataText( matrices, dataText );

		return ActionResult.PASS;
	}

	private void createDataText( HitResult hit )
	{
		switch (hit.getType())
		{
			case MISS ->
			{
				// nothing near enough :)
				dataText = new LiteralTextContent("");
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
				// not the avtual break time (which depends on the tool).
				// This is intentional; I didn't implement the break time calculations
				// because the chances that someone will even *use* Xenon are so astronomically small,
				// and there are so many other *good* WAILA mods,
		    // so there was no point.
				this.dataText = new TranslatableTextContent( "text.xenon.waila.blocktype", block.getName(), block.getHardness(), block.getBlastResistance());
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
				this.dataText = new TranslatableTextContent("text.xenon.waila.entityhealth",
						type.getName(),
						health
				);
			}
		}
	}

	private void drawDataText( MatrixStack matrices, Text text )
	{
		TextUtil.drawPositionedText( matrices, text, WailaGroup.position, 0, 0, false, ColorUtil.RED );
	}
	
	@Override
	public void onEnable() {}

	@Override
	public void onDisable() {}
}