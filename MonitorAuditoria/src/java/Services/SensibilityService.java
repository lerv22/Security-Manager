package Services;

import Beans.Logs;
import Beans.Model;
import Beans.PermissionManagement;
import Beans.ReadJSON;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SensibilityService extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PermissionManagement perman = Model.permMan;
        try (PrintWriter out = response.getWriter()) {
            if (request.getParameter("call") != null) {
                String str = request.getParameter("call");
                switch (str) {
                    /*Call for Tables*/
                    case "tables":
                        String tbsName = request.getParameter("element");
                        String debugeandoA = perman.toStringPrivLevelTables(tbsName);
                        out.print(debugeandoA);
                        break;
                    /*Call for columns*/
                    case "columns":
                        String tabName = request.getParameter("element");
                        String debugeando = perman.toStringPrivLevelColumns(tabName);
                        out.print(debugeando);
                        break;
                    /*Call for resources*/
                    case "rsrc":
                        //request.getParameter("element")
                        out.print("[{\"rName\": \"p1\", \"type\": \"procedure\", \"selected\": \"false\"}, {\"rName\": \"p2\", \"type\": \"function\", \"selected\": \"true\"}]");
                        break;
                    /*Call for sensibilities*/
                    case "sens":
                        out.print(perman.toStringAllPrivLevels());
                        break;
                }
            } else if (request.getParameter("new") != null) { //New Level
                String name = request.getParameter("name");//-->Nombre de nuevo nivel
                perman.createPrivLevel(name);
                perman.writePrivs();//AQUI VA 1
                Logs.logCreation("sensibilidad");
                response.sendRedirect("sensibility.jsp");
            } else if (request.getParameter("delete") != null) {
                //(request.getParameter("delete")); Devuelve sensibilidad a eliminar
                Logs.logDelete("sensibilidad");
                System.err.println("NOT YET SORRY");
            } else {
                String jsonTables = request.getParameter("tables");//JSON
                String jsonColumns = request.getParameter("columns");//JSON
                String senlev = request.getParameter("sens");//Selected level
                ReadJSON.setTables(perman,jsonTables,senlev);
                ReadJSON.setColumns(perman,jsonColumns,senlev);
                 perman.writePrivs();//AQUI VA 1
                //request.getParameter("tables");
                //request.getParameter("columns");
                request.getParameter("rsrc");
                request.getParameter("sens"); //Level selected
                response.sendRedirect("sensibility.jsp");   
                Logs.logEdit("sensibilidad");
                //response.sendRedirect("sensibility.jsp");
            }

        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
