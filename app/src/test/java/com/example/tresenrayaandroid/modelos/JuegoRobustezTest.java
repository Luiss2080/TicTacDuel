package com.example.tresenrayaandroid.modelos;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** Comprueba que el propio modelo (no solo los controladores) protege sus invariantes. */
public class JuegoRobustezTest {

    @Test
    public void juegoRechazaMovimientosTrasVictoria() {
        Juego j = new Juego();
        int[][] movs = {{0, 0}, {1, 0}, {0, 1}, {1, 1}, {0, 2}};
        for (int[] m : movs) {
            assertTrue(j.realizarMovimiento(m[0], m[1]));
            if (!j.estaFinalizado()) {
                j.cambiarTurno();
            }
        }
        assertTrue(j.estaFinalizado());
        assertFalse(j.realizarMovimiento(2, 2));
        assertTrue(j.hayGanador());
    }

    @Test
    public void posicionesFueraDeRangoSeRechazanSinExcepcion() {
        Juego j = new Juego();
        assertFalse(j.realizarMovimiento(-1, 0));
        assertFalse(j.realizarMovimiento(0, -1));
        assertFalse(j.realizarMovimiento(3, 0));
        assertFalse(j.realizarMovimiento(0, 3));
        assertFalse(j.estaFinalizado());
    }

    @Test
    public void reglasRechazanFueraDeRango() {
        Reglas r = new Reglas();
        Tablero t = new Tablero();
        assertFalse(r.esMovimientoValido(t, 3, 3));
        assertFalse(r.esMovimientoValido(t, -1, 1));
        assertTrue(r.esMovimientoValido(t, 2, 2));
        assertNull(t.obtenerFicha(2, 2));
    }
}
