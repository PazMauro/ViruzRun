package codigo.viruzrun.pantallas;

import codigo.viruzrun.Main;
import codigo.viruzrun.entidades.Jugador;
import codigo.viruzrun.entidades.Obstaculo;
import codigo.viruzrun.network.Controlador;
import codigo.viruzrun.network.HiloServidor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaJuego implements Screen, Controlador {

    public static final float ANCHO = 800;
    public static final float ALTO = 480;

    private static final int PUNTOS_POR_NIVEL = 300;
    private static final float AUMENTO_VELOCIDAD = 20f;

    private Main juego;

    private OrthographicCamera camara;
    private Viewport viewport;

    private Texture spriteRojo;
    private Texture spriteVioleta;

    private Jugador jugador1;
    private Jugador jugador2;

    private Array<Jugador> jugadores;

    private boolean jugador1Vivo = true;
    private boolean jugador2Vivo = true;

    private Array<Obstaculo> obstaculos;

    private float tiempoSiguienteObstaculo;
    private float tiempoTranscurrido;

    private float tiempoMin = 1.0f;
    private float tiempoMax = 2.5f;

    private int puntosJugador1;
    private int puntosJugador2;

    private float velocidadJuego = 140f;

    private int nivelActual = 0;
    private boolean fondoCambiado = false;

    private GlyphLayout layout;

    private HiloServidor servidor;

    private int jugadoresConectados = 0;
    private boolean partidaEmpezada = false;

    private static final int MAX_JUGADORES = 2;

    private static final float SPRITE_TAM = 125f;
    private static final float SPRITE_Y = 170f;
    private static final float SPRITE_X_IZQ = 40f;
    private static final float SPRITE_X_DER = ANCHO - SPRITE_X_IZQ - SPRITE_TAM;

    public PantallaJuego(Main juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        viewport = new FitViewport(ANCHO, ALTO, camara);
        viewport.apply();

        spriteRojo = new Texture("jugador.png");
        spriteVioleta = new Texture("jugador2.png");

        jugador1 = new Jugador(50, 80, "jugador.png");
        jugador2 = new Jugador(ANCHO - 110, 80, "jugador2.png");

        jugadores = new Array<>();
        jugadores.add(jugador1); 
        jugadores.add(jugador2); 

        // En el servidor no hay control local de jugadores.

        obstaculos = new Array<>();

        generarTiempoAleatorio();

        layout = new GlyphLayout();

        servidor = new HiloServidor(this);
        servidor.start();

    }

    private void actualizar(float delta) {

        if (!partidaEmpezada) return;

        if (jugador1Vivo) {
            jugador1.actualizar(delta);
            puntosJugador1 += delta * 60;
        }

        if (jugador2Vivo) {
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

        // Cambio grande a los 1500 puntos
        if (puntosMax >= 1500 && !fondoCambiado) {
            fondoCambiado = true;

            velocidadJuego = 220f;
            tiempoMin = 0.6f;
            tiempoMax = 1.6f;
        }

        if (servidor != null && servidor.isPartidaIniciada()) {
            tiempoTranscurrido += delta;

            if (tiempoTranscurrido >= tiempoSiguienteObstaculo) {
                obstaculos.add(new Obstaculo(850, 80));
                generarTiempoAleatorio();
                servidor.enviarMensajeATodos("OBSTACULO");
            }
        }

        for (Obstaculo o : obstaculos) {
            o.actualizar(delta, velocidadJuego);

            if (jugador1Vivo && jugador1.getHitbox().overlaps(o.getHitbox())) {
                jugador1Vivo = false;
                jugador1.eliminar();
            }

            if (jugador2Vivo && jugador2.getHitbox().overlaps(o.getHitbox())) {
                jugador2Vivo = false;
                jugador2.eliminar();
            }
        }
    }

    @Override
    public void render(float delta) {
        actualizar(delta);

        // Sin entrada local en el servidor.

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();
        juego.batch.setProjectionMatrix(camara.combined);

        juego.batch.begin();
        juego.batch.draw(spriteRojo, SPRITE_X_IZQ, SPRITE_Y, SPRITE_TAM, SPRITE_TAM);
        juego.batch.draw(spriteVioleta, SPRITE_X_DER, SPRITE_Y, SPRITE_TAM, SPRITE_TAM);

        layout.setText(juego.font, "Servidor en ejecucion");
        juego.font.draw(
            juego.batch,
            "Servidor en ejecucion",
            ANCHO / 2f - (layout.width / 2f),
            ALTO - 30
        );

        int conectados = servidor != null ? servidor.getClientesConectados() : 0;
        String textoConectados = "Conectados: " + conectados + "/" + MAX_JUGADORES;
        layout.setText(juego.font, textoConectados);
        juego.font.draw(
            juego.batch,
            textoConectados,
            ANCHO / 2f - (layout.width / 2f),
            ALTO - 55
        );

        juego.batch.end();

        // Fondo totalmente negro, sin suelo.

    }

    private void generarTiempoAleatorio() {
        tiempoSiguienteObstaculo = MathUtils.random(tiempoMin, tiempoMax);
        tiempoTranscurrido = 0;
    }

    @Override
    public void conexion(int jugador) {
        jugadoresConectados++;
        System.out.println("Jugador " + jugador + " conectado");

        if (jugadoresConectados >= 2 && !partidaEmpezada) {
            reiniciarEstadoPartida();
            partidaEmpezada = true;
            generarTiempoAleatorio();
            System.out.println("Partida iniciada");
        }

    }

    @Override
    public void salto(int jugador) {
        jugadores.get(jugador - 1).saltar();
    }

    @Override
    public void desconectado(int jugador) {
        System.out.println("Jugador " + jugador + " desconectado");
        reiniciarEstadoPartida();
        cerrarServidor();
        juego.setScreen(new PantallaMenu(juego));

    }

    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        cerrarServidor();
        if (spriteRojo != null) spriteRojo.dispose();
        if (spriteVioleta != null) spriteVioleta.dispose();
    }

    private void reiniciarEstadoPartida() {
        partidaEmpezada = false;
        jugadoresConectados = 0;
        jugador1Vivo = true;
        jugador2Vivo = true;
        puntosJugador1 = 0;
        puntosJugador2 = 0;
        nivelActual = 0;
        velocidadJuego = 140f;
        fondoCambiado = false;
        tiempoMin = 1.0f;
        tiempoMax = 2.5f;
        tiempoTranscurrido = 0f;
        if (obstaculos != null) {
            obstaculos.clear();
        }
    }

    private void cerrarServidor() {
        if (servidor != null) servidor.cerrar();
    }
}

