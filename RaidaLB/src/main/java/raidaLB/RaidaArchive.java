package raidaLB;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

public class RaidaArchive implements Serializable {
    private static final long serialVersionUID = -2561891249331273624L;

    private static final String SER_NAME = "raidas.ser";
    private final HashMap<String, Raida> raidas;


    public static RaidaArchive loadArchive() throws IOException {
        ConfigManager.confirmConfigFolder();
        final File f = new File(ConfigManager.CONFIG_FOLDER_NAME
            + File.separator + SER_NAME);
        // New object if no file found
        if (!f.exists())
            return new RaidaArchive();
        final FileInputStream fs = new FileInputStream(f);
        final ObjectInputStream objIn = new ObjectInputStream(fs);

        try {
            return (RaidaArchive)objIn.readObject();
        }
        catch (final Exception e) {
            System.err.println("Unable to deserialize archive");
            e.printStackTrace();
            return new RaidaArchive();
        }

    }


    public static void saveArchive() throws IOException {
        ConfigManager.confirmConfigFolder();
        final File f = new File(ConfigManager.CONFIG_FOLDER_NAME
            + File.separator + SER_NAME);
        final FileOutputStream fs = new FileOutputStream(f);
        final ObjectOutputStream objOut = new ObjectOutputStream(fs);
    }


    private RaidaArchive() {
        raidas = new HashMap<String, Raida>();
    }


    public Raida getRaida(final String ign) {
        return raidas.get(ign);
    }


    public void addRaida(final String ign, final Raida raida) {
        raidas.put(ign, raida);
    }
}
