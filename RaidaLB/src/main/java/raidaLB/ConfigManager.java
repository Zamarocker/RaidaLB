package raidaLB;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 *
 */
public class ConfigManager {
    public static final String CONFIG_FOLDER_NAME = "RaidaConfig";
    public static final String PLAYER_FILE_NAME = "igns.txt";


    /**
     * Checks for existence of config folder, creates it if missing
     */
    public static void confirmConfigFolder() {
        final File folder = new File(CONFIG_FOLDER_NAME);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }


    /**
     * Deserializes a saved object
     *
     * @param <T>
     *            object type
     * @param fileName
     *            Name of file
     * @param c
     *            class of object
     * @return
     *         Deserialized object
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(final String fileName, final Class<T> c)
        throws IOException {

        ConfigManager.confirmConfigFolder();
        final File f = new File(ConfigManager.CONFIG_FOLDER_NAME
            + File.separator + fileName);
        // New object if no file found
        if (!f.exists())
            return null;
        final FileInputStream fs = new FileInputStream(f);
        final ObjectInputStream objIn = new ObjectInputStream(fs);

        try {
            return (T)objIn.readObject();
        }
        catch (final Exception e) {
            System.err.println("Unable to deserialize " + fileName);
            e.printStackTrace();
            return null;
        }

    }


    /**
     * Serializes and saves an object to disk
     * 
     * @param fileName
     *            Name of file
     * @param obj
     *            Object type
     * @throws IOException
     */
    public static void serialize(final String fileName, final Object obj)
        throws IOException {
        ConfigManager.confirmConfigFolder();
        final File f = new File(ConfigManager.CONFIG_FOLDER_NAME
            + File.separator + fileName);
        final FileOutputStream fs = new FileOutputStream(f);
        final ObjectOutputStream objOut = new ObjectOutputStream(fs);
        objOut.writeObject(obj);

    }
}
