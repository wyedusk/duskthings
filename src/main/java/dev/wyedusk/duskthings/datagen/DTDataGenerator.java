package dev.wyedusk.duskthings.datagen;

import dev.wyedusk.duskthings.DuskThings;
import dev.wyedusk.duskthings.datagen.client.DTItemModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = DuskThings.MODID)
public class DTDataGenerator implements IModBusEvent {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        // Client-side Providers
        generator.addProvider(
                event.includeClient(),
                new DTItemModelProvider(output, existingFileHelper)
        );
    }
}
