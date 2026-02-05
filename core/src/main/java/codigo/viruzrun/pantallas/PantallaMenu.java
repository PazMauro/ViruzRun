package codigo.viruzrun.pantallas;

import codigo.viruzrun.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;

public class PantallaMenu extends ScreenAdapter {

    private Main juego;
    private BitmapFont font;
    private GlyphLayout layout;

    private Rectangle botonJugar;
    private Rectangle botonSalir;
    private Rectangle botonControles;

    // ðŸ”Š BOTONES DE VOLUMEN (SOLO MENU)
    private Rectangle botonVolMas;
    private Rectangle botonVolMenos;

    private final float BOTON_ANCHO = 200;
    private final float BOTON_ALTO = 40;

    // ðŸŽµ MUSICA MENU
    private Music musicaMenu;

    // ðŸ“· IMAGEN CONTROLES
    private Texture imagenControles;
    private boolean mostrandoControles = false;
    private String mensajeAviso;
    private float tiempoAviso = 0f;

    public PantallaMenu(Main juego) {
        this(juego, null);
    }

    public PantallaMenu(Main juego, String mensajeAviso) {
        this.juego = juego;
        this.mensajeAviso = mensajeAviso;
        if (mensajeAviso != null && !mensajeAviso.isEmpty()) {
            tiempoAviso = 3f;
        }
        font = new BitmapFont();
        layout = new GlyphLayout();

        float ancho = Gdx.graphics.getWidth();
        float alto = Gdx.graphics.getHeight();

        float centroX = ancho / 2f;
        float centroY = alto / 2f;

        botonJugar = new Rectangle(centroX - BOTON_ANCHO / 2, centroY + 40, BOTON_ANCHO, BOTON_ALTO);
        botonControles = new Rectangle(centroX - BOTON_ANCHO / 2, centroY - 10, BOTON_ANCHO, BOTON_ALTO);
        botonSalir = new Rectangle(centroX - BOTON_ANCHO / 2, centroY - 60, BOTON_ANCHO, BOTON_ALTO);

        // ðŸ”Š BOTONES VOLUMEN (ARRIBA DERECHA)
        botonVolMas = new Rectangle(ancho - 60, alto - 40, 30, 30);
        botonVolMenos = new Rectangle(ancho - 100, alto - 40, 30, 30);

        musicaMenu = Gdx.audio.newMusic(Gdx.files.internal("musicamenu.mp3"));
        musicaMenu.setLooping(true);
        musicaMenu.setVolume(Main.volumenGlobal);
        musicaMenu.play();

        imagenControles = new Texture("teclado.png");
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        boolean hoverJugar = botonJugar.contains(mouseX, mouseY);
        boolean hoverSalir = botonSalir.contains(mouseX, mouseY);
        boolean hoverControles = botonControles.contains(mouseX, mouseY);
        boolean hoverVolMas = botonVolMas.contains(mouseX, mouseY);
        boolean hoverVolMenos = botonVolMenos.contains(mouseX, mouseY);

        juego.batch.begin();

        // ===== TITULO =====
        font.setColor(1, 1, 1, 1);
        layout.setText(font, "VIRUZ RUN");
        font.draw(
            juego.batch,
            "VIRUZ RUN",
            Gdx.graphics.getWidth() / 2f - (layout.width / 2f),
            Gdx.graphics.getHeight() - 80
        );

        // ===== JUGAR =====
        font.setColor(0, hoverJugar ? 1 : 0.7f, 0, 1);
        layout.setText(font, "JUGAR");
        font.draw(
            juego.batch,
            "JUGAR",
            botonJugar.x + (botonJugar.width / 2f) - (layout.width / 2f),
            botonJugar.y + (botonJugar.height / 2f) + (layout.height / 2f)
        );

        // ===== CONTROLES =====
        font.setColor(hoverControles ? 0.8f : 1, hoverControles ? 0.8f : 1, hoverControles ? 0.8f : 1, 1);
        layout.setText(font, "CONTROLES");
        font.draw(
            juego.batch,
            "CONTROLES",
            botonControles.x + (botonControles.width / 2f) - (layout.width / 2f),
            botonControles.y + (botonControles.height / 2f) + (layout.height / 2f)
        );

        // ===== SALIR =====
        font.setColor(hoverSalir ? 1 : 0.7f, 0, 0, 1);
        layout.setText(font, "SALIR");
        font.draw(
            juego.batch,
            "SALIR",
            botonSalir.x + (botonSalir.width / 2f) - (layout.width / 2f),
            botonSalir.y + (botonSalir.height / 2f) + (layout.height / 2f)
        );

        // ðŸ”Š VOLUMEN
        font.setColor(1, 1, 1, 1);
        font.draw(juego.batch, "+", botonVolMas.x + 8, botonVolMas.y + 22);
        font.draw(juego.batch, "-", botonVolMenos.x + 10, botonVolMenos.y + 22);

        // ===== CONTROLES =====
        if (mostrandoControles) {
            juego.batch.draw(imagenControles,
            Gdx.graphics.getWidth() / 2f - 250,
            Gdx.graphics.getHeight() / 2f - 150,
            500, 300);

            String textoCerrar = "Click para cerrar";
            layout.setText(font, textoCerrar);
            font.draw(
                juego.batch,
                textoCerrar,
                Gdx.graphics.getWidth() / 2f - (layout.width / 2f),
                Gdx.graphics.getHeight() / 2f - 170
            );
        }

        if (tiempoAviso > 0f && mensajeAviso != null && !mensajeAviso.isEmpty()) {
            font.setColor(1, 1, 0, 1);
            layout.setText(font, mensajeAviso);
            font.draw(
                juego.batch,
                mensajeAviso,
                Gdx.graphics.getWidth() / 2f - (layout.width / 2f),
                Gdx.graphics.getHeight() / 2f + 120
            );
        }

        juego.batch.end();

        if (tiempoAviso > 0f) {
            tiempoAviso = Math.max(0f, tiempoAviso - delta);
        }

        // ===== CLICK =====
        if (Gdx.input.justTouched()) {

            if (hoverVolMas) {
                Main.volumenGlobal = Math.min(1f, Main.volumenGlobal + 0.1f);
                musicaMenu.setVolume(Main.volumenGlobal);
            }

            if (hoverVolMenos) {
                Main.volumenGlobal = Math.max(0f, Main.volumenGlobal - 0.1f);
                musicaMenu.setVolume(Main.volumenGlobal);
            }

            if (mostrandoControles) {
                mostrandoControles = false;
                return;
            }

            if (hoverJugar) {
                musicaMenu.stop();
                juego.setScreen(new PantallaJuego(juego));
            }

            if (hoverControles) {
                mostrandoControles = true;
            }

            if (hoverSalir) {
                Gdx.app.exit();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (mostrandoControles) {
                mostrandoControles = false;
            } else {
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void hide() {
        musicaMenu.stop();
    }

    @Override
    public void dispose() {
        font.dispose();
        musicaMenu.dispose();
        imagenControles.dispose();
    }
}

