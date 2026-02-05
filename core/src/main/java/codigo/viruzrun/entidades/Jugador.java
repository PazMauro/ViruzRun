package codigo.viruzrun.entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Jugador {

    private float x, y;
    private float velocidadY;
    private boolean enSuelo;
    private boolean eliminado;

    private Texture sprite;
    private Rectangle hitbox;

    public Jugador(float x, float y, String texture) {
        this.x = x;
        this.y = y;
        this.sprite = new Texture(texture);
        this.enSuelo = true;
        this.eliminado = false;

        this.hitbox = new Rectangle(x, y, 40, 40);
    }

    public void actualizar(float delta) {
        if (eliminado) return;

        velocidadY -= 500 * delta;
        y += velocidadY * delta;

        if (y <= 80) {
            y = 80;
            enSuelo = true;
            velocidadY = 0;
        }

        hitbox.setPosition(x + 8, y + 8);
    }

    public void dibujar(SpriteBatch batch) {
        if (!eliminado) {
            batch.draw(sprite, x, y, 60, 60);
        }
    }

    public void saltar() {
        if (enSuelo && !eliminado) {
            velocidadY = 300;
            enSuelo = false;
        }
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public boolean estaEliminado() {
        return eliminado;
    }

    public void eliminar() {
        eliminado = true;
    }
}