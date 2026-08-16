package com.bromax.durascale;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

@Mod(DuraScale.MODID)
public class DuraScale {

    public static final String MODID = "bromax_durascale";
    private static final Logger LOGGER = LogManager.getLogger(MODID);

    public DuraScale(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, DuraScaleConfig.SPEC);
        modBus.addListener(this::setup);
    }

    private void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(DuraScale::applyMultiplier);
    }

    private static void applyMultiplier() {
        double multiplier = DuraScaleConfig.MULTIPLIER.get();
        int modified = 0;

        try {
            Field componentsField = Item.class.getDeclaredField("components");
            componentsField.setAccessible(true);

            for (Item item : BuiltInRegistries.ITEM) {
                DataComponentMap original = (DataComponentMap) componentsField.get(item);
                Integer maxDamage = original.get(DataComponents.MAX_DAMAGE);
                if (maxDamage == null || maxDamage <= 0) continue;

                int newDamage = (int) Math.round(maxDamage * multiplier);
                DataComponentMap updated = DataComponentMap.builder()
                        .addAll(original)
                        .set(DataComponents.MAX_DAMAGE, newDamage)
                        .build();

                componentsField.set(item, updated);
                modified++;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.error("[DuraScale] Failed to apply durability multiplier: {}", e.getMessage());
            return;
        }

        LOGGER.info("[DuraScale] Applied {}x durability multiplier to {} items.", multiplier, modified);
    }
}
