package raidaLB;
// TODO wip

import java.io.Serializable;
import java.util.Hashtable;
import raidaLB.Raida.Raid;
import raidaLB.Raida.WynnClass;

public class LBManager implements Serializable {
    private static final long serialVersionUID = 8451356162401470407L;
    private Leaderboard allLB;
    private Hashtable<Raid, Leaderboard> rairLbs;
    private Hashtable<WynnClass, Leaderboard> classLbs;

}
