package codigo.viruzrun.pantallas;

import codigo.viruzrun.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;

public class PantallaMenu extends ScreenAdapter {

    private Main juego;
    private BitmapFont font;
    private GlyphLayout layout;

    private Rectangle botonJugar;
    private Rectangle botonSalir;

    private final float BOTON_ANCHO = 200;
    private final float BOTON_ALTO = 40;

    public PantallaMenu(Main juego) {
        this.juego = juego;
        font = new BitmapFont();
        layout = new GlyphLayout();

        float ancho = Gdx.graphics.getWidth();
        float alto = Gdx.graphics.getHeight();

        float centroX = ancho / 2f;
        float centroY = alto / 2f;

        botonJugar = new Rectangle(centroX - BOTON_ANCHO / 2, centroY + 40, BOTON_ANCHO, BOTON_ALTO);
        botonSalir = new Rectangle(centroX - BOTON_ANCHO / 2, centroY - 10, BOTON_ANCHO, BOTON_ALTO);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        boolean hoverJugar = botonJugar.contains(mouseX, mouseY);
        boolean hoverSalir = botonSalir.contains(mouseX, mouseY);

        juego.batch.begin();

        // ===== TITULO =====
        font.setColor(1, 1, 1, 1);
        layout.setText(font, "ViruzRun-Servidor");
        font.draw(
            juego.batch,
            "ViruzRun-Servidor",
            Gdx.graphics.getWidth() / 2f - (layout.width / 2f),
            Gdx.graphics.getHeight() - 80
        );

        // ===== JUGAR =====
        font.setColor(0, hoverJugar ? 1 : 0.7f, 0, 1);
        font.draw(juego.batch, "JUGAR", botonJugar.x + 65, botonJugar.y + 28);

        // ===== SALIR =====
        font.setColor(hoverSalir ? 1 : 0.7f, 0, 0, 1);
        font.draw(juego.batch, "SALIR", botonSalir.x + 70, botonSalir.y + 28);

        juego.batch.end();

        // ===== CLICK =====
        if (Gdx.input.justTouched()) {

            if (hoverJugar) {
                juego.setScreen(new PantallaJuego(juego));
            }

            if (hoverSalir) {
                Gdx.app.exit();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}

