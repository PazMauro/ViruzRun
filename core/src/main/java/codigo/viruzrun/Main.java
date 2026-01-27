package codigo.viruzrun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import codigo.viruzrun.pantallas.PantallaMenu;

public class Main extends Game {
    public SpriteBatch batch;
    public BitmapFont font;
    public static float volumenGlobal = 0.4f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new PantallaMenu(this));
        font = new BitmapFont();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
