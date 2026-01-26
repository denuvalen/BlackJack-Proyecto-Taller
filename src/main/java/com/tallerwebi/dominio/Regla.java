package com.tallerwebi.dominio;

import java.util.function.Predicate;

public class Regla {
    private int minJ, maxJ;
    private Predicate<Integer> condicionCrupier;
    private Accion accion;

    public Regla(int minJ, int maxJ, Predicate<Integer> condicionCrupier, Accion accion) {
        this.minJ = minJ;
        this.maxJ = maxJ;
        this.condicionCrupier = condicionCrupier;
        this.accion = accion;
    }

    public boolean aplica(int puntosJugador, int puntosCrupier) {
        return puntosJugador >= minJ && puntosJugador <= maxJ
                && condicionCrupier.test(puntosCrupier);
    }

    public Accion getAccion() {
        return accion;
    }
}
