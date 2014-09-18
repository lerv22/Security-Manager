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
public class Tablespace {

    private String name;
    ArrayList<Table> tabs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Table getTable(String n) {
        return tabs.stream().
                filter((t)->t.getName().equals(n))
                .findFirst().orElse(null);
    }

    public boolean containsTable(String name) {
        return tabs.stream()
                .anyMatch( (x) -> x.getName().equals(name) );
    }

    public boolean setTable(String name) {
        if (!containsTable(name)) {
            return tabs.add(new Table(name));
        }
        return false;
    }

    public boolean setSensibility(String table, String column, int lvl) {
        Table aux = getTable(table);
        if (aux != null) {
            return aux.setSensibility(column, lvl);
        }
        return false;
    }

    public boolean setSensibility(String table, int lvl) {
        Table aux = getTable(table);
        return aux != null ? aux.setSensibility(lvl):false;
    }

    
    public String toStringTables() {//Tablespace_name\nTable\nTable\n(...).
        StringBuilder str = new StringBuilder();
        str.append(name).append("\n");
        tabs.stream().forEach((c) -> str.append(c.toString()).append("\n"));
        return str.toString();
    }

}