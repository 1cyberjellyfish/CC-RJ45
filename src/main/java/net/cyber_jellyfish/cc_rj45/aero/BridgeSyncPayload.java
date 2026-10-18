package net.cyber_jellyfish.cc_rj45.aero;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//? if <=1.21.1 {
/*import net.minecraft.resources.ResourceLocation;
*///?} else {
import net.minecraft.resources.Identifier;
//?}

public record BridgeSyncPayload(Map<BlockPos, Set<BlockPos>> links) implements CustomPacketPayload {
    //? if <=1.21.1 {
    /*public static final ResourceLocation ID = ResourceLocation.parse("ccrj45:bridge_sync");
    public static final CustomPacketPayload.Type<BridgeSyncPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    *///?} else {
    public static final Identifier ID = Identifier.parse("ccrj45:bridge_sync");
    public static final CustomPacketPayload.Type<BridgeSyncPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    //?}

    public static final StreamCodec<RegistryFriendlyByteBuf, BridgeSyncPayload> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public BridgeSyncPayload decode(RegistryFriendlyByteBuf buf) {
                    int size = buf.readVarInt();
                    Map<BlockPos, Set<BlockPos>> map = new HashMap<>(size);
                    for (int i = 0; i < size; i++) {
                        BlockPos pos = BlockPos.of(buf.readLong());
                        int linkCount = buf.readVarInt();
                        Set<BlockPos> linked = new HashSet<>(linkCount);
                        for (int j = 0; j < linkCount; j++) {
                            linked.add(BlockPos.of(buf.readLong()));
                        }
                        map.put(pos, linked);
                    }
                    return new BridgeSyncPayload(map);
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, BridgeSyncPayload value) {
                    buf.writeVarInt(value.links.size());
                    for (Map.Entry<BlockPos, Set<BlockPos>> entry : value.links.entrySet()) {
                        buf.writeLong(entry.getKey().asLong());
                        buf.writeVarInt(entry.getValue().size());
                        for (BlockPos linked : entry.getValue()) {
                            buf.writeLong(linked.asLong());
                        }
                    }
                }
            };

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
