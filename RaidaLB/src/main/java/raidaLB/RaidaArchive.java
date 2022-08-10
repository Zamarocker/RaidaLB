package raidaLB;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class RaidaArchive implements Serializable {
    private static final long serialVersionUID = -2561891249331273624L;

    private static final String SER_NAME = "raidas.ser";
    private final HashMap<String, Raida> raidas;

    // Global singular Class instance
    private static RaidaArchive instance;


    public static RaidaArchive getInstance() throws IOException {
        if (instance != null)
            return instance;
        instance = ConfigManager.deserialize(SER_NAME, RaidaArchive.class);
        if (instance != null)
            return instance;
        instance = new RaidaArchive();
        return instance;
    }


    public void save() throws IOException {
        ConfigManager.serialize(SER_NAME, this);
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


    public boolean contains(final String ign) {
        return raidas.containsKey(ign);
    }
}
