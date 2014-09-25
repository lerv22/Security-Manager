/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrador
 */
public class DBConnector {

    /**
     * @param args the command line arguments
     */
    private static Connection con = null;
    public static PreparedStatement pst;
    public static ResultSet rs;
    public static String sql;
    public static FileWriter fichero = null;
    public static PrintWriter pw = null;

    public static void escribir(String SID) {
        try {
            fichero = new FileWriter("C:\\prueba.bat", true);
            pw = new PrintWriter(fichero);

            pw.print("@echo off \n");
            pw.print("net stop OracleService" + SID + "\n");
            pw.print("if ERRORLEVEL 1 echo Problem while stopping Oracle Service " + SID + "\n");
            pw.print("net stop Oracle" + SID + "TNSListener" + "\n");
            pw.print("if ERRORLEVEL 1 echo Problem while stopping Oracle " + SID + " Listener service" + "\n");
            pw.print("net start OracleService" + SID + "\n");
            pw.print("if ERRORLEVEL 1 echo Problem while starting Oracle Service " + SID + "\n");
            pw.print("net start Oracle" + SID + "TNSListener" + "\n");
            pw.print("if ERRORLEVEL 1 echo Problem while starting Oracle " + SID + " Listener service" + "\n");
            pw.print("pause" + "\n");
            pw.print("exit" + "\n");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void GrantRoles(String roles, String master) {
        int s = roles.length();
        char letra[] = roles.toCharArray();
        String rol = new String();

        for (int i = 0; i < s; i++) {   
            if (letra[i] != ';') {
                rol += letra[i];
            } else {
                //ejecuta procedure enviandole rol
                sql = "Grant " + rol + " to " + master;
                try {
                    pst = con.prepareStatement(sql);
                    rs = pst.executeQuery();

                    System.out.println(rol + " concedido a " + master);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.out.println("Grant fallido");
                }

                rol = new String();//="";
            }
        }
    }

    public static boolean conectDB() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "sys as sysdba", "root");

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static void getRoles() {

        sql = "select granted_role \"ROL\" from dba_role_privs where grantee not in ('OUTLN', 'DATAPUMP_IMP_FULL_DATABASE', 'SELECT_CATALOG_ROLE', 'HS_ADMIN_ROLE', 'EXP_FULL_DATABASE', 'DBSNMP', 'IMP_FULL_DATABASE', 'LOGSTDBY_ADMINISTRATOR', 'OEM_MONITOR', 'EXECUTE_CATALOG_ROLE', 'DATAPUMP_EXP_FULL_DATABASE')";

        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String rol = rs.getString("ROL");
                System.out.println(rol);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error");
        }
    }

