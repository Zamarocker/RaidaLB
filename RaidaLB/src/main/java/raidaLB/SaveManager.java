package raidaLB;

import java.io.IOException;
import java.io.Serializable;

public class SaveManager implements Serializable {

    private static final String SER_NAME = "raidSaves";
    private static SaveManager instance;

    private final LBManager lbm;
    private final RaidaArchive archive;


    public static SaveManager getInstance() throws IOException {
        if (instance != null)
            return instance;
        instance = ConfigManager.deserialize(SER_NAME, SaveManager.class);
        if (instance != null)
            return instance;
        instance = new SaveManager();
        return instance;
    }


    public SaveManager() {
        lbm = new LBManager();
        archive = new RaidaArchive();
    }


    public void save() throws IOException {
        ConfigManager.serialize(SER_NAME, this);
    }


    public RaidaArchive getArchive() {
        return archive;
    }


    public LBManager getLBM() {
        return lbm;
    }

}
