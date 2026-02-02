package codigo.viruzrun.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import codigo.viruzrun.entidades.Jugador;
import codigo.viruzrun.network.HiloCliente;
import codigo.viruzrun.pantallas.PantallaJuego;

public class ControlJugador implements InputProcessor {

    private Jugador jugador1;
    private Jugador jugador2;

    private HiloCliente cliente;
    private PantallaJuego pantalla;

    public ControlJugador(Jugador j1, Jugador j2, HiloCliente cliente, PantallaJuego pantalla) {
        this.jugador1 = j1;
        this.jugador2 = j2;
        this.cliente = cliente;
        this.pantalla = pantalla;
    }

    @Override
    public boolean keyDown(int keycode) {

        if (!pantalla.isJuegoEmpezado()) return false;

        // JUGADOR 1 → SPACE
        if (keycode == Input.Keys.SPACE) {
            jugador1.saltar();                  // 🔹 salto local
            cliente.enviarMensaje("SALTO:1");   // 🔹 aviso al servidor
            return true;
        }

        // JUGADOR 2 → W
        if (keycode == Input.Keys.W) {
            jugador2.saltar();                  // 🔹 salto local
            cliente.enviarMensaje("SALTO:2");   // 🔹 aviso al servidor
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