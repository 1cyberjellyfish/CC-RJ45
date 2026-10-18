package net.cyber_jellyfish.cc_rj45.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cyber_jellyfish.cc_rj45.CCRJ45;
import net.cyber_jellyfish.cc_rj45.ModDataComponents;
import net.cyber_jellyfish.cc_rj45.item.DiskColor;
import net.cyber_jellyfish.cc_rj45.item.PortableDriveItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.core.registries.Registries;
//? if <=1.21.1 {
/*import net.minecraft.resources.ResourceLocation;
*///?} else {
import net.minecraft.resources.Identifier;
//?}
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class PortableDriveColoringRecipe extends CustomRecipe {
    //? if <=1.21.1 {
    /*public PortableDriveColoringRecipe(CraftingBookCategory category) {
        super(category);
    }
    *///?} else {
    public PortableDriveColoringRecipe() {
        super();
    }
    //?}

    @Override
    public boolean matches(CraftingInput input, Level level) {
        boolean hasDrive = false;
        boolean hasDye = false;
        int matched = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            matched++;

            if (stack.is(CCRJ45.PORTABLE_DRIVE.get()) && !hasDrive) hasDrive = true;
            //? if <=1.21.1 {
            /*else if (stack.getItem() instanceof DyeItem) hasDye = true;
            *///?} else {
            else if (stack.has(net.minecraft.core.component.DataComponents.DYE)) hasDye = true;
            //?}
            else return false;
        }

        return matched >= 2 && hasDrive && hasDye;
    }

    //? if <=1.21.1 {
    /*@Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack drive = ItemStack.EMPTY;
        int r = 0, g = 0, b = 0;
        int count = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(CCRJ45.PORTABLE_DRIVE.get())) {
                drive = stack.copy();
            } else if (stack.getItem() instanceof DyeItem dye) {
                int c = PortableDriveItem.colorFromDye(dye.getDyeColor());
                r += (c >> 16) & 0xFF;
                g += (c >> 8) & 0xFF;
                b += c & 0xFF;
                count++;
            }
        }

        if (!drive.isEmpty() && count > 0) {
            int color = (r / count) << 16 | (g / count) << 8 | (b / count);
            PortableDriveItem.setDriveColor(drive, color);
            drive.setCount(1);
            return drive;
        }

        return ItemStack.EMPTY;
    }
    *///?} else {
    @Override
    public ItemStack assemble(CraftingInput input) {
        ItemStack drive = ItemStack.EMPTY;
        int r = 0, g = 0, b = 0;
        int count = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(CCRJ45.PORTABLE_DRIVE.get())) {
                drive = stack.copy();
            } else {
                net.minecraft.world.item.DyeColor dyeColor = stack.get(net.minecraft.core.component.DataComponents.DYE);
                if (dyeColor != null) {
                    int c = PortableDriveItem.colorFromDye(dyeColor);
                    r += (c >> 16) & 0xFF;
                    g += (c >> 8) & 0xFF;
                    b += c & 0xFF;
                    count++;
                }
            }
        }

        if (!drive.isEmpty() && count > 0) {
            int color = (r / count) << 16 | (g / count) << 8 | (b / count);
            PortableDriveItem.setDriveColor(drive, color);
            drive.setCount(1);
            return drive;
        }

        return ItemStack.EMPTY;
    }
    //?}

    //? if <=1.21.1 {
    /*@Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        ItemStack stack = new ItemStack(CCRJ45.PORTABLE_DRIVE.get());
        PortableDriveItem.setDriveColor(stack, 0xA0A0A0);
        return stack;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(Ingredient.of(CCRJ45.PORTABLE_DRIVE.get()));
        list.add(Ingredient.of(TagKey.create(Registries.ITEM, ResourceLocation.parse("c:dyes"))));
        return list;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.PORTABLE_DRIVE_COLORING.get();
    }
    *///?} else {
    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipeSerializers.PORTABLE_DRIVE_COLORING.get();
    }
    //?}

    //? if <=1.21.1 {
    /*public static class Serializer implements RecipeSerializer<PortableDriveColoringRecipe> {
        private static final MapCodec<PortableDriveColoringRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(r -> r.category())
                ).apply(inst, PortableDriveColoringRecipe::new)
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, PortableDriveColoringRecipe> STREAM_CODEC =
                StreamCodec.of(PortableDriveColoringRecipe.Serializer::toNetwork, PortableDriveColoringRecipe.Serializer::fromNetwork);

        @Override
        public MapCodec<PortableDriveColoringRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PortableDriveColoringRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static PortableDriveColoringRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            CraftingBookCategory category = buf.readEnum(CraftingBookCategory.class);
            return new PortableDriveColoringRecipe(category);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buf, PortableDriveColoringRecipe recipe) {
            buf.writeEnum(recipe.category());
        }
    }
    *///?}
}
