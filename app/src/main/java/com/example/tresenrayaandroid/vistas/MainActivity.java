package com.example.tresenrayaandroid.vistas;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.tresenrayaandroid.R;
import com.example.tresenrayaandroid.controladores.ControladorPartida;
import com.example.tresenrayaandroid.controladores.ControladorTablero;
import com.example.tresenrayaandroid.modelos.Ficha;
import com.example.tresenrayaandroid.modelos.Juego;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

/**
 * Clase MainActivity que actúa como la Vista principal en el patrón MVC.
 */
public class MainActivity extends AppCompatActivity {

    private static final String CLAVE_TABLERO = "tablero";
    private static final String CLAVE_TURNO = "turno";
    private static final String CLAVE_PUNTOS_X = "puntosX";
    private static final String CLAVE_PUNTOS_O = "puntosO";

    private Juego juego;
    private ControladorTablero controladorTablero;
    private ControladorPartida controladorPartida;

    private final MaterialButton[][] matrizBotones = new MaterialButton[3][3];
    private TextView textoEstado;
    private TextView textoPuntuacionX;
    private TextView textoPuntuacionO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        juego = new Juego();
        controladorTablero = new ControladorTablero(juego);
        controladorPartida = new ControladorPartida(juego);

        inicializarComponentes();
        configurarTablero();

        // Configurar botón reiniciar
        MaterialButton botonReiniciar = findViewById(R.id.reset_button);
        if (botonReiniciar != null) {
            botonReiniciar.setOnClickListener(v -> reiniciarJuego());
        }

        // Configurar botón nuevo juego
        MaterialButton botonNuevoJuego = findViewById(R.id.new_game_button);
        if (botonNuevoJuego != null) {
            botonNuevoJuego.setOnClickListener(v -> nuevoJuego());
        }

