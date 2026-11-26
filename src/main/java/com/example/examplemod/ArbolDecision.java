package com.example.examplemod;

public class ArbolDecision {

    // Nivel según puntos: 0 = sin cargos, 1 = leve, 2 = grave, 3 = mortal
    public static int nivel(int puntos) {
        if (puntos <= 0) return 0;
        if (puntos <= 4) return 1;
        if (puntos <= 9) return 2;
        return 3;
    }

    public static String elegirSentencia(int sangre, int codicia, int cobardia, boolean obstruccion) {
        // Rama 1: crimen grave (mataste al testigo frente a otro cuervo)
        if (obstruccion) {
            return "SENTENCIA DE EJECUCION (sobrevive 45 s)";
        }

        // Rama 2: pecado dominante (en empate gana sangre, luego codicia)
        int mayor = Math.max(sangre, Math.max(codicia, cobardia));
        if (mayor <= 0) {
            return "Sin cargos: el Verdugo no viene";
        }

        int nivel = nivel(mayor);

        if (sangre == mayor) {
            if (nivel == 1) return "N1 - Marca del Carnicero";
            if (nivel == 2) return "N2 - Juicio de Sangre";
            return "N3 - Sentencia de Sangre";
        }
        if (codicia == mayor) {
            if (nivel == 1) return "N1 - Peaje";
            if (nivel == 2) return "N2 - Juicio de Codicia";
            return "N3 - Embargo";
        }
        if (nivel == 1) return "N1 - Susto";
        if (nivel == 2) return "N2 - Juicio de Valor";
        return "N3 - Duelo";
    }
}