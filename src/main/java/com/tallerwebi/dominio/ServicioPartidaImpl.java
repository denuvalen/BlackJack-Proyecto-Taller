package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ApuestaInvalidaException;
import com.tallerwebi.dominio.excepcion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@Service
@Transactional
public class ServicioPartidaImpl implements ServicioPartida {

    private RepositorioPartida repositorioPartida;
    private ServicioUsuario servicioUsuario;
    private ServicioEstrategia servicioEstrategia;
    private ServicioDoblarApuesta servicioDoblarApuesta;

    private RepositorioUsuario repositorioUsuario;
    private RepositorioJugador repositorioJugador;
    private ServicioDeckOfCards servicioDeckOfCards;

    public ServicioPartidaImpl(RepositorioPartida repositorioPartida) {
        this.repositorioPartida = repositorioPartida;
    }

    public ServicioPartidaImpl() {
    }

    public ServicioPartidaImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    public ServicioPartidaImpl(ServicioUsuarioImpl servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @Autowired
    public ServicioPartidaImpl(ServicioDeckOfCards servicioDeckOfCards, RepositorioPartida respositorioPartida, RepositorioUsuario repositorioUsuario, RepositorioJugador repositorioJugador, ServicioUsuario servicioUsuario, ServicioEstrategia servicioEstrategia, ServicioDoblarApuesta servicioDoblarApuesta) {

        this.servicioDeckOfCards = servicioDeckOfCards;
        this.repositorioPartida = respositorioPartida;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioJugador = repositorioJugador;
        this.servicioUsuario = servicioUsuario;
        this.servicioEstrategia = servicioEstrategia;
        this.servicioDoblarApuesta = servicioDoblarApuesta;
    }

    public ServicioPartidaImpl(RepositorioPartida repositorioPartida, RepositorioJugador repositorioJugador) {
        this.repositorioJugador = repositorioJugador;
        this.repositorioPartida = repositorioPartida;
    }


    @Override
    public void consultarExistenciaDePartidaActiva(Usuario usuario) throws PartidaExistenteActivaException {
        List<Partida> partidasActivas = repositorioPartida.buscarPartidaActiva(usuario);
        if (!partidasActivas.isEmpty()) {
            throw new PartidaExistenteActivaException();
        }

    }

    @Override
    public void inactivarPartidas(List<Partida> partidasActivas) {
        for (Partida partidaActiva : partidasActivas) {
            partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.ABANDONADO);
            partidaActiva.setEstadoPartida(EstadoPartida.INACTIVA);
            repositorioPartida.guardar(partidaActiva);
        }
    }

    @Override
    public Jugador crearJugador(Usuario usuario) {
        Jugador jugador = new Jugador();
        jugador.setUsuario(usuario);
        repositorioJugador.guardar(jugador);
        return jugador;
    }


    @Override
    public void cambiarEstadoDeJuegoAJuegoDeUnaPartida(Partida p) throws PartidaActivaNoEnApuestaException {
        if (p.getEstadoJuego().equals(EstadoDeJuego.APUESTA)) {
            p.cambiarEstadoDeJuego(EstadoDeJuego.JUEGO);
        } else {
            throw new PartidaActivaNoEnApuestaException();
        }

    }

    @Override
    public List<Partida> buscarPartidaActiva(Usuario usuario) {
        return repositorioPartida.buscarPartidaActiva(usuario);
    }

    @Override
    public ComienzoCartasDTO repartoInicial(Long id) {
        ComienzoCartasDTO dto = new ComienzoCartasDTO();
        Partida partida = repositorioPartida.buscarPartidaPorId(id);
        if (partida != null) {
            List<Map<String, Object>> cartasJugador;
            List<Map<String, Object>> cartasDealer;
            var mazo = servicioDeckOfCards.crearMazo();
            String deckId = (String) mazo.get("deck_id");

            cartasJugador = servicioDeckOfCards.sacarCartas(deckId, 2);
            cartasDealer = servicioDeckOfCards.sacarCartas(deckId, 1);
            int puntajeJugador = calcularPuntaje(cartasJugador);
            int puntajeDealer = calcularPuntaje(cartasDealer);
            partida.getJugador().setPuntaje(puntajeJugador);
            partida.getCrupier().setPuntaje(puntajeDealer);

            dto.setJugadorSePlanto(false);
            dto.setPartida(partida);
            dto.setDeckId(deckId);
            dto.setCartasJugador(cartasJugador);
            dto.setCartasDealer(cartasDealer);
            dto.setPuntajeJugador(puntajeJugador);
            dto.setPuntajeDealer(puntajeDealer);
            return dto;
        }

        return dto;
    }

