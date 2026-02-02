package codigo.viruzrun.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class HiloServidor extends Thread {

    private DatagramSocket socket;
    private int puertoServidor = 5555;
    private boolean fin = false;

    private final int MAX_CLIENTES = 2;
    private int clientesConectados = 0;

    private ArrayList<Cliente> clientes = new ArrayList<>();
    private Controlador controlador;

    private boolean partidaIniciada = false;

    public HiloServidor(Controlador controlador) {
        this.controlador = controlador;
        try {
            socket = new DatagramSocket(puertoServidor);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("🟢 Servidor iniciado, esperando jugadores...");

        while (!fin) {
            try {
                DatagramPacket paquete = new DatagramPacket(new byte[1024], 1024);

                socket.receive(paquete);
                procesarMensaje(paquete);

            } catch (IOException e) {
                if (fin) break;
            }
        }
        System.out.println("🔴 Servidor cerrado");
    }

    public void cerrar() {
        fin = true;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        interrupt();
    }

    // ------------------ RESTO IGUAL ------------------



    private void procesarMensaje(DatagramPacket packet) {

        String mensaje = new String(packet.getData()).trim();
        String[] partes = mensaje.split(":");

        int index = buscarIndiceCliente(packet);

        System.out.println("Mensaje recibido: " + mensaje);

        // =========================
        // CONECTAR
        // =========================
        if (partes[0].equals("Conectado")) {

            if (clientesConectados >= MAX_CLIENTES) {
                enviarMensaje("SERVIDOR_LLENO", packet.getAddress(), packet.getPort());
                return;
            }

            clientesConectados++;

            Cliente nuevo = new Cliente(
                clientesConectados,
                packet.getAddress(),
                packet.getPort()
            );

            clientes.add(nuevo);

            enviarMensaje("CONECTADO:" + clientesConectados,
                          packet.getAddress(), packet.getPort());

            controlador.conexion(clientesConectados);

            // 🔥 ACÁ ESTÁ LA CLAVE
            if (clientesConectados == MAX_CLIENTES) {
                partidaIniciada = true;
                enviarMensajeATodos("EMPEZAR");
                System.out.println("✅ Partida iniciada, se habilitan obstáculos");
            }

            return;
        }
        if(partes[0].equals("SALTO")) {
        	controlador.salto(Integer.parseInt(partes[1]));
        }

    }

    private int buscarIndiceCliente(DatagramPacket paquete) {
        int i = 0;
        int indice = -1;

        while (i < clientes.size() && indice == -1) {
            Cliente cliente = clientes.get(i);
            String id = paquete.getAddress().toString() + ":" + paquete.getPort();

            if (id.equals(cliente.obtenerID())) {
                indice = i;
            }
            i++;
        }
        return indice;
    }

    public void enviarMensaje(String mensaje, InetAddress ip, int puerto) {
        byte[] datos = mensaje.getBytes();
        DatagramPacket paquete = new DatagramPacket(datos, datos.length, ip, puerto);
        try {
            socket.send(paquete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarMensajeATodos(String mensaje) {
        for (Cliente cliente : clientes) {
            enviarMensaje(mensaje, cliente.obtenerIP(), cliente.obtenerPuerto());
        }
    }

    public void clientesDesconectados() {
        for (Cliente cliente : clientes) {
            enviarMensaje("DESCONECTADO", cliente.obtenerIP(), cliente.obtenerPuerto());
        }
        clientes.clear();
        clientesConectados = 0;
    }

    public void terminarServidor() {
        fin = true;
        socket.close();
        interrupt();
    }
    
    public boolean isPartidaIniciada() {
        return partidaIniciada;
    }


}
