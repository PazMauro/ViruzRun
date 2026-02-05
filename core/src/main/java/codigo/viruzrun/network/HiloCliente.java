package codigo.viruzrun.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class HiloCliente extends Thread {

    private static final String MSG_CONECTADO = "CONECTADO";
    private static final String MSG_EMPEZAR = "EMPEZAR";
    private static final String MSG_SALTO = "SALTO";
    private static final String MSG_DESCONECTADO = "DESCONECTADO";
    private static final String MSG_FIN = "FIN";
    private static final String MSG_OBSTACULO = "OBSTACULO";
    private static final String MSG_ELIMINADO = "ELIMINADO";
    private static final String MSG_SERVIDOR_CERRADO = "SERVIDORCERRADO";
    private static final String MSG_CLIENTE_DESCONECTADO = "CLIENTEDESCONECTADO";
    private static final String MSG_SERVIDOR_LLENO = "SERVIDOR_LLENO";
    private static final String MSG_PING = "PING";
    private static final String MSG_PONG = "PONG";

    private static final int SOCKET_TIMEOUT_MS = 1000;
    private static final long SERVER_TIMEOUT_MS = 7000L;
    private static final long PING_INTERVAL_MS = 1500L;

    private DatagramSocket socket;
    private InetAddress ipServidor;
    private int puertoServidor = 5555;
    private boolean fin = false;

    private Controlador controlador;

    private int id;
    private long ultimoMensajeServidorMs = System.currentTimeMillis();
    private long ultimoPingMs = 0L;
    private boolean desconexionNotificada = false;
    private long inicioIntentoMs = System.currentTimeMillis();
    private boolean conectado = false;

    public HiloCliente(Controlador controlador) {
        this.controlador = controlador;
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(SOCKET_TIMEOUT_MS);
            ipServidor = InetAddress.getByName("255.255.255.255");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!fin) {
            try {
                DatagramPacket paquete = new DatagramPacket(new byte[1024], 1024);
                socket.receive(paquete);
                ultimoMensajeServidorMs = System.currentTimeMillis();
                procesarMensaje(paquete);
            } catch (SocketTimeoutException e) {
                // Sin mensajes en este tick, seguimos con el chequeo de estado.
            } catch (Exception e) {
                if (fin) break;
                e.printStackTrace();
            }
            long ahora = System.currentTimeMillis();
            if (!fin) {
                enviarPingSiCorresponde(ahora);
                verificarServidor(ahora);
            }
        }
    }

    private void procesarMensaje(DatagramPacket paquete) {
        String mensaje = new String(paquete.getData(), 0, paquete.getLength()).trim();
        if (mensaje.isEmpty()) return;
        String[] partes = mensaje.split(":");
        System.out.println(mensaje);
        switch (partes[0]) {
        case MSG_CONECTADO:
            controlador.conectar(Integer.parseInt(partes[1]));
            this.id = Integer.parseInt(partes[1]);
            conectado = true;
            break;
        case MSG_PONG:
            // Mantiene viva la conexión.
            break;
        case MSG_EMPEZAR:
            controlador.empezar();
            break;
        case MSG_SALTO:
            controlador.saltoRemoto(Integer.parseInt(partes[1]));
            break;
        case MSG_DESCONECTADO:
            if (controlador instanceof codigo.viruzrun.pantallas.PantallaJuego) {
                codigo.viruzrun.pantallas.PantallaJuego pantalla =
                (codigo.viruzrun.pantallas.PantallaJuego) controlador;
                if (pantalla.isJuegoEmpezado()) {
                    controlador.clienteDesconectado();
                    break;
                }
            }
            controlador.volverAlMenu();
            break;
        case MSG_FIN:
            controlador.terminar(Integer.parseInt(partes[1]));
            break;
        case MSG_OBSTACULO:
            controlador.crearObstaculo();
            break;
        case MSG_ELIMINADO:
            if (partes.length > 1) {
                controlador.eliminarJugador(Integer.parseInt(partes[1]));
            }
            break;
        case MSG_SERVIDOR_CERRADO:
            controlador.terminar(-1);
            break;
        case MSG_SERVIDOR_LLENO:
            if (controlador instanceof codigo.viruzrun.pantallas.PantallaJuego) {
                codigo.viruzrun.pantallas.PantallaJuego pantalla =
                (codigo.viruzrun.pantallas.PantallaJuego) controlador;
                pantalla.volverAlMenuConMensaje("Servidor lleno");
            } else {
                controlador.volverAlMenu();
            }
            break;
        case MSG_CLIENTE_DESCONECTADO:
            if (partes.length > 1) {
                controlador.clienteDesconectado(Integer.parseInt(partes[1]));
            } else {
                controlador.clienteDesconectado(-1);
            }
        }

    }

    public void enviarMensaje(String mensaje) {
        try {
            if (socket == null || socket.isClosed()) return;
            byte[] datos = mensaje.getBytes();
            DatagramPacket paquete =
            new DatagramPacket(datos, datos.length, ipServidor, puertoServidor);
            socket.send(paquete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cerrar() {
        if (fin) return;
        enviarMensaje("DESCONECTADO:" + this.id);
        fin = true;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        interrupt();

    }

    private void enviarPingSiCorresponde(long ahora) {
        if (ahora - ultimoPingMs >= PING_INTERVAL_MS) {
            enviarMensaje(MSG_PING);
            ultimoPingMs = ahora;
        }
    }

    private void verificarServidor(long ahora) {
        if (desconexionNotificada) return;
        if (!conectado && (ahora - inicioIntentoMs) >= 3000L) {
            desconexionNotificada = true;
            if (controlador instanceof codigo.viruzrun.pantallas.PantallaJuego) {
                codigo.viruzrun.pantallas.PantallaJuego pantalla =
                (codigo.viruzrun.pantallas.PantallaJuego) controlador;
                pantalla.volverAlMenuConMensaje("No esta activo el servidor");
            } else {
                controlador.volverAlMenu();
            }
            cerrar();
            return;
        }
        if (ahora - ultimoMensajeServidorMs >= SERVER_TIMEOUT_MS) {
            desconexionNotificada = true;
            if (controlador instanceof codigo.viruzrun.pantallas.PantallaJuego) {
                codigo.viruzrun.pantallas.PantallaJuego pantalla =
                (codigo.viruzrun.pantallas.PantallaJuego) controlador;
                pantalla.forzarVolverAlMenuPorConexion();
            } else {
                controlador.volverAlMenu();
            }
            cerrar();
        }
    }
}
