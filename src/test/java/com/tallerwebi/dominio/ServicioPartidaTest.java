package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioPartidaTest {

    private RepositorioUsuario repositorioUsuario;
    private RepositorioPartida repositorioPartida;
    private RepositorioJugador repositorioJugador;
    private ServicioEstrategia servicioEstrategia;
    private ServicioUsuario servicioUsuario;
    private ServicioDeckOfCards servicioDeckOfCards;
    private ServicioPartidaImpl servicioPartida;
    private ServicioDoblarApuesta servicioDoblarApuesta;
    private ServicioPartida servicioPartidaMock;
    List<Map<String, Object>> cartasJugador = new ArrayList<>();

    @BeforeEach
    public void init() {
        repositorioPartida = mock(RepositorioPartida.class);
        repositorioUsuario = mock(RepositorioUsuario.class);
        servicioDeckOfCards = mock(ServicioDeckOfCards.class);
        repositorioJugador = mock(RepositorioJugador.class);
        servicioEstrategia = mock(ServicioEstrategia.class);
        servicioDoblarApuesta = mock(ServicioDoblarApuesta.class);
        servicioUsuario = mock(ServicioUsuario.class);
        servicioPartidaMock = mock(ServicioPartida.class);
        servicioPartida = new ServicioPartidaImpl( servicioDeckOfCards, repositorioPartida, repositorioUsuario,
                repositorioJugador, servicioUsuario, servicioEstrategia, servicioDoblarApuesta);
    }

    @Test
    public void queAlConsultarSiExistePartidasActivasSeLanceUnaPartidaActivaException(){
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenComienzaUnaPartida(usuario);
        when(repositorioPartida.buscarPartidaActiva(usuario))
                .thenReturn(List.of(partidaActiva));

        assertThrows(PartidaExistenteActivaException.class, ()-> servicioPartida.consultarExistenciaDePartidaActiva(usuario));
    }
    @Test
    public void queSeSeteenEstadosParaPartidasActivasExistentes(){
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenComienzaUnaPartida(usuario);
        servicioPartida.inactivarPartidas(List.of(partidaActiva));
        assertEquals(EstadoPartida.INACTIVA ,partidaActiva.getEstadoPartida());
        assertEquals(EstadoDeJuego.ABANDONADO ,partidaActiva.getEstadoJuego());
    }



    @Test
    public void queSePuedaIntanciarUnaPartida() throws PartidaNoCreadaException {
        Usuario usuario = givenExisteUnUsuario();
        Jugador j = new Jugador();
        when(repositorioPartida.guardar(any(Partida.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        assertNotNull(servicioPartida.instanciarPartida(j));
    }

    @Test
    public void queCalculeCorrectamenteElPuntajeDeCartasNumericas() {
        List<Map<String, Object>> cartas = new ArrayList<>();
        cartas.add(Map.of("value", "5"));
        cartas.add(Map.of("value", "9"));

        int puntaje = servicioPartida.calcularPuntaje(cartas);

        assertEquals(14, puntaje);
    }
    @Test
    public void queCalculeCorrectamenteElPuntajeConCartasEspeciales() {
        List<Map<String, Object>> cartas = new ArrayList<>();
        cartas.add(Map.of("value", "KING"));
        cartas.add(Map.of("value", "QUEEN"));

        int puntaje = servicioPartida.calcularPuntaje(cartas);

        assertEquals(20, puntaje);
    }

    @Test
    public void queAlIniciarEstenHabilitadasSoloLasFichasYNoLosBotonesDeDecision() {
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenComienzaUnaPartida(usuario);
        whenComienzaLaPartida(partidaActiva);
        thenBotonesHabilitadosYDesicionesDesabilitado(partidaActiva);
    }
    private void thenBotonesHabilitadosYDesicionesDesabilitado(Partida partidaActiva) {
        assertEquals(partidaActiva.getEstadoJuego(), EstadoDeJuego.APUESTA);
        assertTrue(partidaActiva.getFichasHabilitadas());
        assertFalse(partidaActiva.getBotonesDesicionHabilitados());
    }

    private void whenComienzaLaPartida(Partida partidaActiva) {
        servicioPartida.setBotonesAlCrearPartida(partidaActiva);
    }


    @Test
    public void queAlSeleccionarElBotonEmpezarPartidaSeHabilitenLosBotonesDeDecision() throws PartidaActivaNoEnApuestaException{
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenComienzaUnaPartida(usuario);
        whenSeleccionoBotonEmpezarPartida(partidaActiva);
        whenComienzaLaPartida(partidaActiva);
        thenSeHabilitanLosBotonesDeDecision(partidaActiva);
    }

    private void whenSeleccionoBotonEmpezarPartida(Partida partidaActiva)throws PartidaActivaNoEnApuestaException  {
        servicioPartida.cambiarEstadoDeJuegoAJuegoDeUnaPartida(partidaActiva);
    }

    private void thenSeHabilitanLosBotonesDeDecision(Partida partidaActiva) {
        assertEquals(partidaActiva.getEstadoJuego(), EstadoDeJuego.JUEGO);
        assertFalse(partidaActiva.getFichasHabilitadas());
        assertTrue(partidaActiva.getBotonesDesicionHabilitados());
    }
    @Test
    public void queAlSeleccionarFichasSuValorSeSumeEnElPozoTotal() throws ApuestaInvalidaException, SaldoInsuficiente {
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenComienzaUnaPartida(usuario);
        when(repositorioUsuario.buscar(usuario.getEmail())).thenReturn(usuario);
        whenSeleccionoFichas(partidaActiva);
        thenSeSumaElPozo(partidaActiva);
    }

    private void whenSeleccionoFichas(Partida partidaActiva) throws ApuestaInvalidaException, SaldoInsuficiente {
        servicioPartida.apostar(partidaActiva, 100);
    }

    private void thenSeSumaElPozo(Partida partidaActiva) {
        assertEquals(200, partidaActiva.getApuesta());
    }

//    @Test
//    public void queAlSeleccionarElBotonEstrategiaElUsuarioRecibaUnaAyuda() throws PartidaActivaNoEnApuestaException{
//        Usuario usuario = givenExisteUnUsuario();
//        Partida partidaActiva = givenComienzaUnaPartida(usuario);
//        whenSeleccionoBotonEmpezarPartida(partidaActiva);
//        String mensajeEsperado= whenSeleccionoBotonEstrategia(partidaActiva, partidaActiva.getJugador());
//        thenElUsuarioRecibeUnaAyuda(partidaActiva.getJugador(), mensajeEsperado);
//    }
//
//    private void thenElUsuarioRecibeUnaAyuda(Jugador jugador, String mensajeEsperado) {
//        assertEquals(mensajeEsperado, "Dobla si podes, sino pedi una carta.");
//    }
//
//    private String whenSeleccionoBotonEstrategia(Partida partidaActiva, Jugador jugador) {
//        servicioPartida.seleccionBotonEstrategia(partidaActiva);
//        String mensajeEsperado= servicioPartida.mandarEstrategia(dto.getCartasJugador(), partidaActiva, partidaActiva.getJugador().getPuntaje(), partidaActiva.getCrupier().getPuntaje());
//        return mensajeEsperado;
//    }
    @Test
    public void queAlUtilizarLaEstrategiaSeDesactiveSuRespectivoBoton() {
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenComienzaUnaPartida(usuario);
        partidaActiva.setBotonEstrategia(true);
        //mockeo el servicioEstrategia
       when(servicioEstrategia.recomendar(anyList(), anyInt(), anyInt())).thenReturn("");
        servicioPartida.mandarEstrategia(new ArrayList<>(), partidaActiva, 10, 5);

        assertEquals(false, partidaActiva.getBotonEstrategia());
    }

    private void whenSeDescuentaElSaldoDeLaApuesta(Partida partidaActiva) throws ApuestaInvalidaException, SaldoInsuficiente {
        servicioPartida.apostar(partidaActiva, partidaActiva.getApuesta());
    }



//    @Test
//    public void queAlSeleccionarElBotonDoblarApuestaSeDobleLaApuesta() throws PartidaActivaNoEnApuestaException{
//        Usuario usuario = givenExisteUnUsuario();
//        Partida partidaActiva = givenComienzaUnaPartida(usuario);
//        whenSeleccionoBotonEmpezarPartida(partidaActiva);
//        Integer apuestaDoblada= whenSeleccionoBotonDoblarApuestaSeDoblaLaApuesta(partidaActiva, usuario);
//        thenApuestaDoblada(partidaActiva, usuario, apuestaDoblada);
//    }





//    private void thenApuestaDoblada(Partida partidaActiva, Usuario usuario, Integer apuestaDoblada) {
//        assertEquals(Integer.valueOf(200), partidaActiva.getApuesta());
//        assertEquals(900.0, usuario.getSaldo(), 0.01);
//    }

//    private Integer whenSeleccionoBotonDoblarApuestaSeDoblaLaApuesta(Partida partidaActiva, Usuario jugador) {
//        Integer resultado= servicioPartida.doblarApuesta(partidaActiva, jugador);
//        return resultado;
//    }
    @Test
    public void queAlEntregarCartaAlCrupierSeLeRepartaUnaSolaCartaSiElJugadorTieneMasDe21Puntos(){
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenComienzaUnaPartida(usuario);
        ComienzoCartasDTO dto = new ComienzoCartasDTO();
        dto.setPuntajeJugador(22);
        dto.setDeckId("test-deck");
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> cartaMock = Map.of("code", "AH", "value", "ACE");
        list.add(cartaMock);

        when(servicioDeckOfCards.sacarCartas(anyString(), anyInt())).thenReturn(list);
        when(servicioPartidaMock.calcularPuntaje(anyList())).thenReturn(10);

        servicioPartida.entregarCartaAlCrupier(partidaActiva, dto);

        assertEquals(1, dto.getCartasDealer().size());
    }



    @Test
    public void queAlSeleccionarElBotonPararseSeComparenLosPuntosYSeDefinaUnGanador() throws PartidaActivaNoEnApuestaException{
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenComienzaUnaPartida(usuario);
        when(repositorioUsuario.buscar(usuario.getEmail())).thenReturn(usuario);
        whenSeleccionoBotonEmpezarPartida(partidaActiva);
        whenSeleccionoBotonPararseSeComparanLosPuntosYSeDefineUnGanador(partidaActiva);
        thenResultadoDeLaPartida(partidaActiva);
    }

    private void thenResultadoDeLaPartida(Partida partidaActiva) {
        assertEquals("Resultado: Jugador gana", partidaActiva.getGanador());
    }

    private String whenSeleccionoBotonPararseSeComparanLosPuntosYSeDefineUnGanador(Partida partidaActiva) {
        Integer puntosCrupier= partidaActiva.getCrupier().getPuntaje();
        Integer puntosJugador= partidaActiva.getJugador().getPuntaje();
        String resultado= servicioPartida.resultadoDeLaPartida(partidaActiva, puntosCrupier, puntosJugador);
        partidaActiva.setGanador(resultado);
        return resultado;
    }

    @Test
    public void queAlSeleccionarElBotonRendirseSeLeResteLaApuestaAlJugadorYLaPartidaPasaAEstadoAbandonado() throws PartidaActivaNoEnApuestaException, ApuestaInvalidaException, SaldoInsuficiente {
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenComienzaUnaPartida(usuario);
        whenSeleccionoBotonEmpezarPartida(partidaActiva);
        whenSeleccionoBotonRendirseSeLeResteLaApuestaAlJugadorYYLaPartidaPasaAEstadoAbandonado(partidaActiva, partidaActiva.getJugador());
        thenEstadoAbandonadoYSaldoRestado(partidaActiva, partidaActiva.getJugador());
    }

    private void thenEstadoAbandonadoYSaldoRestado(Partida partidaActiva, Jugador jugador) {
        assertEquals(EstadoDeJuego.ABANDONADO ,partidaActiva.getEstadoJuego());
    }

    private void whenSeleccionoBotonRendirseSeLeResteLaApuestaAlJugadorYYLaPartidaPasaAEstadoAbandonado(Partida partidaActiva, Jugador jugador) throws ApuestaInvalidaException, SaldoInsuficiente {
        servicioPartida.rendirse(partidaActiva, jugador);
    }

    //--PEDIR CARTA
    @Test
    public void queAlSeleccionarElBotonPedirCartaConMenosDe21PUntosSeLeAgregueUnaCartaAlDTOYSeRetorne(){
        Jugador j = new Jugador();
        j.setPuntaje(15);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> cartaMock = Map.of("code", "AH", "value", "ACE");
        list.add(cartaMock);
        ComienzoCartasDTO dto = new ComienzoCartasDTO();
        dto.setDeckId("test-deck");

        when(servicioDeckOfCards.sacarCartas(anyString(), anyInt())).thenReturn(list);

        servicioPartida.pedirCarta(j, dto);

        assertNotNull(dto.getCartasJugador());
        assertEquals(1, dto.getCartasJugador().size());
    }
    @Test
    public void queAlSeleccionarElBotonPedirCartaConMenosDe21SeLeAsigneElNuevoPuntajeAlJugador(){
        Jugador j = new Jugador();

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> cartaMock = Map.of("value", "3");
        list.add(cartaMock);
        ComienzoCartasDTO dto = new ComienzoCartasDTO();
        dto.setDeckId("test-deck");


        when(servicioDeckOfCards.sacarCartas(anyString(), anyInt())).thenReturn(list);

        servicioPartida.pedirCarta(j, dto);


        assertEquals(3, dto.getPuntajeJugador());
        assertEquals(3, j.getPuntaje());
    }




    @Test
    public void queAlSeleccionarElBotonDividirPartidaSeCreeMano1YMano2YSeResteSaldo() throws Exception {
        Usuario usuario = givenExisteUnUsuario();
        Partida partida = givenComienzaUnaPartida(usuario);

        givenElRepositorioEncuentraAlUsuario(usuario);

        Map<String, Object> carta1 = new HashMap<>();
        carta1.put("value", "8");
        carta1.put("suit", "HEARTS");
        carta1.put("image", "carta1.png");

        Map<String, Object> carta2 = new HashMap<>();
        carta2.put("value", "8");
        carta2.put("suit", "SPADES");
        carta2.put("image", "carta2.png");

        List<Map<String, Object>> cartasJugador = Arrays.asList(carta1, carta2);
        whenElUsuarioDivideLaPartida(partida, cartasJugador);
        thenLaPartidaSeDivideYSeRestaSaldo(partida, cartasJugador);
    }

    private void givenElRepositorioEncuentraAlUsuario(Usuario usuario) {
        when(repositorioUsuario.buscar(anyString())).thenReturn(usuario);
    }

    private void whenElUsuarioDivideLaPartida(Partida partida, List<Map<String, Object>> cartasJugador) throws Exception {
        servicioPartida.dividirPartida(partida, cartasJugador);
    }

    private void thenLaPartidaSeDivideYSeRestaSaldo(Partida partida, List<Map<String, Object>> cartasJugador) {
        assertTrue(partida.getManoDividida());
        assertEquals(1, partida.getMano1().size());
        assertEquals(1, partida.getMano2().size());
        assertNotEquals(partida.getPuntajeMano1(), 0);
        assertNotEquals(partida.getPuntajeMano2(), 0);
}
    //------------------------------------------------------------------------------


    private static @NotNull Usuario givenExisteUnUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail("valen@gmail.com");
        return usuario;
    }


    private @NotNull Partida givenComienzaUnaPartida(Usuario usuario) {
        Partida partidaActiva = new Partida();
        partidaActiva.setEstadoPartida(EstadoPartida.ACTIVA);
        partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.APUESTA);
        Crupier crupier= new Crupier();
        Jugador jugador = new Jugador();
        jugador.setUsuario(usuario);
        usuario.setSaldo(1000);
        crupier.setPuntaje(7);
        jugador.setPuntaje(10);
        partidaActiva.setApuesta(100);
        partidaActiva.setJugador(jugador);
        partidaActiva.setCrupier(crupier);
        return partidaActiva;
    }



}