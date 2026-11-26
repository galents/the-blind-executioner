package com.example.examplemod;

import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "blindexecutioner")
public class ComandoCuervo {

    @SubscribeEvent
    public static void registrar(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("cuervo")
                .executes(ctx -> {
                    ServerPlayerEntity p = ctx.getSource().getPlayerOrException();
                    Testigos.invocar(p);
                    return 1;
                })
        );
    }
}