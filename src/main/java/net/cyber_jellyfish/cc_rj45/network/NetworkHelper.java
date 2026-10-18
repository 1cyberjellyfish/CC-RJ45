package net.cyber_jellyfish.cc_rj45.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class NetworkHelper {
    public static void sendToServer(CustomPacketPayload payload) {
        //? if neoforge {
        /*//? if <=1.21.1 {
        /^net.neoforged.neoforge.network.PacketDistributor.sendToServer(payload);
        ^///?} else {
        net.neoforged.neoforge.client.network.ClientPacketDistributor.sendToServer(payload);
        //?}
        *///?} else {
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(payload);
        //?}
    }

    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        //? if neoforge {
        /*net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player, payload);
        *///?} else {
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, payload);
        //?}
    }

    public static void sendToPlayersInDimension(net.minecraft.server.level.ServerLevel level, CustomPacketPayload payload) {
        //? if neoforge {
        /*net.neoforged.neoforge.network.PacketDistributor.sendToPlayersInDimension(level, payload);
        *///?} else {
        for (ServerPlayer player : level.players()) {
            net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, payload);
        }
        //?}
    }
}
