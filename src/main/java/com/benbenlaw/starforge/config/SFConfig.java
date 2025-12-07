package com.benbenlaw.starforge.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class SFConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Integer> forgeCapacity;
    public static final ModConfigSpec.ConfigValue<Integer> forgeCapacityCap1;
    public static final ModConfigSpec.ConfigValue<Integer> forgeCapacityCap2;
    public static final ModConfigSpec.ConfigValue<Integer> forgeCapacityCap3;
    public static final ModConfigSpec.ConfigValue<Integer> forgeCapacityCap4;
    public static final ModConfigSpec.ConfigValue<Integer> forgeCapacityCap5;


    static {

        // Casting Configs
        BUILDER.comment("Star Forge Startup Config")
                .push("Star Forge");

        forgeCapacity = BUILDER.comment("Base Star Forge star power capacity, default = 500")
                .define("Ore Multiplier", 500);

        forgeCapacityCap1 = BUILDER.comment("Tier 1 Star Forge Cap star power capacity, default = 1000")
                .define("Tier 1 Cap", 1000);

        forgeCapacityCap2 = BUILDER.comment("Tier 2 Star Forge Cap star power capacity, default = 2000")
                .define("Tier 2 Cap", 2000);

        forgeCapacityCap3 = BUILDER.comment("Tier 3 Star Forge Cap star power capacity, default = 4000")
                .define("Tier 3 Cap", 4000);

        forgeCapacityCap4 = BUILDER.comment("Tier 4 Star Forge Cap star power capacity, default = 8000")
                .define("Tier 4 Cap", 8000);

        forgeCapacityCap5 = BUILDER.comment("Tier 5 Star Forge Cap star power capacity, default = 16000")
                .define("Tier 5 Cap", 16000);




        BUILDER.pop();

        //LAST
        SPEC = BUILDER.build();

    }
}