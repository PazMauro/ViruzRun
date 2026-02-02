package codigo.viruzrun.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import codigo.viruzrun.Main;

public class Lwjgl3Launcher {

    public static final int ANCHO = 800;
    public static final int ALTO = 480;

    public static void main(String[] args) {

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setTitle("Viruz Run");
 
        // 🔒 RESOLUCIÓN FIJA
        config.setWindowedMode(ANCHO, ALTO);
        config.setResizable(false);

        // 🎯 FPS ESTABLE
        config.setForegroundFPS(60);

        // 🧼 VSYNC
        config.useVsync(true);

        new Lwjgl3Application(new Main(), config);
    }
}
