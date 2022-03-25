package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.callback.InGameHudRenderCallback;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.util.ScreenPosition;
import me.av306.xenon.util.color.ColorUtil;
import me.av306.xenon.util.text.TextUtil;

import net.minecraft.block.Block;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;

public class WailaFeature extends IToggleableFeature
{
	// Krystal's suggestion :D
	// There will always only be one translatable text, 
	// so we can construct it at class level (?)
	private Text dataText;
	
	// FIXME: Optimise to reduce heavy fps drop
	public WailaFeature()
	{
		super("WAILA"); 

		// register event
		InGameHudRenderCallback.EVENT.register( this::onInGameHudRender );
	}	

	private ActionResult onInGameHudRender( MatrixStack matrices, float tickDelta )
	{
		if ( !this.isEnabled ) return ActionResult.PASS;
		
		// get centre crosshair target
		// TODO: May be expensive, test FPS 
		// TODO: Get Javier to test on a low-end machine
		HitResult hit = Xenon.INSTANCE.client.crosshairTarget;

		// actual waila logic
		switch ( hit.getType() )
		{
			case MISS:
				// nothing near enough :)
				//break;
				return ActionResult.PASS; // does this even help?

			case BLOCK:
				// looking at a block, now what block is it?
				// TODO: add more details, multiline it
				// Let's try this. Is it faster?
				BlockHitResult blockHit = (BlockHitResult) hit;
				BlockPos blockPos = blockHit.getBlockPos();
				BlockState blockState = Xenon.INSTANCE.client.world.getBlockState( blockPos );
				Block block = blockState.getBlock(); // finally.
				//Block block = Xenon.INSTANCE.client.world.getBlockState(((BlockHitResult) hit).getBlockPos()).getBlock();

				dataText = new TranslatableText( "text.xenon.waila.blocktype", block.getName() );

				// display block data
				this.drawDataText( matrices, blockDataText );
				break;

			case ENTITY:
				// looking at a entity, now who/what is it?
				//EntityHitResult entityHit = (EntityHitResult) hit;
				//Entity entity = entityHit.getEntity();
				Entity entity = ((EntityHitResult) hit).getEntity();

				// if not a mob/player, discard
				if ( !(entity instanceof LivingEntity) ) break;

				// don't need to figure out the exact entity type
				LivingEntity livingEntity = (LivingEntity) entity;


				EntityType<?> type = livingEntity.getType();

				// get health
				float health = livingEntity.getHealth();

				// now draw text!!! :D
				dataText = new TranslatableText( "text.xenon.waila.entityhealth",
						type.getName(),
						Text.of( String.valueOf( health ) )
				);

				this.drawDataText( matrices, healthText );
				break; // hmm is this why?
		}

		return ActionResult.PASS;

	}

	private void drawDataText( MatrixStack matrices, Text text )
	{
		TextUtil.drawPositionedText( matrices, text, ScreenPosition.TOP_CENTER, 0, 0, ColorUtil.RED );
	}
	
	@Override
	public void onEnable() {}

	@Override
	public void onDisable() {}
}