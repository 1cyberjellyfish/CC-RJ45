package net.cyber_jellyfish.cc_rj45.recipe;

import net.cyber_jellyfish.cc_rj45.CCRJ45;
import net.cyber_jellyfish.cc_rj45.util.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
//? if neoforge {
/*import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
*///?}

//? if <=1.21.1 {
/*public class ModRecipeSerializers {
    //? if neoforge {
    /^public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, CCRJ45.MODID);

    private static final DeferredHolder<RecipeSerializer<?>, PortableDriveCraftingRecipe.Serializer> CRAFTING_HOLDER =
            RECIPE_SERIALIZERS.register("crafting_portable_drive", PortableDriveCraftingRecipe.Serializer::new);

    private static final DeferredHolder<RecipeSerializer<?>, PortableDriveColoringRecipe.Serializer> COLORING_HOLDER =
            RECIPE_SERIALIZERS.register("portable_drive_coloring", PortableDriveColoringRecipe.Serializer::new);

    public static final RegistryEntry<PortableDriveCraftingRecipe.Serializer> PORTABLE_DRIVE_CRAFTING = CRAFTING_HOLDER::get;
    public static final RegistryEntry<PortableDriveColoringRecipe.Serializer> PORTABLE_DRIVE_COLORING = COLORING_HOLDER::get;
    ^///?} else {
    public static final PortableDriveCraftingRecipe.Serializer RAW_CRAFTING = new PortableDriveCraftingRecipe.Serializer();
    public static final PortableDriveColoringRecipe.Serializer RAW_COLORING = new PortableDriveColoringRecipe.Serializer();

    public static final RegistryEntry<PortableDriveCraftingRecipe.Serializer> PORTABLE_DRIVE_CRAFTING = () -> RAW_CRAFTING;
    public static final RegistryEntry<PortableDriveColoringRecipe.Serializer> PORTABLE_DRIVE_COLORING = () -> RAW_COLORING;

    public static void init() {
        net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.RECIPE_SERIALIZER,
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(CCRJ45.MODID, "crafting_portable_drive"), RAW_CRAFTING);
        net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.RECIPE_SERIALIZER,
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(CCRJ45.MODID, "portable_drive_coloring"), RAW_COLORING);
    }
    //?}
}
*///?} else {
public class ModRecipeSerializers {
    public static final RecipeSerializer<PortableDriveCraftingRecipe> RAW_CRAFTING =
            new RecipeSerializer<>(com.mojang.serialization.MapCodec.unit(new PortableDriveCraftingRecipe()), net.minecraft.network.codec.StreamCodec.unit(new PortableDriveCraftingRecipe()));
    public static final RecipeSerializer<PortableDriveColoringRecipe> RAW_COLORING =
            new RecipeSerializer<>(com.mojang.serialization.MapCodec.unit(new PortableDriveColoringRecipe()), net.minecraft.network.codec.StreamCodec.unit(new PortableDriveColoringRecipe()));

    //? if neoforge {
    /*public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, CCRJ45.MODID);

    private static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PortableDriveCraftingRecipe>> CRAFTING_HOLDER =
            RECIPE_SERIALIZERS.register("crafting_portable_drive", () -> RAW_CRAFTING);

    private static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PortableDriveColoringRecipe>> COLORING_HOLDER =
            RECIPE_SERIALIZERS.register("portable_drive_coloring", () -> RAW_COLORING);

    public static final RegistryEntry<RecipeSerializer<PortableDriveCraftingRecipe>> PORTABLE_DRIVE_CRAFTING = CRAFTING_HOLDER::get;
    public static final RegistryEntry<RecipeSerializer<PortableDriveColoringRecipe>> PORTABLE_DRIVE_COLORING = COLORING_HOLDER::get;
    *///?} else {
    public static final RegistryEntry<RecipeSerializer<PortableDriveCraftingRecipe>> PORTABLE_DRIVE_CRAFTING = () -> RAW_CRAFTING;
    public static final RegistryEntry<RecipeSerializer<PortableDriveColoringRecipe>> PORTABLE_DRIVE_COLORING = () -> RAW_COLORING;

    public static void init() {
        net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.RECIPE_SERIALIZER,
                CCRJ45.id("crafting_portable_drive"), RAW_CRAFTING);
        net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.RECIPE_SERIALIZER,
                CCRJ45.id("portable_drive_coloring"), RAW_COLORING);
    }
    //?}
}
//?}
