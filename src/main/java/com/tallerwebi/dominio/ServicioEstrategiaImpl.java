package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ServicioEstrategiaImpl implements ServicioEstrategia{
    private List<Regla> reglasDuras = new ArrayList<>();
    private List<Regla> reglasBlandas = new ArrayList<>();

    private boolean calcularSiEsSuave(List<Map<String, Object>> cartasJugador, int puntajeJugador) {
        boolean tieneAs = false;
        int total = 0;

        for (Map<String, Object> cartaMap : cartasJugador) {
            if (esAs(cartaMap)) {
                tieneAs = true;
            }
            String valor = (String) cartaMap.get("value");
            switch (valor) {
                case "KING":
                case "QUEEN":
                case "JACK":
                    total += 10;
                    break;
                case "ACE":
                    total += 11;
                    break;
                default:
                    total += Integer.parseInt(valor);
            }

        }
        return tieneAs && (total == puntajeJugador);
    }

    private boolean esAs(Map<String, Object> cartaMap) {
        Object valorObj = cartaMap.get("value");

        if (valorObj == null) return false;

        String valor = String.valueOf(valorObj).toUpperCase();

        return valor.equals("ACE") || valor.equals("A") || valor.equals("1");
    }


    public ServicioEstrategiaImpl() {
        // --- Reglas Duras ---
        reglasDuras.add(new Regla(13, 16, c -> c >= 2 && c <= 6, Accion.PLANTARSE));
        reglasDuras.add(new Regla(17, 21, c -> true, Accion.PLANTARSE));

        // --- Reglas Blandas ---
        reglasBlandas.add(new Regla(19, 21, c -> true, Accion.PLANTARSE));
    }

    @Override
    public String recomendar(List<Map<String, Object>> cartasJugador,int puntosJ, int puntosC) {
        List<Regla> lista = calcularSiEsSuave(cartasJugador, puntosJ) ? reglasBlandas : reglasDuras;

        return lista.stream()
                .filter(r -> r.aplica(puntosJ, puntosC))
                .findFirst()
                .map(r -> r.getAccion().getMensaje())
                .orElse("¡Pedí carta!");
    }
}
