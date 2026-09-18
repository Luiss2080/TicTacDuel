package com.example.tresenrayaandroid.modelos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.tresenrayaandroid.controladores.ControladorPartida;

import org.junit.Test;

/** Serializacion/restauracion del estado, usada para sobrevivir a la rotacion de pantalla. */
public class JuegoEstadoTest {

    @Test
    public void tableroVacioSeSerializaConGuiones() {
        assertEquals("---------", new Juego().serializarTablero());
    }

    @Test
    public void restaurarReconstruyeTableroYTurno() {
        Juego j = new Juego();
        assertTrue(j.restaurarEstado("XO-------", Ficha.X));
        assertEquals(Ficha.X, j.obtenerFicha(0, 0));
        assertEquals(Ficha.O, j.obtenerFicha(0, 1));
        assertNull(j.obtenerFicha(0, 2));
        assertEquals(Ficha.X, j.obtenerJugadorActual().obtenerFicha());
        assertFalse(j.estaFinalizado());
        assertEquals("XO-------", j.serializarTablero());
    }

    @Test
    public void restaurarPartidaGanadaLaDejaFinalizada() {
        Juego j = new Juego();
        assertTrue(j.restaurarEstado("XXXOO----", Ficha.X));
        assertTrue(j.hayGanador());
        assertTrue(j.estaFinalizado());
    }

    @Test
    public void restaurarTableroLlenoSinGanadorEsEmpate() {
        Juego j = new Juego();
        assertTrue(j.restaurarEstado("XOXXOOOXX", Ficha.X));
        assertFalse(j.hayGanador());
        assertTrue(j.estaFinalizado());
    }

    @Test
    public void restaurarConDatosInvalidosNoModificaElEstado() {
        Juego j = new Juego();
        j.realizarMovimiento(1, 1);
        assertFalse(j.restaurarEstado(null, Ficha.X));
        assertFalse(j.restaurarEstado("XO", Ficha.X));
        assertFalse(j.restaurarEstado("XO?------", Ficha.X));
        assertFalse(j.restaurarEstado("---------", null));
        assertEquals("----X----", j.serializarTablero());
    }

    @Test
    public void idaYVueltaConservaLaPartidaEnCurso() {
        Juego origen = new Juego();
        origen.realizarMovimiento(0, 0);
        origen.cambiarTurno();
        origen.realizarMovimiento(1, 1);
        origen.cambiarTurno();

        Juego copia = new Juego();
        assertTrue(copia.restaurarEstado(origen.serializarTablero(),
                origen.obtenerJugadorActual().obtenerFicha()));
        assertEquals(origen.serializarTablero(), copia.serializarTablero());
        assertEquals(Ficha.X, copia.obtenerJugadorActual().obtenerFicha());
        assertFalse(copia.realizarMovimiento(1, 1));
        assertTrue(copia.realizarMovimiento(2, 2));
    }

    @Test
    public void puntuacionesSeRestauranYNoAdmitenNegativos() {
        ControladorPartida c = new ControladorPartida(new Juego());
        c.restaurarPuntuaciones(3, 2);
        assertEquals(3, c.obtenerPuntuacionX());
        assertEquals(2, c.obtenerPuntuacionO());
        c.restaurarPuntuaciones(-5, 0);
        assertEquals(0, c.obtenerPuntuacionX());
    }
}
