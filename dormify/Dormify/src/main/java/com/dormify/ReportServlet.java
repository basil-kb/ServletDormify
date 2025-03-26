package com.dormify;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/report")
public class ReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/dormify";
    final String user = "root";
    final String password = "Basil@2024";

    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);

            // Fetch total customers
            pst = con.prepareStatement("SELECT COUNT(*) FROM customerbasic");
            rs = pst.executeQuery();
            rs.next();
            int totalCustomers = rs.getInt(1);

            // Fetch total rooms 
            pst = con.prepareStatement("SELECT SUM(rs.room_capacity) AS total_capacity FROM rooms r JOIN room_size rs ON r.rs_id = rs.rs_id");
            rs = pst.executeQuery();
            rs.next();
            int totalRooms = rs.getInt(1);

            // Fetch available rooms
            pst = con.prepareStatement("SELECT COUNT(ra.customer_id) AS total_available_slots FROM rooms r JOIN room_size rs ON r.rs_id = rs.rs_id LEFT JOIN customer_room_assignments ra ON r.room_id = ra.room_id");
            rs = pst.executeQuery();
            rs.next();
            int availableRooms = (totalRooms - rs.getInt(1));

            // Fetch total users
            pst = con.prepareStatement("SELECT COUNT(*) FROM user");
            rs = pst.executeQuery();
            rs.next();
            int totalUsers = rs.getInt(1);

            // HTML Structure
            out.println("<html><head><title>Report</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; text-align: center; background-color: #f4f4f4; }");
            out.println(".container { margin: auto; width: 50%; padding: 20px; background: white; border: 1px solid black; border-radius: 10px; box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.2); }");
            out.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
            out.println("th, td { border: 1px solid black; padding: 10px; text-align: center; }");
            out.println("th { background-color: #007bff; color: white; }");
            out.println(".home-button { margin-top: 20px; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; display: inline-block; }");
            out.println(".home-button:hover { background-color: #0056b3; }");
            out.println("</style>");
            out.println("</head><body>");
            out.println("<div class='container'>");
            out.println("<h2>Dormitory Management Report</h2>");
            out.println("<table>");
            out.println("<tr><th>Metric</th><th>Count</th></tr>");
            out.println("<tr><td>Total Customers</td><td>" + totalCustomers + "</td></tr>");
            out.println("<tr><td>Total Rooms</td><td>" + totalRooms + "</td></tr>");
            out.println("<tr><td>Available Rooms</td><td>" + availableRooms + "</td></tr>");
            out.println("<tr><td>Total Users</td><td>" + totalUsers + "</td></tr>");
            out.println("</table>");

            // Home Page Button
            out.println("<a href='admin' class='home-button'>Home Page</a>");

            out.println("</div>");
            out.println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error generating report</h3>");
        } 
    }
}
