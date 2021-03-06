package Beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class PermissionManagement implements Serializable {

    private ArrayList<User> listUsers = new ArrayList<>();
    private ArrayList<Role> listRoles = new ArrayList<>();
    private ArrayList<PrivLevel> listPrivL = new ArrayList<>();
    private InfoSensibility infoSens = null;

    public PermissionManagement(InfoSensibility info) {
       
        File fichero = new File("Users.bin");
        File fichero2 = new File("Roles.bin");
        File fichero3 = new File("Privs.bin");
        if(fichero.exists())
        {
            try {
                this.readUsers();
                
            } catch(IOException e)
                    {
                        
                    } catch (ClassNotFoundException e)
                        {
                        
                        }
        } else if(fichero2.exists())
                {
                    try {
                        this.readRoles();

                    } catch(IOException e)
                        {

                        } catch (ClassNotFoundException e)
                            {

                            }
                } else if(fichero3.exists())
                        {
                            try {
                                this.readPrivs();

                            } catch(IOException e)
                                {

                                } catch (ClassNotFoundException e)
                                    {

                                    }
                        }
        
        this.getFromDatabase();
        infoSens = info;
    }

    public User getUser(String name) {
        return listUsers.stream()
                .filter((x) -> x.getName().equals(name))
                .findFirst()
                .get();
    }

    public boolean insertUser(String name, String pass) {
        if (!listUsers.stream().anyMatch(((x) -> x.getName().equals(name)))) {
            DBConnector.createUser(name, pass);
            Logs.logCreation("user");
            return listUsers.add(new User(name));
        }
        return false;
    }

    public boolean insertUser(User u, String pass) {
        if (!listUsers.stream().anyMatch(((x) -> x.getName().equals(u.getName())))) {
            DBConnector.createUser(u.getName(), pass);
            Logs.logCreation("user");
            return listUsers.add(u);
        }
        return false;
    }

    public String toStringUsers() {
        StringBuilder str = new StringBuilder();
        listUsers.stream()
                .forEach((usr) -> str.append(usr.toString()).append(";"));
        return str.toString();
    }

    public String toStringRoles() {
        StringBuilder str = new StringBuilder();
        listRoles.stream().forEach(x -> str.append(x.toString()));
        return str.toString();
    }

    public Role getRole(String roleName) {
        return listRoles.stream()
                .filter((x) -> x.getName().equals(roleName))
                .findFirst()
                .orElse(null);
    }

    public boolean insertRole(String name) {
        if (!listRoles.stream().anyMatch(((x) -> x.getName().equals(name)))) {
            Logs.logCreation("role");
            Role rol = new Role(name);
            //DBConnector.createRole(name); //Si Si Si, No No No, 
            listRoles.add(rol);
            // this.createThrashRole(name);
            return true;
        }
        return false;
    }

//    public void createThrashRole(String role) {
//        listPrivL.stream().forEach(x -> {
//            this.givePrivsToRole(role, x.getDesc());
//        });
//
//    }
    public boolean givePrivsToRole(String role, String prilvl) {
        Role r = this.getRole(role);
        PrivLevel p = this.getPrivLevel(prilvl);
        if (r != null && p != null) {
            return r.addAssignedLevel(p);
        }
        return false;
    }

    public boolean grantRole(String user, String roleName) {
        Role r = this.getRole(roleName);
        User u = this.getUser(user);
        if (r != null && u != null) {
            u.grantRole(r);
            return true;
        }
        return false;
    }

    //Manage Table Permissions 
    public boolean addPermission(String levelName, String tableSpaceName, String tableName, boolean select, boolean insert, boolean delete, boolean update) {//For a table
        Table t = this.infoSens.getTable(tableSpaceName, tableName);
        PrivLevel p = this.getPrivLevel(levelName);
        if (t != null && p != null) {
            return p.addPermission(new Permission(t, select, insert, delete, update));
        }
        return false;
    }

    public boolean editPermission(String levelName, String tableSpaceName, String tableName, boolean select, boolean insert, boolean delete, boolean update) {//For a table
        Table t = this.infoSens.getTable(tableSpaceName, tableName);
        PrivLevel p = this.getPrivLevel(levelName);
        return (t != null && p != null) ? p.editPermission(t, select, insert, delete, update) : false;
    }

    //Manage Column Permissions
    public boolean addPermission(String levelName, String tableSpaceName, String tableName, String colName, boolean select, boolean update) {//For a column
        Column c = this.infoSens.getColumn(tableSpaceName, tableName, colName);
        PrivLevel p = this.getPrivLevel(levelName);
        return (c != null && p != null) ? p.addPermission(new Permission(c, select, update)) : false;
    }

    public boolean editPermission(String levelName, String tableSpaceName, String tableName, String colName, boolean select, boolean update) {//For a column
        Column c = this.infoSens.getColumn(tableSpaceName, tableName, colName);
        PrivLevel p = this.getPrivLevel(levelName);

        return (c != null && p != null) ? p.editPermission(c, select, update) : false;
    }
    
   

    //Manage Function/Procedure Permissions
    //Not implemented yet.  
    private void getFromDatabase() {
        System.out.println("YOU CANT GET USERS OR ROLE FROM DATABASE YET.");
        DBConnector con = new DBConnector();
        //LEER DE ARCHIVO.
        DBConnector.Usuarios(listUsers);
        DBConnector.getAllRoles(listRoles);
    }


    private void createTrashPriv(String privlvl) {
        ArrayList<Tablespace> lista = infoSens.tbsList;
        for (Tablespace tbs : lista) {
            for (Table tab : tbs.getTabs()) {
                this.addPermission(privlvl, tbs.getName(), tab.getName(), false, false, false, false);
                for (Column col : tab.getCols()) {
                    this.addPermission(privlvl, tbs.getName(), tab.getName(), col.getName(), false, false);
                }
            }
        }
    }

    public boolean existsPrivilege(String d) {
        return listPrivL.stream().anyMatch(x -> x.getDesc().equals(d));
    }

    public PrivLevel getPrivLevel(String levelName) {
        return listPrivL.stream()
                .filter((x) -> x.getDesc().equals(levelName))
                .findFirst()
                .orElse(null);
    }

    public String getAllPrivLevels() {
        StringBuilder str = new StringBuilder();
        listPrivL.stream().forEach(x -> str.append(x.toString()));
        return str.toString();
    }

    public PrivLevel createPrivLevel(String d) {
        PrivLevel privlvl = !existsPrivilege(d) ? (new PrivLevel(d)) : null;
        if (privlvl != null) {
            this.listPrivL.add(privlvl);
            this.createTrashPriv(privlvl.getDesc());
        }
        return privlvl;
    }

    public boolean editPrivLevel(String oldDesc, String newDesc) {
        PrivLevel lev = listPrivL.stream()
                .filter((lvl) -> lvl.getDesc().equals(oldDesc))
                .findFirst().orElse(null);
        if (lev != null) {
            lev.setDesc(newDesc);
            return true;
        }
        return false;
    }

    public boolean editPrivLevelPermissionsTable(String lvlDesc, String tablespace, String table, boolean select, boolean insert, boolean update, boolean delete) {
        PrivLevel lev = this.getPrivLevel(lvlDesc);
        Table tab = this.infoSens.getTable(tablespace, table);
        return lev.editPermission(tab, select, insert, delete, update);
    }

    public boolean editPrivLevelPermissionsColumn(String lvlDesc, String tablespace, String table, String column, boolean update) {
        PrivLevel lev = this.getPrivLevel(lvlDesc);
        Column col = this.infoSens.getTable(tablespace, table).getColumn(column);
        return lev.editPermission(col, update);
    }

    public int listPermissionSize() {//How many levels currently in map.
        return listPrivL.size();
    }

    public String toStringAllPrivLevels() {
        StringBuilder str = new StringBuilder("[");
        if (!listPrivL.isEmpty()) {
            listPrivL.stream().forEach((p) -> {
                str.append(p.toStringSummary());//names only
            });
        } else {
            str.append("[{\"sName\":\"No Data\"}]");
        }
        str.replace(str.length() - 1, str.length(), "]");
        return str.toString();
    }

    public String toStringPrivLevelTables(String privLevel) {
        StringBuilder str = new StringBuilder("[");
        PrivLevel p = this.listPrivL.stream().filter(x -> x.getDesc().equals(privLevel)).findFirst().get();
        str.append(p.toString(true))
                .replace(str.length() - 1, str.length(), "]");
        return str.toString();
    }

    public String toStringPrivLevelColumns(String privLevel) {
        StringBuilder str = new StringBuilder("[");
        PrivLevel p = this.listPrivL.stream().filter(x -> x.getDesc().equals(privLevel)).findFirst().get();
        str.append(p.toString(false))
                .replace(str.length() - 1, str.length(), "]");
        return str.toString();
    }

//Replaced 
//    public String toStringRolesSpecific(String roles) {
//        StringBuilder str = new StringBuilder("\"[");
//        Role p = this.listRoles.stream().filter(x -> x.getName().equals(roles)).findFirst().get();
//        str.append(p.toStringRoleSens())
//                .replace(str.length() - 1, str.length(), "]\"");
//        return str.toString();
//    }
    public HashMap<String, Boolean> generateRoleSensHash(String role) {
        /*Generates a Hashmap of all the sensibilities(PrivLVL) a role has (and does not have), for display purposes */
        Role p = this.listRoles.stream().filter(x -> x.getName().equals(role)).findFirst().get();
        HashMap<String, Boolean> hasPrivilege = new HashMap<>();
        if (p != null) {
            listPrivL.stream().forEach(priv -> {
                hasPrivilege.put(priv.getDesc(), p.hasPriv(priv.getDesc()));
            });
        }
        return hasPrivilege;
    }

    public String toJSONRolesPrivs(String role) {
        StringBuilder json = new StringBuilder("[");
        HashMap<String, Boolean> privileges = this.generateRoleSensHash(role);
        for (String s : privileges.keySet()) {
            json.append("{\"name\":\"").append(s).append("\", ").append("\"selected\":")
                    .append(privileges.get(s) ? "\"true\"}," : "\"false\"},");
            privileges.get(s);
        }
        json.replace(json.length() - 1, json.length(), "]");
        return json.toString();
    }

    public String toStringRolesGeneral() {
        StringBuilder str = new StringBuilder("[");

        listRoles.stream().forEach((p) -> {
            str.append(p.toStringSummary());
        });
        str.replace(str.length() - 1, str.length(), "]");
        return str.toString();
    }

    public String toStringUsersSpecific(String userName) {
        StringBuilder str = new StringBuilder("\"[");
        User u = this.listUsers.stream().filter(x -> x.getName().equals(userName)).findFirst().get();
        str.append(u.toString())
                .replace(str.length() - 1, str.length(), "]\"");

        return str.toString();
    }

    public String toStringUsersGeneral() {
        StringBuilder str = new StringBuilder("[");

        listUsers.stream().forEach((p) -> {
            str.append(p.toStringSummary());
        });
        str.replace(str.length() - 1, str.length(), "]");
        return str.toString();
    }

    public boolean removeRole(String role) {
        //LLAMAR AL DROP DE LA BASE DE DATOS
        //REMOVER ROL DE LOS USUARIOS QUE LO TIENEN
        return this.listRoles.removeIf(x -> x.getName().equals(role));//Looks for the role and removes it from the list.
    }

    public void removePrivFromRole(String rol, String level) {
        Role r = this.getRole(rol);
        PrivLevel pr = this.getPrivLevel(level);
        if(r!=null&&pr!=null)
            r.removeLevel(level);
    }
    
    public String toJSONUserRoles(String user) {
        /*Retunes a JSON formatted object with info about all the roles in relation to this user*/
        StringBuilder json = new StringBuilder("[");
        HashMap<String, Boolean> privileges = this.generateUserRolesHash(user);
        for (String s : privileges.keySet()) {
            json.append("{\"name\":\"").append(s).append("\", ").append("\"selected\":")
                    .append(privileges.get(s) ? "\"true\"},\"" : "\"false\"},");
            privileges.get(s);
        }
        json.replace(json.length() - 1, json.length(), "]");
        return json.toString();
    }
    
    public HashMap<String,Boolean> generateUserRolesHash(String user){
        /*Generates a Hashmap of all the roles a user has (and does not have), for display purposes */
        User u = this.listUsers.stream().filter(x -> x.getName().equals(user)).findFirst().get();
        HashMap<String, Boolean> hasRole = new HashMap<>();
        if (u != null) {
            listRoles.stream().forEach(rl -> {
                hasRole.put(rl.getName(),u.hasRole(rl.getName()));
            });
        }
        return hasRole;        
    }

    public void removeUser(String user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean removeRoleFromUser(String user, String role) {
        User u = getUser(user);
        Role r = getRole(role);
        if(u!=null){
            if(u.hasRole(role)){    
                u.dropRole(r);
                return true;
            }
        }
        return false;
    }

    public void writeUsers() throws FileNotFoundException, IOException
    {
        FileOutputStream fos = new FileOutputStream("Users.bin");
        ObjectOutputStream out = new ObjectOutputStream(fos);
        
        out.writeObject(listUsers);
    }
    
    public void writeRoles() throws FileNotFoundException, IOException
    {
        FileOutputStream fos = new FileOutputStream("Roles.bin");
        ObjectOutputStream out = new ObjectOutputStream(fos);
        
        out.writeObject(listRoles);
    }
    
    public void writePrivs() throws FileNotFoundException, IOException
    {
        FileOutputStream fos = new FileOutputStream("Privs.bin");
        ObjectOutputStream out = new ObjectOutputStream(fos);
        
        out.writeObject(listPrivL);
    }
    
    public void readUsers () throws FileNotFoundException, IOException, ClassNotFoundException
    {
        FileInputStream fis = new FileInputStream("Users.bin");
        ObjectInputStream in = new ObjectInputStream(fis);
        
        ArrayList<User> aux = new ArrayList<>();
        
        aux = (ArrayList)in.readObject();

        listUsers = aux;
    }
    
    public void readRoles () throws FileNotFoundException, IOException, ClassNotFoundException
    {
        FileInputStream fis = new FileInputStream("Roles.bin");
        ObjectInputStream in = new ObjectInputStream(fis);
        
        ArrayList<Role> aux2 = new ArrayList<>();
        
        aux2 = (ArrayList)in.readObject();

        listRoles = aux2;
    }
    
    public void readPrivs () throws FileNotFoundException, IOException, ClassNotFoundException
    {
        FileInputStream fis = new FileInputStream("Privs.bin");
        ObjectInputStream in = new ObjectInputStream(fis);
        
        ArrayList<PrivLevel> aux3 = new ArrayList<>();
        
        aux3 = (ArrayList)in.readObject();

        listPrivL = aux3;
    }

    public InfoSensibility getInfoSens() {
        return infoSens;
    }

}
    

    

//    public PrivLevel createPrivLevelFunctional(int n, String d) {//A little test.
//        if (!listPermissions.containsKey(n) && listPermissions.size() >= n - 1) {
//            if (n > 0 && n <= listPermissions.size()) {
//                for (int i = n; i < listPermissions.size() + 1; i++) {
//                    listPermissions.replace(i + 1, listPermissions.get(i)).setLevelNo(i + 1);
//                }
//            }
//            return listPermissions.put(n, new PrivLevel(n, d));
//        }
//        return null;
//    }
//    //Manage Table Permissions 
//    public boolean addPermission(String roleName, String tableSpaceName, String tableName, boolean select, boolean insert, boolean delete, boolean update) {//For a table
//        Table t = this.infoSens.getTable(tableSpaceName, tableName);
//        Role r = this.getRole(roleName);
//        return (t != null && r != null) ? r.addPermission(new Permission(t, select, insert, delete, update)) : false;
//    }
//
//    public boolean editPermission(String roleName, String tableSpaceName, String tableName, boolean select, boolean insert, boolean delete, boolean update) {//For a table
//        Table t = this.infoSens.getTable(tableSpaceName, tableName);
//        Role r = this.getRole(roleName);
//        return (t != null && r != null) ? this.getRole(roleName).editPermission(t, select, insert, delete, update) : false;
//    }
//
//    //Manage Column Permissions
//    public boolean addPermission(String roleName, String tableSpaceName, String tableName, String colName, boolean select, boolean update) {//For a table
//        Column c = this.infoSens.getColumn(tableSpaceName, tableName, colName);
//        Role r = this.getRole(roleName);
//        return (c != null && r != null) ? this.getRole(roleName).addPermission(new Permission(c, select, update)) : false;
//    }
//
//    public boolean editPermission(String roleName, String tableSpaceName, String tableName, String colName, boolean select, boolean update) {//For a table
//        Column c = this.infoSens.getColumn(tableSpaceName, tableName, colName);
//        Role r = this.getRole(roleName);
//        return (c != null && r != null) ? this.getRole(roleName).editPermission(c, select, update) : false;
//    }
