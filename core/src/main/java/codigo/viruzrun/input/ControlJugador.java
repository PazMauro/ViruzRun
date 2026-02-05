package codigo.viruzrun.input;

import codigo.viruzrun.entidades.Jugador;
import codigo.viruzrun.network.HiloCliente;
import codigo.viruzrun.pantallas.PantallaJuego;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class ControlJugador implements InputProcessor {

    private static final String MSG_SALTO = "SALTO";

    private Jugador jugador1;
    private Jugador jugador2;

    private HiloCliente cliente;
    private PantallaJuego pantalla;
    private int nroJugador;

    public ControlJugador(Jugador j1, Jugador j2, HiloCliente cliente, PantallaJuego pantalla, int nroJugador) {
        this.jugador1 = j1;
        this.jugador2 = j2;
        this.cliente = cliente;
        this.pantalla = pantalla;
        this.nroJugador = nroJugador;
    }

    public void setNumeroJugador(int numero) {
        this.nroJugador = numero;
    }

    @Override
    public boolean keyDown(int keycode) {

        if (!pantalla.isJuegoEmpezado()) return false;
        if (nroJugador <= 0) return false;

        if (keycode == Input.Keys.SPACE) {
            if (nroJugador == 1) {
                jugador1.saltar();
            } else if (nroJugador == 2 && jugador2 != null) {
                jugador2.saltar();
            }
            cliente.enviarMensaje(MSG_SALTO + ":" + nroJugador);
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