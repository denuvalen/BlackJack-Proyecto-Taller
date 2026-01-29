package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PartidaDividaException;
import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;
import com.tallerwebi.dominio.excepcion.UnaCantidadDeCartasSuperadaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioDoblarApuestaTest {
    ServicioDoblarApuesta servicioDoblarApuesta = mock(ServicioDoblarApuesta.class);
    ServicioDeckOfCards servicioDeckOfCards = mock(ServicioDeckOfCards.class);
    @BeforeEach
    public void setUp() {
        this.servicioDoblarApuesta = new ServicioDoblarApuestaImpl();
    }
    @Test
    public void queSeLanceUnSaldoInsuficienteExceptionAlTenerSaldoInsuficiente() throws SaldoInsuficiente {
        Integer saldo, apuesta;
        saldo = 100;
        apuesta=100;

        assertThrows(SaldoInsuficiente.class, () -> {
            servicioDoblarApuesta.validarApuesta(saldo, apuesta);
        });
    }

    @Test
    public void queSeLanceUnaCantidadDeCartasSuperadaExceptionAlTenerTresCartas() throws UnaCantidadDeCartasSuperadaException {
       when(servicioDeckOfCards.sacarCartas(String.valueOf(anyString()), anyInt()))
                .thenReturn(Arrays.asList(null, null, null));
        List<Map<String, Object>> cartasJugador = servicioDeckOfCards.sacarCartas("deck_id_falso", 3);

        assertThrows(UnaCantidadDeCartasSuperadaException.class, () -> {
            servicioDoblarApuesta.validarCantidadDeCartas(cartasJugador);
        });
    }

    @Test
    public void queSeLanceUnPartidaDividaExceptionAlEstarDividaLaPartida()  {

        assertThrows(PartidaDividaException.class, () -> {
            servicioDoblarApuesta.validarPartidaDividida(true);
        });
    }
    @Test
    public void queSePuedaCalcularElDobleDeUnaApuesta()  {
        assertEquals(100, servicioDoblarApuesta.calcularDoblDeApuesta(50));
    }






}
