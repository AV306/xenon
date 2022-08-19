package me.av306.xenon.features.movement;

import me.av306.xenon.config.feature.JumpBoostGroup;
import me.av306.xenon.event.EventFields;
import me.av306.xenon.event.GetJumpVelocityEvent;
import me.av306.xenon.feature.IToggleableFeature;
import net.minecraft.util.ActionResult;

public class JumpBoostFeature extends IToggleableFeature
{
    //protected static LegacyHighJumpFeature instance;

    private short ticks = 0;

    public JumpBoostFeature()
    {
        super( "JumpBoost", "boost", "jb" );

        GetJumpVelocityEvent.EVENT.register( this::onGetJumpVelocity );

        //instance = this;
    }

    @Override
    public String getName()
    {
        // when the name is retrieved, update it and return
        this.setName( "JumpBoost (" + JumpBoostGroup.height + ")" );

        return this.name;
    }

    private ActionResult onGetJumpVelocity()
    {
        if ( this.isEnabled )
            EventFields.JUMP_VELOCITY_MODIFIER = JumpBoostGroup.height * JumpBoostGroup.multiplier;

        return ActionResult.PASS;
    }

    /*@Override
    public static LegacyHighJumpFeature getInstance()
    {
        return instance;
    }*/

    @Override
    protected void onEnable()
    {
        float height = JumpBoostGroup.height * JumpBoostGroup.multiplier;
        EventFields.JUMP_VELOCITY_MODIFIER = height;
        this.setName( "JumpBoost (" + height + ")" );
    }

    @Override
    protected void onDisable()
    {
        EventFields.JUMP_VELOCITY_MODIFIER = 0f; // TODO: make way for other features that might want to modify this
    }

    @Override
    protected boolean onRequestConfigChange(String config, String value ) //throws NumberFormatException
    {
        float v = Float.parseFloat( value );
        if (
            config.contains( "velocity" ) || config.contains( "v" ) ||
            config.contains( "height" ) || config.contains( "h" )
        )
        {
            JumpBoostGroup.height = v;

            return true;
        }
        else if ( config.contains( "multiplier" ) || config.contains( "m" ) )
        {
            JumpBoostGroup.multiplier = v;
            return true;
        }
        
        return false;
    }
}
