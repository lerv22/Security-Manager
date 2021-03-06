package Beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Javier
 */
public class User implements Serializable {

    private String name;
    private PrivLevel userLevel;//DEPRECATED.
    private boolean auditing;
    private ArrayList<Role> listRoles = new ArrayList<>();

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole(String name) {
        return listRoles.stream()
                .filter((r) -> r.getName().equals(name))
                .findFirst()
                .get();
    }

    public void setRole(Role rol) {

        if ((!listRoles.stream().anyMatch((r) -> r.getName().equals(rol.getName())))) {
            listRoles.add(rol);
        }
    }

    public void grantRole(Role rol) {
        this.setRole(rol);
        //ADD DATABASE UPDATE COMMAND HERE.
        DBConnector.otorgaRol(rol.getName(), name);
    }

    public PrivLevel getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(PrivLevel userLevel) {
        this.userLevel = userLevel;
    }

    public boolean isAuditing() {
        return auditing;
    }

    public void setAuditing(boolean auditing) {
        this.auditing = auditing;
    }

    public static User createFromDatabase() {
        return null;//Should somehow get a role already created and represent it as a logical object.
    }

    public String toString() {
        StringBuilder json = new StringBuilder();
        json.append("{\"name\":\"").append(this.name).append("\",");
        listRoles.stream().forEach((p) -> {
            json.append(p.toStringSummary());
        });
        json.append("},");

        return json.toString();
    }

    public String toStringSummary() {
        StringBuilder json = new StringBuilder();
        json.append("{\"name\":\"").append(this.name).append("\"},");
        return json.toString();
    }

    public boolean hasRole(String name) {
        boolean success = this.listRoles.stream().anyMatch(x -> x.getName().equals(name));
        return success;
    }

    public void dropRole(Role r) {
        listRoles.remove(r);
        //ADD DATABASE UPDATE COMMAND HERE.
    }
}

//    public static boolean createOReplaceUser() {
//        return false;//THIS IS SUPPOSED TO CREATE A ROLE IN THE DATABASE.
//    }
