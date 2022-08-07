package raidaLB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import me.bed0.jWynn.WynncraftAPI;

public class ConfigManager {
    public static final String CONFIG_FOLDER_NAME = "RaidaConfig";
    public static final String PLAYER_FILE_NAME = "igns.txt";


    public static ArrayList<Raida> loadPlayerFile(final WynncraftAPI api)
        throws IOException {
        final File folder = new File(CONFIG_FOLDER_NAME);
        if (!folder.exists()) {
            Driver.debugLog("Creating config folder...");
            folder.mkdir();
        }
        final File players = new File(CONFIG_FOLDER_NAME + File.separator
            + PLAYER_FILE_NAME);
        if (!players.exists()) {
            Driver.debugLog("Creating blank players file...");
            players.createNewFile();
        }

        final ArrayList<Raida> list = new ArrayList<Raida>();
        try (FileReader fr = new FileReader(players);
            BufferedReader br = new BufferedReader(fr)) {

            String line;
            while ((line = br.readLine()) != null) {
                final Raida playa = new Raida(Raida.getPlayer(line, api));
                list.add(playa);
            }

        }
        return list;
    }
}
