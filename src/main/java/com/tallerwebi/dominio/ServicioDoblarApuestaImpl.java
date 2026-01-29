package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PartidaDividaException;
import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;
import com.tallerwebi.dominio.excepcion.UnaCantidadDeCartasSuperadaException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service("ServicioDoblarApuesta")
public class ServicioDoblarApuestaImpl implements ServicioDoblarApuesta{


    @Override
    public void validarApuesta(Integer saldo, Integer apuesta) throws SaldoInsuficiente {
        Integer nuevaApuesta = apuesta*2;
        if (saldo < nuevaApuesta){
            throw new SaldoInsuficiente("Saldo insuficiente para realizar la apuesta.");
        }
    }

    @Override
    public void validarCantidadDeCartas(List<Map<String, Object>> cartasJugador) throws UnaCantidadDeCartasSuperadaException {
        if(cartasJugador.size() >= 3){
            throw new UnaCantidadDeCartasSuperadaException("Cantidad de cartas superada");
        }
    }

    @Override
    public void validarPartidaDividida(boolean partidaDivida) throws PartidaDividaException {
        if(partidaDivida){
            throw new PartidaDividaException("Partida dividida");
        }
    }

    @Override
    public int calcularDoblDeApuesta(int i) {
        return (i*2);
    }


}
