package raidaLB;

import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;
import raidaLB.Leaderboard.RaidaComparator;
import raidaLB.Raida.Raid;
import raidaLB.Raida.WynnClass;

/**
 * A class to provide easy access to all leaderboards
 */
public class LBManager implements Serializable {
    private static final long serialVersionUID = 8451356162401470407L;

    private final Leaderboard allLB;
    private final Hashtable<Raid, Leaderboard> raidLbs;
    private final Hashtable<WynnClass, Leaderboard> classLbs;


    /**
     * Gets the current LBManager instance
     *
     * @return
     *         This program's LBManager instance
     * @throws IOException
     */
    public static LBManager getInstance() throws IOException {
        return SaveManager.getInstance().getLBM();
    }


    /**
     * Constructs a new LBManager
     */
    public LBManager() {

        // Creates All raid leaderboard
        allLB = new Leaderboard(new RaidaComparator(null, null));

        // Creates Raid LB
        raidLbs = new Hashtable<Raid, Leaderboard>();

        for (final Raid rf : Raid.values()) {
            raidLbs.put(rf, new Leaderboard(new RaidaComparator(rf, null)));
        }

        // Creates Class LB
        classLbs = new Hashtable<WynnClass, Leaderboard>();

        for (final WynnClass cf : WynnClass.values()) {
            classLbs.put(cf, new Leaderboard(new RaidaComparator(null, cf)));
        }
    }


    /**
     * Attempts to add Raida to all existing leaderboards
     * 
     * @param playa
     */
    public void add(final Raida playa) {

        if (allLB.qualifies(playa))
            allLB.add(playa);

        for (final Raid rf : Raid.values()) {
            final Leaderboard lb = (raidLbs.get(rf));
            if (lb.qualifies(playa))
                lb.add(playa);
        }
        for (final WynnClass cf : WynnClass.values()) {
            final Leaderboard lb = (classLbs.get(cf));
            if (lb.qualifies(playa))
                lb.add(playa);
        }
    }


    /**
     * Get the all raids leaderboard
     * 
     * @return
     *         all Leaderboard
     */
    public Leaderboard getLB() {
        return allLB;
    }


    /**
     * Get a raid leaderboard by filter
     * 
     * @param rf
     * @return
     *         raid Leaderboard
     */
    public Leaderboard getLB(final Raid rf) {
        return raidLbs.get(rf);
    }


    /**
     * Get a class leaderboard by filter
     * 
     * @param cf
     * @return
     *         class Leaderboard
     */
    public Leaderboard getLB(final WynnClass cf) {
        return classLbs.get(cf);
    }

}
