package raidaLB;

import java.io.IOException;
import java.util.Scanner;
import me.bed0.jWynn.WynncraftAPI;

public class Driver {

    public static boolean debugMode;
    private static WynncraftAPI api;


    public static void main(final String[] args) throws IOException {

        api = new WynncraftAPI();
        final Scanner in = new Scanner(System.in);
        CommandManager.init();
        System.out.println("Welcome Raida!");
        while (true) {
            String cmd = in.nextLine();

            cmd = cmd.toLowerCase();
            final String[] cmdarr = cmd.split("\\s+");
            final String command = cmdarr[0];

            final String[] arguments = new String[cmdarr.length - 1];
            System.arraycopy(cmdarr, 1, arguments, 0, arguments.length);

            System.out.println(CommandManager.run(command, arguments));

        }
    }


    public static WynncraftAPI getApi() {
        return api;
    }


    public static void exit() throws IOException {

        RaidaArchive.getInstance().save();
        LBManager.getInstance().save();

        System.exit(0);

    }

}
