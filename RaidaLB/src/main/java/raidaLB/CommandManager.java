package raidaLB;

import java.io.IOException;
import org.apache.commons.lang3.EnumUtils;
import raidaLB.Raida.Raid;
import raidaLB.Raida.WynnClass;

public class CommandManager {
    private static RaidaArchive archive;
    private static LBManager lbm;


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
    public static String run(final String command, final String[] args)
        throws IOException {

        if (command.equals("stats")) {
            if (args.length != 1)
                return "Incorrect usage of 'stats'\nUsage: stats <ign>";

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
        if (command.equals("lb")) {
            if (args.length == 0) {
                return lbm.getLB().printLB();
            }
            if (args.length == 1) {
                // arg is raid
                if (EnumUtils.isValidEnum(Raid.class, args[0]))
                    return lbm.getLB(Raid.valueOf(args[0])).printLB();
                // arg is class
                if (EnumUtils.isValidEnum(WynnClass.class, args[0]))
                    return lbm.getLB(WynnClass.valueOf(args[0])).printLB();
                else
                    return "Incorrect usage of 'lb'\nUsage: lb <raid>/<class>";
            }
        }

        if (command.equals("quit") && args.length == 0)
            Driver.exit();

        return "Invalid command, See usage on README.MD";

    }

}
