package me.av306.xenon.mixin;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.GeneralConfigGroup;
import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin( SharedConstants.class )
public class SharedConstantsMixin
{
    /**
     * A amazingly genius optimisation
     * from <https://github.com/astei/lazydfu>.
     * Defers DFU (DataFixerUpper) "optimisations"
     * to when it is actually needed.
     *
     * Since the bulk of MC's load time is DFU "optimisations",
     * (As evidenced by the low server start time and its corresponding
     * "Building unoptimized datafixer" message, and Vanilla's extremely long
     * load time and "Building optimized datafixer" message)
     * this mixin shaves a considerable amount of load time off.
     *
     * You can check the time MC spends "optimising" DFU
     * in the Vanilla logs.
     *
     * Look for a line in the logs saying something like
     * "[x] Datafixer optimisations took [y] milliseconds".
     * [x] should be in the range of a few thousand,
     * and [y] could be anything.
     * (For me, [x] = 4238 and [y] = 43843)
     */

    @Inject(
        at = @At( "HEAD" ),
        method = "enableDataFixerOptimization()V",
        cancellable = true
    )
    private static void onEnableDataFixerOptimization( CallbackInfo ci )
    {
        // make it conditional
        // cancel the optimisations if user wants MC to be fast
        if ( GeneralConfigGroup.lazyDfu ) ci.cancel();
    }
}