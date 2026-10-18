package net.cyber_jellyfish.cc_rj45.util;

import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
//? if <=1.21.1 {
/*import net.minecraft.resources.ResourceLocation;
*///?} else {
import net.minecraft.resources.Identifier;
//?}
import net.minecraft.world.item.DyeColor;

//? if <=1.21.1 {
/*public record CableLink(ResourceLocation dimension, @Nullable UUID subLevel, BlockPos pos, @Nullable DyeColor color) {
    private static final String TAG_DIM = "dim";
    private static final String TAG_SUB = "sub";
    private static final String TAG_POS = "pos";
    private static final String TAG_COLOR = "color";

    public CableLink(ResourceLocation dimension, @Nullable UUID subLevel, BlockPos pos) {
        this(dimension, subLevel, pos, null);
    }

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putString(TAG_DIM, dimension.toString());
        if (subLevel != null) tag.putUUID(TAG_SUB, subLevel);
        tag.putIntArray(TAG_POS, new int[]{pos.getX(), pos.getY(), pos.getZ()});
        if (color != null) {
            tag.putString(TAG_COLOR, color.getName());
        }
        return tag;
    }

    public static CableLink fromNbt(CompoundTag tag) {
        ResourceLocation dim = ResourceLocation.parse(tag.getString(TAG_DIM));
        UUID sub = tag.hasUUID(TAG_SUB) ? tag.getUUID(TAG_SUB) : null;
        int[] p = tag.getIntArray(TAG_POS);
        DyeColor color = tag.contains(TAG_COLOR) ? DyeColor.byName(tag.getString(TAG_COLOR), null) : null;
        return new CableLink(dim, sub, new BlockPos(p[0], p[1], p[2]), color);
    }
*///?} else {
public record CableLink(Identifier dimension, @Nullable UUID subLevel, BlockPos pos, @Nullable DyeColor color) {
    private static final String TAG_DIM = "dim";
    private static final String TAG_SUB = "sub";
    private static final String TAG_POS = "pos";
    private static final String TAG_COLOR = "color";

    public CableLink(Identifier dimension, @Nullable UUID subLevel, BlockPos pos) {
        this(dimension, subLevel, pos, null);
    }

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putString(TAG_DIM, dimension.toString());
        if (subLevel != null) tag.putString(TAG_SUB, subLevel.toString());
        tag.putIntArray(TAG_POS, new int[]{pos.getX(), pos.getY(), pos.getZ()});
        if (color != null) {
            tag.putString(TAG_COLOR, color.getName());
        }
        return tag;
    }

    public static CableLink fromNbt(CompoundTag tag) {
        Identifier dim = Identifier.parse(tag.getStringOr(TAG_DIM, "minecraft:overworld"));
        UUID sub = tag.getString(TAG_SUB).map(UUID::fromString).orElse(null);
        int[] p = tag.getIntArray(TAG_POS).orElse(new int[]{0, 0, 0});
        DyeColor color = tag.getString(TAG_COLOR).map(c -> DyeColor.byName(c, null)).orElse(null);
        return new CableLink(dim, sub, new BlockPos(p[0], p[1], p[2]), color);
    }
//?}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CableLink other)) return false;
        return Objects.equals(dimension, other.dimension)
                && Objects.equals(subLevel, other.subLevel)
                && Objects.equals(pos, other.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dimension, subLevel, pos);
    }

    @Override
    public String toString() {
        return "CableLink[" + dimension + ", " + subLevel + ", " + pos + ", " + color + "]";
    }
}
