package raidaLB;

import raidaLB.Raida.Raid;

public class Formatter {

    public static final String NOTG_NAME = "Nest of the Grootslangs";
    public static final String NOL_NAME = "Orphion's Nexus of Light";
    public static final String TCC_NAME = "The Canyon Colossus";


    /**
     * Helper function: gets full Raid name from filter
     *
     * @param rf
     * @return
     *         Full raid name
     */
    public static String rfToName(final Raid rf) {
        switch (rf) {
            case nol:
                return NOL_NAME;
            case notg:
                return NOTG_NAME;
            case tcc:
                return TCC_NAME;

        }
        return null;
    }


    public static String capitalize(final String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