        // Tras una rotación (o recreación de la actividad) se recupera la partida en curso
        if (savedInstanceState == null || !restaurarEstado(savedInstanceState)) {
            actualizarMarcadores();
            actualizarEstadoTurno();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CLAVE_TABLERO, juego.serializarTablero());
        outState.putString(CLAVE_TURNO, controladorTablero.obtenerSimboloActual());
        outState.putInt(CLAVE_PUNTOS_X, controladorPartida.obtenerPuntuacionX());
        outState.putInt(CLAVE_PUNTOS_O, controladorPartida.obtenerPuntuacionO());
    }

    /**
     * Reconstruye modelo y vista a partir del estado guardado.
     * @return true si había un estado válido que restaurar.
     */
    private boolean restaurarEstado(Bundle estado) {
        Ficha turno = "O".equals(estado.getString(CLAVE_TURNO)) ? Ficha.O : Ficha.X;
        if (!juego.restaurarEstado(estado.getString(CLAVE_TABLERO), turno)) {
            return false;
        }
        controladorPartida.restaurarPuntuaciones(
                estado.getInt(CLAVE_PUNTOS_X), estado.getInt(CLAVE_PUNTOS_O));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                MaterialButton boton = matrizBotones[i][j];
                if (boton == null) {
                    continue;
                }
                Ficha ficha = juego.obtenerFicha(i, j);
                boton.setText(ficha == null ? "" : ficha.toString());
                boton.setEnabled(ficha == null && !juego.estaFinalizado());
                if (ficha == Ficha.X) {
                    boton.setTextColor(ContextCompat.getColor(this, R.color.game_player_x));
                } else if (ficha == Ficha.O) {
                    boton.setTextColor(ContextCompat.getColor(this, R.color.game_player_o));
                }
            }
        }

        actualizarMarcadores();
        if (juego.hayGanador()) {
            // Al ganar no se cambia el turno: el jugador actual es el ganador
            textoEstado.setText(turno == Ficha.X ? R.string.player_x_wins : R.string.player_o_wins);
        } else if (juego.estaLleno()) {
            textoEstado.setText(R.string.draw);
        } else {
            actualizarEstadoTurno();
        }
        return true;
    }

    private void inicializarComponentes() {
        textoEstado = findViewById(R.id.status);
        textoPuntuacionX = findViewById(R.id.player_x_score);
        textoPuntuacionO = findViewById(R.id.player_o_score);
    }

    private void configurarTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String idBoton = String.format(Locale.US, "button%d%d", i, j);
                int idRecurso = getResources().getIdentifier(idBoton, "id", getPackageName());
                matrizBotones[i][j] = findViewById(idRecurso);
                
                final int fila = i;
                final int columna = j;
                if (matrizBotones[i][j] != null) {
                    matrizBotones[i][j].setOnClickListener(v -> manejarClickCelda(fila, columna));
                    matrizBotones[i][j].setText(""); // Limpiar texto de herramientas/diseño
                }
            }
        }
    }

    private void manejarClickCelda(int fila, int columna) {
        String fichaActual = controladorTablero.obtenerSimboloActual();

        if (controladorTablero.realizarMovimiento(fila, columna)) {
            MaterialButton boton = matrizBotones[fila][columna];
            boton.setText(fichaActual);

            // Aplicar color específico según el jugador
            if (fichaActual.equals("X")) {
                boton.setTextColor(ContextCompat.getColor(this, R.color.game_player_x));
            } else {
                boton.setTextColor(ContextCompat.getColor(this, R.color.game_player_o));
            }

            if (controladorPartida.verificarGanador()) {
                String mensaje = fichaActual.equals("X") ? 
                        getString(R.string.player_x_wins) : getString(R.string.player_o_wins);
                if (textoEstado != null) {
                    textoEstado.setText(mensaje);
                }
                actualizarMarcadores();
                bloquearTablero();
            } else if (controladorPartida.esEmpate()) {
                if (textoEstado != null) {
                    textoEstado.setText(R.string.draw);
                }
                bloquearTablero();
            } else {
                controladorPartida.cambiarTurno();
                actualizarEstadoTurno();
            }
        }
    }

    private void actualizarEstadoTurno() {
        if (textoEstado != null) {
            String turno = controladorTablero.obtenerSimboloActual();
            textoEstado.setText(turno.equals("X") ? R.string.player_x_turn : R.string.player_o_turn);
        }
    }

    private void actualizarMarcadores() {
        if (textoPuntuacionX != null) {
            textoPuntuacionX.setText(getString(R.string.player_x_score, controladorPartida.obtenerPuntuacionX()));
        }
        if (textoPuntuacionO != null) {
            textoPuntuacionO.setText(getString(R.string.player_o_score, controladorPartida.obtenerPuntuacionO()));
        }
    }

    private void bloquearTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (matrizBotones[i][j] != null) {
                    matrizBotones[i][j].setEnabled(false);
                }
            }
        }
    }

    private void reiniciarJuego() {
        controladorPartida.reiniciar();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (matrizBotones[i][j] != null) {
                    matrizBotones[i][j].setText("");
                    matrizBotones[i][j].setEnabled(true);
                    // Restaurar color de texto por defecto del tema usando TypedValue
                    android.util.TypedValue typedValue = new android.util.TypedValue();
                    getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true);
                    matrizBotones[i][j].setTextColor(typedValue.data);
                }
            }
        }
        actualizarEstadoTurno();
    }

    private void nuevoJuego() {
        // Reinicia el juego creando una nueva instancia del modelo y controladores
        juego = new Juego();
        controladorPartida = new ControladorPartida(juego);
        controladorTablero = new ControladorTablero(juego);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (matrizBotones[i][j] != null) {
                    matrizBotones[i][j].setText("");
                    matrizBotones[i][j].setEnabled(true);
                    // Restaurar color de texto por defecto del tema usando TypedValue
                    android.util.TypedValue typedValue = new android.util.TypedValue();
                    getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true);
                    matrizBotones[i][j].setTextColor(typedValue.data);
                }
            }
        }
        actualizarMarcadores();
        actualizarEstadoTurno();
    }
}
