package com.example.tresenrayaandroid.modelos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JuegoTest {

    /** Juega una secuencia como lo hace la vista: mover y, si la partida sigue, cambiar el turno. */
    private static void jugar(Juego j, int[][] movs) {
        for (int[] m : movs) {
            assertTrue(j.realizarMovimiento(m[0], m[1]));
            if (!j.estaFinalizado()) {
                j.cambiarTurno();
            }
        }
    }

    @Test
    public void empiezaXYNoEstaFinalizado() {
        Juego j = new Juego();
        assertEquals(Ficha.X, j.obtenerJugadorActual().obtenerFicha());
        assertFalse(j.estaFinalizado());
        assertFalse(j.hayGanador());
        assertFalse(j.estaLleno());
    }

    @Test
    public void celdaOcupadaSeRechazaYNoFinalizaLaPartida() {
        Juego j = new Juego();
        assertTrue(j.realizarMovimiento(1, 1));
        j.cambiarTurno();
        assertFalse(j.realizarMovimiento(1, 1));
        assertFalse(j.estaFinalizado());
    }

    @Test
    public void victoriaDeXFinalizaLaPartida() {
        Juego j = new Juego();
        // X: (0,0) (0,1) (0,2) ; O: (1,0) (1,1)
        jugar(j, new int[][]{{0, 0}, {1, 0}, {0, 1}, {1, 1}, {0, 2}});
        assertTrue(j.hayGanador());
        assertTrue(j.estaFinalizado());
        assertEquals(Ficha.X, j.obtenerJugadorActual().obtenerFicha());
    }

    @Test
    public void empateSoloConTableroLlenoYSinGanador() {
        Juego j = new Juego();
        // X O X / X O O / O X X (turnos alternados validos)
        jugar(j, new int[][]{
                {0, 0}, {0, 1}, {0, 2}, {1, 1},
                {1, 0}, {1, 2}, {2, 1}, {2, 0}, {2, 2}});
        assertTrue(j.estaLleno());
        assertFalse(j.hayGanador());
        assertTrue(j.estaFinalizado());
    }

    @Test
    public void victoriaEnLaUltimaCeldaEsVictoriaYNoEmpate() {
        Juego j = new Juego();
        // X O X / O X O / O X X -> X gana con la diagonal en el 9o movimiento
        jugar(j, new int[][]{
                {0, 0}, {0, 1}, {0, 2}, {1, 0},
                {1, 1}, {1, 2}, {2, 1}, {2, 0}, {2, 2}});
        assertTrue(j.estaLleno());
        assertTrue(j.hayGanador());
    }

    @Test
    public void reiniciarRestauraEstadoInicial() {
        Juego j = new Juego();
        jugar(j, new int[][]{{0, 0}, {1, 0}, {0, 1}, {1, 1}, {0, 2}});
        j.reiniciar();
        assertFalse(j.estaFinalizado());
        assertFalse(j.hayGanador());
        assertFalse(j.estaLleno());
        assertEquals(Ficha.X, j.obtenerJugadorActual().obtenerFicha());
        assertTrue(j.realizarMovimiento(0, 0));
    }

    @Test
    public void cambiarTurnoAlternaXyO() {
        Juego j = new Juego();
        j.cambiarTurno();
        assertEquals(Ficha.O, j.obtenerJugadorActual().obtenerFicha());
        j.cambiarTurno();
        assertEquals(Ficha.X, j.obtenerJugadorActual().obtenerFicha());
    }
}
