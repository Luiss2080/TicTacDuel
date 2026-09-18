package com.example.tresenrayaandroid.modelos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TableroTest {

    @Test
    public void nuevoTableroEstaVacioYNoLleno() {
        Tablero t = new Tablero();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertNull(t.obtenerFicha(i, j));
            }
        }
        assertFalse(t.estaLleno());
    }

    @Test
    public void colocarFichaEnCeldaVaciaFunciona() {
        Tablero t = new Tablero();
        assertTrue(t.colocarFicha(1, 2, Ficha.O));
        assertEquals(Ficha.O, t.obtenerFicha(1, 2));
    }

    @Test
    public void colocarFichaEnCeldaOcupadaNoSobrescribe() {
        Tablero t = new Tablero();
        t.colocarFicha(0, 0, Ficha.X);
        assertFalse(t.colocarFicha(0, 0, Ficha.O));
        assertEquals(Ficha.X, t.obtenerFicha(0, 0));
    }

    @Test
    public void estaLlenoSoloConLas9CeldasOcupadas() {
        Tablero t = new Tablero();
        for (int k = 0; k < 8; k++) {
            t.colocarFicha(k / 3, k % 3, Ficha.X);
            assertFalse(t.estaLleno());
        }
        t.colocarFicha(2, 2, Ficha.O);
        assertTrue(t.estaLleno());
    }

    @Test
    public void reiniciarVaciaElTablero() {
        Tablero t = new Tablero();
        t.colocarFicha(1, 1, Ficha.X);
        t.reiniciar();
        assertNull(t.obtenerFicha(1, 1));
        assertFalse(t.estaLleno());
    }
}
