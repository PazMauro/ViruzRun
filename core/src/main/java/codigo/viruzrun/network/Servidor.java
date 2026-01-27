package codigo.viruzrun.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor implements Runnable {

    private static final int PUERTO = 9999;
    private ServerSocket serverSocket;
    private ArrayList<HiloServidor> clientes;
    private boolean activo = true;

    public Servidor() {
        clientes = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PUERTO);
            System.out.println("🟢 Servidor iniciado en puerto " + PUERTO);

            while (activo) {
                Socket socket = serverSocket.accept();
                System.out.println("🔵 Cliente conectado");

                HiloServidor hilo = new HiloServidor(socket, this);
                clientes.add(hilo);
                new Thread(hilo).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarATodos(String mensaje) {
        for (HiloServidor cliente : clientes) {
            cliente.enviarMensaje(mensaje);
        }
    }
}
