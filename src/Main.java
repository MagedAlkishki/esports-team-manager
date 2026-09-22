import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TeamManager manager = new TeamManager();
        manager.addTeam(new Team("Occupy", Region.EMEA_MENA));
        manager.addTeam(new Team("RAFHA", Region.EMEA_MENA));

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addTeamFromInput(manager, scanner);
                case "2" -> addPlayerFromInput(manager, scanner);
                case "3" -> {
                    System.out.print("Team name: ");
                    String teamName = scanner.nextLine().trim();
                    System.out.print("Player IGN to remove: ");
                    String ign = scanner.nextLine().trim();
                    manager.removePlayerFromTeam(teamName, ign);
                }
                case "4" -> manager.listTeams();
                case "5" -> { System.out.print("Team name: "); manager.listRoster(scanner.nextLine().trim()); }
                case "6" -> {
                    System.out.print("Team name: ");
                    checkCompliance(manager, scanner.nextLine().trim());
                }
                case "7" -> manager.exportHtmlReport("team_report.html");
                case "0" -> { System.out.println("Goodbye!"); running = false; }
                default -> System.out.println("Please choose a number from the menu.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("=== Esports Team Manager ===");
        System.out.println("1. Add a team");
        System.out.println("2. Add a player to a team");
        System.out.println("3. Remove a player from a team");
        System.out.println("4. List teams");
        System.out.println("5. List a team's roster");
        System.out.println("6. Check a team's rule compliance");
        System.out.println("7. Export HTML report");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    private static void addTeamFromInput(TeamManager manager, Scanner scanner) {
        Region region = chooseRegion(scanner); // Region first: it decides which competitive rules and leagues apply.
        System.out.print("Organisation name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) { System.out.println("The name can't be empty."); return; }
        if (manager.addTeam(new Team(name, region))) {
            System.out.println("Added team: " + name);
        }
    }

    private static void addPlayerFromInput(TeamManager manager, Scanner scanner) {
        System.out.print("Team name: ");
        String teamName = scanner.nextLine().trim();
        System.out.print("Player real name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Player IGN: ");
        String ign = scanner.nextLine().trim();
        Role role = chooseRole(scanner);
        Residency residency = chooseResidency(scanner);
        if (role == Role.FLEX) {
            List<Role> flexRoles = chooseFlexRoles(scanner);
            manager.addPlayerToTeam(teamName, new Player(name, ign, role, flexRoles, residency));
        } else {
            manager.addPlayerToTeam(teamName, new Player(name, ign, role, residency));
        }
    }

    private static Region chooseRegion(Scanner scanner) {
        System.out.println("Region:");
        System.out.println("  1. Americas");
        System.out.println("  2. EMEA");
        System.out.println("  3. EMEA - MENA");
        System.out.println("  4. Pacific");
        System.out.println("  5. China");
        System.out.print("Choose a region number: ");
        try {
            int number = Integer.parseInt(scanner.nextLine().trim());
            return switch (number) {
                case 1 -> Region.AMERICAS;
                case 2 -> Region.EMEA;
                case 3 -> Region.EMEA_MENA;
                case 4 -> Region.PACIFIC;
                case 5 -> Region.CHINA;
                default -> { System.out.println("Unknown number, using EMEA - MENA."); yield Region.EMEA_MENA; }
            };
        } catch (NumberFormatException e) {
            System.out.println("That is not a number, using EMEA - MENA.");
            return Region.EMEA_MENA;
        }
    }

    private static Role chooseRole(Scanner scanner) {
        Role[] roles = Role.values();
        System.out.println("Roles:");
        for (int i = 0; i < roles.length; i++) System.out.println("  " + (i + 1) + ". " + roles[i]);
        System.out.print("Choose a role number: ");
        try {
            int number = Integer.parseInt(scanner.nextLine().trim());
            if (number >= 1 && number <= roles.length) return roles[number - 1];
            System.out.println("Unknown number, using DUELIST.");
        } catch (NumberFormatException e) {
            System.out.println("That is not a number, using DUELIST.");
        }
        return Role.DUELIST;
    }

    private static List<Role> chooseFlexRoles(Scanner scanner) {
        // A FLEX player still needs specific roles they can cover, not just the word "flex".
        Role[] roles = Role.values();
        System.out.println("Which roles does this player flex between?");
        for (int i = 0; i < roles.length; i++) {
            if (roles[i] != Role.FLEX) System.out.println("  " + (i + 1) + ". " + roles[i]);
        }
        System.out.print("Enter the numbers separated by commas (e.g. 1,3): ");
        String input = scanner.nextLine().trim();
        List<Role> chosen = new ArrayList<>();
        if (input.isEmpty()) return chosen;
        for (String part : input.split(",")) {
            try {
                int number = Integer.parseInt(part.trim());
                if (number >= 1 && number <= roles.length && roles[number - 1] != Role.FLEX) {
                    Role role = roles[number - 1];
                    if (!chosen.contains(role)) chosen.add(role);
                }
            } catch (NumberFormatException e) { /* ignore invalid numbers */ }
        }
        return chosen;
    }

    private static Residency chooseResidency(Scanner scanner) {
        System.out.println("Residency:");
        System.out.println("  1. MENA LTR (legal resident of a MENA country)");
        System.out.println("  2. EMEA Resident (legal resident/citizen of an EMEA country, outside MENA)");
        System.out.println("  3. Other (not an EMEA resident)");
        System.out.print("Choose a residency number: ");
        try {
            int number = Integer.parseInt(scanner.nextLine().trim());
            return switch (number) {
                case 1 -> Residency.MENA_LTR;
                case 2 -> Residency.EMEA_RESIDENT;
                case 3 -> Residency.OTHER;
                default -> { System.out.println("Unknown number, using OTHER."); yield Residency.OTHER; }
            };
        } catch (NumberFormatException e) {
            System.out.println("That is not a number, using OTHER.");
            return Residency.OTHER;
        }
    }

    private static void checkCompliance(TeamManager manager, String teamName) {
        Team team = manager.findTeam(teamName);
        if (team == null) { System.out.println("No team called \"" + teamName + "\"."); return; }
        List<String> problems = team.checkRules();
        if (problems.isEmpty()) {
            System.out.println(team.getName() + " meets the roster rules for " + Team.regionLabel(team.getRegion()) + ".");
        } else {
            System.out.println(team.getName() + " does NOT meet the roster rules for " + Team.regionLabel(team.getRegion()) + ":");
            for (String problem : problems) System.out.println(" - " + problem);
        }
    }
}