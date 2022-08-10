package raidaLB;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Hashtable;
import me.bed0.jWynn.WynncraftAPI;
import me.bed0.jWynn.api.v2.player.WynncraftPlayer;
import me.bed0.jWynn.api.v2.player.classes.WynncraftPlayerClass;
import me.bed0.jWynn.api.v2.player.classes.WynncraftPlayerClassRaidIndividual;

public class Raida implements Serializable {
    private static final long serialVersionUID = 4389842069018687021L;


    public static enum Raid {
        notg, nol, tcc
    }


    public static enum WynnClass {
        warrior, archer, mage, assassin, shaman
    }

    public static final String NOTG_NAME = "Nest of the Grootslangs";
    public static final String NOL_NAME = "Orphion's Nexus of Light";
    public static final String TCC_NAME = "The Canyon Colossus";

    // minimum time before refresh, can be bypassed with forceRefresh()
    private static final long REFRESH_CD = (15L * 60L * 1000L);

    // Data structures to store totals in raidcount categories
    // Utilized in all getRaidcount() calls to avoid unnecessary recalculations
    // from WynncraftPlayer stats

    private Integer allRaids;
    private final Hashtable<Raid, Integer> raidCounts;
    private final Hashtable<WynnClass, Integer> classRaidCounts;
    private Timestamp lastRefreshed;

    private final String ign;
    private transient WynncraftPlayer player;


    public void refresh() {

        // Check cooldown, or existence of player object (null on program
        // reload)
        if (lastRefreshed == null || player == null || lastRefreshed.getTime()
            + REFRESH_CD < System.currentTimeMillis())
            // Refresh
            forceRefresh();
    }


    public void forceRefresh() {
        lastRefreshed = new Timestamp(System.currentTimeMillis());
        // Asks api for updated player info
        player = getPlayer(ign, Driver.getApi());

        // Clear cache
        allRaids = null;
        raidCounts.clear();
        classRaidCounts.clear();

    }


    public Raida(final WynncraftPlayer player) {
        this.player = player;
        ign = player.getUsername();
        raidCounts = new Hashtable<Raida.Raid, Integer>();
        classRaidCounts = new Hashtable<Raida.WynnClass, Integer>();

    }


    public Raida(final String ign) {
        this(getPlayer(ign, Driver.getApi()));
    }


    public String getIGN() {
        return ign;
    }


    public int getRaidcount() {
        if (allRaids != null)
            return allRaids;
        allRaids = calcRaidcount(null, null);
        return allRaids;
    }


    public int getRaidcount(final Raid rf) {
        Integer count = raidCounts.get(rf);
        if (count != null)
            return count;
        count = calcRaidcount(rf, null);
        raidCounts.put(rf, count);
        return count;
    }


    public int getRaidcount(final WynnClass cf) {
        Integer count = classRaidCounts.get(cf);
        if (count != null)
            return count;
        count = calcRaidcount(null, cf);
        classRaidCounts.put(cf, count);
        return count;
    }


    public int getRaidcount(final Raid rf, final WynnClass cf) {
        // Appropriate methods used when possible

        if (rf == null && cf == null)
            return getRaidcount();
        if (rf != null && cf == null)
            return getRaidcount(rf);
        if (rf == null && cf != null)
            return getRaidcount(cf);

        return calcRaidcount(rf, cf);
    }


    private int calcRaidcount(final Raid rf, final WynnClass cf) {
        refresh();
        int sum = 0;

        // for each class on account:
        for (final WynncraftPlayerClass character : player.getClasses()) {
            // Filter class if provided
            if (cf == null || WynnClass.valueOf(getBaseClass(character)).equals(
                cf))
                // Get raidcount
                sum += getClassRaidCount(character, rf);

        }
        return sum;
    }


    /*
     * This exists because He forgor skyseer reskin
     */
    private static String getBaseClass(final WynncraftPlayerClass character) {
        final String replacedName = character.getName().replaceAll("\\d", "");
        switch (replacedName) {
            case "darkwizard":
                return "mage";
            case "ninja":
                return "assassin";
            case "knight":
                return "warrior";
            case "hunter":
                return "archer";
            case "skyseer":
                return "shaman";

            default:
                return replacedName;
        }
    }


    private static int getClassRaidCount(
        final WynncraftPlayerClass character,
        final Raid rf) {

        // Case 1: no raid filter
        // Get total raids
        if (rf == null)
            return character.getRaids().getCompleted();

        // Otherwise match full raid name to provided filter
        final WynncraftPlayerClassRaidIndividual[] list = character.getRaids()
            .getList();
        final String raidName = rfToName(rf);

        // Finds count of specified raid, if it exists
        // Returns 0 if not found
        for (final WynncraftPlayerClassRaidIndividual entry : list)
            if (entry.getName().equals(raidName))
                return entry.getCompleted();
        return 0;

    }


    private static String rfToName(final Raid rf) {
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


    public static WynncraftPlayer getPlayer(
        final String ign,
        final WynncraftAPI api) {
        return api.v2().player().stats(ign).run()[0];
    }


    public String summarize() throws IOException {
        // Summary calls force refresh, for convenience
        forceRefresh();
        final StringBuilder builder = new StringBuilder();
        final String format = "%s: %d\n";
        final String formatWithPlacement = "%s: %d\t(#%d)\n";
        final LBManager lbm = LBManager.getInstance();

        // Header
        final String header = String.format("Stats: %s\n", player
            .getUsername());
        builder.append(header);

        // by raid

        builder.append("\n");
        builder.append("By Raid: \n");

        for (final Raid rf : Raid.values()) {
            final int count = getRaidcount(rf);
            // Check count, omit if zero
            if (count != 0) {
                final int placement = lbm.getLB(rf).placementOf(this);
                String entry = null;
                // Check for any leaderboard placements
                if (placement > 0)
                    entry = String.format(formatWithPlacement, rfToName(rf),
                        count, placement);

                else
                    entry = String.format(format, rfToName(rf), count);
                builder.append(entry);
            }
        }

        // by class

        builder.append("\n");
        builder.append("By class: \n");
        for (final WynnClass cf : WynnClass.values()) {
            final int count = getRaidcount(cf);
            final String className = cf.toString();
            // Check count, omit if zero
            if (count != 0) {
                final int placement = lbm.getLB(cf).placementOf(this);
                String entry = null;
                // Check for any leaderboard placements
                if (placement > 0)
                    entry = String.format(formatWithPlacement, className, count,
                        placement);
                else
                    entry = String.format(format, className, count);
                builder.append(entry);
            }
        }

        builder.append("\n");
        // Totals
        final int count = getRaidcount();

        final int placement = lbm.getLB().placementOf(this);
        String entry = null;
        // Check for any leaderboard placements
        if (placement > 0)
            entry = String.format(formatWithPlacement, "Total", count,
                placement);
        else
            entry = String.format(format, "Total", count);

        builder.append(entry);
        return builder.toString();

    }


    @Override
    public boolean equals(final Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Raida))
            return false;
        return ign.equals(((Raida)obj).ign);
    }

}
