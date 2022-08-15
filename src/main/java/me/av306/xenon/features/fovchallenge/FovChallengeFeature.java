package me.av306.xenon.features.fovchallenge;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.EventFields;
import me.av306.xenon.event.LivingEntityKilledEvent;
import me.av306.xenon.event.PlayerKillEntityEvent;
import me.av306.xenon.feature.IToggleableFeature;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.Nullable;

public class FovChallengeFeature extends IToggleableFeature
{
    public FovChallengeFeature()
    {
        super( "FovChallenge", "fc", "fovc" );

        PlayerKillEntityEvent.EVENT.register( this::onPlayerKillEntity );
        LivingEntityKilledEvent.EVENT.register( this::onPlayerKilledByEntity );
        
        this.enable();
    }

    private ActionResult onPlayerKilledByEntity( @Nullable LivingEntity livingEntity )
    {
        if ( this.isEnabled /*&& livingEntity instanceof PlayerEntity*/ )
        {
            EventFields.FOV_MODIFIER += 10;
        }

        return ActionResult.PASS;
    }

    private ActionResult onPlayerKillEntity( ServerWorld serverWorld, LivingEntity livingEntity )
    {
        if ( this.isEnabled )
        {
            EventFields.FOV_MODIFIER -= 10;
            Xenon.INSTANCE.sendInfoMessage( "Decreased FOV on killed entity!" );
        }
        
        return ActionResult.PASS;
    }

    @Override
    protected void onEnable()
    {
        
    }

    @Override
    protected void onDisable()
    {

    }
}
