import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private String ign;
    private Role role;
    private List<Role> flexRoles;
    private Residency residency;

    public Player(String name, String ign, Role role, Residency residency) {
        this(name, ign, role, new ArrayList<>(), residency);
    }

    public Player(String name, String ign, Role role, List<Role> flexRoles, Residency residency) {
        this.name = name;
        this.ign = ign;
        this.role = role;
        this.flexRoles = flexRoles;
        this.residency = residency;
    }

    public String getName() { return name; }
    public String getIgn() { return ign; }
    public Role getRole() { return role; }
    public List<Role> getFlexRoles() { return flexRoles; }
    public Residency getResidency() { return residency; }

    @Override
    public String toString() {
        String base = ign + " (" + name + ") - " + role + " [" + residency + "]";
        if (role == Role.FLEX && !flexRoles.isEmpty()) {
            base += " (flexes: " + flexRolesText() + ")";
        }
        return base;
    }

    private String flexRolesText() {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < flexRoles.size(); i++) {
            if (i > 0) text.append(", ");
            text.append(flexRoles.get(i));
        }
        return text.toString();
    }
}