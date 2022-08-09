package raidaLB;

import java.io.File;

public class ConfigManager {
    public static final String CONFIG_FOLDER_NAME = "RaidaConfig";
    public static final String PLAYER_FILE_NAME = "igns.txt";


    public static void confirmConfigFolder() {
        final File folder = new File(CONFIG_FOLDER_NAME);
        if (!folder.exists()) {
            Driver.debugLog("Creating config folder...");
            folder.mkdir();
        }
    }

}
