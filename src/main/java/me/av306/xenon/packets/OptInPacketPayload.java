package me.av306.xenon.packets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record OptInPacketPayload( String featureName ) implements CustomPayload
{
    public static final Id<OptInPacketPayload> ID = CustomPayload.id( "xenon:opt_in" );
    public static final PacketCodec<PacketByteBuf, OptInPacketPayload> CODEC = PacketCodec.tuple( PacketCodecs.STRING, OptInPacketPayload::featureName, OptInPacketPayload::new );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

}
