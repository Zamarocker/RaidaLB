package raidaLB;

import java.io.Serializable;
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

    private WynncraftPlayer player;


    public void refresh(final WynncraftAPI api) {
        // Asks api for updated player info
        player = getPlayer(player.getUsername(), api);
    }


    public Raida(final WynncraftPlayer player) {
        this.player = player;
    }


    public Raida(final String ign, final WynncraftAPI api) {
        this(getPlayer(ign, api));
    }


    public String getIGN() {
        return player.getUsername();
    }


    public int getRaidcount() {
        return getRaidcount(null, null);
    }


    public int getRaidcount(final Raid rf) {
        return getRaidcount(rf, null);
    }


    public int getRaidcount(final WynnClass cf) {
        return getRaidcount(null, cf);
    }


    public int getRaidcount(final Raid rf, final WynnClass cf) {
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
    private String getBaseClass(final WynncraftPlayerClass character) {
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


    private int getClassRaidCount(
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


    private String rfToName(final Raid rf) {
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


    public String summarize() {
        return summarize(null, null);
    }


    public String summarize(final Raid rf) {
        return summarize(rf, null);
    }


    public String summarize(final WynnClass cf) {
        return summarize(null, cf);
    }


    public String summarize(final Raid rf, final WynnClass cf) {
        final StringBuilder builder = new StringBuilder();
        final String format = "%s: %d\n";

        // Header
        final String header = String.format("Stats: %s, Filtered by: %s, %s\n",
            player.getUsername(), rf != null ? rf.toString() : "all raids",
            cf != null ? cf.toString() : "all classes");
        builder.append(header);

        int sum = 0;

        // NOTG
        if (rf == null || rf == Raid.notg) {
            final int notgCount = getRaidcount(Raid.notg, cf);
            sum += notgCount;
            final String notg = String.format(format, NOTG_NAME, notgCount);
            builder.append(notg);
        }

        // NOL
        if (rf == null || rf == Raid.nol) {
            final int nolCount = getRaidcount(Raid.nol, cf);
            sum += nolCount;
            final String nol = String.format(format, NOL_NAME, nolCount);
            builder.append(nol);
        }

        // TCC
        if (rf == null || rf == Raid.tcc) {
            final int tccCount = getRaidcount(Raid.tcc, cf);
            sum += tccCount;
            final String tcc = String.format(format, TCC_NAME, tccCount);
            builder.append(tcc);
        }

        // Totals (displayed only if no raid filtering)
        if (rf == null) {
            final String all = String.format(format, "Total", sum);
            builder.append(all);
        }

        return builder.toString();

    }

}
