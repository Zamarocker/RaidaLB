package raidaLB;

import java.util.ArrayList;
import java.util.Comparator;
import raidaLB.Raida.Raid;
import raidaLB.Raida.WynnClass;

public class Leaderboard {

    private final ArrayList<Raida> playas;


    public Leaderboard(final ArrayList<Raida> arrayList) {
        this.playas = arrayList;
    }


    public String generateLB(final RaidaComparator comparator) {
        playas.sort(comparator);
        final StringBuilder builder = new StringBuilder();
        int count = 0;

        final Raid rf = comparator.rf;
        final WynnClass cf = comparator.cf;

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


    public static class RaidaComparator implements Comparator<Raida> {

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
