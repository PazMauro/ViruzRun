package codigo.viruzrun.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import codigo.viruzrun.Main;
import codigo.viruzrun.entidades.Jugador;
import codigo.viruzrun.entidades.Obstaculo;
import codigo.viruzrun.input.ControlJugador;
import com.badlogic.gdx.graphics.Color;

public class PantallaJuego implements Screen {

    public static final float ANCHO = 800;
    public static final float ALTO = 480;

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

    private float tiempoSiguienteObstaculo;
    private float tiempoTranscurrido;

    private float tiempoMin = 1.0f;
    private float tiempoMax = 2.5f;

    private int puntosJugador1;
    private int puntosJugador2;

    private float velocidadJuego = 140f;

    private int nivelActual = 0;
    private boolean fondoCambiado = false;

    private Music musicaFondo;
    private Sound sonidoMuerte;

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

        Gdx.input.setInputProcessor(new ControlJugador(jugador1, jugador2));

        obstaculos = new Array<>();
        generarTiempoAleatorio();

        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("musicafondo.mp3"));
        musicaFondo.setLooping(true);
        musicaFondo.setVolume(Main.volumenGlobal);
        musicaFondo.play();

        sonidoMuerte = Gdx.audio.newSound(Gdx.files.internal("muerte.wav"));
    }

    private void actualizar(float delta) {

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

        // 🔼 Subida de dificultad cada 300 puntos
        if (nuevoNivel > nivelActual) {
            nivelActual = nuevoNivel;
            velocidadJuego += AUMENTO_VELOCIDAD;

            if (tiempoMin > 0.5f) {
                tiempoMin -= 0.1f;
                tiempoMax -= 0.1f;
            }
        }

        // 🔥 Cambio grande a los 1500 puntos
        if (puntosMax >= 1500 && !fondoCambiado) {
            fondo = fondoDificil;
            fondoCambiado = true;

            velocidadJuego = 220f;
            tiempoMin = 0.6f;
            tiempoMax = 1.6f;
        }

        tiempoTranscurrido += delta;
        if (tiempoTranscurrido >= tiempoSiguienteObstaculo) {
            obstaculos.add(new Obstaculo(850, 80));
            generarTiempoAleatorio();
        }

        for (Obstaculo o : obstaculos) {
            o.actualizar(delta, velocidadJuego);

            if (jugador1Vivo && jugador1.getHitbox().overlaps(o.getHitbox())) {
                jugador1Vivo = false;
                jugador1.eliminar();
                sonidoMuerte.play(Main.volumenGlobal);
            }

            if (jugador2Vivo && jugador2.getHitbox().overlaps(o.getHitbox())) {
                jugador2Vivo = false;
                jugador2.eliminar();
                sonidoMuerte.play(Main.volumenGlobal);
            }
        }
    }

    @Override
    public void render(float delta) {
        actualizar(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            musicaFondo.stop();
            juego.setScreen(new PantallaMenu(juego));
            return;
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();
        juego.batch.setProjectionMatrix(camara.combined);

        juego.batch.begin();
        juego.batch.draw(fondo, 0, 0, ANCHO, ALTO);

        if (jugador1Vivo) jugador1.dibujar(juego.batch);
        if (jugador2Vivo) jugador2.dibujar(juego.batch);

        for (Obstaculo o : obstaculos) o.dibujar(juego.batch);

        juego.font.draw(juego.batch, "Jugador 1: " + puntosJugador1, 20, 460);
        juego.font.draw(juego.batch, "Jugador 2: " + puntosJugador2, 20, 430);
        
        if (!jugador1Vivo)
            juego.font.draw(juego.batch, "Jugador 1 eliminado", 300, 350);

        if (!jugador2Vivo)
            juego.font.draw(juego.batch, "Jugador 2 eliminado", 300, 320);
        juego.font.draw(juego.batch, "Nivel: " + nivelActual, 650, 460);

        juego.batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(0, SUELO_Y, ANCHO, ALTURA_SUELO);
        shapeRenderer.end();

        if (!jugador1Vivo && !jugador2Vivo) {
            juego.batch.begin();
            juego.font.draw(juego.batch, "GAME OVER", 350, 240);
            juego.font.draw(juego.batch, "Presiona cualquier tecla", 300, 200);
            juego.batch.end();

            if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                musicaFondo.stop();
                juego.setScreen(new PantallaJuego(juego));
            }
        }
    }

    private void generarTiempoAleatorio() {
        tiempoSiguienteObstaculo = MathUtils.random(tiempoMin, tiempoMax);
        tiempoTranscurrido = 0;
    }

    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void show() {}
    @Override public void pause() { musicaFondo.pause(); }
    @Override public void resume() { musicaFondo.play(); }
    @Override public void hide() {}

    @Override
    public void dispose() {
        fondoNormal.dispose();
        fondoDificil.dispose();
        shapeRenderer.dispose();
        musicaFondo.dispose();
        sonidoMuerte.dispose();
    }
}
