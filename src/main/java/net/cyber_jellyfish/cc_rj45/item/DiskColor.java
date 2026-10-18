package net.cyber_jellyfish.cc_rj45.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record DiskColor(int color) {
    public static final Codec<DiskColor> CODEC = Codec.INT.xmap(DiskColor::new, DiskColor::color);
    public static final StreamCodec<ByteBuf, DiskColor> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(DiskColor::new, DiskColor::color);
}
