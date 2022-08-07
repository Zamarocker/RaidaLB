package raidaLB;

import java.io.IOException;
import org.apache.commons.lang3.EnumUtils;
import me.bed0.jWynn.WynncraftAPI;
import raidaLB.Leaderboard.RaidaComparator;
import raidaLB.Raida.Raid;
import raidaLB.Raida.WynnClass;

public class CommandManager {

    private static WynncraftAPI api;
    private static Leaderboard lb;


    public static void init() throws IOException {
        api = new WynncraftAPI();
        lb = new Leaderboard(ConfigManager.loadPlayerFile(api));

    }


/*
 * Commands:
 * stats <ign>
 * lb <raid>
 * lb <class>
 * lb <raid> <class>
 *
 */
    public static String run(String cmd) {
        cmd = cmd.toLowerCase();

        final String[] cmdarr = cmd.split("\\s+");
        final String command = cmdarr[0];

        final String[] args = new String[cmdarr.length - 1];
        System.arraycopy(cmdarr, 1, args, 0, args.length);

        if (command.equals("stats")) {
            if (args.length != 1)
                return "Incorrect usage of 'stats'\nUsage: stats <ign>";
            return (new Raida(args[0], api).summarize());
        }
        if (command.equals("lb")) {
            if (args.length == 1) {
                // arg is raid
                if (EnumUtils.isValidEnum(Raid.class, args[0]))
                    return lb.generateLB(new RaidaComparator(Raid.valueOf(
                        args[0]), null));
                // arg is class
                if (EnumUtils.isValidEnum(WynnClass.class, args[0]))
                    return lb.generateLB(new RaidaComparator(null, WynnClass
                        .valueOf(args[0])));
                else
                    return "Incorrect usage of 'lb'\nUsage: lb <raid>/<class>";
            }
            if (args.length == 2) {
                if (EnumUtils.isValidEnum(Raid.class, args[0]) && EnumUtils
                    .isValidEnum(WynnClass.class, args[1]))
                    return lb.generateLB(new RaidaComparator(Raid.valueOf(
                        args[0]), WynnClass.valueOf(args[1])));
                else
                    return "Incorrect usage of 'lb'\nUsage: lb <raid> <class>";
            }
        }

        return null;

    }

}
