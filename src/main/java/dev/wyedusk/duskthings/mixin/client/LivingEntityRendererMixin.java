package dev.wyedusk.duskthings.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.wyedusk.duskthings.DuskThings;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Unique
    private static final ThreadLocal<LivingEntity> CURRENT_ENTITY = new ThreadLocal<>();

    @Inject(method = "render", at = @At("HEAD"))
    private void duskthings$render$captureCurrentEntity(
            LivingEntity entity, float yaw, float tick, PoseStack pose, MultiBufferSource buffer, int light,
            CallbackInfo ci) {
        CURRENT_ENTITY.set(entity);
    }

    @ModifyArg(
            method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"
            ),
            index = 4
    )
    private int duskthings$render$modifyRenderToBufferAlpha(
            int original) {
        if (CURRENT_ENTITY.get().getData(DuskThings.IS_GHOST).equals(true)) {
            int alpha = 120;
            return (alpha << 24) | (original & 0x00FFFFFF);
        }
        return original;
    }

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    private void duskthings$getRenderType(
            LivingEntity entity, boolean p_115323_, boolean p_115324_, boolean p_115325_,
            CallbackInfoReturnable<RenderType> cir) {
        if (entity.getData(DuskThings.IS_GHOST).equals(false)) return;
        LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> renderer = (LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>)(Object)this;
        ResourceLocation texture = renderer.getTextureLocation(entity);

        cir.setReturnValue(RenderType.entityTranslucent(texture));
    }
}
