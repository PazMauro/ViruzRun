package codigo.viruzrun.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HiloServidor implements Runnable {

    private Socket socket;
    private Servidor servidor;
    private BufferedReader entrada;
    private PrintWriter salida;

    public HiloServidor(Socket socket, Servidor servidor) {
        this.socket = socket;
        this.servidor = servidor;

        try {
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String mensaje;

            while ((mensaje = entrada.readLine()) != null) {
                System.out.println("📩 Mensaje recibido: " + mensaje);
                servidor.enviarATodos(mensaje);
            }

        } catch (IOException e) {
            System.out.println("🔴 Cliente desconectado");
        } finally {
            try {
                socket.close(); // 👈 USO DEL SOCKET
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void enviarMensaje(String mensaje) {
        salida.println(mensaje);
    }
}
