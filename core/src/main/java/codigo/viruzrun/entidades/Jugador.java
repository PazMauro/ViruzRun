package codigo.viruzrun.entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Jugador {
    private Texture textura;
    private float x, y;

    public Jugador() {
        textura = new Texture("jugador.png"); // pon esta imagen en assets/
        x = 100;
        y = 100;
    }

    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) x -= 200 * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x += 200 * delta;
    }

    public void render(SpriteBatch batch) {
        batch.draw(textura, x, y);
    }

    public void dispose() {
        textura.dispose();
    }
}
