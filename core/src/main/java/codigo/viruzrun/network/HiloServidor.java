package codigo.viruzrun.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class HiloServidor extends Thread {

    private static final String MSG_CONECTADO = "Conectado";
    private static final String MSG_CONECTADO_OK = "CONECTADO";
    private static final String MSG_SERVIDOR_LLENO = "SERVIDOR_LLENO";
    private static final String MSG_EMPEZAR = "EMPEZAR";
    private static final String MSG_SALTO = "SALTO";
    private static final String MSG_PERDIO = "PERDIO";
    private static final String MSG_ELIMINADO = "ELIMINADO";
    private static final String MSG_FIN = "FIN";
    private static final String MSG_DESCONECTADO = "DESCONECTADO";
    private static final String MSG_CLIENTE_DESCONECTADO = "CLIENTEDESCONECTADO";
    private static final String MSG_SERVIDOR_CERRADO = "SERVIDORCERRADO";
    private static final String MSG_PING = "PING";
    private static final String MSG_PONG = "PONG";

    private DatagramSocket socket;
    private int puertoServidor = 5555;
    private boolean fin = false;

    private final int MAX_CLIENTES = 2;
    private int clientesConectados = 0;

    private ArrayList<Cliente> clientes = new ArrayList<>();
    private Controlador controlador;

    private boolean partidaIniciada = false;
    private boolean jugador1Vivo = true;
    private boolean jugador2Vivo = true;
    private int ultimoEnMorir = 0;
    private boolean finEnviado = false;

    public HiloServidor(Controlador controlador) {
        this.controlador = controlador;
        try {
            socket = new DatagramSocket(puertoServidor);
        } catch (SocketException e) {
            e.printStackTrace();
            fin = true;
        }
    }

    @Override
    public void run() {
        if (socket == null) {
            System.out.println("No se pudo iniciar el servidor: puerto ocupado.");
            return;
        }
        System.out.println("Servidor iniciado, esperando jugadores...");

        while (!fin) {
            try {
                DatagramPacket paquete = new DatagramPacket(new byte[1024], 1024);

                socket.receive(paquete);
                procesarMensaje(paquete);

            } catch (IOException e) {
                if (fin) break;
            }
        }
        System.out.println("Servidor cerrado");
    }

    public void cerrar() {
        enviarMensajeATodos(MSG_SERVIDOR_CERRADO);
        fin = true;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        interrupt();
    }

    private void procesarMensaje(DatagramPacket packet) {

        String mensaje = new String(packet.getData(), 0, packet.getLength()).trim();
        if (mensaje.isEmpty()) return;
        String[] partes = mensaje.split(":");

        int index = buscarIndiceCliente(packet);

        System.out.println("Mensaje recibido: " + mensaje);

        if (partes[0].equals(MSG_PING)) {
            enviarMensaje(MSG_PONG, packet.getAddress(), packet.getPort());
            return;
        }

        // =========================
        // CONECTAR
        // =========================
        if (partes[0].equals(MSG_CONECTADO)) {
            if (index != -1) {
                Cliente existente = clientes.get(index);
                enviarMensaje(MSG_CONECTADO_OK + ":" + existente.obtenerNum(),
                packet.getAddress(), packet.getPort());
                return;
            }

            if (clientesConectados >= MAX_CLIENTES && !partidaIniciada) {
                clientesDesconectados();
            }

            if (clientesConectados >= MAX_CLIENTES) {
                enviarMensaje(MSG_SERVIDOR_LLENO, packet.getAddress(), packet.getPort());
                return;
            }

            clientesConectados++;
            System.out.println(clientesConectados);
            Cliente nuevo = new Cliente(
            clientesConectados,
            packet.getAddress(),
            packet.getPort()
            );

            clientes.add(nuevo);

            enviarMensaje(MSG_CONECTADO_OK + ":" + clientesConectados,
            packet.getAddress(), packet.getPort());

            controlador.conexion(clientesConectados);

            // ACA ESTA LA CLAVE
            if (clientesConectados == MAX_CLIENTES) {
                partidaIniciada = true;
                enviarMensajeATodos(MSG_EMPEZAR);
                System.out.println("Partida iniciada, se habilitan obstaculos");
            }

            return;
        }
        if (partes[0].equals(MSG_SALTO)) {
            int numJugador = Integer.parseInt(partes[1]);
            controlador.salto(numJugador);
            enviarMensajeATodos(MSG_SALTO + ":" + numJugador);
            return;
        }

        if (partes[0].equals(MSG_PERDIO)) {
            int numJugador = Integer.parseInt(partes[1]);
            if (numJugador == 1 && jugador1Vivo) {
                jugador1Vivo = false;
                ultimoEnMorir = 1;
                enviarMensajeATodos(MSG_ELIMINADO + ":" + numJugador);
            } else if (numJugador == 2 && jugador2Vivo) {
                jugador2Vivo = false;
                ultimoEnMorir = 2;
                enviarMensajeATodos(MSG_ELIMINADO + ":" + numJugador);
            }

            if (!finEnviado && !jugador1Vivo && !jugador2Vivo) {
                finEnviado = true;
                enviarMensajeATodos(MSG_FIN + ":" + ultimoEnMorir);
            }
        }

        if (partes[0].equals(MSG_DESCONECTADO)) {
            if (partes.length > 1) {
                enviarMensajeATodos(MSG_CLIENTE_DESCONECTADO + ":" + partes[1]);
            } else {
                enviarMensajeATodos(MSG_CLIENTE_DESCONECTADO);
            }
            if (index != -1) {
                clientes.remove(index);
            } else {
                clientes.clear();
            }
            clientesConectados = clientes.size();
            reiniciarEstadoPartida();
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
        if (socket == null || socket.isClosed()) return;
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
            enviarMensaje(MSG_DESCONECTADO, cliente.obtenerIP(), cliente.obtenerPuerto());
        }
        clientes.clear();
        clientesConectados = 0;
        reiniciarEstadoPartida();
    }

    public boolean isPartidaIniciada() {
        return partidaIniciada;
    }

    public int getClientesConectados() {
        return clientesConectados;
    }

    private void reiniciarEstadoPartida() {
        partidaIniciada = false;
        jugador1Vivo = true;
        jugador2Vivo = true;
        ultimoEnMorir = 0;
        finEnviado = false;
    }

}

