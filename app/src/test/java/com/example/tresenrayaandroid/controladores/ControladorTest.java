package com.example.tresenrayaandroid.controladores;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.tresenrayaandroid.modelos.Juego;

import org.junit.Test;

public class ControladorTest {

    private static void jugar(ControladorTablero tablero, ControladorPartida partida, int[][] movs) {
        for (int[] m : movs) {
            assertTrue(tablero.realizarMovimiento(m[0], m[1]));
            if (!partida.verificarGanador() && !partida.esEmpate()) {
                partida.cambiarTurno();
            }
        }
    }

    @Test
    public void tableroRechazaMovimientosTrasFinDelJuego() {
        Juego juego = new Juego();
        ControladorTablero tablero = new ControladorTablero(juego);
        ControladorPartida partida = new ControladorPartida(juego);
        jugar(tablero, partida, new int[][]{{0, 0}, {1, 0}, {0, 1}, {1, 1}, {0, 2}});
        assertTrue(juego.estaFinalizado());
        assertFalse(tablero.realizarMovimiento(2, 2));
        assertEquals(1, partida.obtenerPuntuacionX());
        assertEquals(0, partida.obtenerPuntuacionO());
    }

    @Test
    public void empateNoSumaPuntos() {
        Juego juego = new Juego();
        ControladorTablero tablero = new ControladorTablero(juego);
        ControladorPartida partida = new ControladorPartida(juego);
        jugar(tablero, partida, new int[][]{
                {0, 0}, {0, 1}, {0, 2}, {1, 1}, {1, 0}, {1, 2}, {2, 1}, {2, 0}, {2, 2}});
        assertTrue(partida.esEmpate());
        assertEquals(0, partida.obtenerPuntuacionX());
        assertEquals(0, partida.obtenerPuntuacionO());
    }

    @Test
    public void reiniciarConservaPuntuacionYPermiteJugar() {
        Juego juego = new Juego();
        ControladorTablero tablero = new ControladorTablero(juego);
        ControladorPartida partida = new ControladorPartida(juego);
        jugar(tablero, partida, new int[][]{{0, 0}, {1, 0}, {0, 1}, {1, 1}, {0, 2}});
        partida.reiniciar();
        assertEquals(1, partida.obtenerPuntuacionX());
        assertEquals("X", tablero.obtenerSimboloActual());
        assertTrue(tablero.realizarMovimiento(0, 0));
    }

    @Test
    public void ganaOSumaPuntoAlJugadorO() {
        Juego juego = new Juego();
        ControladorTablero tablero = new ControladorTablero(juego);
        ControladorPartida partida = new ControladorPartida(juego);
        // X: (0,0) (0,1) (2,2) ; O: (1,0) (1,1) (1,2)
        jugar(tablero, partida, new int[][]{{0, 0}, {1, 0}, {0, 1}, {1, 1}, {2, 2}, {1, 2}});
        assertEquals(0, partida.obtenerPuntuacionX());
        assertEquals(1, partida.obtenerPuntuacionO());
    }
}
