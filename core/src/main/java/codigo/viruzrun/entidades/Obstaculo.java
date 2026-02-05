package codigo.viruzrun.entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Obstaculo {

    private Texture texture;
    private float x, y;
    private Rectangle hitbox;

    public Obstaculo(float x, float y) {
        this.x = x;
        this.y = y;
        texture = new Texture("obstaculo.png");
        hitbox = new Rectangle(x, y, 32, 32);
    }

    public void actualizar(float delta, float velocidadJuego) {
        x -= velocidadJuego * delta;
        hitbox.setPosition(x + 8, y + 8);
    }

    public void dibujar(SpriteBatch batch) {
        batch.draw(texture, x, y, 60, 60);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}

