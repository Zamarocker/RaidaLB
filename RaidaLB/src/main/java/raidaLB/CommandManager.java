package raidaLB;

import java.io.IOException;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.math.NumberUtils;
import me.bed0.jWynn.exceptions.APIRequestException;
import raidaLB.Raida.Raid;
import raidaLB.Raida.WynnClass;

/**
 * Class for processing commands
 */
public class CommandManager {
    private static RaidaArchive archive;
    private static LBManager lbm;


    /**
     * @throws IOException
     */
    public static void init() throws IOException {
        lbm = LBManager.getInstance();
        archive = RaidaArchive.getInstance();

    }


/*
 * Commands:
 * stats <ign>
 * lb
 * lb <raid>
 * lb <class>
 * quit
 *
 */
    /**
     * Run a command
     * 
     * @param command
     * @param args
     * @return
     *         command output
     * @throws IOException
     */
    public static String run(final String command, final String[] args)
        throws IOException {

        if (command.equals("stats"))
            return stats(args);

        if (command.equals("lb"))
            return lb(args);

        if (command.equals("quit") && args.length == 0)
            Driver.exit();
        return "Invalid command, See usage on README.MD";

    }


    /**
     * Helper function to process the stats command
     * 
     * @param args
     * @return
     *         stats command output
     * @throws IOException
     */
    private static String stats(final String[] args) throws IOException {
        if (args.length != 1)
            return "Incorrect usage of 'stats'\nUsage: stats <ign>";

        try {
            final String ign = args[0];
            Raida playa = null;
            if (archive.contains(ign))
                playa = archive.getRaida(ign);
            else {
                playa = new Raida(args[0]);
                archive.addRaida(playa.getIGN(), playa);
            }
            lbm.add(playa);
            return playa.summarize();
        }
        catch (final APIRequestException e) {
            return "Invalid Username";
        }
    }


    /**
     * Helper function to process the leaderboard command
     * 
     * @param args
     * @return
     *         lb command output
     */
    private static String lb(final String[] args) {
        if (args.length == 0) {
            return lbm.getLB().printLB();
        }

        Leaderboard lb = null;
        // arg is raid
        if (EnumUtils.isValidEnum(Raid.class, args[0]))
            lb = lbm.getLB(Raid.valueOf(args[0]));
        // arg is class
        else if (EnumUtils.isValidEnum(WynnClass.class, args[0]))
            lb = lbm.getLB(WynnClass.valueOf(args[0]));
        else if (args[0].equalsIgnoreCase("all"))
            lb = lbm.getLB();
        else
            return "Incorrect usage of 'lb'\nUsage: lb <raid>/<class>";

        // lb <raid>/<class>
        if (args.length == 1)
            return lb.printLB();

        // lb <raid>/<class> max
        else if (args.length == 2 && NumberUtils.isCreatable(args[1])) {
            final int max = Integer.parseInt(args[1]);
            return lb.printLB(0, max);
        }

        // lb <raid>/<class> min max
        else if (args.length == 3 && NumberUtils.isCreatable(args[1])
            && NumberUtils.isCreatable(args[2])) {
            final int min = Integer.parseInt(args[1]);
            final int max = Integer.parseInt(args[2]);
            return lb.printLB(min, max);
        }
        else
            return "Invalid arguments of 'lb'\nUsage: \nlb <raid>/<class> (max)\nlb <raid>/<class> (min) (max)";

    }

}
