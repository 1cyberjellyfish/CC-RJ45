package net.cyber_jellyfish.cc_rj45.item;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.Mount;
import dan200.computercraft.api.media.IMedia;
import net.cyber_jellyfish.cc_rj45.Config;
import net.cyber_jellyfish.cc_rj45.ModDataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class PortableDriveMedia implements IMedia {
    @Override
    public String getLabel(HolderLookup.Provider registries, ItemStack stack) {
        return stack.getHoverName().getString();
    }

    @Override
    public boolean setLabel(ItemStack stack, @Nullable String label) {
        if (label != null && !label.isEmpty()) {
            stack.set(DataComponents.CUSTOM_NAME, Component.literal(label));
        } else {
            stack.remove(DataComponents.CUSTOM_NAME);
        }
        return true;
    }

    @Override
    @Nullable
    public Mount createDataMount(ItemStack stack, ServerLevel level) {
        var diskId = stack.get(ModDataComponents.DISK_ID.get());
        if (diskId == null) {
            int id = ComputerCraftAPI.createUniqueNumberedSaveDir(level.getServer(), "portable_drive");
            stack.set(ModDataComponents.DISK_ID.get(), new DiskId(id));
            diskId = new DiskId(id);
        }
        long bytes = Config.diskCapacityBytes();
        return ComputerCraftAPI.createSaveDirMount(level.getServer(), "portable_drive/" + diskId.id(), bytes);
    }
}
