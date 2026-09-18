package com.example.tresenrayaandroid.modelos;

/**
 * CLASE REGLAS (MODELO)
 * 
 * Define las normas y condiciones del juego.
 * Utiliza la Calculadora para delegar la comprobación de victoria.
 */
public class Reglas {

    // --- Atributos ---
    private final Calculadora calculadora; // Motor de cálculo lógico

    /**
     * Constructor: Inicializa las reglas junto con su calculadora.
     */
    public Reglas() {
        this.calculadora = new Calculadora();
    }

    /**
     * Valida si un movimiento es permitido (posición dentro del tablero y celda vacía).
     * @param tablero Estado actual del tablero.
     * @param fila Fila del movimiento.
     * @param columna Columna del movimiento.
     * @return true si el movimiento cumple las reglas.
     */
    public boolean esMovimientoValido(Tablero tablero, int fila, int columna) {
        if (fila < 0 || fila > 2 || columna < 0 || columna > 2) {
            return false;
        }
        return tablero.obtenerFicha(fila, columna) == null;
    }

    /**
     * Determina si el estado actual del tablero representa una victoria.
     * @param tablero Tablero a evaluar.
     * @return true si se cumple el tres en raya.
     */
    public boolean es3EnRaya(Tablero tablero) {
        return calculadora.calcularGanador(tablero.obtenerFichas());
    }
}
