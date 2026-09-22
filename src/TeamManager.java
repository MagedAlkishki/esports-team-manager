import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamManager {
    private ArrayList<Team> teams = new ArrayList<>();

    public boolean addTeam(Team team) {
        if (findTeam(team.getName()) != null) {
            System.out.println("A team called \"" + team.getName() + "\" already exists.");
            return false;
        }
        teams.add(team);
        return true;
    }

    public Team findTeam(String name) {
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(name)) return team;
        }
        return null;
    }

    public void addPlayerToTeam(String teamName, Player player) {
        Team team = findTeam(teamName);
        if (team == null) { System.out.println("No team called \"" + teamName + "\"."); return; }
        if (team.findPlayer(player.getIgn()) != null) {
            System.out.println(player.getIgn() + " is already on \"" + teamName + "\".");
            return;
        }
        if (team.addPlayer(player)) {
            System.out.println("Added " + player.getIgn() + " to " + teamName + ".");
        }
    }

    public void removePlayerFromTeam(String teamName, String ign) {
        Team team = findTeam(teamName);
        if (team == null) { System.out.println("No team called \"" + teamName + "\"."); return; }
        if (team.removePlayer(ign)) {
            System.out.println("Removed " + ign + " from " + teamName + ".");
        } else {
            System.out.println(ign + " is not on \"" + teamName + "\".");
        }
    }

    public void listTeams() {
        System.out.println("Teams:");
        if (teams.isEmpty()) { System.out.println(" (no teams yet)"); return; }
        for (Team team : teams) System.out.println(" - " + team);
    }

    public void listRoster(String teamName) {
        Team team = findTeam(teamName);
        if (team == null) { System.out.println("No team called \"" + teamName + "\"."); return; }
        System.out.println("Roster for " + team.getName() + ":");
        if (team.getRoster().isEmpty()) { System.out.println(" (no players yet)"); return; }
        for (Player player : team.getRoster()) System.out.println(" - " + player);
    }

    public void exportHtmlReport(String filePath) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Esports Team Manager Report</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 40px; background: #f7f7f7; }");
        html.append("h1 { color: #1f1f1f; }");
        html.append(".team { background: white; border-radius: 8px; padding: 16px 24px; margin-bottom: 24px; box-shadow: 0 1px 3px rgba(0,0,0,0.15); }");
        html.append(".team h2 { margin-top: 0; }");
        html.append("table { border-collapse: collapse; width: 100%; margin-top: 8px; }");
        html.append("th, td { text-align: left; padding: 6px 10px; border-bottom: 1px solid #ddd; }");
        html.append(".ok { color: #1a7f37; font-weight: bold; }");
        html.append(".fail { color: #c62828; font-weight: bold; }");
        html.append("</style></head><body>");
        html.append("<h1>Esports Team Manager Report</h1>");

        if (teams.isEmpty()) {
            html.append("<p>No teams yet.</p>");
        }

        for (Team team : teams) {
            RegionRules rules = RegionRules.forRegion(team.getRegion());
            int maxRosterSize = rules.getMinStarters() + rules.getMaxSubs();

            html.append("<div class=\"team\">");
            html.append("<h2>").append(team.getName()).append(" &mdash; ").append(Team.regionLabel(team.getRegion())).append("</h2>");
            html.append("<p>Roster: ").append(team.getRoster().size()).append("/").append(maxRosterSize).append(" player(s)</p>");

            html.append("<table><tr><th>IGN</th><th>Name</th><th>Role</th><th>Flex roles</th><th>Residency</th></tr>");
            for (Player player : team.getRoster()) {
                html.append("<tr>");
                html.append("<td>").append(player.getIgn()).append("</td>");
                html.append("<td>").append(player.getName()).append("</td>");
                html.append("<td>").append(player.getRole()).append("</td>");
                html.append("<td>").append(flexRolesToText(player)).append("</td>");
                html.append("<td>").append(player.getResidency()).append("</td>");
                html.append("</tr>");
            }
            html.append("</table>");

            List<String> problems = team.checkRules();
            if (problems.isEmpty()) {
                html.append("<p class=\"ok\">Meets all roster rules.</p>");
            } else {
                html.append("<p class=\"fail\">Does not meet the roster rules:</p><ul>");
                for (String problem : problems) {
                    html.append("<li>").append(problem).append("</li>");
                }
                html.append("</ul>");
            }

            html.append("</div>");
        }

        html.append("</body></html>");

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(html.toString());
            System.out.println("Report written to " + filePath);
        } catch (IOException e) {
            System.out.println("Could not write the report: " + e.getMessage());
        }
    }

    private String flexRolesToText(Player player) {
        if (player.getRole() != Role.FLEX || player.getFlexRoles().isEmpty()) return "-";
        List<Role> flexRoles = player.getFlexRoles();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < flexRoles.size(); i++) {
            if (i > 0) text.append(", ");
            text.append(flexRoles.get(i));
        }
        return text.toString();
    }
}