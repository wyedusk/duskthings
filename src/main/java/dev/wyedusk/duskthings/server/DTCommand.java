package dev.wyedusk.duskthings.server;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.wyedusk.duskthings.DuskThings;
import dev.wyedusk.duskthings.network.packet.ClientboundSyncGhostPacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = DuskThings.MODID)
public class DTCommand {
    @SubscribeEvent
    public static void registerCommands(
            RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("duskthings")
                        .then(Commands.literal("ghost")
                                .then(Commands.literal("add")
                                        .requires(source -> source.hasPermission(2))
                                        .then(Commands.argument("entity", EntityArgument.entity())
                                                .executes((CommandContext<CommandSourceStack> context) -> {
                                                    Entity entity = EntityArgument.getEntity(context, "entity");

                                                    CommandSourceStack source = context.getSource();
                                                    ServerLevel level = source.getLevel();

                                                    if (level.isClientSide) {
                                                        context.getSource().sendFailure(Component.literal("This command must be ran on the server!"));
                                                        return 0;
                                                    }

                                                    entity.setData(DuskThings.IS_GHOST, true);
                                                    Component entityComponent = entity.getName().copy().withStyle(Style.EMPTY.withHoverEvent(
                                                            new HoverEvent(
                                                                    HoverEvent.Action.SHOW_ENTITY,
                                                                    new HoverEvent.EntityTooltipInfo(
                                                                            entity.getType(),
                                                                            entity.getUUID(),
                                                                            entity.getName()
                                                                    )
                                                            )
                                                    ));
                                                    context.getSource().sendSuccess(() -> entityComponent.copy().append(Component.literal(" is now a ghost.")), true);
                                                    PacketDistributor.sendToPlayersTrackingEntity(
                                                            entity,
                                                            new ClientboundSyncGhostPacket(entity.getId(), true)
                                                    );
                                                    return 1;
                                                })))
                                .then(Commands.literal("remove")
                                        .requires(source -> source.hasPermission(2))
                                        .then(Commands.argument("entity", EntityArgument.entity())
                                                .executes((CommandContext<CommandSourceStack> context) -> {
                                                    Entity entity = EntityArgument.getEntity(context, "entity");

                                                    CommandSourceStack source = context.getSource();
                                                    ServerLevel level = source.getLevel();

                                                    if (level.isClientSide) {
                                                        context.getSource().sendFailure(Component.literal("This command must be ran on the server!"));
                                                        return 0;
                                                    }

                                                    entity.setData(DuskThings.IS_GHOST, false);
                                                    Component entityComponent = entity.getName().copy().withStyle(Style.EMPTY.withHoverEvent(
                                                            new HoverEvent(
                                                                    HoverEvent.Action.SHOW_ENTITY,
                                                                    new HoverEvent.EntityTooltipInfo(
                                                                            entity.getType(),
                                                                            entity.getUUID(),
                                                                            entity.getName()
                                                                    )
                                                            )
                                                    ));
                                                    context.getSource().sendSuccess(() -> entityComponent.copy().append(Component.literal(" is no longer a ghost.")), true);
                                                    PacketDistributor.sendToPlayersTrackingEntity(
                                                            entity,
                                                            new ClientboundSyncGhostPacket(entity.getId(), false)
                                                    );
                                                    return 1;
                                                })))
                        .then(Commands.literal("get")
                                .then(Commands.argument("entity", EntityArgument.entity())
                                        .executes((CommandContext<CommandSourceStack> context) -> {
                                            Entity entity = EntityArgument.getEntity(context, "entity");

                                            CommandSourceStack source = context.getSource();
                                            ServerLevel level = source.getLevel();

                                            if (level.isClientSide) {
                                                context.getSource().sendFailure(Component.literal("This command must be ran on the server!"));
                                                return 0;
                                            }

                                            boolean isGhost = entity.getData(DuskThings.IS_GHOST);
                                            Component entityComponent = entity.getName().copy().withStyle(Style.EMPTY.withHoverEvent(
                                                    new HoverEvent(
                                                            HoverEvent.Action.SHOW_ENTITY,
                                                            new HoverEvent.EntityTooltipInfo(
                                                                    entity.getType(),
                                                                    entity.getUUID(),
                                                                    entity.getName()
                                                            )
                                                    )
                                            ));
                                            if (isGhost) {
                                                context.getSource().sendSuccess(() -> entityComponent.copy().append(Component.literal(" is a ghost.")), false);
                                            } else {
                                                context.getSource().sendSuccess(() -> entityComponent.copy().append(Component.literal(" is not a ghost.")), false);
                                            }
                                            PacketDistributor.sendToPlayersTrackingEntity(
                                                    entity,
                                                    new ClientboundSyncGhostPacket(entity.getId(), true)
                                            );
                                            return 1;
                                        })))
                        )
        );
    }
}
