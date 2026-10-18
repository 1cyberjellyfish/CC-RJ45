package net.cyber_jellyfish.cc_rj45.item;

import java.util.List;
import net.cyber_jellyfish.cc_rj45.Config;
import net.cyber_jellyfish.cc_rj45.block.NetCableConnectorHugeBlock;
import net.cyber_jellyfish.cc_rj45.block.NetCableHubBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class NetCableConnectorBlockItem extends BlockItem {
    public NetCableConnectorBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    //? if <=1.21.1 {
    /*@Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.ccrj45.connectable_with_net_cable").withStyle(ChatFormatting.GRAY));
        int maxConnections = getBlock() instanceof NetCableHubBlock ? 4 : 2;
        tooltipComponents.add(
                Component.translatable("tooltip.ccrj45.max_connections", maxConnections)
                        .withStyle(ChatFormatting.GRAY)
        );
        int maxLength = getBlock() instanceof NetCableConnectorHugeBlock
                ? Config.connectorHugeMaxCableLength()
                : Config.connectorMaxCableLength();
        tooltipComponents.add(
                Component.translatable("tooltip.ccrj45.max_cable_length", maxLength)
                        .withStyle(ChatFormatting.GRAY)
        );
    }
    *///?} else {
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, display, tooltipComponents, tooltipFlag);
        tooltipComponents.accept(Component.translatable("tooltip.ccrj45.connectable_with_net_cable").withStyle(ChatFormatting.GRAY));
        int maxConnections = getBlock() instanceof NetCableHubBlock ? 4 : 2;
        tooltipComponents.accept(
                Component.translatable("tooltip.ccrj45.max_connections", maxConnections)
                        .withStyle(ChatFormatting.GRAY)
        );
        int maxLength = getBlock() instanceof NetCableConnectorHugeBlock
                ? Config.connectorHugeMaxCableLength()
                : Config.connectorMaxCableLength();
        tooltipComponents.accept(
                Component.translatable("tooltip.ccrj45.max_cable_length", maxLength)
                        .withStyle(ChatFormatting.GRAY)
        );
    }
    //?}
}
