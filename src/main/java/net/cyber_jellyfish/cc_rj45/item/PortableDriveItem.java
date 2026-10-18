package net.cyber_jellyfish.cc_rj45.item;

import net.cyber_jellyfish.cc_rj45.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
//? if <=1.21.1 {
/*import net.minecraft.world.InteractionResultHolder;
*///?}
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class PortableDriveItem extends Item {
    public PortableDriveItem(Properties properties) {
        super(properties);
    }

    public static int colorFromDye(DyeColor dc) {
        return switch (dc) {
            case WHITE -> 0xf0f0f0;
            case ORANGE -> 0xf2b233;
            case MAGENTA -> 0xe57fd8;
            case LIGHT_BLUE -> 0x99b2f2;
            case YELLOW -> 0xdede6c;
            case LIME -> 0x7fcc19;
            case PINK -> 0xf2b2cc;
            case GRAY -> 0x4c4c4c;
            case LIGHT_GRAY -> 0x999999;
            case CYAN -> 0x4c99b2;
            case PURPLE -> 0xb266e5;
            case BLUE -> 0x3366cc;
            case BROWN -> 0x7f664c;
            case GREEN -> 0x57A64E;
            case RED -> 0xcc4c4c;
            case BLACK -> 0x111111;
        };
    }

    public static void setDriveColor(ItemStack stack, int color) {
        stack.set(ModDataComponents.DISK_COLOR.get(), new DiskColor(color));
        //? if <=1.21.1 {
        /*stack.set(net.minecraft.core.component.DataComponents.DYED_COLOR, new net.minecraft.world.item.component.DyedItemColor(color, false));
        *///?} else {
        stack.set(net.minecraft.core.component.DataComponents.DYED_COLOR, new net.minecraft.world.item.component.DyedItemColor(color));
        //?}
    }

    //? if <=1.21.1 {
    /*@Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (flag.isAdvanced()) {
            var diskId = stack.get(ModDataComponents.DISK_ID.get());
            if (diskId != null) {
                tooltip.add(Component.translatable("gui.computercraft.tooltip.disk_id", diskId.id()).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack driveStack = player.getItemInHand(hand);
        InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack otherStack = player.getItemInHand(otherHand);

        if (otherStack.getItem() instanceof DyeItem dye) {
            if (!level.isClientSide) {
                int color = colorFromDye(dye.getDyeColor());
                setDriveColor(driveStack, color);
                otherStack.shrink(1);
            }
            return InteractionResultHolder.success(driveStack);
        }

        return InteractionResultHolder.pass(driveStack);
    }
    *///?} else {
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltip, TooltipFlag flag) {
        if (flag.isAdvanced()) {
            var diskId = stack.get(ModDataComponents.DISK_ID.get());
            if (diskId != null) {
                tooltip.accept(Component.translatable("gui.computercraft.tooltip.disk_id", diskId.id()).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @Override
    public net.minecraft.world.InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack driveStack = player.getItemInHand(hand);
        InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack otherStack = player.getItemInHand(otherHand);

        DyeColor dyeColor = otherStack.get(net.minecraft.core.component.DataComponents.DYE);
        if (dyeColor != null) {
            if (!level.isClientSide()) {
                int color = colorFromDye(dyeColor);
                setDriveColor(driveStack, color);
                otherStack.shrink(1);
            }
            return net.minecraft.world.InteractionResult.SUCCESS;
        }

        return net.minecraft.world.InteractionResult.PASS;
    }
    //?}
}
