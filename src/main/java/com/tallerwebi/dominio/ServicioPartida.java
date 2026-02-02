package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;

import java.util.List;
import java.util.Map;

public interface ServicioPartida {

    void apostar(Partida partida, Integer monto) throws ApuestaInvalidaException, SaldoInsuficiente;
    void setBotonesAlCrearPartida(Partida partida);
    void setearApuesta(Usuario usuario, Integer apuesta);
    Map<String, Object> pedirCarta(Jugador jugador, ComienzoCartasDTO dto);
    int calcularPuntaje(List<Map<String, Object>> cartas);
    void consultarExistenciaDePartidaActiva(Usuario usuario) throws PartidaExistenteActivaException;
    Partida instanciarPartida(Jugador jugador) throws PartidaNoCreadaException;
    void inactivarPartidas(List<Partida> partidaActiva);
    Jugador crearJugador(Usuario usuario);

    String mandarEstrategia(List<Map<String, Object>> cartasJugador, Partida partidaActiva, Integer jugadorPuntaje, Integer  puntajeDealer);

    void doblarApuesta(Partida partidaActiva, Usuario usuario, List<Map<String, Object>> cartasJugador) throws SaldoInsuficiente, UnaCantidadDeCartasSuperadaException, PartidaDividaException;

    Partida obtenerPartidaActiva(Usuario usuario);
    String resultadoDeLaPartida(Partida partida, Integer puntosCrupier, Integer puntosCrupier1);
    void rendirse(Partida partidaActiva, Jugador jugador);
    void cambiarEstadoDeJuegoAJuegoDeUnaPartida(Partida p) throws PartidaActivaNoEnApuestaException;
    List<Partida> buscarPartidaActiva(Usuario usuario);

    ComienzoCartasDTO repartoInicial(Long id);
    void bloquearBotones(Partida partida);
    void dividirPartida(Partida partida, List<Map<String, Object>> cartasJugador) throws SaldoInsuficiente, ApuestaInvalidaException;
    String determinarResultadoDividido(Partida partida);
    void logicaBotonDividir(Partida partida, List<Map<String, Object>> cartasJugador, ComienzoCartasDTO dto);
    String verficarPuntaje(Partida p, ComienzoCartasDTO dto);
    String determinarResultado(Partida partida, ComienzoCartasDTO dto, List<Map<String, Object>> cartasJugador);
    String gestionarTurnoPararse( Partida partida, ComienzoCartasDTO dto, List<Map<String, Object>> cartasDealer, List<Map<String, Object>> cartasJugador, String deckId);
    void entregarCartaAlCrupier (Partida partida, List < Map < String, Object >> cartasDealer, String deckId);

    String bloquearDoblarApuesta(Partida partida);
}
