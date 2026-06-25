package dev.wyedusk.duskthings.compat.curios;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

public class CuriosBridge {
    public static boolean hasCuriosItem(
            LivingEntity entity, Item item) {
        return false;
    }
}
