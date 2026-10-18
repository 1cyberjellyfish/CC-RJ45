package net.cyber_jellyfish.cc_rj45.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record DiskId(int id) {
    public static final Codec<DiskId> CODEC = Codec.INT.xmap(DiskId::new, DiskId::id);
    public static final StreamCodec<ByteBuf, DiskId> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(DiskId::new, DiskId::id);
}
