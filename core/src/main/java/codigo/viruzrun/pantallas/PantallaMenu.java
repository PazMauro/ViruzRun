package codigo.viruzrun.pantallas;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import codigo.viruzrun.Main;

public class PantallaMenu extends ScreenAdapter {
    private Main juego;
    private SpriteBatch batch;
    private BitmapFont font;

    public PantallaMenu(Main juego) {
        this.juego = juego;
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.draw(batch, "Presiona ENTER para jugar", 100, 150);
        batch.end();

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ENTER)) {
            juego.setScreen(new PantallaJuego(juego));
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
