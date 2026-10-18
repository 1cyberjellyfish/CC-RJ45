package net.cyber_jellyfish.cc_rj45.item;

import java.util.List;
import java.util.UUID;

import net.cyber_jellyfish.cc_rj45.CCRJ45;
import net.cyber_jellyfish.cc_rj45.aero.BridgeLinkWorldSaveData;
import net.cyber_jellyfish.cc_rj45.aero.SableHelper;
import net.cyber_jellyfish.cc_rj45.aero.SimulatedReflect;
import net.cyber_jellyfish.cc_rj45.block.entity.NetCableConnectorBlockEntity;
import net.cyber_jellyfish.cc_rj45.util.CableLink;
import net.cyber_jellyfish.cc_rj45.util.PlayerDataHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
//? if <=1.21.1 {
/*import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResultHolder;
*///?} else {
import net.minecraft.resources.Identifier;
//?}
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class NetCableItem extends Item {
    public static final String TAG_ROOT = "ccrj45_net_cable";
    public static final String TAG_DIMENSION = "dimension";
    public static final String TAG_POS = "pos";
    public static final String TAG_SUBLEVEL = "sublevel";
    public static final String TAG_DISCONNECT = "disconnect";
    public static final String TAG_IS_BRIDGE = "is_bridge";

    @javax.annotation.Nullable
    private final DyeColor color;

    public NetCableItem(Properties properties) {
        this(properties, null);
    }

    public NetCableItem(Properties properties, @javax.annotation.Nullable DyeColor color) {
        super(properties);
        this.color = color;
    }

    @javax.annotation.Nullable
    public DyeColor getColor() {
        return color;
    }

    //? if <=1.21.1 {
    /*@Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.ccrj45.net_connector_cable.use_link").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.ccrj45.net_connector_cable.use_unlink").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            if (net.cyber_jellyfish.cc_rj45.client.ClientCableSelection.isActive()) {
                net.cyber_jellyfish.cc_rj45.client.ClientCableSelection.reset();
                PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.cable.selection_cleared"));
                return InteractionResultHolder.sidedSuccess(stack, true);
            }
        } else {
            CompoundTag playerData = net.cyber_jellyfish.cc_rj45.util.PlayerDataHelper.getCustomData(player);
            if (playerData.contains(TAG_ROOT)) {
                playerData.remove(TAG_ROOT);
                PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.cable.selection_cleared"));
                return InteractionResultHolder.sidedSuccess(stack, false);
            }
        }
        return InteractionResultHolder.pass(stack);
    }
    *///?} else {
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, display, tooltipComponents, tooltipFlag);
        tooltipComponents.accept(Component.translatable("tooltip.ccrj45.net_connector_cable.use_link").withStyle(ChatFormatting.GRAY));
        tooltipComponents.accept(Component.translatable("tooltip.ccrj45.net_connector_cable.use_unlink").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            if (net.cyber_jellyfish.cc_rj45.client.ClientCableSelection.isActive()) {
                net.cyber_jellyfish.cc_rj45.client.ClientCableSelection.reset();
                PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.cable.selection_cleared"));
                return InteractionResult.SUCCESS;
            }
        } else {
            CompoundTag playerData = net.cyber_jellyfish.cc_rj45.util.PlayerDataHelper.getCustomData(player);
            if (playerData.contains(TAG_ROOT)) {
                playerData.remove(TAG_ROOT);
                PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.cable.selection_cleared"));
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
    //?}

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getHand() != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }

        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockEntity clickedEntity = NetCableConnectorBlockEntity.getConnector(level, clickedPos);
        if (clickedEntity == null) {
            clickedEntity = level.getBlockEntity(clickedPos);
        }

        boolean isRopeAnchor = SimulatedReflect.isRopeAnchor(clickedEntity);

        if (!(clickedEntity instanceof NetCableConnectorBlockEntity) && !isRopeAnchor) {
            if (level.isClientSide()) {
                if (net.cyber_jellyfish.cc_rj45.client.ClientCableSelection.isActive()) {
                    net.cyber_jellyfish.cc_rj45.client.ClientCableSelection.reset(true);
                    PlayerDataHelper.sendActionMessage(context.getPlayer(), Component.translatable("message.ccrj45.cable.selection_cleared"));
                    return InteractionResult.SUCCESS;
                }
            } else if (context.getPlayer() != null) {
                CompoundTag playerData = net.cyber_jellyfish.cc_rj45.util.PlayerDataHelper.getCustomData(context.getPlayer());
                if (playerData.contains(TAG_ROOT)) {
                    playerData.remove(TAG_ROOT);
                    PlayerDataHelper.sendActionMessage(context.getPlayer(), Component.translatable("message.ccrj45.cable.selection_cleared"));
                    return InteractionResult.CONSUME;
                }
            }
            return InteractionResult.PASS;
        }

        if (context.getPlayer() == null) {
            return InteractionResult.FAIL;
        }

        boolean disconnectMode = context.getPlayer().isShiftKeyDown();

        if (disconnectMode && clickedEntity instanceof NetCableConnectorBlockEntity connector) {
            CompoundTag pData = net.cyber_jellyfish.cc_rj45.util.PlayerDataHelper.getCustomData(context.getPlayer());
            if (!pData.contains(TAG_ROOT) && !level.isClientSide() && connector.getConnectionCount() == 0) {
                PlayerDataHelper.sendActionMessage(context.getPlayer(), Component.translatable("message.ccrj45.cable.no_connections"));
                return InteractionResult.FAIL;
            }
        }

        if (level.isClientSide()) {
            if (net.cyber_jellyfish.cc_rj45.client.ClientCableSelection.isActive()) {
                net.cyber_jellyfish.cc_rj45.client.ClientCableSelection.reset(false);
            } else {
                net.cyber_jellyfish.cc_rj45.client.ClientCableSelection.setFirst(clickedPos, clickedEntity, disconnectMode, this.color, isRopeAnchor);
            }
            return InteractionResult.SUCCESS;
        }

        CompoundTag playerData = net.cyber_jellyfish.cc_rj45.util.PlayerDataHelper.getCustomData(context.getPlayer());
        CompoundTag cableData = net.cyber_jellyfish.cc_rj45.util.TagHelper.getCompound(playerData, TAG_ROOT);

        if (!cableData.contains(TAG_POS)) {
            UUID subUUID = SableHelper.getContainingUUID(clickedEntity);
            cableData.putLong(TAG_POS, clickedPos.asLong());
            cableData.putString(TAG_DIMENSION, CCRJ45.getDimensionId(level).toString());
            if (subUUID != null) {
                cableData.putString(TAG_SUBLEVEL, subUUID.toString());
            } else {
                cableData.remove(TAG_SUBLEVEL);
            }
            cableData.putBoolean(TAG_DISCONNECT, disconnectMode);
            cableData.putBoolean(TAG_IS_BRIDGE, isRopeAnchor);
            playerData.put(TAG_ROOT, cableData);

            if (isRopeAnchor) {
                PlayerDataHelper.sendActionMessage(context.getPlayer(),
                        Component.translatable("message.ccrj45.cable.anchor_selected"));
            } else {
                PlayerDataHelper.sendActionMessage(context.getPlayer(),
                        Component.translatable(
                                disconnectMode ? "message.ccrj45.cable.first_selected_disconnect" : "message.ccrj45.cable.first_selected",
                                ((NetCableConnectorBlockEntity) clickedEntity).getConnectionCount(),
                                ((NetCableConnectorBlockEntity) clickedEntity).getMaxConnections()
                        ));
            }
            return InteractionResult.CONSUME;
        }

        String firstDimension = net.cyber_jellyfish.cc_rj45.util.TagHelper.getString(cableData, TAG_DIMENSION);
        BlockPos firstPos = BlockPos.of(net.cyber_jellyfish.cc_rj45.util.TagHelper.getLong(cableData, TAG_POS));
        UUID firstSubLevel = cableData.contains(TAG_SUBLEVEL) ? UUID.fromString(net.cyber_jellyfish.cc_rj45.util.TagHelper.getString(cableData, TAG_SUBLEVEL)) : null;
        boolean storedDisconnectMode = net.cyber_jellyfish.cc_rj45.util.TagHelper.getBoolean(cableData, TAG_DISCONNECT);
        boolean firstIsBridge = net.cyber_jellyfish.cc_rj45.util.TagHelper.getBoolean(cableData, TAG_IS_BRIDGE);
        boolean secondIsBridge = isRopeAnchor;
        playerData.remove(TAG_ROOT);

        if (!firstDimension.equals(CCRJ45.getDimensionId(level).toString())) {
            PlayerDataHelper.sendActionMessage(context.getPlayer(), Component.translatable("message.ccrj45.cable.same_dimension"));
            return InteractionResult.FAIL;
        }

        if (firstPos.equals(clickedPos)) {
            PlayerDataHelper.sendActionMessage(context.getPlayer(), Component.translatable("message.ccrj45.cable.selection_cleared"));
            return InteractionResult.CONSUME;
        }

        if (firstIsBridge && secondIsBridge) {
            return handleBridgeLink(level, context, firstPos, clickedPos, storedDisconnectMode);
        }

        //? if <=1.21.1 {
        /*ResourceLocation firstDim = CCRJ45.parseId(firstDimension);
        *///?} else {
        Identifier firstDim = CCRJ45.parseId(firstDimension);
        //?}
        NetCableConnectorBlockEntity firstConnector = null;
        if (!firstIsBridge) {
            firstConnector = NetCableConnectorBlockEntity.getConnector(
                    (ServerLevel) level, firstDim, firstSubLevel, firstPos);
        }
        if (firstIsBridge || secondIsBridge) {
            PlayerDataHelper.sendActionMessage(context.getPlayer(), Component.translatable("message.ccrj45.cable.mixed_bridge"));
            return InteractionResult.FAIL;
        }

        if (firstConnector == null) {
            PlayerDataHelper.sendActionMessage(context.getPlayer(), Component.translatable("message.ccrj45.cable.first_missing"));
            return InteractionResult.FAIL;
        }

        UUID secondSubLevel = SableHelper.getContainingUUID(clickedEntity);
        //? if <=1.21.1 {
        /*ResourceLocation secondDim = CCRJ45.getDimensionId(level);
        *///?} else {
        Identifier secondDim = CCRJ45.getDimensionId(level);
        //?}

        boolean sameSub = java.util.Objects.equals(firstSubLevel, secondSubLevel);
        CableLink firstLink = new CableLink(firstDim, sameSub ? null : firstSubLevel, firstPos, this.color);
        CableLink secondLink = new CableLink(secondDim, sameSub ? null : secondSubLevel, clickedPos, this.color);

        return handleConnectorLink(context, firstConnector,
                (NetCableConnectorBlockEntity) clickedEntity,
                secondLink, firstLink, storedDisconnectMode);
    }

    private InteractionResult handleBridgeLink(Level level, UseOnContext context, BlockPos firstPos, BlockPos secondPos, boolean disconnectMode) {
        if (!(level instanceof ServerLevel serverLevel)) return InteractionResult.FAIL;
        Player player = context.getPlayer();

        if (disconnectMode) {
            BridgeLinkWorldSaveData bridgeData = BridgeLinkWorldSaveData.get(serverLevel);
            if (!bridgeData.isLinked(firstPos, secondPos)) {
                PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.cable.not_linked"));
                return InteractionResult.FAIL;
            }

            Object ba = SimulatedReflect.getRopeHolder(level, firstPos);
            Object bb = SimulatedReflect.getRopeHolder(level, secondPos);
            ServerPlayer sp = player instanceof ServerPlayer s ? s : null;
            if (ba != null) SimulatedReflect.destroyRope(ba, sp, null);
            if (bb != null) SimulatedReflect.destroyRope(bb, sp, null);
            bridgeData.removeLink(firstPos, secondPos);
            CCRJ45.disconnectBridgeNodes(serverLevel, firstPos, secondPos);
            CCRJ45.broadcastBridgeLinks(serverLevel);

            PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.cable.link_removed"));
            if (!player.getAbilities().instabuild) {
                player.getInventory().add(new ItemStack(CCRJ45.NET_CONNECTOR_CABLE.get()));
            }
            return InteractionResult.CONSUME;
        }

        BridgeLinkWorldSaveData bridgeData = BridgeLinkWorldSaveData.get(serverLevel);
        if (bridgeData.isLinked(firstPos, secondPos)) {
            PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.cable.already_linked"));
            return InteractionResult.CONSUME;
        }

        Object behaviorA = SimulatedReflect.getRopeHolder(level, firstPos);
        Object behaviorB = SimulatedReflect.getRopeHolder(level, secondPos);

        if (behaviorA == null || behaviorB == null) {
            PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.cable.anchor_missing_behavior"));
            return InteractionResult.FAIL;
        }

        if (!SimulatedReflect.createRope(behaviorA, behaviorB)) {
            PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.cable.rope_failed"));
            return InteractionResult.FAIL;
        }

        bridgeData.addLink(firstPos, secondPos);
        CCRJ45.connectBridgeNodes(serverLevel, firstPos, secondPos);
        CCRJ45.broadcastBridgeLinks(serverLevel);

        PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.cable.bridge_created"));

        if (!player.getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.CONSUME;
    }

    private InteractionResult handleConnectorLink(UseOnContext context,
                                                   NetCableConnectorBlockEntity firstConnector,
                                                   NetCableConnectorBlockEntity connector,
                                                   CableLink firstToSecond,
                                                   CableLink secondToFirst,
                                                   boolean disconnectMode) {
        if (disconnectMode) {
            if (!firstConnector.isLinkedTo(firstToSecond)) {
                PlayerDataHelper.sendActionMessage(context.getPlayer(), Component.translatable("message.ccrj45.cable.not_linked"));
                return InteractionResult.FAIL;
            }

            DyeColor droppedColor = null;
            for (CableLink link : firstConnector.getLinks()) {
                if (link.equals(firstToSecond)) {
                    droppedColor = link.color();
                    break;
                }
            }

            firstConnector.unlinkTo(firstToSecond);
            connector.unlinkTo(secondToFirst);
            NetCableConnectorBlockEntity.refreshConnectedNetwork(firstConnector.getLevel(), secondToFirst.pos());
            NetCableConnectorBlockEntity.refreshConnectedNetwork(connector.getLevel(), firstToSecond.pos());
            PlayerDataHelper.sendActionMessage(context.getPlayer(), Component.translatable("message.ccrj45.cable.link_removed"));
            if (!context.getPlayer().getAbilities().instabuild) {
                ItemStack refund = droppedColor != null && CCRJ45.COLORED_NET_CABLES.containsKey(droppedColor)
                        ? new ItemStack(CCRJ45.COLORED_NET_CABLES.get(droppedColor).get())
                        : new ItemStack(CCRJ45.NET_CONNECTOR_CABLE.get());
                if (!context.getPlayer().getInventory().add(refund)) {
                    net.minecraft.world.level.block.Block.popResource(context.getLevel(), firstToSecond.pos(), refund);
                }
            }
            PlayerDataHelper.addCooldown(context.getPlayer(), this, context.getItemInHand(), 5);
            return InteractionResult.CONSUME;
        }

        if (firstConnector.isLinkedTo(firstToSecond) || connector.isLinkedTo(secondToFirst)) {
            CableLink existingFirst = firstConnector.getLink(firstToSecond);
            if (existingFirst == null) existingFirst = connector.getLink(secondToFirst);
            DyeColor existingColor = existingFirst != null ? existingFirst.color() : null;
            DyeColor newColor = this.color;

            if (java.util.Objects.equals(existingColor, newColor)) {
                PlayerDataHelper.sendActionMessage(context.getPlayer(), Component.translatable("message.ccrj45.cable.already_linked"));
                return InteractionResult.CONSUME;
            }

            firstConnector.updateLinkColor(firstToSecond);
            connector.updateLinkColor(secondToFirst);

            if (!context.getPlayer().getAbilities().instabuild) {
                ItemStack oldRefund = existingColor != null && CCRJ45.COLORED_NET_CABLES.containsKey(existingColor)
                        ? new ItemStack(CCRJ45.COLORED_NET_CABLES.get(existingColor).get())
                        : new ItemStack(CCRJ45.NET_CONNECTOR_CABLE.get());
                if (!context.getPlayer().getInventory().add(oldRefund)) {
                    net.minecraft.world.level.block.Block.popResource(context.getLevel(), firstToSecond.pos(), oldRefund);
                }
                context.getItemInHand().shrink(1);
            }

            PlayerDataHelper.addCooldown(context.getPlayer(), this, context.getItemInHand(), 5);
            PlayerDataHelper.sendActionMessage(context.getPlayer(), Component.translatable("message.ccrj45.cable.color_updated"));
            return InteractionResult.CONSUME;
        }

        if (!firstConnector.canLinkToConnector(connector)) {
            PlayerDataHelper.sendActionMessage(context.getPlayer(),
                    Component.translatable("message.ccrj45.cable.too_far",
                            distanceBlocks(firstToSecond.pos(), secondToFirst.pos()),
                            firstConnector.getMaxLinkDistance(),
                            connector.getMaxLinkDistance()));
            return InteractionResult.FAIL;
        }

        if (firstConnector.getConnectionCount() >= firstConnector.getMaxConnections()) {
            PlayerDataHelper.sendActionMessage(context.getPlayer(),
                    Component.translatable("message.ccrj45.cable.first_max_connections",
                            firstConnector.getConnectionCount(), firstConnector.getMaxConnections()));
            return InteractionResult.FAIL;
        }

        if (connector.getConnectionCount() >= connector.getMaxConnections()) {
            PlayerDataHelper.sendActionMessage(context.getPlayer(),
                    Component.translatable("message.ccrj45.cable.second_max_connections",
                            connector.getConnectionCount(), connector.getMaxConnections()));
            return InteractionResult.FAIL;
        }

        boolean firstAdded = firstConnector.linkTo(firstToSecond);
        boolean secondAdded = connector.linkTo(secondToFirst);
        if (!firstAdded || !secondAdded) {
            if (firstAdded) firstConnector.unlinkTo(firstToSecond);
            if (secondAdded) connector.unlinkTo(secondToFirst);
            NetCableConnectorBlockEntity.refreshConnectedNetwork(firstConnector.getLevel(), secondToFirst.pos());
            NetCableConnectorBlockEntity.refreshConnectedNetwork(connector.getLevel(), firstToSecond.pos());
            PlayerDataHelper.sendActionMessage(context.getPlayer(), Component.translatable("message.ccrj45.cable.link_failed"));
            return InteractionResult.FAIL;
        }

        NetCableConnectorBlockEntity.refreshConnectedNetwork(firstConnector.getLevel(), secondToFirst.pos());
        NetCableConnectorBlockEntity.refreshConnectedNetwork(connector.getLevel(), firstToSecond.pos());
        PlayerDataHelper.sendActionMessage(context.getPlayer(), Component.translatable("message.ccrj45.cable.link_created"));

        if (!context.getPlayer().getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
        PlayerDataHelper.addCooldown(context.getPlayer(), this, context.getItemInHand(), 5);
        return InteractionResult.CONSUME;
    }

    private static int distanceBlocks(BlockPos a, BlockPos b) {
        return (int) Math.ceil(Math.sqrt(a.distSqr(b)));
    }
}
