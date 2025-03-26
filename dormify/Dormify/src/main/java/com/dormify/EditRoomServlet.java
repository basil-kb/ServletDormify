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

@WebServlet("/editrooms")
public class EditRoomServlet extends HttpServlet {

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
	int id;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			int roomSizeId = 0;
			
			HttpSession session = request.getSession(false);
			String name = (session != null) ? (String) session.getAttribute("user") : "Guest";
			
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);

			id = Integer.parseInt(request.getParameter("eid"));
			pst = con.prepareStatement("SELECT room_number, rs_id, room_id FROM rooms WHERE room_id=" + id);
			rs = pst.executeQuery();

			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			out.println("<html>");
			out.println("<head>");
			out.println("<meta charset='ISO-8859-1'>");
			out.println("<title>Edit Room</title>");
			out.println("<style>");
			out.println("body { background-color: #f4f4f4; font-family: Arial, sans-serif; text-align: center; margin: 0; padding: 20px; }");
			out.println(".container { width: 50%; margin: auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.2); }");
			out.println("h1 { color: #333; }");
			out.println("form { display: flex; flex-direction: column; align-items: center; }");
			out.println("label { font-weight: bold; margin-top: 10px; }");
			out.println("input, select { width: 80%; padding: 8px; margin: 5px 0; border: 1px solid #ccc; border-radius: 5px; }");
			out.println("input[type='submit'] { background: #007bff; color: white; border: none; cursor: pointer; width: 50%; font-size: 16px; padding: 10px; }");
			out.println("input[type='submit']:hover { background: #0056b3; }");
			out.println(".button { display: inline-block; padding: 10px 20px; background: #007bff; color: white; border-radius: 5px; text-decoration: none; margin: 10px; font-weight: bold; }");
			out.println(".button:hover { background: #0056b3; }");
			out.println("</style>");
			out.println("</head>");
			out.println("<body>");
			out.println("<div class='container'>");

			out.println("<h1>Welcome, " + name + "</h1>");
			out.println("<a href='admin' class='button'>Home</a>");
			out.println("<h2>Edit Room Details</h2>");

			out.print("<form name='edit_room' action='updateroom' method='post'>");

			while (rs.next()) {
				roomSizeId = rs.getInt(2);
				out.print("<input type='hidden' name='room_id' value='" + rs.getInt(3) + "'>");
				out.print("<label>Room Number</label>");
				out.print("<input type='text' name='roomNumber' value='" + rs.getString(1) + "' required>");
				
				out.print("<label>Room Size</label>");
				out.print("<select name='RoomSize'>");
				out.print("<option value='SMALL'>SMALL</option>");
				out.print("<option value='MEDIUM'>MEDIUM</option>");
				out.print("<option value='LARGE'>LARGE</option>");
			}

			pstTwo = con.prepareStatement("SELECT room_size FROM room_size WHERE rs_id=" + roomSizeId);
			rsTwo = pstTwo.executeQuery();

			while (rsTwo.next()) {
				out.print("<option value='" + rsTwo.getString(1) + "' selected>" + rsTwo.getString(1) + "</option>");
			}

			out.print("</select>");
			out.print("<br><br><input type='submit' value='UPDATE'>");
			out.print("</form>");

			out.println("</div>");
			out.println("</body>");
			out.println("</html>");
			out.close();
			
		} catch (IOException | SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
}
