package me.av306.xenon.mixin;

import com.mojang.authlib.GameProfile;
import me.av306.xenon.config.feature.HighJumpGroup;
import me.av306.xenon.event.EventFields;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;

@Mixin( ClientPlayerEntity.class )
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity
{
    public ClientPlayerEntityMixin( ClientWorld world, GameProfile profile )
    {
        super( world, profile );
    }

    @Override
    public float getJumpVelocity()
    {
        return super.getJumpVelocity() + EventFields.JUMP_VELOCITY_MODIFIER;
    }


}
