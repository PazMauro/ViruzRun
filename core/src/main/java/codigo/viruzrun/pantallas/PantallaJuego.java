package codigo.viruzrun.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
import codigo.viruzrun.network.Controlador;
import codigo.viruzrun.network.HiloCliente;


public class PantallaJuego implements Screen, Controlador {

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

    private HiloCliente cliente;
    
    private int miNumeroJugador = 0;
    private boolean juegoEmpezado = false;

    private boolean juegoTerminado = false;
    private int ganador = 0;


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
        
        cliente = new HiloCliente(this);
        cliente.start();
        cliente.enviarMensaje("Conectado");

        Gdx.input.setInputProcessor(
            new ControlJugador(jugador1, jugador2, cliente, this)
        );

    }

    private void actualizar(float delta) {
    	
    	if (!juegoEmpezado || juegoTerminado) return;

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

        // 🔼 Subida de dificultad cada 300 puntos
        if (nuevoNivel > nivelActual) {
            nivelActual = nuevoNivel;
            velocidadJuego += AUMENTO_VELOCIDAD;

            if (tiempoMin > 0.5f) {
                tiempoMin -= 0.1f;
                tiempoMax -= 0.1f;
            }
        }

        // 🔥 CAMBIO FUERTE A LOS 1500 PUNTOS (ESTO QUEDA)
        if (puntosMax >= 1500 && !fondoCambiado) {
            fondo = fondoDificil;
            fondoCambiado = true;

            velocidadJuego = 220f;
            tiempoMin = 0.6f;
            tiempoMax = 1.6f;
        }

     // -------- SPAWN OBSTÁCULOS --------
        tiempoTranscurrido += delta;
        
        


        // -------- COLISIONES --------
        for (Obstaculo o : obstaculos) {
            o.actualizar(delta, velocidadJuego);

            if (jugador1Vivo && jugador1.getHitbox().overlaps(o.getHitbox())) {
                jugador1Vivo = false;
                jugador1.eliminar();
                sonidoMuerte.play(Main.volumenGlobal);
                cliente.enviarMensaje("PERDIO:1");
            }

            if (jugador2 != null && jugador2Vivo && jugador2.getHitbox().overlaps(o.getHitbox())) {
                jugador2Vivo = false;
                jugador2.eliminar();
                sonidoMuerte.play(Main.volumenGlobal);
                cliente.enviarMensaje("PERDIO:2");
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
            juego.font.draw(juego.batch, "Esperando otro jugador...", 280, 240);
        } else {

            // -------- JUGADORES --------
            if (jugador1Vivo) jugador1.dibujar(juego.batch);
            if (jugador2 != null && jugador2Vivo) jugador2.dibujar(juego.batch);

            // -------- OBSTÁCULOS --------
            for (Obstaculo o : obstaculos) o.dibujar(juego.batch);

            // -------- HUD --------
            juego.font.draw(juego.batch, "Jugador 1: " + puntosJugador1, 20, 460);
            juego.font.draw(juego.batch, "Jugador 2: " + puntosJugador2, 20, 430);
            juego.font.draw(juego.batch, "Nivel: " + nivelActual, 650, 460);

            // -------- FIN DE PARTIDA --------
            if (juegoTerminado) {
                if (ganador == miNumeroJugador) {
                    juego.font.draw(juego.batch, "¡GANASTE!", 360, 260);
                } else {
                    juego.font.draw(juego.batch, "PERDISTE", 360, 260);
                }
                juego.font.draw(juego.batch, "Volviendo al menú...", 300, 220);
            }
        }

        juego.batch.end();

        // -------- SUELO --------
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(0, SUELO_Y, ANCHO, ALTURA_SUELO);
        shapeRenderer.end();

        // -------- VOLVER AL MENÚ (UNA SOLA VEZ) --------
        if (juegoTerminado) {
            Gdx.app.postRunnable(() -> {
                juego.setScreen(new PantallaMenu(juego));
            });
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
        
        if (cliente != null) {
            cliente.cerrar();
        }

    }
    
    public int getMiNumeroJugador() {
        return miNumeroJugador;
    }
    
    public boolean isJuegoEmpezado() {
        return juegoEmpezado;
    }
    
    @Override
    public void conectar(int numeroJugador) {
        miNumeroJugador = numeroJugador;
        System.out.println("Soy el jugador " + miNumeroJugador);
    }


    @Override
    public void empezar() {
        juegoEmpezado = true;
    }


    @Override
    public void saltoRemoto(int numeroJugador) {
        if (numeroJugador == 1 && jugador1Vivo) {
            jugador1.saltar();
        }

        if (numeroJugador == 2 && jugador2Vivo) {
            jugador2.saltar();
        }
    }

	@Override
	public void terminar(int ganador) {
	    this.ganador = ganador;
	    juegoTerminado = true;

	    musicaFondo.stop();
	    cliente.cerrar();
	}

	@Override
	public void volverAlMenu() {
	    musicaFondo.stop();
	    cliente.cerrar();
	    juego.setScreen(new PantallaMenu(juego));
	}

	@Override
	public void crearObstaculo() {
	    Gdx.app.postRunnable(() -> {
	        obstaculos.add(new Obstaculo(850, 80));
	        generarTiempoAleatorio();
	    });
	}


}
