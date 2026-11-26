package com.example.examplemod;

import java.util.List;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "blindexecutioner")
public class Testigos {

    public static final String TAG = "be_testigo";
    public static final int TICKS_ESCAPE = 100; // 5 segundos

    // Invoca un cuervo (loro) cerca del jugador
    public static void invocar(ServerPlayerEntity p) {
        ParrotEntity loro = EntityType.PARROT.create(p.level);
        loro.moveTo(p.getX() + 3, p.getY() + 1, p.getZ(), 0, 0);
        loro.setCustomName(new StringTextComponent("Cuervo Palido"));
        loro.getPersistentData().putBoolean(TAG, true);
        p.level.addFreshEntity(loro);
    }

    // Se llama cuando el jugador peca: busca un cuervo que lo vea
    public static void reportar(PlayerEntity p, String categoria, int puntos) {
        List<ParrotEntity> loros = p.level.getEntitiesOfClass(
                ParrotEntity.class, p.getBoundingBox().inflate(16));

        for (ParrotEntity loro : loros) {
            CompoundNBT d = loro.getPersistentData();
            if (!d.getBoolean(TAG) || d.getBoolean("be_delatando")) continue;

            if (loro.canSee(p)) {
                d.putBoolean("be_delatando", true);
                d.putInt("be_ticks", 0);
                d.putString("be_cat", categoria);
                d.putInt("be_pts", puntos);
                d.putUUID("be_dueno", p.getUUID());
                loro.setNoGravity(true);
                p.sendMessage(new StringTextComponent(
                        "Un cuervo te vio! Matalo en 5 segundos"), Util.NIL_UUID);
                return;
            }
        }
    }

    // Cada tick: si el cuervo esta delatando, sube; al llegar a 5 s escapa
    @SubscribeEvent
    public static void alActualizar(LivingEvent.LivingUpdateEvent event) {
        LivingEntity e = event.getEntityLiving();
        if (e.level.isClientSide()) return;

        CompoundNBT d = e.getPersistentData();
        if (!d.getBoolean(TAG) || !d.getBoolean("be_delatando")) return;

        int t = d.getInt("be_ticks") + 1;
        d.putInt("be_ticks", t);
        e.setDeltaMovement(0, 0.15, 0);

        if (t >= TICKS_ESCAPE) {
            PlayerEntity dueno = e.level.getPlayerByUUID(d.getUUID("be_dueno"));
                        if (dueno != null) {
                String s = ArbolDecision.elegirSentencia(
                        Pecados.getSangre(dueno), Pecados.getCodicia(dueno),
                        Pecados.getCobardia(dueno), false);
                dueno.sendMessage(new StringTextComponent(
                        "El cuervo escapo. Sentencia: " + s), Util.NIL_UUID);
                if (!s.startsWith("Sin cargos")) {
                    Verdugo.invocar(dueno);
                }
            }
            e.remove();
        }
    }

    // Si matan al cuervo mientras delata: se anula el cargo
    @SubscribeEvent
    public static void alMorir(LivingDeathEvent event) {
        LivingEntity e = event.getEntityLiving();
        if (e.level.isClientSide()) return;

        CompoundNBT d = e.getPersistentData();
        if (!d.getBoolean(TAG) || !d.getBoolean("be_delatando")) return;

        PlayerEntity dueno = e.level.getPlayerByUUID(d.getUUID("be_dueno"));
        if (dueno != null) {
            Pecados.ajustar(dueno, d.getString("be_cat"), -d.getInt("be_pts"));
            dueno.sendMessage(new StringTextComponent(
                    "Testigo silenciado. Crimen impune."), Util.NIL_UUID);
        }
    }
}