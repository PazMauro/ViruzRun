package codigo.viruzrun.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class HiloCliente extends Thread {

    private DatagramSocket socket;
    private InetAddress ipServidor;
    private int puertoServidor = 5555;
    private boolean fin = false;

    private Controlador controlador;

    public HiloCliente(Controlador controlador) {
        this.controlador = controlador;
        try {
            socket = new DatagramSocket();
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
                procesarMensaje(paquete);
            } catch (Exception e) {
                if (fin) break;
            }
        }
    }

    private void procesarMensaje(DatagramPacket paquete) {
        String mensaje = new String(paquete.getData()).trim();
        String[] partes = mensaje.split(":");
        System.out.println(mensaje);
        switch (partes[0]) {
            case "CONECTADO":
                controlador.conectar(Integer.parseInt(partes[1]));
                
                break;
            case "EMPEZAR":
                controlador.empezar();
                break;
            case "SALTO":
                controlador.saltoRemoto(Integer.parseInt(partes[1]));
                break;
            case "DESCONECTADO":
                controlador.volverAlMenu();
                break;
            case "FIN":
                controlador.terminar(Integer.parseInt(partes[1]));
                break;
            case "OBSTACULO":
                controlador.crearObstaculo();
                break;

        }
        

    }

    public void enviarMensaje(String mensaje) {
        try {
            byte[] datos = mensaje.getBytes();
            DatagramPacket paquete =
                    new DatagramPacket(datos, datos.length, ipServidor, puertoServidor);
            socket.send(paquete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cerrar() {
        fin = true;
        socket.close();
        interrupt();
    }
}
