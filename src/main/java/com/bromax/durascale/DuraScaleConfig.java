package com.bromax.durascale;

import net.neoforged.neoforge.common.ModConfigSpec;

public class DuraScaleConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue MULTIPLIER = BUILDER
            .comment("Multiplier applied to all item max durability. Default 3.0 = triple durability. Requires restart.")
            .defineInRange("multiplier", 3.0, 0.1, 100.0);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
