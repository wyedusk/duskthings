package dev.wyedusk.duskthings.network.packet;

import dev.wyedusk.duskthings.DuskThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record ClientboundSyncGhostPacket(int entityId, boolean isGhost) implements CustomPacketPayload {

    public static final Type<ClientboundSyncGhostPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(DuskThings.MODID, "is_ghost"));

    public static final StreamCodec<FriendlyByteBuf, ClientboundSyncGhostPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ClientboundSyncGhostPacket::entityId,
            ByteBufCodecs.BOOL, ClientboundSyncGhostPacket::isGhost,
            ClientboundSyncGhostPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}