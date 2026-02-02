package codigo.viruzrun.network;

public interface Controlador {

    void conectar(int numeroJugador);

    void empezar();

    void saltoRemoto(int numeroJugador);

    void terminar(int ganador);

    void volverAlMenu();
    
    void crearObstaculo();
}
