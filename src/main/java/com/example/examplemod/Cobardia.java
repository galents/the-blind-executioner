package com.example.examplemod;

import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "blindexecutioner")
public class Cobardia {

    // Un monstruo te golpea: guardamos donde y cuando
    @SubscribeEvent
    public static void alRecibirDanio(LivingHurtEvent event) {
        if (!(event.getEntityLiving() instanceof PlayerEntity)) return;
        PlayerEntity p = (PlayerEntity) event.getEntityLiving();
        if (p.level.isClientSide()) return;
        if (!(event.getSource().getEntity() instanceof MonsterEntity)) return;

        CompoundNBT d = p.getPersistentData();
        d.putDouble("be_hitx", p.getX());
        d.putDouble("be_hitz", p.getZ());
        d.putLong("be_hitt", p.level.getGameTime());
        d.putBoolean("be_huyendo", true);
    }

    // Cada tick: vemos si ya te alejaste 20 bloques en menos de 5 segundos
    @SubscribeEvent
    public static void cadaTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        PlayerEntity p = event.player;
        if (p.level.isClientSide()) return;

        CompoundNBT d = p.getPersistentData();
        if (!d.getBoolean("be_huyendo")) return;

        long pasado = p.level.getGameTime() - d.getLong("be_hitt");
        if (pasado > 100) {
            d.putBoolean("be_huyendo", false);
            return;
        }

        double dx = p.getX() - d.getDouble("be_hitx");
        double dz = p.getZ() - d.getDouble("be_hitz");
        if (Math.sqrt(dx * dx + dz * dz) >= 20) {
            d.putBoolean("be_huyendo", false);
            Pecados.sumarCobardia(p, 1);
            p.sendMessage(new StringTextComponent(
                    "Cobardia: " + Pecados.getCobardia(p)), Util.NIL_UUID);
            Testigos.reportar(p, "cobardia", 1);
        }
    }
}