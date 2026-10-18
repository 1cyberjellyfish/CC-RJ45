package net.cyber_jellyfish.cc_rj45;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

//? if neoforge {
/*import dan200.computercraft.api.media.MediaCapability;
import dan200.computercraft.api.network.wired.WiredElementCapability;
*///?}
import net.cyber_jellyfish.cc_rj45.aero.BridgeLinkWorldSaveData;
import net.cyber_jellyfish.cc_rj45.aero.BridgeSyncPayload;
import net.cyber_jellyfish.cc_rj45.aero.BridgeWiredElement;
import net.cyber_jellyfish.cc_rj45.aero.ClientBridgeState;
import net.cyber_jellyfish.cc_rj45.aero.SableHelper;
import net.cyber_jellyfish.cc_rj45.aero.SimulatedReflect;
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
import net.cyber_jellyfish.cc_rj45.network.NetworkHelper;
import net.cyber_jellyfish.cc_rj45.recipe.ModRecipeSerializers;
import net.cyber_jellyfish.cc_rj45.util.CableLink;
import net.cyber_jellyfish.cc_rj45.util.PlayerDataHelper;
import net.cyber_jellyfish.cc_rj45.util.RegistryEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
//? if <=1.21.1 {
/*import net.minecraft.resources.ResourceLocation;
*///?} else {
import net.minecraft.resources.Identifier;
//?}
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

