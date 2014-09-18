/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.ArrayList;

/**
 *
 * @author Javier
 */
public class InfoSensibility {

    ArrayList<Tablespace> tbsList = new ArrayList<>();

    public InfoSensibility() {
        PrivLevel.createPrivLevel(-1, "Indefinido");
    }

    
    
    public Tablespace getTableSpace(String name) {
        return tbsList.stream()
                .filter((t) -> t.getName().equals(name))
                .findFirst().orElse(null);
    }
    public Table getTable(String tablespace,String table){
        return this.getTableSpace(tablespace).getTable(table);
    }
    
    public Column getColumn(String tablespace,String table,String col){
        return this.getTable(tablespace, table).getColumn(col);
    }

    public String toStringTablespaces() {
        StringBuilder str = new StringBuilder();
        tbsList.stream()
                .forEach((tbs) -> str.append(tbs.getName()));
        return str.toString();
        //return tbs!=null ? tbs.toString():"";
    }

    public String toStringTables(String name) {
        Tablespace aux = this.getTableSpace(name);
        return aux!=null ? aux.toStringTables():"";
    }
    
    public String toStringColumns(String tbs,String table){
        Tablespace aux = this.getTableSpace(tbs);
        Table tAux = aux!=null ? aux.getTable(table):null;
        return tAux!=null ? tAux.toStringCols():"";
    }

    public boolean setSensibiltiy(String tablespace, String table, String column, int sens) {//CHANGE A COLUMN'S SENSITIVITY
        Tablespace tbsp = this.getTableSpace(tablespace);
        return tbsp != null ? tbsp.setSensibility(table, column, sens):false;
    }

    public boolean setSensibiltiy(String tablespace, String table, int sens) {//CHANGEE WHOLE TABLE SENSITIBITY.
        Tablespace tbsp = this.getTableSpace(tablespace);
        return tbsp!=null ? tbsp.setSensibility(table, sens):false;
    }

    public boolean createLevel(int num, String desc) {
        return PrivLevel.createPrivLevel(num, desc) != null;
    }
    public boolean editLevel(int num,String newDesc){
        return PrivLevel.editPrivLevel(num,newDesc);
    }

    public String toStringLevels() {
        return PrivLevel.toStringPrivLevels();
    }

}