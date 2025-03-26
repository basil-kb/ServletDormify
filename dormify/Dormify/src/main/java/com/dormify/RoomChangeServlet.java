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

@WebServlet("/changeRoom")
public class RoomChangeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/dormify";
    final String user = "root";
    final String password = "Basil@2024";
    

    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    PreparedStatement pstTwo = null;
    ResultSet rsTwo = null;
   

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        	
        	 response.setContentType("text/html");
             PrintWriter out = response.getWriter();
             HttpSession session = request.getSession(false);
             String name = (String) session.getAttribute("user");
             
            int i = 1;
            int cusID = Integer.parseInt(request.getParameter("cusID"));
            String ORoomNo = request.getParameter("ORoomNo");
            String roomSize = "";

            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);

            pst = con.prepareStatement("SELECT rs.room_size, rs.rs_id FROM rooms r JOIN room_size rs ON r.rs_id = rs.rs_id "
            		+ "JOIN customer_room_assignments cra ON cra.room_id = r.room_id WHERE r.room_number = ?");
            pst.setString(1, ORoomNo);
            rs = pst.executeQuery();
            if (rs.next()) {
                roomSize = rs.getString(1);
            } else {
            	out.println("<script>alert('Room number not exists! Please choose a different one.');"
						+ "window.location='changeroom.html';</script>");
                
                return;
            }

           

            out.println("<html><head><style>");
            out.println("body { font-family: Arial, sans-serif; background-color: #f4f4f4; }");
            out.println("h1 { text-align: center; color: #333; }");
            out.println("table { width: 80%; margin: auto; border-collapse: collapse; background: white; }");
            out.println("th, td { border: 1px solid #ddd; padding: 10px; text-align: center; }");
            out.println("th { background: #007bff; color: white; }");
            out.println("tr:nth-child(even) { background: #f2f2f2; }");
            out.println("a { text-decoration: none; color: #007bff; font-weight: bold; }");
            out.println("a:hover { color: #0056b3; }");
            out.println("</style></head><body>");

            out.println("<h1>Welcome " + name + "</h1>");
            out.println("<h1>Select Your Room</h1>");
            out.println("<table>");
            out.println("<tr><th>SINO</th><th>Room ID</th><th>Room Number</th><th>Room Size</th><th>Room Capacity</th><th>Occupied</th><th>Available Slots</th><th>Select</th></tr>");

            pstTwo = con.prepareStatement("SELECT r.room_id, r.room_number, rs.room_size, rs.room_capacity, COUNT(ra.customer_id) AS occupied, "
                    + "(rs.room_capacity - COUNT(ra.customer_id)) AS available_slots FROM rooms r "
                    + "JOIN room_size rs ON r.rs_id = rs.rs_id "
                    + "LEFT JOIN customer_room_assignments ra ON r.room_id = ra.room_id "
                    + "WHERE rs.room_size = ? GROUP BY r.room_id ORDER BY r.room_number");
            pstTwo.setString(1, roomSize);
            rsTwo = pstTwo.executeQuery();

            while (rsTwo.next()) {
                out.println("<tr>");
                out.println("<td>" + i + "</td>");
                out.println("<td>" + rsTwo.getInt(1) + "</td>");
                out.println("<td>" + rsTwo.getString(2) + "</td>");
                out.println("<td>" + rsTwo.getString(3) + "</td>");
                out.println("<td>" + rsTwo.getInt(4) + "</td>");
                out.println("<td>" + rsTwo.getInt(5) + "</td>");
                out.println("<td>" + rsTwo.getInt(6) + "</td>");
                out.println("<td><a href='Reassignroom?roomid=" + rsTwo.getInt(1) + "&cusID=" + cusID + "'>Select</a></td>");
                out.println("</tr>");
                i++;
            }
            out.println("</table>");
            out.println("</body></html>");
            out.close();

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rsTwo != null) rsTwo.close();
                if (pstTwo != null) pstTwo.close();
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
