package com.bromax.durascale;

import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

@Mod(DuraScale.MODID)
public class DuraScale {

    public static final String MODID = "bromax_durascale";
    private static final Logger LOGGER = LogManager.getLogger(MODID);

    public DuraScale() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DuraScaleConfig.SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(DuraScale::applyMultiplier);
    }

    private static void applyMultiplier() {
        double multiplier = DuraScaleConfig.MULTIPLIER.get();
        int modified = 0;

        try {
            // Item.maxDamage (SRG f_41371_) is a private final int in 1.20.1
            Field maxDamageField = ObfuscationReflectionHelper.findField(Item.class, "f_41371_");

            for (Item item : ForgeRegistries.ITEMS) {
                int maxDamage = maxDamageField.getInt(item);
                if (maxDamage <= 0) continue;

                maxDamageField.setInt(item, (int) Math.round(maxDamage * multiplier));
                modified++;
            }
        } catch (Exception e) {
            LOGGER.error("[DuraScale] Failed to apply durability multiplier: {}", e.getMessage());
            return;
        }

        LOGGER.info("[DuraScale] Applied {}x durability multiplier to {} items.", multiplier, modified);
    }
}
