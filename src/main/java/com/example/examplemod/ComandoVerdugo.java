package com.example.examplemod;

import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "blindexecutioner")
public class ComandoVerdugo {

    @SubscribeEvent
    public static void registrar(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("verdugo")
                .executes(ctx -> {
                    ServerPlayerEntity p = ctx.getSource().getPlayerOrException();
                    Verdugo.invocar(p);
                    return 1;
                })
        );
    }
}