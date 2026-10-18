package net.cyber_jellyfish.cc_rj45;

//? if neoforge {
/*import net.neoforged.neoforge.common.ModConfigSpec;
*///?}

public final class Config {
    //? if neoforge {
    /*private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue CONNECTOR_MAX_CABLE_LENGTH = BUILDER
            .comment("Maximum cable link distance for Net Cable Connector blocks.")
            .defineInRange("connectorMaxCableLength", 15, 1, 256);

    public static final ModConfigSpec.IntValue CONNECTOR_HUGE_MAX_CABLE_LENGTH = BUILDER
            .comment("Maximum cable link distance for Net Cable Connector Huge blocks.")
            .defineInRange("connectorHugeMaxCableLength", 38, 1, 512);

    public static final ModConfigSpec.IntValue DISK_CAPACITY_BYTES = BUILDER
            .comment("Portable drive capacity in bytes. Default 1_048_576 (1 MB).")
            .defineInRange("diskCapacityBytes", 1_048_576, 16_384, 268_435_456);

    public static final ModConfigSpec SPEC = BUILDER.build();
    *///?}

    private Config() {
    }

    public static int connectorMaxCableLength() {
        //? if neoforge {
        /*return CONNECTOR_MAX_CABLE_LENGTH.get();
        *///?} else {
        return 15;
        //?}
    }

    public static int connectorHugeMaxCableLength() {
        //? if neoforge {
        /*return CONNECTOR_HUGE_MAX_CABLE_LENGTH.get();
        *///?} else {
        return 38;
        //?}
    }

    public static long diskCapacityBytes() {
        //? if neoforge {
        /*return DISK_CAPACITY_BYTES.get();
        *///?} else {
        return 1_048_576L;
        //?}
    }
}
