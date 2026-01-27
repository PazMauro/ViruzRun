package codigo.viruzrun.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class HiloCliente implements Runnable {

    private Socket socket;
    private BufferedReader entrada;

    public HiloCliente(Socket socket) {
        this.socket = socket;

        try {
            entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String mensaje;

            while ((mensaje = entrada.readLine()) != null) {
                System.out.println("📥 Servidor dice: " + mensaje);
            }

        } catch (IOException e) {
            System.out.println("🔴 Conexión cerrada");
        } finally {
            try {
                socket.close(); // 👈 USO REAL DEL SOCKET
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
