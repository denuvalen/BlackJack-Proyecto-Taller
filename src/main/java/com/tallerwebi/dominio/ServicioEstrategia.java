package com.tallerwebi.dominio;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public interface ServicioEstrategia {
//    String obtenerRecomendacion(ComienzoCartasDTO dtoCartas);


    String recomendar(List<Map<String, Object>> cartasJugador, int puntosJ, int puntosC);

}
