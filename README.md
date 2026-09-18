# TicTacDuel

Juego de **Tres en Raya** (tres en línea) para Android, escrito en **Java**, para **dos jugadores en el mismo dispositivo**. Se juega por turnos: X empieza siempre y O responde.

> Estado del proyecto: aplicación pequeña de aprendizaje/portafolio. Funciona, tiene tests unitarios para toda la lógica del juego, pero no está publicada ni pensada para producción (ver [Limitaciones](#limitaciones)).

## Características (verificadas en el código)

- Partida de dos jugadores por turnos (X / O) en un tablero de 3x3.
- Detección de victoria en las **8 líneas** posibles (3 filas, 3 columnas, 2 diagonales).
- Detección de **empate**: solo cuando el tablero está lleno y no hay ganador. Ganar con la última casilla cuenta como victoria, no como empate.
- No se puede jugar en una casilla ocupada ni después de terminada la partida (lo garantiza el propio modelo, no solo la interfaz).
- **Marcador de sesión** de victorias por jugador.
- Dos botones: **Reiniciar** (nueva partida conservando el marcador) y **Nuevo juego** (nueva partida y marcador a cero).
- La partida en curso, el turno y el marcador **sobreviven a la rotación de pantalla** (se guardan en `onSaveInstanceState`).
- Interfaz con Material Components (tema Material 3), colores distintos por jugador y tema `DayNight` (recursos `values` y `values-night`; el fondo degradado y algunos colores del layout son fijos).

No hay inteligencia artificial ni modo de un jugador: siempre juegan dos personas.

## Arquitectura

Patrón **MVC** sencillo, paquete `com.example.tresenrayaandroid`:

| Capa | Clases | Responsabilidad |
|------|--------|-----------------|
| Modelo (`modelos`) | `Ficha`, `Jugador`, `Tablero`, `Calculadora`, `Reglas`, `Juego` | Estado y reglas. Sin dependencias de Android, por lo que se prueba con JUnit en la JVM. |
| Controladores (`controladores`) | `ControladorTablero`, `ControladorPartida` | Traducen las acciones de la vista al modelo y llevan el marcador. |
| Vista (`vistas`) | `MainActivity` + `res/layout/activity_main.xml` | Dibuja el estado y captura los toques. |

`Juego` orquesta `Tablero`, `Jugador`s y `Reglas`; `Reglas` delega la detección de victoria en `Calculadora`. `Juego` también sabe serializar/restaurar su estado (9 caracteres `X`/`O`/`-`) para la rotación de pantalla.

## Requisitos

- Android Studio reciente (Otter/Narwhal o superior) **o** línea de comandos con:
- **JDK 17** (obligatorio para Android Gradle Plugin 8.x).
- Android SDK con la plataforma **API 36** (`compileSdk`/`targetSdk` = 36; `minSdk` = 24, Android 7.0).
- Gradle se descarga solo mediante el wrapper (`gradle-wrapper.properties`: Gradle 8.13; AGP 8.13.2).

## Compilar y ejecutar

```bash
git clone <URL-del-repositorio>
cd TresEnRayaAndroid
./gradlew assembleDebug        # genera app/build/outputs/apk/debug/
./gradlew installDebug         # instala en un dispositivo/emulador conectado
```

También puedes abrir la carpeta en Android Studio y pulsar **Run**. Si Android Studio no lo hace por ti, crea `local.properties` con `sdk.dir=<ruta-a-tu-SDK>` (el archivo está ignorado por git).

## Tests

Los tests unitarios de la lógica (modelo y controladores) corren en la JVM, sin emulador:

```bash
./gradlew test
```

Cubren: las 8 líneas ganadoras para X y O, falsos positivos, empate, victoria en la novena casilla, celdas ocupadas, movimientos fuera de rango o tras terminar la partida, reinicio, alternancia de turno, marcador y serialización/restauración del estado.

`MainActivity` (la parte que necesita Android) **no** tiene tests automatizados; el proyecto conserva solo el `ExampleInstrumentedTest` generado por la plantilla.

Integración continua: `.github/workflows/ci.yml` ejecuta `./gradlew test` con JDK 17 en cada push y pull request.

## Limitaciones

- Solo dos jugadores locales; no hay IA, red ni multijugador en línea.
- El marcador vive en memoria: se pierde al cerrar la aplicación (solo se conserva durante rotaciones/recreaciones de la actividad).
- Textos del título y subtítulo en el layout y otros literales no están completamente externalizados a `strings.xml`; solo hay recursos en español.
- Sin animaciones, sonidos ni accesibilidad específica más allá de la de Material Components.
- No hay tests de interfaz (instrumentados) reales.
- El `applicationId` es el de plantilla (`com.example.tresenrayaandroid`) y el build de *release* no está firmado ni minificado.

## Licencia

Este repositorio **no incluye ningún archivo de licencia**. Sin licencia explícita, todos los derechos quedan reservados por el autor y no se concede permiso de uso, copia ni redistribución. Si deseas que otros puedan reutilizarlo, añade un archivo `LICENSE` (por ejemplo MIT o Apache-2.0).
