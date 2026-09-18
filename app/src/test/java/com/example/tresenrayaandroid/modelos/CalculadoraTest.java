package com.example.tresenrayaandroid.modelos;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;

public class CalculadoraTest {

    private final Calculadora calculadora = new Calculadora();

    private static final int[][][] LINEAS = {
            {{0, 0}, {0, 1}, {0, 2}}, {{1, 0}, {1, 1}, {1, 2}}, {{2, 0}, {2, 1}, {2, 2}}, // filas
            {{0, 0}, {1, 0}, {2, 0}}, {{0, 1}, {1, 1}, {2, 1}}, {{0, 2}, {1, 2}, {2, 2}}, // columnas
            {{0, 0}, {1, 1}, {2, 2}}, {{0, 2}, {1, 1}, {2, 0}}                            // diagonales
    };

    @Test
    public void tableroVacioNoTieneGanador() {
        assertFalse(calculadora.calcularGanador(new Ficha[3][3]));
    }

    @Test
    public void detectaLasOchoLineasParaAmbasFichas() {
        for (Ficha ficha : Ficha.values()) {
            for (int[][] linea : LINEAS) {
                Ficha[][] f = new Ficha[3][3];
                for (int[] c : linea) {
                    f[c[0]][c[1]] = ficha;
                }
                assertTrue("Debe ganar " + ficha + " en " + Arrays.deepToString(linea),
                        calculadora.calcularGanador(f));
            }
        }
    }

    @Test
    public void dosEnRayaNoEsVictoria() {
        for (int[][] linea : LINEAS) {
            Ficha[][] f = new Ficha[3][3];
            f[linea[0][0]][linea[0][1]] = Ficha.X;
            f[linea[1][0]][linea[1][1]] = Ficha.X;
            assertFalse(calculadora.calcularGanador(f));
        }
    }

    @Test
    public void lineaMezclada_noEsVictoria() {
        Ficha[][] f = new Ficha[3][3];
        f[0][0] = Ficha.X;
        f[0][1] = Ficha.O;
        f[0][2] = Ficha.X;
        assertFalse(calculadora.calcularGanador(f));
    }

    @Test
    public void tableroLlenoSinLineaNoTieneGanador() {
        // X O X
        // X O O
        // O X X
        Ficha[][] f = {
                {Ficha.X, Ficha.O, Ficha.X},
                {Ficha.X, Ficha.O, Ficha.O},
                {Ficha.O, Ficha.X, Ficha.X}
        };
        assertFalse(calculadora.calcularGanador(f));
    }
}
