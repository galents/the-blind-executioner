package com.example.examplemod;

import net.minecraft.entity.player.PlayerEntity;

public class Pecados {

    private static final String LLAVE_SANGRE = "be_sangre";
    private static final String LLAVE_CODICIA = "be_codicia";
    private static final String LLAVE_COBARDIA = "be_cobardia";

    private static int get(PlayerEntity p, String llave) {
        return p.getPersistentData().getInt(llave);
    }

    private static void sumar(PlayerEntity p, String llave, int cantidad) {
        p.getPersistentData().putInt(llave, get(p, llave) + cantidad);
    }

    public static int getSangre(PlayerEntity p) { return get(p, LLAVE_SANGRE); }
    public static int getCodicia(PlayerEntity p) { return get(p, LLAVE_CODICIA); }
    public static int getCobardia(PlayerEntity p) { return get(p, LLAVE_COBARDIA); }

    public static void sumarSangre(PlayerEntity p, int c) { sumar(p, LLAVE_SANGRE, c); }
    public static void sumarCodicia(PlayerEntity p, int c) { sumar(p, LLAVE_CODICIA, c); }
    public static void sumarCobardia(PlayerEntity p, int c) { sumar(p, LLAVE_COBARDIA, c); }

        public static void reiniciar(PlayerEntity p) {
        p.getPersistentData().putInt(LLAVE_SANGRE, 0);
        p.getPersistentData().putInt(LLAVE_CODICIA, 0);
        p.getPersistentData().putInt(LLAVE_COBARDIA, 0);
    }
        public static void ajustar(PlayerEntity p, String categoria, int delta) {
        String llave = "be_" + categoria;
        int nuevo = Math.max(0, get(p, llave) + delta);
        p.getPersistentData().putInt(llave, nuevo);
    }
}