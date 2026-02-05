package codigo.viruzrun.network;

public interface Controlador {

    void conectar(int nroJugador);

    void empezar();

    void saltoRemoto(int nroJugador);

    void terminar(int ganador);

    void volverAlMenu();

    void crearObstaculo();

    void clienteDesconectado();

    void clienteDesconectado(int nroJugador);

    void eliminarJugador(int nroJugador);
}

