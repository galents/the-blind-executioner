package com.example.examplemod;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "blindexecutioner")
public class PecadoEvents {

    // CODICIA: romper minerales raros
    @SubscribeEvent
    public static void alRomperBloque(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.level.isClientSide()) return;

        Block bloque = event.getState().getBlock();
        int puntos = 0;

        if (bloque == Blocks.GOLD_ORE) puntos = 1;
        else if (bloque == Blocks.DIAMOND_ORE || bloque == Blocks.EMERALD_ORE) puntos = 3;
        else if (bloque == Blocks.ANCIENT_DEBRIS || bloque == Blocks.SPAWNER) puntos = 5;

        if (puntos > 0) {
            Pecados.sumarCodicia(player, puntos);
            player.sendMessage(
                new StringTextComponent("Codicia: " + Pecados.getCodicia(player)),
                Util.NIL_UUID
            );
            Testigos.reportar(player, "codicia", puntos);
        }
    }

    // SANGRE: matar aldeanos, crias o animales con la comida llena
    @SubscribeEvent
    public static void alMorirEntidad(LivingDeathEvent event) {
        LivingEntity victima = event.getEntityLiving();
        if (victima.level.isClientSide()) return;
        if (victima.getPersistentData().getBoolean(Testigos.TAG)) return;
        if (!(event.getSource().getEntity() instanceof PlayerEntity)) return;

        PlayerEntity player = (PlayerEntity) event.getSource().getEntity();
        int puntos = 0;

        if (victima instanceof VillagerEntity) {
            puntos = 5;
        } else if (victima instanceof AnimalEntity) {
            if (victima.isBaby()) {
                puntos = 3;
            } else if (player.getFoodData().getFoodLevel() >= 20) {
                puntos = 1;
            }
        }

        if (puntos > 0) {
            Pecados.sumarSangre(player, puntos);
            player.sendMessage(
                new StringTextComponent("Sangre: " + Pecados.getSangre(player)),
                Util.NIL_UUID
            );
            Testigos.reportar(player, "sangre", puntos);
        }
    }
}