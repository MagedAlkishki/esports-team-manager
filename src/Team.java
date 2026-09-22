import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;
    private Region region;
    private ArrayList<Player> roster = new ArrayList<>();

    public Team(String name, Region region) {
        this.name = name;
        this.region = region;
    }

    public String getName() { return name; }
    public Region getRegion() { return region; }
    public ArrayList<Player> getRoster() { return roster; }

    public boolean addPlayer(Player player) {
        RegionRules rules = RegionRules.forRegion(region);
        int maxRosterSize = rules.getMinStarters() + rules.getMaxSubs();
        if (roster.size() >= maxRosterSize) {
            System.out.println("\"" + name + "\" already has the maximum of " + maxRosterSize +
                    " players (" + rules.getMinStarters() + " starters + " + rules.getMaxSubs() + " subs).");
            return false;
        }
        roster.add(player);
        return true;
    }

    public boolean removePlayer(String ign) {
        Player found = findPlayer(ign);
        if (found == null) return false;
        roster.remove(found);
        return true;
    }

    public Player findPlayer(String ign) {
        for (Player player : roster) {
            if (player.getIgn().equalsIgnoreCase(ign)) return player;
        }
        return null;
    }

    public List<String> checkRules() {
        RegionRules rules = RegionRules.forRegion(region);
        return rules.checkCompliance(roster);
    }

    @Override
    public String toString() {
        RegionRules rules = RegionRules.forRegion(region);
        int maxRosterSize = rules.getMinStarters() + rules.getMaxSubs();
        return name + " [" + regionLabel(region) + "] - " + roster.size() + "/" + maxRosterSize + " players";
    }

    public static String regionLabel(Region region) {
        return switch (region) {
            case EMEA_MENA -> "EMEA (MENA)";
            default -> region.toString();
        };
    }
}