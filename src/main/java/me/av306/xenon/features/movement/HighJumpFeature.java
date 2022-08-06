package me.av306.xenon.features.movement;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.HighJumpGroup;
import me.av306.xenon.event.EventFields;
import me.av306.xenon.feature.IFeature;

public class HighJumpFeature extends IFeature
{
    public HighJumpFeature()
    {
        super( "HighJump", "hj" );

        //GetJumpVelocityEvent.EVENT.register( this::onGetJumpVelocity );
    }

    @Override
    protected void onEnable()
    {
        float h = HighJumpGroup.height * HighJumpGroup.multiplier;
        EventFields.JUMP_VELOCITY_MODIFIER += h;

        Xenon.INSTANCE.client.player.jump();

        EventFields.JUMP_VELOCITY_MODIFIER -= h;
    }

    @Override
    protected boolean onConfigChange( String config, String value ) //throws NumberFormatException
    {
        float v = Float.parseFloat( value );
        if (
            config.contains( "velocity" ) || config.contains( "v" ) ||
            config.contains( "height" ) || config.contains( "h" )
        )
        {
            HighJumpGroup.height = v;

            return true;
        }
        else if ( config.contains( "multiplier" ) || config.contains( "m" ) )
        {
            HighJumpGroup.multiplier = v;
            return true;
        }
        
        return false;
    }
}
