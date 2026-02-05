package codigo.viruzrun;

import codigo.viruzrun.pantallas.PantallaMenu;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
    public SpriteBatch batch;
    public BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new PantallaMenu(this));
        font = new BitmapFont();
    }

    @Override
    public void dispose() {
        batch.dispose();
        getScreen().dispose();
    }
}

