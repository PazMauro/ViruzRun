package codigo.viruzrun;

import com.badlogic.gdx.Game;
import codigo.viruzrun.pantallas.PantallaMenu;

public class Main extends Game {

    @Override
    public void create() {
        setScreen(new PantallaMenu(this));
    }
}
