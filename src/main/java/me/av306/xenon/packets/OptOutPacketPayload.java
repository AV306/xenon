package me.av306.xenon.packets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record OptOutPacketPayload( String featureName ) implements CustomPayload
{
    public static final Id<OptOutPacketPayload> ID = CustomPayload.id( "xenon:opt_out" );
    public static final PacketCodec<PacketByteBuf, OptOutPacketPayload> CODEC = PacketCodec.tuple( PacketCodecs.STRING, OptOutPacketPayload::featureName, OptOutPacketPayload::new );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

}
