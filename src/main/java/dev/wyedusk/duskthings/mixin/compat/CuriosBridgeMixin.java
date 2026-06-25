package dev.wyedusk.duskthings.mixin.compat;

import dev.wyedusk.duskthings.DuskThings;
import dev.wyedusk.duskthings.compat.curios.CuriosBridge;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

@Mixin(CuriosBridge.class)
public class CuriosBridgeMixin {
    @Inject(method = "hasCuriosItem", at = @At("HEAD"), cancellable = true)
    private static void duskthings$hasCuriosItem(
            LivingEntity entity, Item item,
            CallbackInfoReturnable<Boolean> cir) {
        Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(entity);
        curiosInventory.ifPresent(
                iCuriosItemHandler -> cir.setReturnValue(!iCuriosItemHandler.findCurios(DuskThings.SPECTRAL_LENS.get()).isEmpty()));
    }
}
