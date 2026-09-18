package com.example.tresenrayaandroid.controladores;

import com.example.tresenrayaandroid.modelos.Ficha;
import com.example.tresenrayaandroid.modelos.Juego;

/**
 * CONTROLADOR ESPECIALIZADO: GESTIÓN DE LA PARTIDA
 * 
 * Esta clase se encarga de supervisar el flujo global del juego.
 * Responsabilidades:
 * - Controlar el cambio de turnos.
 * - Monitorear el estado de victoria o empate.
 * - Gestionar el marcador de puntuaciones.
 * - Coordinar el reinicio de la partida.
 */
public class ControladorPartida {

    // --- Atributos ---
    private final Juego modeloJuego;    // Referencia al modelo principal
    private int puntuacionX = 0;        // Contador histórico de victorias para X
    private int puntuacionO = 0;        // Contador histórico de victorias para O

    /**
     * Constructor: Vincula el controlador con la instancia del modelo de juego.
     * @param juego Instancia del modelo compartido.
     */
    public ControladorPartida(Juego juego) {
        this.modeloJuego = juego;
    }

    // --- Métodos de Acción y Gestión ---

    /**
     * Verifica si el último movimiento resultó en un ganador.
     * Si hay ganador, incrementa automáticamente la puntuación del jugador actual.
     * 
     * @return true si alguien ha ganado la partida.
     */
    public boolean verificarGanador() {
        boolean hayGanador = modeloJuego.hayGanador();
        
        if (hayGanador) {
            // Evaluamos quién ganó para actualizar su marcador
            if (modeloJuego.obtenerJugadorActual().obtenerFicha() == Ficha.X) {
                puntuacionX++;
            } else {
                puntuacionO++;
            }
        }
        return hayGanador;
    }

    /**
     * Comprueba si el tablero está lleno y no ha habido un ganador (Empate).
     */
    public boolean esEmpate() {
        return modeloJuego.estaLleno() && !modeloJuego.hayGanador();
    }

    /**
     * Solicita al modelo que alterne el turno entre los jugadores.
     */
    public void cambiarTurno() {
        modeloJuego.cambiarTurno();
    }

    /**
     * Limpia el estado del modelo para comenzar una nueva partida.
     */
    public void reiniciar() {
        modeloJuego.reiniciar();
    }

    // --- Métodos de Consulta (Getters) ---

    public int obtenerPuntuacionX() {
        return puntuacionX;
    }

    public int obtenerPuntuacionO() {
        return puntuacionO;
    }

    /**
     * Restaura el marcador (por ejemplo tras recrearse la actividad).
     */
    public void restaurarPuntuaciones(int puntuacionX, int puntuacionO) {
        this.puntuacionX = Math.max(0, puntuacionX);
        this.puntuacionO = Math.max(0, puntuacionO);
    }
}
