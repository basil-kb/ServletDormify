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

@WebServlet("/backofficeroom")
public class BackOfficeRoomServlet extends HttpServlet {

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
                     "SELECT r.room_id, r.room_number, rs.room_size, rs.room_capacity, " +
                             "COUNT(ra.customer_id) AS occupied, " +
                             "(rs.room_capacity - COUNT(ra.customer_id)) AS available_slots " +
                             "FROM rooms r " +
                             "JOIN room_size rs ON r.rs_id = rs.rs_id " +
                             "LEFT JOIN customer_room_assignments ra ON r.room_id = ra.room_id " +
                             "GROUP BY r.room_id " +
                             "ORDER BY r.room_number");
             ResultSet rs = pst.executeQuery()) {

            Class.forName(DRIVER);

            // HTML Structure
            out.println("<html><head>");
            out.println("<title>Room Details</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; text-align: center; } ");
            out.println("table { width: 80%; margin: auto; border-collapse: collapse; } ");
            out.println("th, td { border: 1px solid black; padding: 10px; text-align: center; }");
            out.println("th { background-color: #f2f2f2; }");
            out.println("a { text-decoration: none; color: blue; font-weight: bold; }");
            out.println("</style></head><body>");

            out.println("<h1>Welcome, " + name + "</h1>");
            out.println("<h2><a href='backoffice_home.html'>Home</a></h2>");
            out.println("<h2>Room Details</h2>");
            out.println("<h2><a href='newroom.html'>Add New Room</a> | <a href='changeroom.html'>Change Room</a></h2>");

            // Table Header
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>SINO</th>");
            out.println("<th>Room ID</th>");
            out.println("<th>Room No</th>");
            out.println("<th>Room Size</th>");
            out.println("<th>Room Capacity</th>");
            out.println("<th>Occupied</th>");
            out.println("<th>Available Slots</th>");
            out.println("</tr>");

            // Table Data
            int i = 1;
            while (rs.next()) {
				if (rs.getInt(6) <= 0) {
					out.println("<tr>");
					out.println("<td >" + i + "</td>");
					out.println("<td>" + rs.getInt(1) + "</td>");
					out.println("<td bgcolor='red'>" + rs.getString(2) + "</td>");
					out.println("<td>" + rs.getString(3) + "</td>");
					out.println("<td>" + rs.getString(4) + "</td>");
					out.println("<td>" + rs.getInt(5) + "</td>");
					out.println("<td>" + rs.getInt(6) + "</td>");
					out.print("<td bgcolor='red'><b>FULL<b></td>");
				} else {
					out.println("<tr>");
					out.println("<td>" + i + "</td>");
					out.println("<td>" + rs.getInt(1) + "</td>");
					out.println("<td>" + rs.getString(2) + "</td>");
					out.println("<td>" + rs.getString(3) + "</td>");
					out.println("<td>" + rs.getString(4) + "</td>");
					out.println("<td>" + rs.getInt(5) + "</td>");
					out.println("<td>" + rs.getInt(6) + "</td>");
				}
				out.println("</tr>");
				i++;
            }
            out.println("</table>");
            out.println("</body></html>");
        } catch (SQLException | ClassNotFoundException ex) {
            out.println("<h3 style='color:red;'>An error occurred while fetching room details.</h3>");
            ex.printStackTrace(out);
        }
    }
}
