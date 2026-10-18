package net.cyber_jellyfish.cc_rj45;

import net.cyber_jellyfish.cc_rj45.item.DiskColor;
import net.cyber_jellyfish.cc_rj45.item.DiskId;
import net.cyber_jellyfish.cc_rj45.util.RegistryEntry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
//? if neoforge {
/*import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
*///?}

public class ModDataComponents {
    //? if neoforge {
    /*public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, CCRJ45.MODID);

    private static final DeferredHolder<DataComponentType<?>, DataComponentType<DiskId>> DISK_ID_HOLDER = DATA_COMPONENTS.registerComponentType(
            "disk_id",
            builder -> builder.persistent(DiskId.CODEC).networkSynchronized(DiskId.STREAM_CODEC)
    );
    private static final DeferredHolder<DataComponentType<?>, DataComponentType<DiskColor>> DISK_COLOR_HOLDER = DATA_COMPONENTS.registerComponentType(
            "disk_color",
            builder -> builder.persistent(DiskColor.CODEC).networkSynchronized(DiskColor.STREAM_CODEC)
    );

    public static final RegistryEntry<DataComponentType<DiskId>> DISK_ID = DISK_ID_HOLDER::get;
    public static final RegistryEntry<DataComponentType<DiskColor>> DISK_COLOR = DISK_COLOR_HOLDER::get;
    *///?} else {
    public static final DataComponentType<DiskId> RAW_DISK_ID = DataComponentType.<DiskId>builder()
            .persistent(DiskId.CODEC)
            .networkSynchronized(DiskId.STREAM_CODEC)
            .build();

    public static final DataComponentType<DiskColor> RAW_DISK_COLOR = DataComponentType.<DiskColor>builder()
            .persistent(DiskColor.CODEC)
            .networkSynchronized(DiskColor.STREAM_CODEC)
            .build();

    public static final RegistryEntry<DataComponentType<DiskId>> DISK_ID = () -> RAW_DISK_ID;
    public static final RegistryEntry<DataComponentType<DiskColor>> DISK_COLOR = () -> RAW_DISK_COLOR;

    public static void init() {
        net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.DATA_COMPONENT_TYPE,
                CCRJ45.id("disk_id"), RAW_DISK_ID);
        net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.DATA_COMPONENT_TYPE,
                CCRJ45.id("disk_color"), RAW_DISK_COLOR);
    }
    //?}
}
