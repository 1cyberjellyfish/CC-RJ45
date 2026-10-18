package net.cyber_jellyfish.cc_rj45.aero;

import java.lang.reflect.Method;
import java.util.Optional;

import net.cyber_jellyfish.cc_rj45.CCRJ45;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;

public class SimulatedReflect {
    private static final String ROPE_HOLDER_BE = "dev.simulated_team.simulated.content.blocks.rope.RopeStrandHolderBlockEntity";
    private static final String ROPE_HOLDER_BEHAVIOR = "dev.simulated_team.simulated.content.blocks.rope.RopeStrandHolderBehavior";
    private static final String ROPE_ITEM_CLASS = "dev.simulated_team.simulated.content.items.rope.RopeItem.RopeItem";

    private static Class<?> ropeHolderBeClass;
    private static Class<?> ropeHolderBehaviorClass;
    private static Method createRopeMethod;
    private static Method destroyRopeMethod;
    private static Method getRopeHolderMethod;
    private static Boolean available = null;

    public static boolean isAvailable() {
        if (available != null) return available;
        //? if <=1.21.1 && neoforge {
        /*try {
            net.neoforged.fml.ModList modList = net.neoforged.fml.ModList.get();
            if (modList != null && !modList.isLoaded("simulated") && !modList.isLoaded("create_aeronautics") && !modList.isLoaded("aeronautics_bundled")) {
                available = false;
                CCRJ45.LOGGER.debug("Simulated mod not loaded");
                return false;
            }

            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) cl = SimulatedReflect.class.getClassLoader();

            ropeHolderBeClass = Class.forName(ROPE_HOLDER_BE, true, cl);
            ropeHolderBehaviorClass = Class.forName(ROPE_HOLDER_BEHAVIOR, true, cl);
            Class<?> ropeItemClass = Class.forName(ROPE_ITEM_CLASS, true, cl);

            createRopeMethod = ropeHolderBehaviorClass.getMethod("createRope", ropeHolderBehaviorClass);
            destroyRopeMethod = ropeHolderBehaviorClass.getMethod("destroyRope", ServerPlayer.class, Vec3.class);
            getRopeHolderMethod = ropeItemClass.getMethod("getRopeHolder", Level.class, BlockPos.class);

            available = true;
            CCRJ45.LOGGER.info("Simulated mod detected, rope bridge integration enabled");
        } catch (Throwable e) {
            available = false;
            CCRJ45.LOGGER.debug("Simulated mod not available: {}", e.getMessage());
        }
        *///?} else {
        available = false;
        //?}
        return available;
    }

    public static Optional<BlockEntityType<?>> getRopeConnectorType() {
        if (!isAvailable()) return Optional.empty();
        return getBeType("simulated:rope_connector");
    }

    public static Optional<BlockEntityType<?>> getRopeWinchType() {
        if (!isAvailable()) return Optional.empty();
        return getBeType("simulated:rope_winch");
    }

    private static Optional<BlockEntityType<?>> getBeType(String registryName) {
        if (!isAvailable()) return Optional.empty();
        try {
            return BuiltInRegistries.BLOCK_ENTITY_TYPE.getOptional(CCRJ45.parseId(registryName));
        } catch (Throwable e) {
            CCRJ45.LOGGER.debug("Failed to get BE type {}: {}", registryName, e.getMessage());
        }
        return Optional.empty();
    }

    public static boolean isRopeAnchor(BlockEntity be) {
        if (!isAvailable() || be == null) return false;
        return ropeHolderBeClass.isAssignableFrom(be.getClass());
    }

    public static Object getRopeHolder(Level level, BlockPos pos) {
        if (!isAvailable()) return null;
        try {
            return getRopeHolderMethod.invoke(null, level, pos);
        } catch (Throwable e) {
            CCRJ45.LOGGER.error("Failed to get rope holder", e);
            return null;
        }
    }

    public static boolean createRope(Object behaviorA, Object behaviorB) {
        if (!isAvailable()) return false;
        try {
            return (boolean) createRopeMethod.invoke(behaviorA, behaviorB);
        } catch (Throwable e) {
            CCRJ45.LOGGER.error("Failed to create rope bridge", e);
            return false;
        }
    }

    public static void destroyRope(Object behavior, ServerPlayer player, Vec3 dropPos) {
        if (!isAvailable()) return;
        try {
            destroyRopeMethod.invoke(behavior, player, dropPos);
        } catch (Throwable e) {
            CCRJ45.LOGGER.error("Failed to destroy rope bridge", e);
        }
    }
}
