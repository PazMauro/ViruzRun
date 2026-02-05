package codigo.viruzrun.network;

import java.net.InetAddress;

public class Cliente {

    private String id;
    private int num;
    private InetAddress ip;
    private int puerto;

    public Cliente(int num, InetAddress ip, int puerto) {
        this.num = num;
        this.id = ip.toString() + ":" + puerto;
        this.ip = ip;
        this.puerto = puerto ;
    }

    public String obtenerID() {
        return id;
    }

    public InetAddress obtenerIP() {
        return ip;
    }

    public int obtenerPuerto() {
        return puerto ;
    }

    public int obtenerNum() {
        return num;
    }
}

