package com.tallerwebi.dominio;


import java.util.List;
import java.util.Map;

public interface ServicioEstrategia {
//    String obtenerRecomendacion(ComienzoCartasDTO dtoCartas);


    String recomendar(List<Map<String, Object>> cartasJugador, int puntosJ, int puntosC);

}
