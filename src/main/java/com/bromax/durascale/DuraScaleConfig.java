package com.bromax.durascale;

import net.minecraftforge.common.ForgeConfigSpec;

public class DuraScaleConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.DoubleValue MULTIPLIER = BUILDER
            .comment("Multiplier applied to all item max durability. Default 3.0 = triple durability. Requires restart.")
            .defineInRange("multiplier", 3.0, 0.1, 100.0);

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