    public static void getTablespaces(ArrayList<Tablespace> lista){
        sql = "select tablespace_name \"TS\" from dba_tablespaces where tablespace_name not in ('SYSTEM', 'SYSAUX', 'UNDOTBS1')";
        
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String TS = rs.getString("TS");
                System.out.println(TS);
                lista.add(new Tablespace(TS));
            }
        } catch (SQLException ex){
            ex.printStackTrace();
            System.out.println("Error");
        }
    }

    public static void tablas(ArrayList<Tablespace> tablespaces,ArrayList<Table> tables) {
        sql = "select table_name \"Tab\" ,tablespace_name \"TS\" from dba_tables where OWNER not in ('ANONYMOUS', 'APEX_030200', 'APEX_PUBLIC_USER', 'APPQOSSYS', 'BI', 'CTXSYS', 'DBSNMP', 'DIP', 'EXFSYS', 'FLOWS_FILES', 'HR', 'IX', 'MDDATA', 'MDSYS', 'MGMT_VIEW', 'OE', 'OLAPSYS', 'ORACLE_OCM', 'ORDDATA', 'ORDPLUGINS', 'ORDSYS', 'OUTLN', 'OWBSYS', 'OWBSYS_AUDIT', 'PM', 'SCOTT', 'SH', 'SI_INFORMTN_SCHEMA', 'SPATIAL_CSW_ADMIN_USR', 'SPATIAL_WFS_ADMIN_USR', 'SYS', 'SYSMAN', 'SYSTEM', 'WMSYS', 'XDB', 'XS$NULL', 'APEX_040000')";

        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            Tablespace t = null;
            while (rs.next()) {
                String tabname = rs.getString("Tab");
                String tsname=  rs.getString("TS");
                
                tablespaces.stream()
                        .filter(x->x.getName().equals(tsname))
                        .findAny().get()
                        .setTable(tabname);
                
                System.out.println(tabname);
                System.out.println(tsname);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error");
        }
    }

    public static void columnas(Table table) {
        //BUSCAR ALTERNATIVA A DESCRIBE
        String upperCaseTable = table.getName().toUpperCase();
        sql = "select DISTINCT column_name \"Col\" from all_tab_columns where table_name = '" + upperCaseTable + "'";

        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String auxCol = rs.getString("Col");
                table.setColumn(auxCol);
                System.out.println(auxCol);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error");
        }
    }

    public static void CreateRole(String rol) {
        String Rol = rol.toUpperCase();
        sql = "CREATE ROLE " + Rol;

        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            System.out.println("Rol Creado");

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error");
        }
    }

    public static void CreateUser(String usr, String pas) {
        String Usr = usr.toUpperCase();
        String Pass = pas.toUpperCase();
        sql = "CREATE USER " + Usr + " IDENTIFIED BY " + Pass;

        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            
            System.out.println("User " + Usr + " Creado");

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error");
        }
    }

    public static void OtorgaRol(String rol, String usr) {
        String Rol = rol.toUpperCase();
        String Usr = usr.toUpperCase();
        sql = "GRANT " + Rol + " TO " + Usr;

        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            System.out.println(Rol + " otorgado a " + Usr);

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error");
        }
    }

    public static void RolesUser(String usr) {//NOT CURRENTLY IN USE.
        String Usr = usr.toUpperCase();
        sql = "SELECT grantee \"USR\", granted_role \"ROL\" from dba_role_privs where grantee = '" + Usr + "' order by grantee";

        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String Usua = Usr;//rs.getString("USR");
                String rol = rs.getString("ROL");

                System.out.println(Usua);
                System.out.println(rol);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error");
        }
    }

    public static void Usuarios() {
        sql = "SELECT username \"USRN\", account_status \"EST\",created \"FEC\", EXPIRY_DATE \"LIM\" FROM dba_users WHERE username not in ('ANONYMOUS', 'APEX_030200', 'APEX_PUBLIC_USER', 'APPQOSSYS', 'BI', 'CTXSYS', 'DBSNMP', 'DIP', 'EXFSYS', 'FLOWS_FILES', 'HR', 'IX', 'MDDATA', 'MDSYS', 'MGMT_VIEW', 'OE', 'OLAPSYS', 'ORACLE_OCM', 'ORDDATA', 'ORDPLUGINS', 'ORDSYS', 'OUTLN', 'OWBSYS', 'OWBSYS_AUDIT', 'PM', 'SCOTT', 'SH', 'SI_INFORMTN_SCHEMA', 'SPATIAL_CSW_ADMIN_USR', 'SPATIAL_WFS_ADMIN_USR', 'SYS', 'SYSMAN', 'SYSTEM', 'WMSYS', 'XDB', 'XS$NULL', 'APEX_040000')";

        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String Usua = rs.getString("USRN");
                String Estado = rs.getString("EST");
                String Creado = rs.getString("FEC");
                String Expira = rs.getString("LIM");

                System.out.println(Usua);
                System.out.println(Estado);
                System.out.println(Creado);
                System.out.println(Expira);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error");
        }
    }

    public static void ResumenAudit() {
        sql = "SELECT USERNAME \"USR\", ACTION_NAME \"CONS\",SQL_TEXT \"SQL\", EXTENDED_TIMESTAMP \"TIME\", RETURNCODE \"EXITO\" FROM DBA_AUDIT_TRAIL";
        String json = "[";

        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String Exito, sql, fecha;
                String Usua = rs.getString("USR");
                String Querie = rs.getString("CONS");
                String SQL = rs.getString("SQL");
                String Tiempo = rs.getString("TIME");
                String Exit = rs.getString("EXITO");

                if (Exit.equals("0")) {
                    Exito = "Success";
                } else {
                    Exito = "Dennied";
                }

                if (SQL == null/*.isEmpty()*/) {
                    sql = "---";
                } else {
                    sql = SQL;
                }

                fecha = Tiempo.substring(0, 19);

                String JSon = "{\"User\":\"" + Usua + "\"," + "\"Statement\":\"" + Querie + "\"," + "\"SQL\":\"" + sql + "\"," + "\"Date\":\"" + fecha + "\"," + "\"State\":\"" + Exito + "\"},";
                json = json + JSon;

                /*
                 System.out.println(Usua);
                 System.out.println(Querie);
                 System.out.println(SQL);
                 System.out.println(Tiempo);
                 System.out.println(Exit);*/
            }
            json = json.substring(0, json.length() - 1);
            json = json + "]";
            System.out.println(json);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error");
        }
    }

    public static void accionesAudit() {
        sql = "SELECT ACTION_NAME \"ACC\", COUNT(DISTINCT ACTION_NAME) \"SUM\" FROM DBA_AUDIT_TRAIL WHERE ACTION_NAME NOT IN ('LOGON', 'CREATE PROCEDURE') AND ACTION_NAME NOT LIKE 'LOGOFF%' GROUP BY ACTION_NAME";

        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String Acc = rs.getString("ACC");
                String Total = rs.getString("SUM");

                System.out.println(Acc);
                System.out.println(Total);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error");
        }
    }

    public static void consultasxUsuario() {
        sql = "SELECT USERNAME \"USER\", COUNT(ACTION_NAME) \"SUM\" FROM DBA_AUDIT_TRAIL GROUP BY USERNAME";

        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String User = rs.getString("USER");
                String Total = rs.getString("SUM");

                System.out.println(User);
                System.out.println(Total);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error");
        }
    }

    public static void Procedures() {
        sql = "SELECT DISTINCT name \"NOM\", type \"TIPO\", owner \"DUEÑO\" FROM ALL_SOURCE WHERE OWNER NOT IN ('SYS', 'SYSTEM', 'OUTLN', 'DATAPUMP_IMP_FULL_DATABASE', 'SELECT_CATALOG_ROLE', 'HS_ADMIN_ROLE', 'EXP_FULL_DATABASE', 'DBSNMP', 'IMP_FULL_DATABASE', 'LOGSTDBY_ADMINISTRATOR', 'OEM_MONITOR', 'EXECUTE_CATALOG_ROLE', 'DATAPUMP_EXP_FULL_DATABASE', 'APEX_040000', 'HR', 'MDSYS', 'XDB', 'CTXSYS') AND TYPE IN ('PROCEDURE','FUNCTION') ORDER BY 2";

        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String User = rs.getString("NOM");
                String Tipo = rs.getString("TIPO");
                String Own = rs.getString("DUEÑO");

                System.out.println(User);
                System.out.println(Tipo);
                System.out.println(Own);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error");
        }
    }

    public static void QuitaRol(String rol, String user) {
        String Rol = rol.toUpperCase();
        String User = user.toUpperCase();
        sql = "REVOKE " + Rol + " FROM " + User;

        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            System.out.println(Rol + " removido de " + User);

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error");
        }
    }

    public static void ReiniciarBase() {
        Runtime aplicacion = Runtime.getRuntime();

        escribir("XE");

        try {
            System.out.println("Reiniciando la base de datos");
            aplicacion.exec("cmd.exe /K C:\\prueba.bat");
            System.out.println("Base de datos reiniciada");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void ActivarAudit() {
        sql = "ALTER SYSTEM SET audit_trail = DB_EXTENDED SCOPE=SPFILE";

        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            System.out.println("Auditoria activada ");

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error");
        }
    }

//    public static void main(String[] args) {
//        // TODO code application logic here
//
//        if (conectDB()) {
//            System.out.println("Exito");
//            //ActivarAudit();
//            //ReiniciarBase();
//            //Roles();
//            //tablespaces();
//            tablas();
//            //columnas("t1");
//            //CreateRole("adiosa");
//            //CreateUser("sofia","sophi");
//            //OtorgaRol("hola","sofia");
//            //RolesUser("sofia");
//            //Usuarios();
//            //ResumenAudit();
//            //accionesAudit();
//            //consultasxUsuario();
//            //Procedures();
//            //QuitaRol("hola", "sofia");
//            //GrantRoles("connect;resource;","hola");// este metodo recibe un string con el rol+; y el nombre del rol en el que se almacenaran todos
//        } else {
//            System.out.println("No Exito");
//        }
//    }

}