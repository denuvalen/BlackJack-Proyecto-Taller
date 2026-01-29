package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PartidaDividaException;
import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;
import com.tallerwebi.dominio.excepcion.UnaCantidadDeCartasSuperadaException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface  ServicioDoblarApuesta {
    void validarApuesta(Integer saldo, Integer apuesta) throws SaldoInsuficiente;

    void validarCantidadDeCartas(List<Map<String, Object>> cartasJugador) throws UnaCantidadDeCartasSuperadaException;

    void validarPartidaDividida(boolean b) throws PartidaDividaException;

    int calcularDoblDeApuesta(int i);
}
