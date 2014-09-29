package Beans;

import com.google.gson.Gson;

public class ReadJSON {

    public void setTables(String json, String level) {
        JSONTable[] t = new Gson().fromJson(json, JSONTable[].class);
        for (JSONTable tt : t) {
//            tt.tName;
//             tt.tablespace;
//             tt.insert;
//             tt.select;
//             tt.delte;
//             tt.update;
        }
    }

    public void setColumns(String json, String level) {
        JSONColumn[] c = new Gson().fromJson(json, JSONColumn[].class);
        //Then you can loop "t" and get attributes like "tName" or "delete"    
        for (JSONColumn cc : c) {
//            cc.cName;
//             cc.tName;
//             cc.update;
        }
    }

    public void setRsrc(String json, String level) {
        JSONRsrc[] r = new Gson().fromJson(json, JSONRsrc[].class);
        for (JSONRsrc rr : r) {
            //rr.rName;
            //rr.type;
            //rr.selected;
        }
    }

    public void setSens(String json, String rol) {
        JSONSens[] s = new Gson().fromJson(json, JSONSens[].class);
        for (JSONSens ss : s) {
            //ss.name;
            //ss.selected;
        }
    }

    public void setRoles(String json, String user) {
        JSONRole[] r = new Gson().fromJson(json, JSONRole[].class);
        for (JSONRole rr : r) {
            //rr.name;
            //rr.selected;
        }
    }
};

/*Dummy Classes for extracting info from JSON as Java Objects*/
class JSONTable {

    public String tName;
    public String tablespace;
    public boolean insert;
    public boolean select;
    public boolean delte;
    public boolean update;
};

class JSONColumn {

    public String tName;
    public String cName;
    public boolean update;
};

class JSONRsrc {

    public String rName;
    public String type;
    public boolean selected;
};

class JSONSens {

    public String name;
    public boolean selected;
};

class JSONRole {

    public String name;
    public boolean selected;
};