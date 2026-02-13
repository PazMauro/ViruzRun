package codigo.viruzrun.pantallas;

import codigo.viruzrun.Main;
import codigo.viruzrun.entidades.Jugador;
import codigo.viruzrun.entidades.Obstaculo;
import codigo.viruzrun.input.ControlJugador;
import codigo.viruzrun.network.Controlador;
import codigo.viruzrun.network.HiloCliente;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaJuego implements Screen, Controlador {

    public static final float ANCHO = 800;
    public static final float ALTO = 480;

    private static final String MSG_CONECTADO = "Conectado";
    private static final String MSG_PERDIO = "PERDIO";

    private static final float SUELO_Y = 90;
    private static final float ALTURA_SUELO = 10;

    private static final int PUNTOS_POR_NIVEL = 300;
    private static final float AUMENTO_VELOCIDAD = 20f;

    private Main juego;

    private OrthographicCamera camara;
    private Viewport viewport;

    private Texture fondo;
    private Texture fondoNormal;
    private Texture fondoDificil;

    private ShapeRenderer shapeRenderer;

    private Jugador jugador1;
    private Jugador jugador2;

    private boolean jugador1Vivo = true;
    private boolean jugador2Vivo = true;

    private Array<Obstaculo> obstaculos;


    private float tiempoMin = 1.0f;
    private float tiempoMax = 2.5f;

    private int puntosJugador1;
    private int puntosJugador2;

    private float velocidadJuego = 140f;

    private int nivelActual = 0;
    private boolean fondoCambiado = false;

    private Music musicaFondo;
    private Sound sonidoMuerte;
    private GlyphLayout layout;

    private HiloCliente cliente;
    private ControlJugador controlJugador;

    private int numeroJugadorLocal = 0;
    private boolean juegoEmpezado = false;

    private boolean juegoTerminado = false;
    private int ganador = 0;
    private float tiempoFin = 0f;
    private boolean volverMenuProgramado = false;
    private boolean rivalSeDesconecto = false;
    private boolean forzarVolverMenuPorConexion = false;
    private static final String MSG_CONEXION_PERDIDA = "Se perdio la conexion con el servidor";
    private String mensajeJugador;
    private float tiempoMensajeJugador = 0f;

    public PantallaJuego(Main juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        viewport = new FitViewport(ANCHO, ALTO, camara);
        viewport.apply();

        fondoNormal = new Texture("fondo.png");
        fondoDificil = new Texture("fondo_dificil.png");
        fondo = fondoNormal;

        shapeRenderer = new ShapeRenderer();

        jugador1 = new Jugador(50, 80, "jugador.png");
        jugador2 = new Jugador(120, 80, "jugador2.png");

        obstaculos = new Array<>();
        generarTiempoAleatorio();

        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("musicafondo.mp3"));
        musicaFondo.setLooping(true);
        musicaFondo.setVolume(Main.volumenGlobal);
        musicaFondo.play();

        sonidoMuerte = Gdx.audio.newSound(Gdx.files.internal("muerte.wav"));
        layout = new GlyphLayout();

        cliente = new HiloCliente(this);
        cliente.start();
        cliente.enviarMensaje(MSG_CONECTADO);

        controlJugador = new ControlJugador(jugador1, jugador2, cliente, this, numeroJugadorLocal);
        Gdx.input.setInputProcessor(controlJugador);

    }

    private void actualizar(float delta) {

        if (!juegoEmpezado) return;
        if (juegoTerminado) {
            // Evita saltos grandes de tiempo que puedan cerrar la pantalla al instante
            tiempoFin += Math.min(delta, 0.1f);
            return;
        }

        // -------- JUGADORES --------
        if (jugador1Vivo) {
            jugador1.actualizar(delta);
            puntosJugador1 += delta * 60;
        }

        if (jugador2 != null && jugador2Vivo) {
            jugador2.actualizar(delta);
            puntosJugador2 += delta * 60;
        }

        int puntosMax = Math.max(puntosJugador1, puntosJugador2);
        int nuevoNivel = puntosMax / PUNTOS_POR_NIVEL;

        // Subida de dificultad cada 300 puntos
        if (nuevoNivel > nivelActual) {
            nivelActual = nuevoNivel;
            velocidadJuego += AUMENTO_VELOCIDAD;

            if (tiempoMin > 0.5f) {
                tiempoMin -= 0.1f;
                tiempoMax -= 0.1f;
            }
        }

        // CAMBIO FUERTE A LOS 1500 PUNTOS (ESTO QUEDA)
        if (puntosMax >= 1500 && !fondoCambiado) {
            fondo = fondoDificil;
            fondoCambiado = true;

            velocidadJuego = 220f;
            tiempoMin = 0.6f;
            tiempoMax = 1.6f;
        }

        // -------- COLISIONES --------
        for (Obstaculo o : obstaculos) {
            o.actualizar(delta, velocidadJuego);

            if (numeroJugadorLocal == 1) {
                if (jugador1Vivo && jugador1.getHitbox().overlaps(o.getHitbox())) {
                    jugador1Vivo = false;
                    jugador1.eliminar();
                    sonidoMuerte.play(Main.volumenGlobal);
                    System.out.println("Enviando PERDIO del jugador 1");
                    enviarPerdio(1);
                }
            } else if (numeroJugadorLocal == 2) {
                if (jugador2 != null && jugador2Vivo && jugador2.getHitbox().overlaps(o.getHitbox())) {
                    jugador2Vivo = false;
                    jugador2.eliminar();
                    sonidoMuerte.play(Main.volumenGlobal);
                    System.out.println("Enviando PERDIO del jugador 2");
                    enviarPerdio(2);
                }
            }
        }
    }

    @Override
    public void render(float delta) {

        actualizar(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();
        juego.batch.setProjectionMatrix(camara.combined);

        juego.batch.begin();

        // -------- FONDO --------
        juego.batch.draw(fondo, 0, 0, ANCHO, ALTO);

        // -------- ESPERANDO JUGADOR --------
        if (!juegoEmpezado) {
            drawCentered("Esperando otro jugador...", 240);
        } else {

            // -------- JUGADORES --------
            if (jugador1Vivo) jugador1.dibujar(juego.batch);
            if (jugador2 != null && jugador2Vivo) jugador2.dibujar(juego.batch);

            // -------- OBSTÃCULOS --------
            for (Obstaculo o : obstaculos) o.dibujar(juego.batch);

            // -------- HUD --------
            juego.font.draw(juego.batch, "Jugador 1: " + puntosJugador1, 20, 460);
            juego.font.draw(juego.batch, "Jugador 2: " + puntosJugador2, 20, 430);
            juego.font.draw(juego.batch, "Nivel: " + nivelActual, 650, 460);

            // -------- FIN DE PARTIDA --------
            if (juegoTerminado) {
                if (rivalSeDesconecto) {
                    drawCentered("GANADOR", 260);
                    drawCentered("El rival se a desconectado", 300);
                } else {
                    if (ganador == numeroJugadorLocal) {
                        drawCentered("¡GANASTE!", 260);
                    } else {
                        drawCentered("PERDISTE", 260);
                    }
                }
                drawCentered("Presiona una tecla para volver", 220);
            }
        }

        if (tiempoMensajeJugador > 0f && mensajeJugador != null && !mensajeJugador.isEmpty()) {
            drawCentered(mensajeJugador, 420);
        }

        juego.batch.end();

        // -------- SUELO --------
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(0, 0, ANCHO, SUELO_Y + ALTURA_SUELO);
        shapeRenderer.end();

        // -------- VOLVER AL MENÚ (UNA SOLA VEZ) --------
        if (juegoTerminado && !volverMenuProgramado) {
            boolean inputParaVolver = Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY);
            // Espera un poco para que no vuelva al menú por la misma tecla con la que saltó al morir
            if (tiempoFin >= 2.5f || (tiempoFin >= 0.8f && inputParaVolver)) {
                volverMenuProgramado = true;
                Gdx.app.postRunnable(() -> {
                    juego.setScreen(new PantallaMenu(juego));
                });
            }
        }

        if (tiempoMensajeJugador > 0f) {
            tiempoMensajeJugador = Math.max(0f, tiempoMensajeJugador - delta);
        }
    }

    private void generarTiempoAleatorio() {
        MathUtils.random(tiempoMin, tiempoMax);
    }

    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void show() {}
    @Override public void pause() { if (musicaFondo != null) musicaFondo.pause(); }
    @Override public void resume() { if (musicaFondo != null) musicaFondo.play(); }
    @Override public void hide() {}

    @Override
    public void dispose() {
        fondoNormal.dispose();
        fondoDificil.dispose();
        shapeRenderer.dispose();
        if (musicaFondo != null) musicaFondo.dispose();
        if (sonidoMuerte != null) sonidoMuerte.dispose();
        cerrarCliente();
    }

    public boolean isJuegoEmpezado() {
        return juegoEmpezado;
    }

    @Override
    public void conectar(int numeroJugador) {
        numeroJugadorLocal = numeroJugador;
        System.out.println("Soy el jugador " + numeroJugadorLocal);
        if (controlJugador != null) {
            controlJugador.setNumeroJugador(numeroJugadorLocal);
        }
        if (numeroJugadorLocal == 1) {
            mensajeJugador = "Sos el jugador ROJO";
        } else if (numeroJugadorLocal == 2) {
            mensajeJugador = "Sos el jugador VIOLETA";
        } else {
            mensajeJugador = "Sos el jugador " + numeroJugadorLocal;
        }
        tiempoMensajeJugador = 3f;
    }

    @Override
    public void empezar() {
        juegoEmpezado = true;
    }

    @Override
    public void saltoRemoto(int numeroJugador) {
        if (numeroJugador == numeroJugadorLocal) return;
        if (numeroJugador == 1 && jugador1Vivo) {
            jugador1.saltar();
        }

        if (numeroJugador == 2 && jugador2Vivo) {
            jugador2.saltar();
        }
    }

    @Override
    public void terminar(int ganador) {
        if (ganador == -1) {
            Gdx.app.postRunnable(() -> volverAlMenuConMensaje(MSG_CONEXION_PERDIDA));
            return;
        }
        this.ganador = ganador;
        juegoTerminado = true;
        tiempoFin = 0f;
        volverMenuProgramado = false;

        detenerMusicaEnHiloPrincipal();
        cerrarCliente();
    }

    @Override
    public void volverAlMenu() {
        if (juegoEmpezado && !juegoTerminado && !forzarVolverMenuPorConexion) {
            // Si el servidor corta antes de llegar el FIN, esperamos el resultado
            return;
        }
        if (juegoTerminado) {
            // Ya hay fin de partida, dejamos que el temporizador/tecla gestione el regreso
            return;
        }
        cerrarCliente();
        Gdx.app.postRunnable(() -> {
            detenerMusica();
            juego.setScreen(new PantallaMenu(juego));
        });
    }

    @Override
    public void crearObstaculo() {
        Gdx.app.postRunnable(() -> {
            obstaculos.add(new Obstaculo(850, 80));
            generarTiempoAleatorio();
        });
    }

    @Override
    public void clienteDesconectado() {
        ganador = numeroJugadorLocal;
        rivalSeDesconecto = true;
        juegoTerminado = true;
        tiempoFin = 0f;
        volverMenuProgramado = false;
        detenerMusicaEnHiloPrincipal();
        cerrarCliente();
    }


    @Override
    public void clienteDesconectado(int nroJugador) {
        clienteDesconectado();
    }

    @Override
    public void eliminarJugador(int nroJugador) {
        if (nroJugador == 1) {
            jugador1Vivo = false;
            jugador1.eliminar();
        } else if (nroJugador == 2 && jugador2 != null) {
            jugador2Vivo = false;
            jugador2.eliminar();
        }
    }

    private void detenerMusica() {
        if (musicaFondo != null) musicaFondo.stop();
    }

    private void detenerMusicaEnHiloPrincipal() {
        Gdx.app.postRunnable(this::detenerMusica);
    }

    private void cerrarCliente() {
        if (cliente != null) cliente.cerrar();
    }

    private void enviarPerdio(int jugador) {
        cliente.enviarMensaje(MSG_PERDIO + ":" + jugador);
    }

    private void drawCentered(String texto, float y) {
        layout.setText(juego.font, texto);
        juego.font.draw(juego.batch, texto, ANCHO / 2f - (layout.width / 2f), y);
    }

    public void forzarVolverAlMenuPorConexion() {
        if (forzarVolverMenuPorConexion) return;
        forzarVolverMenuPorConexion = true;
        volverAlMenuConMensaje(MSG_CONEXION_PERDIDA);
    }

    public void volverAlMenuConMensaje(String mensaje) {
        resetearEstadoLocal();
        cerrarCliente();
        Gdx.app.postRunnable(() -> {
            detenerMusica();
            juego.setScreen(new PantallaMenu(juego, mensaje));
        });
    }

    private void resetearEstadoLocal() {
        forzarVolverMenuPorConexion = false;
        mensajeJugador = null;
        tiempoMensajeJugador = 0f;
        rivalSeDesconecto = false;
        juegoEmpezado = false;
        juegoTerminado = false;
        volverMenuProgramado = false;
    }

}
