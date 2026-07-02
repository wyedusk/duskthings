package dev.wyedusk.duskthings;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = DuskThings.MODID)
public class DTConfig implements IModBusEvent {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    //private static final ModConfigSpec.ConfigValue<Boolean> GHOSTS_FEATURE_ENABLED;
    private static final ModConfigSpec.IntValue GHOST_TRANSPARENCY;

    static final ModConfigSpec SPEC;

    //public static boolean ghostsFeatureEnabled;
    public static int ghostTransparency;

    static {
        BUILDER.comment("Settings related to the Ghost Entities feature").push("ghost_entities");

        //GHOSTS_FEATURE_ENABLED = BUILDER.comment("Whether the Ghost Entities feature is enabled.")
        //        .define("ghosts_feature_enabled", true);
        GHOST_TRANSPARENCY = BUILDER.comment("The transparency of Ghosts when they can be seen.\n0 = Invisible, 255 = Visible")
                .defineInRange("ghost_transparency", 120, 0, 255);

        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    static void onLoad(
            final ModConfigEvent event) {
        //ghostsFeatureEnabled = GHOSTS_FEATURE_ENABLED.get();
        ghostTransparency = GHOST_TRANSPARENCY.getAsInt();
    }
}
