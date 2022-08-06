package me.av306.xenon.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( Block.class )
public abstract class BlockMixin extends AbstractBlock implements ItemConvertible
{
    protected BlockMixin( Settings settings ) {
        super( settings );
    }
    /*@Shadow
    @Final
    protected float slipperiness;*/

    @Inject(
            at = @At( "RETURN" ),
            method = "getSlipperiness()F",
            cancellable = true
    )
    private void onGetSlipperiness( CallbackInfoReturnable<Float> cir )
    {

        //cir.setReturnValue( cir.getReturnValue() + EventFields);

        /*if ( EventFields.SLIPPERINESS_OVERRIDE < 0f )
            cir.setReturnValue( EventFields.SLIPPERINESS_OVERRIDE );*/
        //cir.setReturnValue( 0.1f );
    }
}
