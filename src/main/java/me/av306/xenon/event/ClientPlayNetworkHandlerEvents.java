package me.av306.xenon.event;

import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.util.ActionResult;

public interface ClientPlayNetworkHandlerEvents
{
    Event<EntityDamaged> ENTITY_DAMAGED = EventFactory.createArrayBacked(
            EntityDamaged.class,
            (listeners) -> (packet) ->
            {
                for ( EntityDamaged listener : listeners )
                {
                    ActionResult result = listener.onEntityDamaged( packet );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    Event<PlayerHealthUpdated> PLAYER_HEALTH_UPDATE = EventFactory.createArrayBacked(
            PlayerHealthUpdated.class,
            (listeners) -> (packet) ->
            {
                for ( PlayerHealthUpdated listener : listeners )
                {
                    ActionResult result = listener.onPlayerHealthUpdated( packet );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    Event<PlayerRespawned> PLAYER_RESPAWN = EventFactory.createArrayBacked(
            PlayerRespawned.class,
            (listeners) -> (packet) ->
            {
                for ( PlayerRespawned listener : listeners )
                {
                    ActionResult result = listener.onPlayerRespawned( packet );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );



    interface EntityDamaged
    {
        ActionResult onEntityDamaged( EntityDamageS2CPacket packet );
    }

    interface PlayerHealthUpdated
    {
        ActionResult onPlayerHealthUpdated( HealthUpdateS2CPacket packet );
    }

    interface PlayerRespawned
    {
        ActionResult onPlayerRespawned( PlayerRespawnS2CPacket packet );
    }

}
