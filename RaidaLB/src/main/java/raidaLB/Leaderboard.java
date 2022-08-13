package raidaLB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import raidaLB.Raida.Raid;
import raidaLB.Raida.WynnClass;

/**
 * Object to represent a raid Leaderboard
 */
public class Leaderboard implements Serializable {
    private static final long serialVersionUID = -8045090452313515439L;
    private static final int DEFAULT_PRINT_COUNT = 20;
    private static final int LBCAP = 100;
    private final List<Raida> playas;
    private final RaidaComparator comp;


    /**
     * Creates a new leaderboard object
     *
     * @param comparator
     *            RaidaComparator param to dictate how this leaderboard sorts
     */
    public Leaderboard(final RaidaComparator comparator) {
        playas = new ArrayList<Raida>();
        comp = comparator;
    }


    /**
     * Adds a Raida to this leaderboard, if they dont exist
     *
     * @param playa
     */
    public void add(final Raida playa) {
        if (!playas.contains(playa)) {
            playas.add(playa);
            sort();
        }

        // Removes last entry if cap is reached
        if (playas.size() > LBCAP)
            playas.remove(playas.size() - 1);

    }


    /**
     * Checks if a Raida qualifies for this leaderboard, checking various
     * conditions
     *
     * @param playa
     * @return
     *         if qualifies
     */
    public boolean qualifies(final Raida playa) {
        // Players with 0 raids never qualify
        if (playa.getRaidcount(comp.rf, comp.cf) == 0)
            return false;

        // Players always qualify if the leaderboard isnt full
        if (playas.size() < LBCAP)
            return true;
        // Otherwise see if they exceed the count of last place
        if (playas.isEmpty() || comp.compare(playa, playas.get(playas.size()
            - 1)) < 0)
            return true;
        return false;
    }


    /**
     * Returns the leaderboard placement/position of the provided Raida
     *
     * @param playa
     * @return
     *         leaderboard position
     */
    public int placementOf(final Raida playa) {
        return playas.indexOf(playa) + 1;
    }


    /**
     * Prints the leaderboard with default range
     *
     * @return
     *         Formatted String Leaderboard Summary
     */
    public String printLB() {
        return printLB(0, DEFAULT_PRINT_COUNT);

    }


    /**
     * Prints the leaderboard given a range
     *
     * @param min
     * @param max
     * @return
     *         Formatted String Leaderboard Summary
     */
    public String printLB(final int min, final int max) {
        sort();
        final StringBuilder builder = new StringBuilder();

        final Raid rf = comp.rf;
        final WynnClass cf = comp.cf;

        String header = null;

        // No filter header
        if (rf == null && cf == null)
            header = "Total Leaderboard";
        // Raid filter header
        else if (rf != null && cf == null)
            header = String.format("%s Leaderboard\n", rf.toString()
                .toUpperCase());
        // Class filter header
        else if (rf == null && cf != null)
            header = String.format("%s Leaderboard\n", Formatter.capitalize(cf
                .toString()));
        // Unused (both filter) header
        else
            header = String.format("%s, %s Leaderboard\n", rf != null
                ? rf.toString()
                : "All raids", cf != null ? cf.toString() : "All classes");

        builder.append(header);

        for (int i = min; i < Math.min(playas.size(), max); i++) {
            final Raida entry = playas.get(i);

            final String line = String.format("%d. %s - %d Raids\n", i + 1,
                entry.getIGN(), entry.getRaidcount(rf, cf));
            builder.append(line);
        }
        return builder.toString();

    }


    /**
     * Sorts the leaderboard by its comparator
     */
    private void sort() {
        Collections.sort(playas, comp);

    }


    public static class RaidaComparator
        implements Comparator<Raida>, Serializable {

        private static final long serialVersionUID = 3420663841597749404L;
        private final Raid rf;
        private final WynnClass cf;


        public RaidaComparator(final Raid rf, final WynnClass cf) {
            this.rf = rf;
            this.cf = cf;
        }


        @Override
        public int compare(final Raida o1, final Raida o2) {
            return Integer.compare(o2.getRaidcount(rf, cf), o1.getRaidcount(rf,
                cf));
        }

    }
}
