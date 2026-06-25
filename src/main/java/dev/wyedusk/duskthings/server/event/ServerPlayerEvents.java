package dev.wyedusk.duskthings.server.event;

import dev.wyedusk.duskthings.DuskThings;
import dev.wyedusk.duskthings.network.packet.ClientboundSyncGhostPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = DuskThings.MODID)
public class ServerPlayerEvents {
    @SubscribeEvent
    public static void onStartTracking(
            PlayerEvent.StartTracking event) {
        if (!(event.getTarget() instanceof LivingEntity entity)) return;

        boolean ghost = entity.getData(DuskThings.IS_GHOST.get());

        PacketDistributor.sendToPlayer(
                (ServerPlayer) event.getEntity(),
                new ClientboundSyncGhostPacket(entity.getId(), ghost)
        );
    }
}
