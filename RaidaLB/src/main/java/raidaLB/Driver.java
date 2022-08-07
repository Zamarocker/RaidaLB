package raidaLB;

import java.io.IOException;
import java.util.Scanner;

public class Driver {

    public static boolean debugMode;


    public static void main(final String[] args) throws IOException {
        // Debug
        if (args.length == 1 && args[0].equals("-debug"))
            debugMode = true;

        final Scanner in = new Scanner(System.in);
        CommandManager.init();
        while (true) {
            final String cmd = in.nextLine();
            System.out.println(CommandManager.run(cmd));

        }
    }


    public static void debugLog(final String str) {
        if (debugMode)
            System.out.println(str);
    }

}
