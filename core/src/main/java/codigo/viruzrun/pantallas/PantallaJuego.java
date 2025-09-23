package codigo.viruzrun.pantallas;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import codigo.viruzrun.Main;
import codigo.viruzrun.entidades.Jugador;

public class PantallaJuego extends ScreenAdapter {
    private Main juego;
    private SpriteBatch batch;
    private Jugador jugador;

    public PantallaJuego(Main juego) {
        this.juego = juego;
        batch = new SpriteBatch();
        jugador = new Jugador();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        jugador.render(batch);
        batch.end();

        jugador.update(delta);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