//? if neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(CCRJ45.MODID)
*///?}
public class CCRJ45 {
    public static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CCRJ45.MODID);
    private static final String EMPTY_HAND_DISCONNECT_TAG = "ccrj45_empty_hand_disconnect";
    private static final String EMPTY_HAND_DIM = "dimension";
    private static final String EMPTY_HAND_POS = "pos";
    private static final String EMPTY_HAND_SUB = "sublevel";

    private static int distanceTickCounter = 0;

    public static final String MODID = "ccrj45";

    //? if <=1.21.1 {
    /*public static final ResourceLocation COMPUTERCRAFT_TAB_ID = ResourceLocation.parse("computercraft:tab");

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static ResourceLocation parseId(String id) {
        return ResourceLocation.parse(id);
    }

    public static ResourceLocation getDimensionId(Level level) {
        return level != null ? level.dimension().location() : ResourceLocation.parse("minecraft:overworld");
    }

    public static BlockBehaviour.Properties blockProps(String name) {
        return BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(0.5f, 3.0f).noOcclusion();
    }

    public static Item.Properties itemProps(String name) {
        return new Item.Properties();
    }

    public static Item.Properties blockItemProps(String name) {
        return itemProps(name);
    }
    *///?} else {
    public static final Identifier COMPUTERCRAFT_TAB_ID = Identifier.parse("computercraft:tab");

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }

    public static Identifier parseId(String id) {
        return Identifier.parse(id);
    }

    public static Identifier getDimensionId(Level level) {
        return level != null ? level.dimension().identifier() : Identifier.parse("minecraft:overworld");
    }

    public static BlockBehaviour.Properties blockProps(String name) {
        return BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, id(name)))
                .mapColor(MapColor.METAL)
                .strength(0.5f, 3.0f)
                .noOcclusion();
    }

    public static Item.Properties itemProps(String name) {
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id(name)));
    }

    public static Item.Properties blockItemProps(String name) {
        return itemProps(name).useBlockDescriptionPrefix();
    }
    //?}

    //? if neoforge {
    /*public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);

    private static final DeferredBlock<NetCableConnectorBlock> NET_CABLE_CONNECTOR_HOLDER = BLOCKS.registerBlock(
            "net_cable_connector",
            registryName -> SableHelper.createConnectorBlock(
                    blockProps("net_cable_connector")
            )
    );
    public static final RegistryEntry<NetCableConnectorBlock> NET_CABLE_CONNECTOR = NET_CABLE_CONNECTOR_HOLDER::get;

    private static final DeferredItem<BlockItem> NET_CABLE_CONNECTOR_ITEM_HOLDER = ITEMS.register(
            "net_cable_connector",
            () -> new NetCableConnectorBlockItem(NET_CABLE_CONNECTOR.get(), blockItemProps("net_cable_connector"))
    );
    public static final RegistryEntry<BlockItem> NET_CABLE_CONNECTOR_ITEM = NET_CABLE_CONNECTOR_ITEM_HOLDER::get;

    private static final DeferredItem<NetCableItem> NET_CONNECTOR_CABLE_HOLDER = ITEMS.register("net_connector_cable", () -> new NetCableItem(itemProps("net_connector_cable"), null));
    public static final RegistryEntry<NetCableItem> NET_CONNECTOR_CABLE = NET_CONNECTOR_CABLE_HOLDER::get;

    public static final Map<DyeColor, RegistryEntry<NetCableItem>> COLORED_NET_CABLES = new java.util.EnumMap<>(DyeColor.class);

    static {
        for (DyeColor dye : DyeColor.values()) {
            DeferredItem<NetCableItem> item = ITEMS.register(dye.getName() + "_net_connector_cable",
                    () -> new NetCableItem(itemProps(dye.getName() + "_net_connector_cable"), dye));
            COLORED_NET_CABLES.put(dye, item::get);
        }
    }

    //? if <=1.21.1 {
    /^private static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NetCableConnectorBlockEntity>> NET_CABLE_CONNECTOR_BE_HOLDER =
            BLOCK_ENTITY_TYPES.register("net_cable_connector",
                    () -> BlockEntityType.Builder.of(NetCableConnectorBlockEntity::new, NET_CABLE_CONNECTOR.get()).build(null));
    ^///?} else {
    private static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NetCableConnectorBlockEntity>> NET_CABLE_CONNECTOR_BE_HOLDER =
            BLOCK_ENTITY_TYPES.register("net_cable_connector",
                    () -> new BlockEntityType<>(NetCableConnectorBlockEntity::new, java.util.Set.of(NET_CABLE_CONNECTOR.get())));
    //?}
    public static final RegistryEntry<BlockEntityType<NetCableConnectorBlockEntity>> NET_CABLE_CONNECTOR_BE = NET_CABLE_CONNECTOR_BE_HOLDER::get;

    private static final DeferredBlock<NetCableHubBlock> NET_CABLE_HUB_HOLDER = BLOCKS.registerBlock(
            "net_cable_hub",
            registryName -> SableHelper.createHubBlock(
                    blockProps("net_cable_hub")
            )
    );
    public static final RegistryEntry<NetCableHubBlock> NET_CABLE_HUB = NET_CABLE_HUB_HOLDER::get;

    private static final DeferredItem<BlockItem> NET_CABLE_HUB_ITEM_HOLDER = ITEMS.register(
            "net_cable_hub",
            () -> new NetCableConnectorBlockItem(NET_CABLE_HUB.get(), blockItemProps("net_cable_hub"))
    );
    public static final RegistryEntry<BlockItem> NET_CABLE_HUB_ITEM = NET_CABLE_HUB_ITEM_HOLDER::get;

    //? if <=1.21.1 {
    /^private static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NetCableHubBlockEntity>> NET_CABLE_HUB_BE_HOLDER =
            BLOCK_ENTITY_TYPES.register("net_cable_hub",
                    () -> BlockEntityType.Builder.of(NetCableHubBlockEntity::new, NET_CABLE_HUB.get()).build(null));
    ^///?} else {
    private static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NetCableHubBlockEntity>> NET_CABLE_HUB_BE_HOLDER =
            BLOCK_ENTITY_TYPES.register("net_cable_hub",
                    () -> new BlockEntityType<>(NetCableHubBlockEntity::new, java.util.Set.of(NET_CABLE_HUB.get())));
    //?}
    public static final RegistryEntry<BlockEntityType<NetCableHubBlockEntity>> NET_CABLE_HUB_BE = NET_CABLE_HUB_BE_HOLDER::get;

    private static final DeferredBlock<NetCableConnectorHugeBlock> NET_CABLE_CONNECTOR_HUGE_HOLDER = BLOCKS.registerBlock(
            "net_cable_connector_huge",
            registryName -> SableHelper.createHugeConnectorBlock(
                    blockProps("net_cable_connector_huge")
            )
    );
    public static final RegistryEntry<NetCableConnectorHugeBlock> NET_CABLE_CONNECTOR_HUGE = NET_CABLE_CONNECTOR_HUGE_HOLDER::get;

    private static final DeferredItem<BlockItem> NET_CABLE_CONNECTOR_HUGE_ITEM_HOLDER = ITEMS.register(
            "net_cable_connector_huge",
            () -> new NetCableConnectorBlockItem(NET_CABLE_CONNECTOR_HUGE.get(), blockItemProps("net_cable_connector_huge"))
    );
    public static final RegistryEntry<BlockItem> NET_CABLE_CONNECTOR_HUGE_ITEM = NET_CABLE_CONNECTOR_HUGE_ITEM_HOLDER::get;

    //? if <=1.21.1 {
    /^private static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NetCableConnectorHugeBlockEntity>> NET_CABLE_CONNECTOR_HUGE_BE_HOLDER =
            BLOCK_ENTITY_TYPES.register("net_cable_connector_huge",
                    () -> BlockEntityType.Builder.of(NetCableConnectorHugeBlockEntity::new, NET_CABLE_CONNECTOR_HUGE.get()).build(null));
    ^///?} else {
    private static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NetCableConnectorHugeBlockEntity>> NET_CABLE_CONNECTOR_HUGE_BE_HOLDER =
            BLOCK_ENTITY_TYPES.register("net_cable_connector_huge",
                    () -> new BlockEntityType<>(NetCableConnectorHugeBlockEntity::new, java.util.Set.of(NET_CABLE_CONNECTOR_HUGE.get())));
    //?}
    public static final RegistryEntry<BlockEntityType<NetCableConnectorHugeBlockEntity>> NET_CABLE_CONNECTOR_HUGE_BE = NET_CABLE_CONNECTOR_HUGE_BE_HOLDER::get;

    private static final DeferredItem<PortableDriveItem> PORTABLE_DRIVE_HOLDER = ITEMS.register(
            "portable_drive",
            () -> new PortableDriveItem(itemProps("portable_drive").stacksTo(1))
    );
    public static final RegistryEntry<PortableDriveItem> PORTABLE_DRIVE = PORTABLE_DRIVE_HOLDER::get;
    *///?} else {
    
    public static NetCableConnectorBlock RAW_NET_CABLE_CONNECTOR;
    public static BlockItem RAW_NET_CABLE_CONNECTOR_ITEM;
    public static NetCableItem RAW_NET_CONNECTOR_CABLE;
    public static final Map<DyeColor, NetCableItem> RAW_COLORED_NET_CABLES = new java.util.EnumMap<>(DyeColor.class);
    public static BlockEntityType<NetCableConnectorBlockEntity> RAW_NET_CABLE_CONNECTOR_BE;
    public static NetCableHubBlock RAW_NET_CABLE_HUB;
    public static BlockItem RAW_NET_CABLE_HUB_ITEM;
    public static BlockEntityType<NetCableHubBlockEntity> RAW_NET_CABLE_HUB_BE;
    public static NetCableConnectorHugeBlock RAW_NET_CABLE_CONNECTOR_HUGE;
    public static BlockItem RAW_NET_CABLE_CONNECTOR_HUGE_ITEM;
    public static BlockEntityType<NetCableConnectorHugeBlockEntity> RAW_NET_CABLE_CONNECTOR_HUGE_BE;
    public static PortableDriveItem RAW_PORTABLE_DRIVE;

    public static final RegistryEntry<NetCableConnectorBlock> NET_CABLE_CONNECTOR = () -> RAW_NET_CABLE_CONNECTOR;
    public static final RegistryEntry<BlockItem> NET_CABLE_CONNECTOR_ITEM = () -> RAW_NET_CABLE_CONNECTOR_ITEM;
    public static final RegistryEntry<NetCableItem> NET_CONNECTOR_CABLE = () -> RAW_NET_CONNECTOR_CABLE;
    public static final Map<DyeColor, RegistryEntry<NetCableItem>> COLORED_NET_CABLES = new java.util.EnumMap<>(DyeColor.class);
    public static final RegistryEntry<BlockEntityType<NetCableConnectorBlockEntity>> NET_CABLE_CONNECTOR_BE = () -> RAW_NET_CABLE_CONNECTOR_BE;
    public static final RegistryEntry<NetCableHubBlock> NET_CABLE_HUB = () -> RAW_NET_CABLE_HUB;
    public static final RegistryEntry<BlockItem> NET_CABLE_HUB_ITEM = () -> RAW_NET_CABLE_HUB_ITEM;
    public static final RegistryEntry<BlockEntityType<NetCableHubBlockEntity>> NET_CABLE_HUB_BE = () -> RAW_NET_CABLE_HUB_BE;
    public static final RegistryEntry<NetCableConnectorHugeBlock> NET_CABLE_CONNECTOR_HUGE = () -> RAW_NET_CABLE_CONNECTOR_HUGE;
    public static final RegistryEntry<BlockItem> NET_CABLE_CONNECTOR_HUGE_ITEM = () -> RAW_NET_CABLE_CONNECTOR_HUGE_ITEM;
    public static final RegistryEntry<BlockEntityType<NetCableConnectorHugeBlockEntity>> NET_CABLE_CONNECTOR_HUGE_BE = () -> RAW_NET_CABLE_CONNECTOR_HUGE_BE;
    public static final RegistryEntry<PortableDriveItem> PORTABLE_DRIVE = () -> RAW_PORTABLE_DRIVE;
    //?}

    //? if neoforge {
    /*public CCRJ45(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::onBuildCreativeTab);
        modEventBus.addListener(this::registerPayloads);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        ModDataComponents.DATA_COMPONENTS.register(modEventBus);
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void onBuildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        //? if <=1.21.1 {
        /^if (!event.getTabKey().location().equals(COMPUTERCRAFT_TAB_ID)) return;
        ^///?} else {
        if (!event.getTabKey().identifier().equals(COMPUTERCRAFT_TAB_ID)) return;
        //?}

        event.accept(NET_CABLE_CONNECTOR_ITEM.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        event.accept(NET_CABLE_HUB_ITEM.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        event.accept(NET_CABLE_CONNECTOR_HUGE_ITEM.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        event.accept(NET_CONNECTOR_CABLE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        for (DyeColor dye : DyeColor.values()) {
            RegistryEntry<NetCableItem> cableItem = COLORED_NET_CABLES.get(dye);
            if (cableItem != null) {
                event.accept(cableItem.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
        var defaultDrive = new ItemStack(PORTABLE_DRIVE.get());
        PortableDriveItem.setDriveColor(defaultDrive, 0xA0A0A0);
        event.accept(defaultDrive, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        for (DyeColor dye : DyeColor.values()) {
            var driveStack = new ItemStack(PORTABLE_DRIVE.get());
            PortableDriveItem.setDriveColor(driveStack, PortableDriveItem.colorFromDye(dye));
            event.accept(driveStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(MODID);
        registrar.playToClient(
                BridgeSyncPayload.TYPE, BridgeSyncPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> ClientBridgeState.update(payload.links()));
                }
        );
        registrar.playToServer(
                net.cyber_jellyfish.cc_rj45.network.ClearCableSelectionPayload.TYPE,
                net.cyber_jellyfish.cc_rj45.network.ClearCableSelectionPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        PlayerDataHelper.getCustomData(context.player()).remove(net.cyber_jellyfish.cc_rj45.item.NetCableItem.TAG_ROOT);
                        PlayerDataHelper.getCustomData(context.player()).remove(EMPTY_HAND_DISCONNECT_TAG);
                    });
                }
        );
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                WiredElementCapability.get(),
                NET_CABLE_CONNECTOR_BE.get(),
                (blockEntity, side) -> blockEntity.isPhysicalSideAllowed(side) ? blockEntity : null
        );
        event.registerBlockEntity(
                WiredElementCapability.get(),
                NET_CABLE_HUB_BE.get(),
                (blockEntity, side) -> blockEntity.isPhysicalSideAllowed(side) ? blockEntity : null
        );
        event.registerBlockEntity(
                WiredElementCapability.get(),
                NET_CABLE_CONNECTOR_HUGE_BE.get(),
                (blockEntity, side) -> blockEntity.isPhysicalSideAllowed(side) ? blockEntity : null
        );

        if (SimulatedReflect.isAvailable()) {
            SimulatedReflect.getRopeConnectorType().ifPresent(type -> {
                event.registerBlockEntity(
                        WiredElementCapability.get(), type,
                        (be, side) -> BridgeWiredElement.getOrCreate(be)
                );
                LOGGER.info("Registered WiredElement capability on rope connector blocks");
            });
            SimulatedReflect.getRopeWinchType().ifPresent(type -> {
                event.registerBlockEntity(
                        WiredElementCapability.get(), type,
                        (be, side) -> BridgeWiredElement.getOrCreate(be)
                );
                LOGGER.info("Registered WiredElement capability on rope winch blocks");
            });
        }

        event.registerItem(
                MediaCapability.get(),
                (stack, context) -> new PortableDriveMedia(),
                PORTABLE_DRIVE.get()
        );
    }

    //? if <=1.21.1 {
    /^@SubscribeEvent
    public void onBlockBreakEvent(BlockEvent.BreakEvent event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            onBlockBreak(serverLevel, event.getPos(), event.getPlayer());
        }
    }
    ^///?} else {
    @SubscribeEvent
    public void onBlockBreakEvent(net.neoforged.neoforge.event.level.block.BreakBlockEvent event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            onBlockBreak(serverLevel, event.getPos(), event.getPlayer());
        }
    }
    //?}

    @SubscribeEvent
    public void onChunkUnloadEvent(ChunkEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            onChunkUnload(serverLevel);
        }
    }

    @SubscribeEvent
    public void onLevelTickEvent(ServerTickEvent.Pre event) {
        onServerTick();
    }

    @SubscribeEvent
    public void onRightClickBlockEvent(PlayerInteractEvent.RightClickBlock event) {
        InteractionResult res = handleRightClickBlock(event.getEntity(), event.getLevel(), event.getHand(), event.getPos());
        if (res != InteractionResult.PASS) {
            event.setCancellationResult(res);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onServerStoppedEvent(net.neoforged.neoforge.event.server.ServerStoppedEvent event) {
        onServerStop();
    }
    *///?}

    public static void broadcastBridgeLinks(ServerLevel level) {
        if (!SimulatedReflect.isAvailable()) return;
        BridgeLinkWorldSaveData bridgeData = BridgeLinkWorldSaveData.get(level);
        Map<BlockPos, Set<BlockPos>> allLinks = bridgeData.getAll();
        var payload = new BridgeSyncPayload(allLinks);
        NetworkHelper.sendToPlayersInDimension(level, payload);
    }

    public static boolean isChunkLoadedNow(Level level, BlockPos pos) {
        if (level == null || pos == null) return false;
        if (level.isClientSide()) {
            return level.hasChunkAt(pos);
        }
        if (level instanceof ServerLevel sl) {
            int cx = net.minecraft.core.SectionPos.blockToSectionCoord(pos.getX());
            int cz = net.minecraft.core.SectionPos.blockToSectionCoord(pos.getZ());
            return sl.getChunkSource().getChunkNow(cx, cz) != null;
        }
        return false;
    }

    public static void connectBridgeNodes(ServerLevel level, BlockPos a, BlockPos b) {
        if (!isChunkLoadedNow(level, a) || !isChunkLoadedNow(level, b)) return;
        BlockEntity beA = level.getBlockEntity(a);
        BlockEntity beB = level.getBlockEntity(b);
        if (beA == null || beB == null) return;

        BridgeWiredElement elemA = BridgeWiredElement.getOrCreate(beA);
        BridgeWiredElement elemB = BridgeWiredElement.getOrCreate(beB);
        if (elemA == null || elemB == null) return;

        elemA.getNode().connectTo(elemB.getNode());
    }

    public static void disconnectBridgeNodes(ServerLevel level, BlockPos a, BlockPos b) {
        if (!isChunkLoadedNow(level, a) || !isChunkLoadedNow(level, b)) return;
        BlockEntity beA = level.getBlockEntity(a);
        BlockEntity beB = level.getBlockEntity(b);
        if (beA == null || beB == null) return;

        BridgeWiredElement elemA = BridgeWiredElement.getOrCreate(beA);
        BridgeWiredElement elemB = BridgeWiredElement.getOrCreate(beB);
        if (elemA == null || elemB == null) return;

        elemA.getNode().disconnectFrom(elemB.getNode());
    }

    public static void onBlockBreak(ServerLevel serverLevel, BlockPos pos, Player player) {
        if (SimulatedReflect.isAvailable() && SimulatedReflect.isRopeAnchor(
                serverLevel.getBlockEntity(pos))) {
            BridgeLinkWorldSaveData bridgeData = BridgeLinkWorldSaveData.get(serverLevel);
            if (bridgeData.hasAnyLink(pos)) {
                for (BlockPos linked : bridgeData.getLinksFor(pos)) {
                    disconnectBridgeNodes(serverLevel, pos, linked);
                    bridgeData.removeLink(pos, linked);
                }
                if (!player.getAbilities().instabuild) {
                    player.getInventory().add(new ItemStack(NET_CONNECTOR_CABLE.get()));
                }
                broadcastBridgeLinks(serverLevel);
            }
            BridgeWiredElement.invalidate(serverLevel, pos);
        }
    }

    public static void onChunkUnload(ServerLevel level) {
        BridgeWiredElement.invalidateLevel(level);
    }

    public static void onServerStop() {
        NetCableConnectorBlockEntity.ALL_CONNECTORS.clear();
        NetCableConnectorBlockEntity.PENDING_REFRESH.clear();
        distanceTickCounter = 0;
    }

    public static void onServerTick() {
        NetCableConnectorBlockEntity.processPendingRefreshes();
        if (++distanceTickCounter % 20 != 0) return;
        for (NetCableConnectorBlockEntity connector : NetCableConnectorBlockEntity.ALL_CONNECTORS) {
            if (connector.isRemoved() || connector.getLevel() == null || connector.getLevel().isClientSide()) continue;
            connector.checkLinkDistances();
        }
    }

    public static InteractionResult handleRightClickBlock(Player player, Level level, InteractionHand hand, BlockPos pos) {
        if (hand != InteractionHand.MAIN_HAND || player == null) return InteractionResult.PASS;

        ItemStack held = player.getMainHandItem();

        if (held.is(Items.SHEARS) && SimulatedReflect.isAvailable()) {
            if (!level.isClientSide() && level.getBlockEntity(pos) != null && SimulatedReflect.isRopeAnchor(
                    level.getBlockEntity(pos))) {
                BridgeLinkWorldSaveData bridgeData = BridgeLinkWorldSaveData.get((ServerLevel) level);
                if (bridgeData.hasAnyLink(pos)) {
                    for (BlockPos linked : bridgeData.getLinksFor(pos)) {
                        disconnectBridgeNodes((ServerLevel) level, pos, linked);
                        Object ba = SimulatedReflect.getRopeHolder(level, pos);
                        Object bb = SimulatedReflect.getRopeHolder(level, linked);
                        ServerPlayer sp = player instanceof ServerPlayer s ? s : null;
                        if (ba != null) SimulatedReflect.destroyRope(ba, sp, null);
                        if (bb != null) SimulatedReflect.destroyRope(bb, sp, null);
                        bridgeData.removeLink(pos, linked);
                    }
                    if (!player.getAbilities().instabuild) {
                        player.getInventory().add(new ItemStack(NET_CONNECTOR_CABLE.get()));
                    }
                    broadcastBridgeLinks((ServerLevel) level);
                    PlayerDataHelper.sendActionMessage(
                            player, Component.translatable("message.ccrj45.cable.shears_disconnected"));
                    return InteractionResult.CONSUME;
                }
            }
        }

        if (!held.isEmpty()) return InteractionResult.PASS;

        var blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof NetCableConnectorBlockEntity connector)) {
            if (!level.isClientSide() && PlayerDataHelper.getCustomData(player).contains(EMPTY_HAND_DISCONNECT_TAG)) {
                PlayerDataHelper.getCustomData(player).remove(EMPTY_HAND_DISCONNECT_TAG);
                PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.cable.selection_cleared"));
            }
            return InteractionResult.PASS;
        }

        if (level.isClientSide()) return InteractionResult.PASS;

        boolean hasFirstSelected = PlayerDataHelper.getCustomData(player).contains(EMPTY_HAND_DISCONNECT_TAG);
        if (!player.isShiftKeyDown() && !hasFirstSelected) return InteractionResult.PASS;

        return handleEmptyHandDisconnect((ServerLevel) level, player, pos, connector);
    }

    private static InteractionResult handleEmptyHandDisconnect(ServerLevel level, Player player, BlockPos pos, NetCableConnectorBlockEntity secondConnector) {
        CompoundTag data = PlayerDataHelper.getCustomData(player);
        CompoundTag state = net.cyber_jellyfish.cc_rj45.util.TagHelper.getCompound(data, EMPTY_HAND_DISCONNECT_TAG);

        if (!state.contains(EMPTY_HAND_POS)) {
            UUID subUUID = SableHelper.getContainingUUID(secondConnector);
            state.putLong(EMPTY_HAND_POS, pos.asLong());
            state.putString(EMPTY_HAND_DIM, CCRJ45.getDimensionId(level).toString());
            if (subUUID != null) {
                state.putString(EMPTY_HAND_SUB, subUUID.toString());
            } else {
                state.remove(EMPTY_HAND_SUB);
            }
            data.put(EMPTY_HAND_DISCONNECT_TAG, state);
            PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.disconnect.select_second"));
            return InteractionResult.SUCCESS;
        }

        String firstDimStr = net.cyber_jellyfish.cc_rj45.util.TagHelper.getString(state, EMPTY_HAND_DIM);
        var firstPos = BlockPos.of(net.cyber_jellyfish.cc_rj45.util.TagHelper.getLong(state, EMPTY_HAND_POS));
        UUID firstSub = state.contains(EMPTY_HAND_SUB) ? UUID.fromString(net.cyber_jellyfish.cc_rj45.util.TagHelper.getString(state, EMPTY_HAND_SUB)) : null;
        data.remove(EMPTY_HAND_DISCONNECT_TAG);

        if (!firstDimStr.equals(CCRJ45.getDimensionId(level).toString())) {
            PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.disconnect.same_dimension"));
            return InteractionResult.SUCCESS;
        }

        if (firstPos.equals(pos)) {
            PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.disconnect.pick_different"));
            return InteractionResult.SUCCESS;
        }

        //? if <=1.21.1 {
        /*ResourceLocation firstDim = CCRJ45.parseId(firstDimStr);
        *///?} else {
        Identifier firstDim = CCRJ45.parseId(firstDimStr);
        //?}
        var firstEntity = SableHelper.findBlockEntity(level, firstDim, firstSub, firstPos);
        if (!(firstEntity instanceof NetCableConnectorBlockEntity firstConnector)) {
            PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.disconnect.first_missing"));
            return InteractionResult.SUCCESS;
        }

        UUID secondSub = SableHelper.getContainingUUID(secondConnector);
        //? if <=1.21.1 {
        /*ResourceLocation secondDim = CCRJ45.getDimensionId(level);
        *///?} else {
        Identifier secondDim = CCRJ45.getDimensionId(level);
        //?}

        CableLink firstToSecond = new CableLink(secondDim, secondSub, pos);
        CableLink firstToSecondNoSub = new CableLink(secondDim, null, pos);
        CableLink secondToFirst = new CableLink(firstDim, firstSub, firstPos);
        CableLink secondToFirstNoSub = new CableLink(firstDim, null, firstPos);

        CableLink actualFirstToSecond = firstConnector.isLinkedTo(firstToSecond) ? firstToSecond : firstToSecondNoSub;
        CableLink actualSecondToFirst = secondConnector.isLinkedTo(secondToFirst) ? secondToFirst : secondToFirstNoSub;

        if (firstConnector.isLinkedTo(actualFirstToSecond)) {
            DyeColor droppedColor = null;
            for (CableLink link : firstConnector.getLinks()) {
                if (link.equals(actualFirstToSecond)) {
                    droppedColor = link.color();
                    break;
                }
            }
            firstConnector.unlinkTo(actualFirstToSecond);
            secondConnector.unlinkTo(actualSecondToFirst);
            NetCableConnectorBlockEntity.refreshConnectedNetwork(firstConnector.getLevel(), firstPos);
            NetCableConnectorBlockEntity.refreshConnectedNetwork(secondConnector.getLevel(), pos);
            PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.disconnect.success"));
            if (!player.getAbilities().instabuild) {
                ItemStack refund = droppedColor != null && CCRJ45.COLORED_NET_CABLES.containsKey(droppedColor)
                        ? new ItemStack(CCRJ45.COLORED_NET_CABLES.get(droppedColor).get())
                        : new ItemStack(CCRJ45.NET_CONNECTOR_CABLE.get());
                if (!player.getInventory().add(refund)) {
                    net.minecraft.world.level.block.Block.popResource(level, pos, refund);
                }
            }
        } else {
            PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.disconnect.not_connected"));
        }

        return InteractionResult.SUCCESS;
    }
}
