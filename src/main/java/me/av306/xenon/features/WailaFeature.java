package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.callback.InGameHudRenderCallback;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.util.color.ColorUtil;

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

		switch ( hit.getType() )
		{
			case MISS:
				// nothing near enough :)
				break;

			case BLOCK:
				// looking at a block, now what block is it?
				// TODO: implement
				//BlockHitResult blockHit = (BlockHitResult) hit;
				//BlockPos blockPos = blockHit.getBlockPos();
				//BlockState blockState = Xenon.INSTANCE.client.world.getBlockState( blockPos );
				//Block block = blockState.getBlock(); // finally.
				Block block = Xenon.INSTANCE.client.world.getBlockState(((BlockHitResult) hit).getBlockPos()).getBlock();

				Text blockDataText = new TranslatableText( "text.xenon.waila.blocktype", block.getName() );

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
				// StringBuilder for optimisation
				Text healthText = new TranslatableText( "text.xenon.waila.entityhealth",
						type.getName(),
						Text.of( String.valueOf( health) )
				);

				this.drawDataText( matrices, healthText );
		}

		
		return ActionResult.PASS;

	}

	private void drawDataText(MatrixStack matrices, Text text )
	{
		TextRenderer tr = Xenon.INSTANCE.client.textRenderer;
		// calculate x and y
		int x = (Xenon.INSTANCE.client.getWindow().getScaledWidth() - tr.getWidth( text )) / 2;
		int y = 5;
				
		tr.drawWithShadow( matrices, text, x, y, ColorUtil.rgbToInt( 255, 0, 0 ) );
	}
	
	@Override
	public void onEnable() {}

	@Override
	public void onDisable() {}
}