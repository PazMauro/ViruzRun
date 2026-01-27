package codigo.viruzrun.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.audio.Music;

import codigo.viruzrun.Main;

public class PantallaMenu extends ScreenAdapter {

    private Main juego;
    private BitmapFont font;

    private Rectangle botonJugar;
    private Rectangle botonSalir;
    private Rectangle botonControles;

    // 🔊 BOTONES DE VOLUMEN (SOLO MENU)
    private Rectangle botonVolMas;
    private Rectangle botonVolMenos;

    private final float BOTON_ANCHO = 200;
    private final float BOTON_ALTO = 40;

    // 🎵 MUSICA MENU
    private Music musicaMenu;

    // 📷 IMAGEN CONTROLES
    private Texture imagenControles;
    private boolean mostrandoControles = false;

    public PantallaMenu(Main juego) {
        this.juego = juego;
        font = new BitmapFont();

        float ancho = Gdx.graphics.getWidth();
        float alto = Gdx.graphics.getHeight();

        float centroX = ancho / 2f;
        float centroY = alto / 2f;

        botonJugar = new Rectangle(centroX - BOTON_ANCHO / 2, centroY + 40, BOTON_ANCHO, BOTON_ALTO);
        botonControles = new Rectangle(centroX - BOTON_ANCHO / 2, centroY - 10, BOTON_ANCHO, BOTON_ALTO);
        botonSalir = new Rectangle(centroX - BOTON_ANCHO / 2, centroY - 60, BOTON_ANCHO, BOTON_ALTO);

        // 🔊 BOTONES VOLUMEN (ARRIBA DERECHA)
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
        font.draw(juego.batch, "VIRUZ RUN",
                Gdx.graphics.getWidth() / 2f - 50,
                Gdx.graphics.getHeight() - 80);

        // ===== JUGAR =====
        font.setColor(0, hoverJugar ? 1 : 0.7f, 0, 1);
        font.draw(juego.batch, "JUGAR", botonJugar.x + 65, botonJugar.y + 28);

        // ===== CONTROLES =====
        font.setColor(hoverControles ? 0.8f : 1, hoverControles ? 0.8f : 1, hoverControles ? 0.8f : 1, 1);
        font.draw(juego.batch, "CONTROLES", botonControles.x + 35, botonControles.y + 28);

        // ===== SALIR =====
        font.setColor(hoverSalir ? 1 : 0.7f, 0, 0, 1);
        font.draw(juego.batch, "SALIR", botonSalir.x + 70, botonSalir.y + 28);

        // 🔊 VOLUMEN
        font.setColor(1, 1, 1, 1);
        font.draw(juego.batch, "+", botonVolMas.x + 8, botonVolMas.y + 22);
        font.draw(juego.batch, "-", botonVolMenos.x + 10, botonVolMenos.y + 22);

        // ===== CONTROLES =====
        if (mostrandoControles) {
            juego.batch.draw(imagenControles,
                    Gdx.graphics.getWidth() / 2f - 250,
                    Gdx.graphics.getHeight() / 2f - 150,
                    500, 300);

            font.draw(juego.batch, "Click para cerrar",
                    Gdx.graphics.getWidth() / 2f - 60,
                    Gdx.graphics.getHeight() / 2f - 170);
        }

        juego.batch.end();

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
