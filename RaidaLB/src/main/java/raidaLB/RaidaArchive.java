package raidaLB;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class RaidaArchive implements Serializable {
    private static final long serialVersionUID = -2561891249331273624L;
    private final HashMap<String, Raida> raidas;


    public static RaidaArchive getInstance() throws IOException {
        return SaveManager.getInstance().getArchive();
    }


    public RaidaArchive() {
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
