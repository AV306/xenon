package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.callback.InGameHudRenderCallback;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.util.General;

//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
//import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
//import net.minecraft.util.math.BlockPos;

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
				//BlockState blockState = Xenon.INSTANCE.client.getBlockState( blockPos );
				//Block block = blockState.getBlock(); // finally.
				
				//Block block = Xenon.INSTANCE.client.getBlockState( ((BlockHitResult) hit).getBlockPos() )
				//	.getBlock(); // ugh

				// display block data TODO: maybe put in private method?
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

				// get health
				float health = livingEntity.getHealth();

				// now draw text!!! :D
				String healthText = (new TranslatableText( "text.xenon.waila.entityhealth" )).asString() + 
					": " + Float.toString( health );

				this.drawDataText( matrices, healthText );
		}
	}

	private void drawDataText( MatrixStack matrices, String text )
	{
		TextRenderer tr = Xenon.INSTANCE.client.textRenderer;
		// calculate x and y
		int x = (Xenon.INSTANCE.client.getWindow().getScaledWidth() - tr.getWidth( text )) / 2;
		int y = 5;
				
		tr.drawWithShadow( matrices, text, x, y, General.rgbToInt( 255, 0, 0 ) );
	}
	
	@Override
	public void onEnable() {}

	@Override
	public void onDisable() {}
}