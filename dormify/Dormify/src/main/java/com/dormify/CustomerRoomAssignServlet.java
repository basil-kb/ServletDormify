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

@WebServlet("/roomassign")
public class CustomerRoomAssignServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/dormify";
    final String user = "root";
    final String password = "Basil@2024";

    Connection con = null;
    PreparedStatement pst = null;
    PreparedStatement pstTwo = null;
    ResultSet rsTwo = null;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int i = 1;
            int cusId = Integer.parseInt(request.getParameter("customerid"));
            int roomId = Integer.parseInt(request.getParameter("roomid"));
            String roomSize = request.getParameter("roomSize");

            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);

            pst = con.prepareStatement("INSERT INTO customer_room_assignments (customer_id, room_id) VALUES (?, ?)");
            pst.setInt(1, cusId);
            pst.setInt(2, roomId);
            pst.executeUpdate();

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            HttpSession session = request.getSession(false);
            String name = (String) session.getAttribute("user");

            out.println("<html><head><style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; }");
            out.println("h1 { color: #333; text-align: center; }");
            out.println("table { width: 80%; margin: auto; border-collapse: collapse; background: white; }");
            out.println("th, td { border: 1px solid #ddd; padding: 10px; text-align: center; }");
            out.println("th { background: #007bff; color: white; }");
            out.println("tr:nth-child(even) { background: #f2f2f2; }");
            out.println("a { text-decoration: none; color: #007bff; font-weight: bold; }");
            out.println("a:hover { color: #0056b3; }");
            out.println("</style></head><body>");

            out.println("<h1>Welcome " + name + "</h1>");
            out.println("<h1>Select Your Package</h1>");
            out.println("<table>");
            out.println("<tr><th>SINO</th><th>Package Type</th><th>Room Size</th><th>Package Price</th><th>Select</th></tr>");

            pstTwo = con.prepareStatement("SELECT pp.pp_id, p.package_type, pp.packagePrice, rs.room_size "
                    + "FROM packageprice pp "
                    + "JOIN package p ON pp.package_id = p.package_id "
                    + "JOIN room_size rs ON pp.rs_id = rs.rs_id "
                    + "WHERE rs.room_size=? ORDER BY p.package_type");

            pstTwo.setString(1, roomSize);
            rsTwo = pstTwo.executeQuery();

            while (rsTwo.next()) {
                out.println("<tr>");
                out.println("<td>" + i + "</td>");
                out.println("<td>" + rsTwo.getString(2) + "</td>");
                out.println("<td>" + rsTwo.getString(4) + "</td>");
                out.println("<td>" + rsTwo.getInt(3) + "</td>");
                out.println("<td><a href='selectpack?packPid=" + rsTwo.getInt(1) + "&customerid=" + cusId + "'>Select</a></td>");
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
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
