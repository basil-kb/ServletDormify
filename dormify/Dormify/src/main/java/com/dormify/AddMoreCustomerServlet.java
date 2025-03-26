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

@WebServlet("/addmorecustomer")
public class AddMoreCustomerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String driver = "com.mysql.cj.jdbc.Driver";
	final String url = "jdbc:mysql://localhost:3306/dormify";
	final String user = "root";
	final String password = "Basil@2024";

	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	int cusId;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			int i = 1;
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
				
			HttpSession session = request.getSession(false);
			String name = (session != null) ? (String) session.getAttribute("user") : "Guest";
			
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);

			cusId = Integer.parseInt(request.getParameter("addid"));
			
			pst = con.prepareStatement("SELECT r.room_id, r.room_number, rs.room_size, rs.room_capacity, COUNT(ra.customer_id) AS occupied,"
					+ " (rs.room_capacity - COUNT(ra.customer_id)) AS available_slots FROM rooms r JOIN room_size rs ON r.rs_id = rs.rs_id "
					+ "LEFT JOIN customer_room_assignments ra ON r.room_id = ra.room_id GROUP BY r.room_id ORDER BY r.room_number"); 
			rs = pst.executeQuery();
			
			out.println("<html>");
			out.println("<head>");
			out.println("<meta charset='ISO-8859-1'>");
			out.println("<title>Assign Room</title>");
			out.println("<style>");
			out.println("body { background: linear-gradient(to right top, #0b2e64, #00446d, #00545c, #026041, #54662e); font-family: Arial, sans-serif; color: white; margin: 0; padding: 20px; text-align: center; }");
			out.println(".container { width: 90%; margin: auto; background: rgba(255, 255, 255, 0.1); padding: 20px; border-radius: 10px; box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.3); }");
			out.println("h1 span { color: #ffcc00; }");
			out.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
			out.println("th, td { border: 1px solid white; padding: 10px; text-align: center; }");
			out.println("th { background: rgba(255, 255, 255, 0.2); }");
			out.println("td.full { background-color: red; color: white; }");
			out.println("a { color: white; text-decoration: none; font-weight: bold; }");
			out.println("a:hover { text-decoration: underline; }");
			out.println(".button { display: inline-block; padding: 10px 20px; background: #00545c; color: white; border-radius: 5px; text-decoration: none; margin: 10px; font-weight: bold; }");
			out.println(".button:hover { background: #026041; }");
			out.println("</style>");
			out.println("</head>");
			out.println("<body>");
			out.println("<div class='container'>");

			out.println("<h1>Welcome <span>Admin</span>, " + name + "</h1>");
			out.println("<a href='admin' class='button'>Home</a>");
			out.println("<a href='newroom.html' class='button'>Add New Room</a>");

			out.println("<h2>All Rooms Details</h2>");
			out.println("<table>");
			out.println("<tr><th>SINO</th><th>Room ID</th><th>Room NO</th><th>Room Size</th><th>Room Capacity</th><th>Occupied</th><th>Available Slots</th><th>Assign</th></tr>");
			
			while (rs.next()) {
				if (rs.getInt(6) <= 0) {
					out.println("<tr>");
					out.println("<td class='full'>" + i + "</td>");
					out.println("<td class='full'>" + rs.getInt(1) + "</td>");
					out.println("<td class='full'>" + rs.getString(2) + "</td>");
					out.println("<td class='full'>" + rs.getString(3) + "</td>");
					out.println("<td class='full'>" + rs.getString(4) + "</td>");
					out.println("<td class='full'>" + rs.getInt(5) + "</td>");
					out.println("<td class='full'>" + rs.getInt(6) + "</td>");
					out.print("<td class='full'><b>FULL<b></td>");
				} else {
					out.println("<tr>");
					out.println("<td>" + i + "</td>");
					out.println("<td>" + rs.getInt(1) + "</td>");
					out.println("<td>" + rs.getString(2) + "</td>");
					out.println("<td>" + rs.getString(3) + "</td>");
					out.println("<td>" + rs.getString(4) + "</td>");
					out.println("<td>" + rs.getInt(5) + "</td>");
					out.println("<td>" + rs.getInt(6) + "</td>");
					out.print("<td><a href='roomassign?roomid=" + rs.getInt(1) + "&customerid=" + cusId + "&roomSize=" + rs.getString(3) + "'>Select</a></td>");
				}
				out.println("</tr>");
				i++;
			}
			out.println("</table>");
			out.println("</div>");
			out.println("</body>");
			out.println("</html>");

			out.close();
			
		} catch (IOException | SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
}
