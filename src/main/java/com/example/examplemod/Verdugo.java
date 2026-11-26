package com.example.examplemod;

import java.util.List;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "blindexecutioner")
public class Verdugo {

    public static final String TAG = "be_verdugo";
    public static final int DURACION = 900; // 45 segundos

    public static void invocar(PlayerEntity p) {
        // Evita invocar dos verdugos para el mismo jugador
        List<VexEntity> cercanos = p.level.getEntitiesOfClass(
                VexEntity.class, p.getBoundingBox().inflate(128));
        for (VexEntity otro : cercanos) {
            if (otro.getPersistentData().getBoolean(TAG)) return;
        }

        VexEntity v = EntityType.VEX.create(p.level);
        v.moveTo(p.getX() + 25, p.getY() + 3, p.getZ(), 0, 0);
        v.setCustomName(new StringTextComponent("El Verdugo"));
        v.setCustomNameVisible(true);
        v.setNoAi(true);
        v.setNoGravity(true);
        v.setInvulnerable(true);

        CompoundNBT d = v.getPersistentData();
        d.putBoolean(TAG, true);
        d.putInt("be_vticks", 0);
        d.putInt("be_cd", 0);
        d.putUUID("be_vdueno", p.getUUID());
        d.putDouble("be_hx", p.getX());
        d.putDouble("be_hy", p.getY() + 1);
        d.putDouble("be_hz", p.getZ());

        p.level.addFreshEntity(v);
        p.sendMessage(new StringTextComponent(
                "El Verdugo viene. Agachate y no hagas ruido 45 segundos."), Util.NIL_UUID);
    }

    @SubscribeEvent
    public static void alActualizar(LivingEvent.LivingUpdateEvent event) {
        LivingEntity e = event.getEntityLiving();
        if (e.level.isClientSide()) return;

        CompoundNBT d = e.getPersistentData();
        if (!d.getBoolean(TAG)) return;

        PlayerEntity owner = e.level.getPlayerByUUID(d.getUUID("be_vdueno"));
        if (owner == null || !owner.isAlive()) {
            e.remove();
            return;
        }

        int t = d.getInt("be_vticks") + 1;
        d.putInt("be_vticks", t);

        // 1) OIDO: si el ruido llega hasta el, recuerda tu posicion
        double radio = owner.isCrouching() ? 0 : (owner.isSprinting() ? 20 : 8);
        if (e.distanceTo(owner) <= radio) {
            d.putDouble("be_hx", owner.getX());
            d.putDouble("be_hy", owner.getY() + 1);
            d.putDouble("be_hz", owner.getZ());
        }

        // 2) MOVIMIENTO: va hacia el ultimo lugar donde te oyo (sin pathfinding)
        double dx = d.getDouble("be_hx") - e.getX();
        double dy = d.getDouble("be_hy") - e.getY();
        double dz = d.getDouble("be_hz") - e.getZ();
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (dist > 1.0) {
            double paso = Math.min(0.2, dist);
            float yaw = (float) (Math.atan2(dz, dx) * 180.0 / Math.PI) - 90.0F;
            e.moveTo(e.getX() + dx / dist * paso,
                     e.getY() + dy / dist * paso,
                     e.getZ() + dz / dist * paso, yaw, 0);
        }

        // 3) GOLPE: si esta cerca, ataca (con espera de 2 segundos)
        int cd = d.getInt("be_cd");
        if (cd > 0) d.putInt("be_cd", cd - 1);
        if (e.distanceTo(owner) < 2.5 && cd <= 0) {
            owner.hurt(DamageSource.mobAttack(e), 6.0F);
            d.putInt("be_cd", 40);
        }

        // 4) VEREDICTO: sobreviviste 45 segundos
        if (t >= DURACION) {
            Pecados.reiniciar(owner);
            owner.sendMessage(new StringTextComponent(
                    "Sobreviviste. El Verdugo baja el hacha y te perdona."), Util.NIL_UUID);
            e.remove();
        }
    }
}