package codigo.viruzrun.input;

import codigo.viruzrun.entidades.Jugador;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class ControlJugador implements InputProcessor {

    private Jugador jugador1;
    private Jugador jugador2;

    public ControlJugador(Jugador jugador1, Jugador jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
    }

    @Override
    public boolean keyDown(int keycode) {

        // JUGADOR 1  SALTA CON ESPACIO
        if (keycode == Input.Keys.SPACE) {
            jugador1.saltar();
            return true;
        }

        // JUGADOR 2  SALTA CON W
        if (keycode == Input.Keys.W) {
            jugador2.saltar();
            return true;
        }

        return false;
    }

    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean touchDown(int x, int y, int pointer, int button) { return false; }
    @Override public boolean touchUp(int x, int y, int pointer, int button) { return false; }
    @Override public boolean touchDragged(int x, int y, int pointer) { return false; }
    @Override public boolean mouseMoved(int x, int y) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }
    @Override public boolean touchCancelled(int x, int y, int pointer, int button) { return false; }
}

