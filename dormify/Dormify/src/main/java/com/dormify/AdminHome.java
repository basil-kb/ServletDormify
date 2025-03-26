package com.dormify;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin")
public class AdminHome extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            HttpSession session = request.getSession(false);
            String name = (session != null) ? (String) session.getAttribute("user") : "Guest";
            
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset='ISO-8859-1'>");
            out.println("<title>Dormify Back Office</title>");
            out.println("<style>");
            out.println("body { background: linear-gradient(to right top, #0b2e64, #00446d, #00545c, #026041, #54662e); font-family: Arial, sans-serif; color: white; margin: 0; height: 100vh; display: flex; align-items: center; justify-content: center; text-align: center; }");
            out.println(".container { width: 400px; background: rgba(255, 255, 255, 0.1); padding: 30px; border-radius: 10px; box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.3); }");
            out.println("h1 { margin-bottom: 20px; }");
            out.println(".nav-container { margin-top: 20px; }");
            out.println(".nav-container a { display: block; background: rgba(255, 255, 255, 0.2); color: white; padding: 15px; margin: 10px 0; text-decoration: none; font-size: 18px; border-radius: 8px; transition: background 0.3s; }");
            out.println(".nav-container a:hover { background: #00545c; }");
            out.println(".logout { background: #f44336 !important; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<div class='container'>");
            out.println("<h1>Welcome " + name + "</h1>");
            out.println("<div class='nav-container'>");
            out.println("<a href='user'>User</a>");
            out.println("<a href='customer'>Customer Handbook</a>");
            out.println("<a href='room'>Room Management</a>");
            out.println("<a href='package'>Package Details</a>");
            out.println("<a href='report'>Reports</a>");
            out.println("<a href='logout' class='logout'>Sign Out</a>");
            out.println("</div>");
            out.println("</div>");
            
            out.println("</body>");
            out.println("</html>");
            
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}