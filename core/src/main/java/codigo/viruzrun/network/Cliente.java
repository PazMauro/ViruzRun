package codigo.viruzrun.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {

    private Socket socket;
    private PrintWriter salida;
    private HiloCliente hiloCliente;

    public void conectar(String ip, int puerto) {
        try {
            socket = new Socket(ip, puerto);
            salida = new PrintWriter(socket.getOutputStream(), true);

            hiloCliente = new HiloCliente(socket);
            new Thread(hiloCliente).start();

            System.out.println("🟢 Conectado al servidor");

        } catch (IOException e) {
            System.out.println("🔴 No se pudo conectar al servidor");
        }
    }

    public void enviarMensaje(String mensaje) {
        if (salida != null) {
            salida.println(mensaje);
        }
    }
}
