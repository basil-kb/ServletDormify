package com.dormify;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/backofficepackage")
public class BackofficePackageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/dormify";
    private static final String USER = "root";
    private static final String PASSWORD = "Basil@2024";
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);
        String name = (session != null) ? (String) session.getAttribute("user") : "Guest";
        
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pst = con.prepareStatement(
                "SELECT p.package_type, pp.packagePrice, rs.room_size " +
                "FROM packageprice pp " +
                "JOIN package p ON pp.package_id = p.package_id " +
                "JOIN room_size rs ON pp.rs_id = rs.rs_id " +
                "ORDER BY p.package_type");
             ResultSet rs = pst.executeQuery()) {
            
            Class.forName(DRIVER);
            
            out.println("<html><head>");
            out.println("<title>Package Details</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; text-align: center; } ");
            out.println("table { width: 80%; margin: auto; border-collapse: collapse; } ");
            out.println("th, td { border: 1px solid black; padding: 10px; text-align: center; }");
            out.println("th { background-color: #f2f2f2; }");
            out.println("a { text-decoration: none; color: blue; font-weight: bold; }");
            out.println("</style></head><body>");
            
            out.println("<h1>Welcome, " + name + "</h1>");
            out.println("<h2><a href='backoffice_home.html'>Home</a></h2>");
            out.println("<h2>Package Details</h2>");
            
            out.println("<table>");
            out.println("<tr><th>SINO</th><th>Package Type</th><th>Room Size</th><th>Package Price</th></tr>");
            
            int i = 1;
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + i + "</td>");
                out.println("<td>" + rs.getString("package_type") + "</td>");
                out.println("<td>" + rs.getString("room_size") + "</td>");
                out.println("<td>" + rs.getInt("packagePrice") + "/--</td>");
                out.println("</tr>");
                i++;
            }
            out.println("</table>");
            out.println("</body></html>");
        } catch (SQLException | ClassNotFoundException ex) {
            out.println("<h3 style='color:red;'>An error occurred while fetching package details.</h3>");
            ex.printStackTrace(out);
        }
    }
}
