import java.util.ArrayList;
import java.util.List;

public class RegionRules {
    private final int minStarters;
    private final int maxSubs;
    private final int minEmeaResidents;
    private final int minMenaLtrs;

    public RegionRules(int minStarters, int maxSubs, int minEmeaResidents, int minMenaLtrs) {
        this.minStarters = minStarters;
        this.maxSubs = maxSubs;
        this.minEmeaResidents = minEmeaResidents;
        this.minMenaLtrs = minMenaLtrs;
    }

    public static RegionRules forRegion(Region region) {
        return switch (region) {
            // EMEA Challengers Rulebook v2.1, section 1.2: min 3 EMEA Residents on the starting roster.
            case EMEA -> new RegionRules(5, 2, 3, 0);
            // MENA Kickoff Rulebook v2.0: min 3 MENA LTRs AND 3 EMEA Residents on the starting roster.
            case EMEA_MENA -> new RegionRules(5, 2, 3, 3);
            // No rulebook read yet for these, so only the general roster size applies.
            default -> new RegionRules(5, 2, 0, 0);
        };
    }

    public List<String> checkCompliance(List<Player> starters) {
        List<String> problems = new ArrayList<>();

        if (starters.size() < minStarters) {
            problems.add("Needs " + minStarters + " starters, currently has " + starters.size() + ".");
        }

        int emeaResidents = 0;
        int menaLtrs = 0;
        for (Player player : starters) {
            if (player.getResidency().countsAsEmeaResident()) emeaResidents++;
            if (player.getResidency() == Residency.MENA_LTR) menaLtrs++;
        }

        if (emeaResidents < minEmeaResidents) {
            problems.add("Needs at least " + minEmeaResidents + " EMEA Residents on the starting roster, currently has " + emeaResidents + ".");
        }
        if (menaLtrs < minMenaLtrs) {
            problems.add("Needs at least " + minMenaLtrs + " MENA LTRs on the starting roster, currently has " + menaLtrs + ".");
        }

        return problems;
    }

    public int getMinStarters() { return minStarters; }
    public int getMaxSubs() { return maxSubs; }
}