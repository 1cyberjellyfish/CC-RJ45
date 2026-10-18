package net.cyber_jellyfish.cc_rj45.util;

import net.minecraft.nbt.CompoundTag;

public class TagHelper {
    public static CompoundTag getCompound(CompoundTag tag, String key) {
        //? if <=1.21.1 {
        /*return tag.getCompound(key);
        *///?} else {
        return tag.getCompoundOrEmpty(key);
        //?}
    }

    public static String getString(CompoundTag tag, String key) {
        //? if <=1.21.1 {
        /*return tag.getString(key);
        *///?} else {
        return tag.getStringOr(key, "");
        //?}
    }

    public static long getLong(CompoundTag tag, String key) {
        //? if <=1.21.1 {
        /*return tag.getLong(key);
        *///?} else {
        return tag.getLongOr(key, 0L);
        //?}
    }

    public static boolean getBoolean(CompoundTag tag, String key) {
        //? if <=1.21.1 {
        /*return tag.getBoolean(key);
        *///?} else {
        return tag.getBooleanOr(key, false);
        //?}
    }

    public static long[] getLongArray(CompoundTag tag, String key) {
        //? if <=1.21.1 {
        /*return tag.getLongArray(key);
        *///?} else {
        return tag.getLongArray(key).orElse(new long[0]);
        //?}
    }
}
