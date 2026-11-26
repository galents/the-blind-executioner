package com.example.examplemod;

import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "blindexecutioner")
public class ComandoJuicio {

    @SubscribeEvent
    public static void registrar(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("juicio")
                .then(Commands.literal("info")
                    .executes(ctx -> {
                        ServerPlayerEntity p = ctx.getSource().getPlayerOrException();
                        String texto = "Sangre: " + Pecados.getSangre(p)
                                + " | Codicia: " + Pecados.getCodicia(p)
                                + " | Cobardia: " + Pecados.getCobardia(p);
                        ctx.getSource().sendSuccess(new StringTextComponent(texto), false);
                        return 1;
                    })
                )
                .then(Commands.literal("sentencia")
                    .executes(ctx -> {
                        ServerPlayerEntity p = ctx.getSource().getPlayerOrException();
                        String s = ArbolDecision.elegirSentencia(
                            Pecados.getSangre(p), Pecados.getCodicia(p), Pecados.getCobardia(p), false);
                        ctx.getSource().sendSuccess(new StringTextComponent("Sentencia: " + s), false);
                        return 1;
                    })
                )
                .then(Commands.literal("reiniciar")
                    .executes(ctx -> {
                        ServerPlayerEntity p = ctx.getSource().getPlayerOrException();
                        Pecados.reiniciar(p);
                        ctx.getSource().sendSuccess(new StringTextComponent("Cargos reiniciados"), false);
                        return 1;
                    })
                )
        );
    }
}