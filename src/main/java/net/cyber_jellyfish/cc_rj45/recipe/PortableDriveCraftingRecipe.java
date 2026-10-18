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
import net.minecraft.world.item.Items;
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

public class PortableDriveCraftingRecipe extends CustomRecipe {
    //? if <=1.21.1 {
    /*public PortableDriveCraftingRecipe(CraftingBookCategory category) {
        super(category);
    }
    *///?} else {
    public PortableDriveCraftingRecipe() {
        super();
    }
    //?}

    @Override
    public boolean matches(CraftingInput input, Level level) {
        boolean hasGold = false;
        boolean hasPaper = false;
        boolean hasRedstone = false;
        boolean hasTerracotta = false;
        boolean hasAmethyst = false;
        boolean hasDye = false;
        int matched = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            matched++;

            if (stack.is(Items.GOLD_INGOT) && !hasGold) hasGold = true;
            else if (stack.is(Items.PAPER) && !hasPaper) hasPaper = true;
            else if (stack.is(Items.REDSTONE) && !hasRedstone) hasRedstone = true;
            else if (stack.is(Items.TERRACOTTA) && !hasTerracotta) hasTerracotta = true;
            else if (stack.is(Items.AMETHYST_SHARD) && !hasAmethyst) hasAmethyst = true;
            //? if <=1.21.1 {
            /*else if (stack.getItem() instanceof DyeItem) hasDye = true;
            *///?} else {
            else if (stack.has(net.minecraft.core.component.DataComponents.DYE)) hasDye = true;
            //?}
            else return false;
        }

        return matched >= 6 && hasGold && hasPaper && hasRedstone && hasTerracotta && hasAmethyst && hasDye;
    }

    //? if <=1.21.1 {
    /*@Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        int r = 0, g = 0, b = 0;
        int count = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.getItem() instanceof DyeItem dye) {
                int c = PortableDriveItem.colorFromDye(dye.getDyeColor());
                r += (c >> 16) & 0xFF;
                g += (c >> 8) & 0xFF;
                b += c & 0xFF;
                count++;
            }
        }

        ItemStack result = new ItemStack(CCRJ45.PORTABLE_DRIVE.get());
        if (count > 0) {
            int color = (r / count) << 16 | (g / count) << 8 | (b / count);
            PortableDriveItem.setDriveColor(result, color);
        }
        return result;
    }
    *///?} else {
    @Override
    public ItemStack assemble(CraftingInput input) {
        int r = 0, g = 0, b = 0;
        int count = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            net.minecraft.world.item.DyeColor dyeColor = stack.get(net.minecraft.core.component.DataComponents.DYE);
            if (dyeColor != null) {
                int c = PortableDriveItem.colorFromDye(dyeColor);
                r += (c >> 16) & 0xFF;
                g += (c >> 8) & 0xFF;
                b += c & 0xFF;
                count++;
            }
        }

        ItemStack result = new ItemStack(CCRJ45.PORTABLE_DRIVE.get());
        if (count > 0) {
            int color = (r / count) << 16 | (g / count) << 8 | (b / count);
            PortableDriveItem.setDriveColor(result, color);
        }
        return result;
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
        list.add(Ingredient.of(Items.GOLD_INGOT));
        list.add(Ingredient.of(Items.PAPER));
        list.add(Ingredient.of(Items.REDSTONE));
        list.add(Ingredient.of(Items.TERRACOTTA));
        list.add(Ingredient.of(Items.AMETHYST_SHARD));
        list.add(Ingredient.of(TagKey.create(Registries.ITEM, ResourceLocation.parse("c:dyes"))));
        return list;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 6;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.PORTABLE_DRIVE_CRAFTING.get();
    }
    *///?} else {
    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipeSerializers.PORTABLE_DRIVE_CRAFTING.get();
    }
    //?}

    //? if <=1.21.1 {
    /*public static class Serializer implements RecipeSerializer<PortableDriveCraftingRecipe> {
        private static final MapCodec<PortableDriveCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(r -> r.category())
                ).apply(inst, PortableDriveCraftingRecipe::new)
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, PortableDriveCraftingRecipe> STREAM_CODEC =
                StreamCodec.of(PortableDriveCraftingRecipe.Serializer::toNetwork, PortableDriveCraftingRecipe.Serializer::fromNetwork);

        @Override
        public MapCodec<PortableDriveCraftingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PortableDriveCraftingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static PortableDriveCraftingRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            CraftingBookCategory category = buf.readEnum(CraftingBookCategory.class);
            return new PortableDriveCraftingRecipe(category);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buf, PortableDriveCraftingRecipe recipe) {
            buf.writeEnum(recipe.category());
        }
    }
    *///?}
}