    public void bloquearBotones(Partida partida) {
        partida.setEstadoPartida(EstadoPartida.INACTIVA);
        partida.cambiarEstadoDeJuego(EstadoDeJuego.FINALIZADA);
        partida.setBotonesDesicionHabilitados(false);
        partida.setFichasHabilitadas(false);

    }

    private void corroborarExistenciaDePartidaActiva(Usuario usuario) {
        List<Partida> partidasActivas = repositorioPartida.buscarPartidaActiva(usuario);
        if (!partidasActivas.isEmpty()) {
            for (Partida partidaActiva : partidasActivas) {
                partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.ABANDONADO);
                partidaActiva.setEstadoPartida(EstadoPartida.INACTIVA);
            }
        }
    }


    @Override
    public String mandarEstrategia(List<Map<String, Object>> cartasJugador, Partida partidaActiva, Integer jugadorPuntaje, Integer crupierPuntaje) {
        partidaActiva.setBotonEstrategia(false);
        return servicioEstrategia.recomendar(cartasJugador ,jugadorPuntaje, crupierPuntaje);
    }



        @Override
        public void doblarApuesta(Partida partidaActiva, Usuario usuario, List<Map<String, Object>> cartasJugador) throws SaldoInsuficiente, UnaCantidadDeCartasSuperadaException, PartidaDividaException {
            servicioDoblarApuesta.validarApuesta(usuario.getSaldo(), partidaActiva.getApuesta());
            servicioDoblarApuesta.validarCantidadDeCartas(cartasJugador);
            servicioDoblarApuesta.validarPartidaDividida(partidaActiva.getManoDividida());

            doblarApuestaPartida(partidaActiva ,servicioDoblarApuesta.calcularDoblDeApuesta(partidaActiva.getApuesta()));
            setearApuesta(usuario, partidaActiva.getApuesta());



        }

    private void doblarApuestaPartida(Partida partidaActiva, int i) {
        partidaActiva.setApuesta(i);
        repositorioPartida.actualizar(partidaActiva);
    }

    @Override
    public Partida obtenerPartidaActiva(Usuario usuario) {
        List<Partida> partidas = repositorioPartida.buscarPartidaActiva(usuario);
        if (!partidas.isEmpty()) {
            return partidas.get(0);
        }
        return null;
    }


    @Override
    public void rendirse(Partida partidaActiva, Jugador jugador) {
        partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.ABANDONADO);
        partidaActiva.setEstadoPartida(EstadoPartida.INACTIVA);
        repositorioPartida.actualizar(partidaActiva);
    }


    @Override
    public void apostar(Partida partida, Integer monto) throws ApuestaInvalidaException, SaldoInsuficiente {

        Usuario usuario = partida.getJugador().getUsuario();
        Usuario u = repositorioUsuario.buscar(usuario.getEmail());
        Integer saldoActual = u.getSaldo();


        if (saldoActual == null || saldoActual < monto) {
            throw new SaldoInsuficiente("Saldo insuficiente para realizar la apuesta.");
        }


        if (u != null) {
            setearApuesta(u, monto);
            partida.getJugador().getUsuario().setSaldo(u.getSaldo());
        }

        if (partida.getApuesta() == null) {
            partida.setApuesta(0);
        }
        partida.setApuesta(partida.getApuesta() + monto);

    }

    @Override
    public void setearApuesta(Usuario usuario, Integer apuesta) {
        servicioUsuario.actualizarSaldoDeUsuario(usuario, apuesta);
    }


    @Override
    public void setBotonesAlCrearPartida(Partida partida) {

        if (partida.getEstadoJuego().equals(EstadoDeJuego.APUESTA)) {
            partida.setBotonesDesicionHabilitados(false);
            partida.setFichasHabilitadas(true);
        } else if (partida.getEstadoJuego().equals(EstadoDeJuego.JUEGO)) {
            partida.setBotonesDesicionHabilitados(true);
            partida.setBotonEstrategia(true);
            partida.setBotonDoblarApuesta(true);
            partida.setFichasHabilitadas(false);
        } else if (partida.getEstadoJuego().equals(EstadoDeJuego.FINALIZADA) || partida.getEstadoJuego().equals(EstadoDeJuego.ABANDONADO)) {
            partida.setBotonesDesicionHabilitados(false);
            partida.setFichasHabilitadas(false);
        }
    }

    public Partida instanciarPartida(Jugador jugador) throws PartidaNoCreadaException {
        Partida partida = new Partida();
        Crupier crupier = new Crupier();
        partida.setCrupier(crupier);
        partida.setJugador(jugador);
        partida.setBotonEmpezar(false);
        partida.setEstadoPartida(EstadoPartida.ACTIVA);
        partida.cambiarEstadoDeJuego(EstadoDeJuego.APUESTA);
        if (isNull(partida)) {
            throw new PartidaNoCreadaException();
        }
        return repositorioPartida.guardar(partida);
    }


    @Override
    public int calcularPuntaje(List<Map<String, Object>> cartas) {
        int total = 0;
        int ases = 0;

        for (Map<String, Object> carta : cartas) {
            String valor = (String) carta.get("value");
            switch (valor) {
                case "KING":
                case "QUEEN":
                case "JACK":
                    total += 10;
                    break;
                case "ACE":
                    total += 11;
                    ases++;
                    break;
                default:
                    total += Integer.parseInt(valor);
            }
        }
        while (total > 21 && ases > 0) {
            total -= 10;
            ases--;

        }


        return total;
    }



        @Override
        public void dividirPartida (Partida partida, List < Map < String, Object >> cartasJugador) throws
        NoSePuedenDividirMasDeDosCartasException, NoSePuedenDividirDosCartasDistintasException, ApuestaInvalidaException, SaldoInsuficiente
        {
            Jugador jugador = partida.getJugador();

            if (cartasJugador == null || cartasJugador.size() != 2) {
                throw new NoSePuedenDividirMasDeDosCartasException("No se puede dividir: el jugador no tiene exactamente dos cartas.");
            }

            String valor1 = (String) cartasJugador.get(0).get("value");
            String valor2 = (String) cartasJugador.get(1).get("value");

            if (!valor1.equals(valor2)) {
                throw new NoSePuedenDividirDosCartasDistintasException("No se puede dividir: las cartas deben tener el mismo valor.");
            }


            this.apostar(partida, partida.getApuesta());


            List<Map<String, Object>> mano1 = new ArrayList<>();
            List<Map<String, Object>> mano2 = new ArrayList<>();

            mano1.add(cartasJugador.get(0));
            mano2.add(cartasJugador.get(1));

            partida.setMano1(mano1);
            partida.setMano2(mano2);
            partida.setManoDividida(true);
            partida.setPuntajeMano1(calcularPuntaje(mano1));
            partida.setPuntajeMano2(calcularPuntaje(mano2));
        }


        @Override
        public String determinarResultado (Partida partida, ComienzoCartasDTO
        dto, List < Map < String, Object >> cartasJugador){
            int puntajeFinalJugador = calcularPuntaje(cartasJugador);
            partida.getJugador().setPuntaje(puntajeFinalJugador);
            int puntajeCrupier = partida.getCrupier().getPuntaje();
            dto.setJugadorSePlanto(true);

            if (partida.getManoDividida()) {
                return determinarResultadoDividido(partida);
            }
            return resultadoDeLaPartida(partida, puntajeCrupier, puntajeFinalJugador);
        }

        public String determinarResultadoDividido (Partida partida){
            int puntajeCrupier = partida.getCrupier().getPuntaje();
            int factorMano1 = calcularFactorPago(partida.getPuntajeMano1(), puntajeCrupier);
            int factorMano2 = calcularFactorPago(partida.getPuntajeMano2(), puntajeCrupier);

            Integer apuestaPorMano = partida.getApuesta() / 2;
            Integer pagoTotalAcumulado = (apuestaPorMano * factorMano1) +
                    (apuestaPorMano * factorMano2);

            String resMano1 = (factorMano1 == 2) ? "Ganó mano 1. " : (factorMano1 == 1) ? "Empate mano 1. " : "Perdió mano 1. ";
            String resMano2 = (factorMano2 == 2) ? "Ganó mano 2. " : (factorMano2 == 1) ? "Empate mano 2. " : "Perdió mano 2. ";
            String resultado = resMano1 + resMano2;

            if (resMano1.equalsIgnoreCase("Ganó mano 1. ") || resMano1.equalsIgnoreCase("Ganó mano 2. ")) {
                partida.setResultadoPartida(ResultadoPartida.GANO);
            } else if (resMano1.equalsIgnoreCase("Perdió mano 1. ") || resMano1.equalsIgnoreCase("Perdió mano 2. ")) {
                partida.setResultadoPartida(ResultadoPartida.PERDIO);
            }
            if (pagoTotalAcumulado > 0) {
                Usuario usuarioDeDB = repositorioUsuario.buscar(partida.getJugador().getUsuario().getEmail());
                Integer saldoActual = usuarioDeDB.getSaldo();
                Integer nuevoSaldo = saldoActual + pagoTotalAcumulado;

                usuarioDeDB.setSaldo(nuevoSaldo);
                repositorioUsuario.actualizar(usuarioDeDB);
                partida.getJugador().getUsuario().setSaldo(nuevoSaldo);
            }

            return resultado;
        }


        private int calcularFactorPago ( int puntajeJugador, int puntajeCrupier){
            if (puntajeJugador > 21) {
                return 0;
            }
            if (puntajeCrupier > 21) {
                return 2;
            }
            if (puntajeJugador > puntajeCrupier) {
                return 2;
            }
            if (puntajeJugador == puntajeCrupier) {
                return 1;
            }
            return 0;
        }

        @Override
        public String resultadoDeLaPartida (Partida partida, Integer puntosCrupier, Integer puntosJugador){
            if (puntosJugador > 21) {
                partida.setResultadoPartida(ResultadoPartida.PERDIO);
                servicioUsuario.actualizarManosGanadas(partida.getJugador().getUsuario(), false);
                return "Resultado: Superaste los 21, Crupier gana";
            }
            if (puntosCrupier > 21) {
                realizarPagoDeApuesta(partida, 2.0);
                partida.setResultadoPartida(ResultadoPartida.GANO);
                servicioUsuario.actualizarManosGanadas(partida.getJugador().getUsuario(), true);
                return "Resultado: El crupier se paso de 21, Jugador gana";
            }
            if (puntosJugador > puntosCrupier) {
                partida.setResultadoPartida(ResultadoPartida.GANO);
                realizarPagoDeApuesta(partida, 2.0);
                servicioUsuario.actualizarManosGanadas(partida.getJugador().getUsuario(), true);
                return "Resultado: Jugador gana";
            }
            if (puntosCrupier > puntosJugador) {
                partida.setResultadoPartida(ResultadoPartida.PERDIO);
                servicioUsuario.actualizarManosGanadas(partida.getJugador().getUsuario(), false);
                return "Resultado: Crupier gana";
            }
            realizarPagoDeApuesta(partida, 1.0);

            servicioUsuario.actualizarManosGanadas(partida.getJugador().getUsuario(), false);
            return "Resultado: Empate. Se devuelve la apuesta.";
        }

        private void realizarPagoDeApuesta (Partida partida, Double factorDeApuesta){
            String email = partida.getJugador().getUsuario().getEmail();
            Usuario usuario = repositorioUsuario.buscar(email);
            Integer saldo = calcularNuevoSaldo(usuario, partida, factorDeApuesta);
            partida.getJugador().getUsuario().setSaldo(saldo);
            servicioUsuario.incrementarSaldoDeGanador(saldo, usuario);
        }


        @Override
        public void entregarCartaAlCrupier (Partida
        partida, List < Map < String, Object >> cartasDealer, String deckId){

            List<Map<String, Object>> nuevaCarta = servicioDeckOfCards.sacarCartas(deckId, 1);
            cartasDealer.add(nuevaCarta.get(0));
            int puntajeCrupier = calcularPuntaje(cartasDealer);
            partida.getCrupier().setPuntaje(puntajeCrupier);
            repositorioPartida.actualizar(partida);

            while (puntajeCrupier <= 16) {
                nuevaCarta = servicioDeckOfCards.sacarCartas(deckId, 1);
                cartasDealer.add(nuevaCarta.get(0));
                puntajeCrupier = calcularPuntaje(cartasDealer);
                partida.getCrupier().setPuntaje(puntajeCrupier);
                repositorioPartida.actualizar(partida);
            }
        }

    @Override
    public String bloquearDoblarApuesta(Partida partida) {
        partida.setBotonDoblarApuesta(false);
    return "No puede doblar la apuesta";
    }


