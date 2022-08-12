package raidaLB;

import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import me.bed0.jWynn.WynncraftAPI;

public class Driver {

    public static boolean debugMode;
    private static WynncraftAPI api;


    public static void main(final String[] args) {

        try {

            api = new WynncraftAPI();
            CommandManager.init();

            // Hourly save timer
            final Timer timer = new Timer();
            final TimerTask saveTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        SaveManager.getInstance().save();
                    }
                    catch (final IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            };

            // schedule the task to run starting now and then every hour...
            timer.schedule(saveTask, 0l, 1000 * 60 * 60);

            System.out.println("Welcome Raida!");

            if (args.length == 2 && args[0].equals("-discord")) {
                final DiscordBot bot = new DiscordBot(args[1]);
            }

            else {
                final Scanner in = new Scanner(System.in);
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
        }
        catch (final Exception e) {
            try {
                SaveManager.getInstance().save();
            }
            catch (final IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            System.exit(1);
        }
    }


    public static WynncraftAPI getApi() {
        return api;
    }


    public static void exit() throws IOException {

        SaveManager.getInstance().save();

        System.exit(0);

    }

}
