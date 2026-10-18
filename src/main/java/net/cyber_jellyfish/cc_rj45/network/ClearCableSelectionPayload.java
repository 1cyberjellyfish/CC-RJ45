package net.cyber_jellyfish.cc_rj45.network;

import io.netty.buffer.ByteBuf;
import net.cyber_jellyfish.cc_rj45.CCRJ45;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//? if <=1.21.1 {
/*import net.minecraft.resources.ResourceLocation;
*///?} else {
import net.minecraft.resources.Identifier;
//?}

public record ClearCableSelectionPayload() implements CustomPacketPayload {
    //? if <=1.21.1 {
    /*public static final Type<ClearCableSelectionPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CCRJ45.MODID, "clear_cable_selection"));
    *///?} else {
    public static final Type<ClearCableSelectionPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(CCRJ45.MODID, "clear_cable_selection"));
    //?}

    public static final StreamCodec<ByteBuf, ClearCableSelectionPayload> STREAM_CODEC =
            StreamCodec.unit(new ClearCableSelectionPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
