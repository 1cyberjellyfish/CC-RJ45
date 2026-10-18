package net.cyber_jellyfish.cc_rj45.util;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PlayerDataHelper {
    //? if neoforge {
    /*public static CompoundTag getCustomData(Player player) {
        return player.getPersistentData();
    }
    *///?} else {
    private static final Map<UUID, CompoundTag> PLAYER_DATA = new ConcurrentHashMap<>();

    public static CompoundTag getCustomData(Player player) {
        return PLAYER_DATA.computeIfAbsent(player.getUUID(), u -> new CompoundTag());
    }
    //?}

    public static void sendActionMessage(Player player, Component message) {
        if (player == null) return;
        //? if <=1.21.1 {
        /*player.displayClientMessage(message, true);
        *///?} else {
        player.sendOverlayMessage(message);
        //?}
    }

    public static void addCooldown(Player player, Item item, ItemStack stack, int ticks) {
        if (player == null) return;
        //? if <=1.21.1 {
        /*player.getCooldowns().addCooldown(item, ticks);
        *///?} else {
        player.getCooldowns().addCooldown(stack, ticks);
        //?}
    }
}
