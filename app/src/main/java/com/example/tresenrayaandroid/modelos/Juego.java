package com.example.tresenrayaandroid.modelos;

/**
 * CLASE JUEGO (MODELO)
 * 
 * Es la clase principal de la capa de Modelo. Actúa como el "Director" que
 * orquesta todas las piezas lógicas (Tablero, Jugadores, Reglas) para
 * gestionar el estado de una partida de Tres en Raya.
 */
public class Juego {

    // --- Atributos ---
    private final Tablero tablero;       // El tablero físico del juego
    private final Jugador jugadorX;      // Representación del Jugador X
    private final Jugador jugadorO;      // Representación del Jugador O
    private Jugador jugadorActual;       // Referencia al jugador que tiene el turno
    private final Reglas reglas;         // Las normas que validan el juego
    private boolean finalizado;          // Estado que indica si la partida ha terminado

    /**
     * Constructor: Inicializa una nueva partida con todos sus componentes.
     * Por defecto, el Jugador X siempre comienza el juego.
     */
    public Juego() {
        this.tablero = new Tablero();
        this.jugadorX = new Jugador(Ficha.X);
        this.jugadorO = new Jugador(Ficha.O);
        this.jugadorActual = jugadorX;
        this.reglas = new Reglas();
        this.finalizado = false;
    }

    // --- Métodos de Estado ---

    /**
     * Informa si la partida ha llegado a su fin.
     * @return true si hay un ganador o el tablero está lleno.
     */
    public boolean estaFinalizado() {
        return finalizado;
    }

    /**
     * Devuelve el objeto del jugador que debe mover actualmente.
     * @return Instancia de Jugador (X u O).
     */
    public Jugador obtenerJugadorActual() {
        return jugadorActual;
    }

    // --- Métodos de Acción Lógica ---

    /**
     * Intenta ejecutar un movimiento completo en el modelo.
     * 0. Rechaza cualquier movimiento si la partida ya terminó.
     * 1. Valida el movimiento según las reglas.
     * 2. Coloca la ficha si es válido.
     * 3. Comprueba si el movimiento termina la partida (victoria o empate).
     * 
     * @param fila Fila del tablero.
     * @param columna Columna del tablero.
     * @return true si el movimiento fue legal y se procesó.
     */
    public boolean realizarMovimiento(int fila, int columna) {
        if (finalizado) {
            return false;
        }
        if (reglas.esMovimientoValido(tablero, fila, columna)) {
            // Colocamos la ficha del jugador actual en el tablero
            tablero.colocarFicha(fila, columna, jugadorActual.obtenerFicha());
            
            // Verificamos si este movimiento causa el fin del juego
            if (reglas.es3EnRaya(tablero) || tablero.estaLleno()) {
                finalizado = true;
            }
            return true;
        }
        return false;
    }

    /**
     * Alterna el turno entre el Jugador X y el Jugador O.
     */
    public void cambiarTurno() {
        jugadorActual = (jugadorActual == jugadorX) ? jugadorO : jugadorX;
    }

    /**
     * Restablece todo el modelo a su estado inicial para una nueva partida.
     */
    public void reiniciar() {
        tablero.reiniciar();
        jugadorActual = jugadorX;
        finalizado = false;
    }

    // --- Métodos de Consulta ---

    /**
     * Consulta a las reglas si existe un ganador en el tablero actual.
     * @return true si alguien ha conseguido tres en raya.
     */
    public boolean hayGanador() {
        return reglas.es3EnRaya(tablero);
    }

    /**
     * Consulta al tablero si ya no quedan espacios vacíos.
     * @return true si no se pueden realizar más movimientos.
     */
    public boolean estaLleno() {
        return tablero.estaLleno();
    }

    // --- Persistencia del estado (rotación de pantalla, etc.) ---

    /**
     * Devuelve la ficha situada en una celda (null si está vacía).
     */
    public Ficha obtenerFicha(int fila, int columna) {
        return tablero.obtenerFicha(fila, columna);
    }

    /**
     * Serializa el tablero en 9 caracteres (fila a fila): 'X', 'O' o '-' si está vacía.
     */
    public String serializarTablero() {
        StringBuilder sb = new StringBuilder(9);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Ficha f = tablero.obtenerFicha(i, j);
                sb.append(f == null ? '-' : (f == Ficha.X ? 'X' : 'O'));
            }
        }
        return sb.toString();
    }

    /**
     * Restaura el estado a partir de un tablero serializado y del jugador con el turno.
     * Recalcula si la partida está finalizada. Si los datos no son válidos, no modifica nada.
     *
     * @param tableroSerializado cadena de 9 caracteres ('X', 'O' o '-').
     * @param turno ficha del jugador al que le corresponde mover.
     * @return true si el estado se restauró.
     */
    public boolean restaurarEstado(String tableroSerializado, Ficha turno) {
        if (tableroSerializado == null || tableroSerializado.length() != 9 || turno == null) {
            return false;
        }
        Ficha[] celdas = new Ficha[9];
        for (int k = 0; k < 9; k++) {
            char c = tableroSerializado.charAt(k);
            if (c == 'X') {
                celdas[k] = Ficha.X;
            } else if (c == 'O') {
                celdas[k] = Ficha.O;
            } else if (c != '-') {
                return false;
            }
        }
        tablero.reiniciar();
        for (int k = 0; k < 9; k++) {
            if (celdas[k] != null) {
                tablero.colocarFicha(k / 3, k % 3, celdas[k]);
            }
        }
        jugadorActual = (turno == Ficha.X) ? jugadorX : jugadorO;
        finalizado = reglas.es3EnRaya(tablero) || tablero.estaLleno();
        return true;
    }
}
