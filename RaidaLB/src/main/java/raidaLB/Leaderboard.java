package raidaLB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import raidaLB.Raida.Raid;
import raidaLB.Raida.WynnClass;

public class Leaderboard implements Serializable {
    private static final long serialVersionUID = -8045090452313515439L;
    private static final int LBCAP = 100;
    private final List<Raida> playas;
    private final RaidaComparator comp;


    public Leaderboard(final RaidaComparator comparator) {
        playas = new ArrayList<Raida>();
        comp = comparator;
    }


    public void add(final Raida playa) {
        if (!playas.contains(playa)) {
            playas.add(playa);
            sort();
        }

        // Removes last entry if cap is reached
        if (playas.size() > LBCAP)
            playas.remove(playas.size() - 1);

    }


    public boolean qualifies(final Raida playa) {
        // Players always qualify if the leaderboard isnt full
        if (playas.size() < LBCAP)
            return true;
        // Otherwise see if they exceed the count of last place
        if (playas.isEmpty() || comp.compare(playa, playas.get(playas.size()
            - 1)) < 0)
            return true;
        return false;
    }


    public int placementOf(final Raida playa) {
        return playas.indexOf(playa) + 1;
    }


    public String printLB() {
        sort();
        final StringBuilder builder = new StringBuilder();
        int count = 0;

        final Raid rf = comp.rf;
        final WynnClass cf = comp.cf;

        final String header = String.format("%s, %s Leaderboard\n", rf != null
            ? rf.toString()
            : "all raids", cf != null ? cf.toString() : "all classes");
        builder.append(header);

        for (final Raida entry : playas) {
            count++;
            final String line = String.format("%d. %s - %d Raids\n", count,
                entry.getIGN(), entry.getRaidcount(rf, cf));
            builder.append(line);
        }
        return builder.toString();

    }


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