//    @Override
//        public String verficarPuntaje (Partida partida,int puntajeJugador){
//            String mensaje = "Superaste los 21, el crupier gana.";
//            if (puntajeJugador > 21) {
//                bloquearBotones(partida);
//                partida.setEstadoPartida(EstadoPartida.INACTIVA);
//                repositorioPartida.guardar(partida);
//                return mensaje;
//            }
//            return null;
//
//        }

    @Override
    public String verficarPuntaje (Partida p, ComienzoCartasDTO dto){
        if (dto.getPuntajeJugador() >= 21) {
          String resultado =  gestionarTurnoPararse(p, dto, dto.getCartasDealer(), dto.getCartasJugador(), dto.getDeckId());
          return resultado;
        }
        return null;
    }

        @Override
        public String gestionarTurnoPararse (
                Partida partida,
                ComienzoCartasDTO dto,
                List < Map < String, Object >> cartasDealer,
                List < Map < String, Object >> cartasJugador,
                String deckId
    ){
            entregarCartaAlCrupier(partida, cartasDealer, deckId);
            String mensajeResultado = determinarResultado(partida, dto, cartasJugador);
            dto.setPuntajeDealer(partida.getCrupier().getPuntaje());
            bloquearBotones(partida);
            partida.setEstadoPartida(EstadoPartida.INACTIVA);
            Usuario usuario = partida.getJugador().getUsuario();
            servicioUsuario.registrarResultado(usuario, mensajeResultado);
            servicioUsuario.actualizarLogros(usuario);

            repositorioPartida.actualizar(partida);
            repositorioUsuario.actualizar(usuario);

            return mensajeResultado;
        }


        @Override
        public Map<String, Object>  pedirCarta (Jugador jugador, ComienzoCartasDTO dto){
            List<Map<String, Object>> cartasJugador = dto.getCartasJugador();
            String deckId = dto.getDeckId();

            if (jugador.getPuntaje() < 21) {
                List<Map<String, Object>> nuevaCarta = servicioDeckOfCards.sacarCartas(deckId, 1);
                cartasJugador.add(nuevaCarta.get(0));

                int puntajeJugador = calcularPuntaje(cartasJugador);
                jugador.setPuntaje(puntajeJugador);

                dto.setPuntajeJugador(puntajeJugador);
               return cartasJugador.get(0);
            }
            return null;
        }


        @Override
        public void logicaBotonDividir (Partida partida, List < Map < String, Object >> cartasJugador, ComienzoCartasDTO
        dto){
            if (cartasJugador == null || cartasJugador.size() != 2 || dto.getBotonDividir() == true) {
                dto.setBotonDividir(false);
                return;
            }

            String valor1 = (String) cartasJugador.get(0).get("value");
            String valor2 = (String) cartasJugador.get(1).get("value");

            String valorCarta1 = obtenerValorDeCartasEspeciales(valor1);
            String valorCarta2 = obtenerValorDeCartasEspeciales(valor2);


            double saldo = partida.getJugador().getUsuario().getSaldo();
            double apuesta = partida.getApuesta();

            if (valorCarta1.equals(valorCarta2) && saldo >= apuesta) {
                dto.setBotonDividir(true);
            } else {
                dto.setBotonDividir(false);
            }
        }

    private String obtenerValorDeCartasEspeciales(String valor) {
        if (valor.equals("KING") || valor.equals("QUEEN") || valor.equals("JACK")) {
            return "10";
        }
        return valor;
    }


    private Integer calcularNuevoSaldo (Usuario usuario, Partida partida, Double factorDeApuesta){
            Integer nuevoSaldo = (int) ((Integer) usuario.getSaldo() + partida.getApuesta() * factorDeApuesta);
            return nuevoSaldo;
        }


        public void setServicioDeckOfCards (ServicioDeckOfCards servicioDeckOfCards){
            this.servicioDeckOfCards = servicioDeckOfCards;

        }

    }