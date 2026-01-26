package com.tallerwebi.dominio;

public enum Accion {

    PEDIR("¡Pedí carta!"),
    PLANTARSE("Mejor plantate."),
    DOBLAR("¡Doblá la apuesta!");

    private final String mensaje;

    Accion(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }
}
