//? if fabric {
package net.cyber_jellyfish.cc_rj45.fabric;

import dan200.computercraft.api.media.MediaLookup;
import dan200.computercraft.api.network.wired.WiredElementLookup;
import net.cyber_jellyfish.cc_rj45.CCRJ45;
import net.cyber_jellyfish.cc_rj45.ModDataComponents;
import net.cyber_jellyfish.cc_rj45.aero.BridgeSyncPayload;
import net.cyber_jellyfish.cc_rj45.aero.SableHelper;
import net.cyber_jellyfish.cc_rj45.block.NetCableConnectorBlock;
import net.cyber_jellyfish.cc_rj45.block.NetCableConnectorHugeBlock;
import net.cyber_jellyfish.cc_rj45.block.NetCableHubBlock;
import net.cyber_jellyfish.cc_rj45.block.entity.NetCableConnectorBlockEntity;
import net.cyber_jellyfish.cc_rj45.block.entity.NetCableConnectorHugeBlockEntity;
import net.cyber_jellyfish.cc_rj45.block.entity.NetCableHubBlockEntity;
import net.cyber_jellyfish.cc_rj45.item.DiskColor;
import net.cyber_jellyfish.cc_rj45.item.NetCableConnectorBlockItem;
import net.cyber_jellyfish.cc_rj45.item.NetCableItem;
import net.cyber_jellyfish.cc_rj45.item.PortableDriveItem;
import net.cyber_jellyfish.cc_rj45.item.PortableDriveMedia;
import net.cyber_jellyfish.cc_rj45.network.ClearCableSelectionPayload;
import net.cyber_jellyfish.cc_rj45.recipe.ModRecipeSerializers;
import net.cyber_jellyfish.cc_rj45.util.PlayerDataHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
//? if <=1.21.1 {
/*import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
*///?} else {
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
//?}
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class CCRJ45Fabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CCRJ45.RAW_NET_CABLE_CONNECTOR = Registry.register(
                BuiltInRegistries.BLOCK,
                CCRJ45.id("net_cable_connector"),
                SableHelper.createConnectorBlock(
                        CCRJ45.blockProps("net_cable_connector")
                )
        );
        CCRJ45.RAW_NET_CABLE_CONNECTOR_ITEM = Registry.register(
                BuiltInRegistries.ITEM,
                CCRJ45.id("net_cable_connector"),
                new NetCableConnectorBlockItem(CCRJ45.RAW_NET_CABLE_CONNECTOR, CCRJ45.blockItemProps("net_cable_connector"))
        );

        CCRJ45.RAW_NET_CONNECTOR_CABLE = Registry.register(
                BuiltInRegistries.ITEM,
                CCRJ45.id("net_connector_cable"),
                new NetCableItem(CCRJ45.itemProps("net_connector_cable"))
        );

        for (DyeColor dye : DyeColor.values()) {
            NetCableItem item = Registry.register(
                    BuiltInRegistries.ITEM,
                    CCRJ45.id(dye.getName() + "_net_connector_cable"),
                    new NetCableItem(CCRJ45.itemProps(dye.getName() + "_net_connector_cable"), dye)
            );
            CCRJ45.RAW_COLORED_NET_CABLES.put(dye, item);
            CCRJ45.COLORED_NET_CABLES.put(dye, () -> item);
        }

        //? if <=1.21.1 {
        /*CCRJ45.RAW_NET_CABLE_CONNECTOR_BE = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                CCRJ45.id("net_cable_connector"),
                BlockEntityType.Builder.of(NetCableConnectorBlockEntity::new, CCRJ45.RAW_NET_CABLE_CONNECTOR).build(null)
        );
        *///?} else {
        CCRJ45.RAW_NET_CABLE_CONNECTOR_BE = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                CCRJ45.id("net_cable_connector"),
                new BlockEntityType<>(NetCableConnectorBlockEntity::new, java.util.Set.of(CCRJ45.RAW_NET_CABLE_CONNECTOR))
        );
        //?}

        CCRJ45.RAW_NET_CABLE_HUB = Registry.register(
                BuiltInRegistries.BLOCK,
                CCRJ45.id("net_cable_hub"),
                SableHelper.createHubBlock(
                        CCRJ45.blockProps("net_cable_hub")
                )
        );
        CCRJ45.RAW_NET_CABLE_HUB_ITEM = Registry.register(
                BuiltInRegistries.ITEM,
                CCRJ45.id("net_cable_hub"),
                new NetCableConnectorBlockItem(CCRJ45.RAW_NET_CABLE_HUB, CCRJ45.blockItemProps("net_cable_hub"))
        );
        //? if <=1.21.1 {
        /*CCRJ45.RAW_NET_CABLE_HUB_BE = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                CCRJ45.id("net_cable_hub"),
                BlockEntityType.Builder.of(NetCableHubBlockEntity::new, CCRJ45.RAW_NET_CABLE_HUB).build(null)
        );
        *///?} else {
        CCRJ45.RAW_NET_CABLE_HUB_BE = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                CCRJ45.id("net_cable_hub"),
                new BlockEntityType<>(NetCableHubBlockEntity::new, java.util.Set.of(CCRJ45.RAW_NET_CABLE_HUB))
        );
        //?}

        CCRJ45.RAW_NET_CABLE_CONNECTOR_HUGE = Registry.register(
                BuiltInRegistries.BLOCK,
                CCRJ45.id("net_cable_connector_huge"),
                SableHelper.createHugeConnectorBlock(
                        CCRJ45.blockProps("net_cable_connector_huge")
                )
        );
        CCRJ45.RAW_NET_CABLE_CONNECTOR_HUGE_ITEM = Registry.register(
                BuiltInRegistries.ITEM,
                CCRJ45.id("net_cable_connector_huge"),
                new NetCableConnectorBlockItem(CCRJ45.RAW_NET_CABLE_CONNECTOR_HUGE, CCRJ45.blockItemProps("net_cable_connector_huge"))
        );
        //? if <=1.21.1 {
        /*CCRJ45.RAW_NET_CABLE_CONNECTOR_HUGE_BE = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                CCRJ45.id("net_cable_connector_huge"),
                BlockEntityType.Builder.of(NetCableConnectorHugeBlockEntity::new, CCRJ45.RAW_NET_CABLE_CONNECTOR_HUGE).build(null)
        );
        *///?} else {
        CCRJ45.RAW_NET_CABLE_CONNECTOR_HUGE_BE = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                CCRJ45.id("net_cable_connector_huge"),
                new BlockEntityType<>(NetCableConnectorHugeBlockEntity::new, java.util.Set.of(CCRJ45.RAW_NET_CABLE_CONNECTOR_HUGE))
        );
        //?}

        CCRJ45.RAW_PORTABLE_DRIVE = Registry.register(
                BuiltInRegistries.ITEM,
                CCRJ45.id("portable_drive"),
                new PortableDriveItem(CCRJ45.itemProps("portable_drive").stacksTo(1))
        );

        ModDataComponents.init();
        ModRecipeSerializers.init();

        WiredElementLookup.get().registerForBlockEntities(
                (be, side) -> (be instanceof NetCableConnectorBlockEntity conn && conn.isPhysicalSideAllowed(side)) ? conn : null,
                CCRJ45.RAW_NET_CABLE_CONNECTOR_BE,
                CCRJ45.RAW_NET_CABLE_HUB_BE,
                CCRJ45.RAW_NET_CABLE_CONNECTOR_HUGE_BE
        );

        MediaLookup.get().registerForItems(
                (stack, context) -> new PortableDriveMedia(),
                CCRJ45.RAW_PORTABLE_DRIVE
        );

        //? if <=1.21.1 {
        /*PayloadTypeRegistry.playS2C().register(BridgeSyncPayload.TYPE, BridgeSyncPayload.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(ClearCableSelectionPayload.TYPE, ClearCableSelectionPayload.STREAM_CODEC);
        *///?} else {
        PayloadTypeRegistry.clientboundPlay().register(BridgeSyncPayload.TYPE, BridgeSyncPayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ClearCableSelectionPayload.TYPE, ClearCableSelectionPayload.STREAM_CODEC);
        //?}
        ServerPlayNetworking.registerGlobalReceiver(ClearCableSelectionPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                PlayerDataHelper.getCustomData(context.player()).remove(NetCableItem.TAG_ROOT);
                PlayerDataHelper.getCustomData(context.player()).remove("ccrj45_empty_hand_disconnect");
            });
        });

        //? if <=1.21.1 {
        /*ItemGroupEvents.modifyEntriesEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, CCRJ45.COMPUTERCRAFT_TAB_ID)).register(entries -> {
            entries.accept(CCRJ45.NET_CABLE_CONNECTOR_ITEM.get());
            entries.accept(CCRJ45.NET_CABLE_HUB_ITEM.get());
            entries.accept(CCRJ45.NET_CABLE_CONNECTOR_HUGE_ITEM.get());
            entries.accept(CCRJ45.NET_CONNECTOR_CABLE.get());
            for (DyeColor dye : DyeColor.values()) {
                var cableItem = CCRJ45.COLORED_NET_CABLES.get(dye);
                if (cableItem != null) {
                    entries.accept(cableItem.get());
                }
            }
            ItemStack defaultDrive = new ItemStack(CCRJ45.PORTABLE_DRIVE.get());
            PortableDriveItem.setDriveColor(defaultDrive, 0xA0A0A0);
            entries.accept(defaultDrive);
            for (DyeColor dye : DyeColor.values()) {
                ItemStack driveStack = new ItemStack(CCRJ45.PORTABLE_DRIVE.get());
                PortableDriveItem.setDriveColor(driveStack, PortableDriveItem.colorFromDye(dye));
                entries.accept(driveStack);
            }
        });
        *///?} else {
        CreativeModeTabEvents.modifyOutputEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, CCRJ45.COMPUTERCRAFT_TAB_ID)).register(entries -> {
            entries.accept(CCRJ45.NET_CABLE_CONNECTOR_ITEM.get());
            entries.accept(CCRJ45.NET_CABLE_HUB_ITEM.get());
            entries.accept(CCRJ45.NET_CABLE_CONNECTOR_HUGE_ITEM.get());
            entries.accept(CCRJ45.NET_CONNECTOR_CABLE.get());
            for (DyeColor dye : DyeColor.values()) {
                var cableItem = CCRJ45.COLORED_NET_CABLES.get(dye);
                if (cableItem != null) {
                    entries.accept(cableItem.get());
                }
            }
            ItemStack defaultDrive = new ItemStack(CCRJ45.PORTABLE_DRIVE.get());
            PortableDriveItem.setDriveColor(defaultDrive, 0xA0A0A0);
            entries.accept(defaultDrive);
            for (DyeColor dye : DyeColor.values()) {
                ItemStack driveStack = new ItemStack(CCRJ45.PORTABLE_DRIVE.get());
                PortableDriveItem.setDriveColor(driveStack, PortableDriveItem.colorFromDye(dye));
                entries.accept(driveStack);
            }
        });
        //?}

        ServerTickEvents.START_SERVER_TICK.register(server -> CCRJ45.onServerTick());
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            return CCRJ45.handleRightClickBlock(player, world, hand, hitResult.getBlockPos());
        });
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (world instanceof ServerLevel serverLevel) {
                CCRJ45.onBlockBreak(serverLevel, pos, player);
            }
            return true;
        });
        ServerChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> {
            CCRJ45.onChunkUnload(world);
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> CCRJ45.onServerStop());
    }
}
//?}
