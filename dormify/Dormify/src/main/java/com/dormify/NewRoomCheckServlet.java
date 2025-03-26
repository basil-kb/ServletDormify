package com.dormify;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/newroomcheck")
public class NewRoomCheckServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	final String driver = "com.mysql.cj.jdbc.Driver";
	final String url = "jdbc:mysql://localhost:3306/dormify";
	final String user = "root";
	final String password = "Basil@2024";
	
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String checkQuery;
	String userRole;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		// Retrieve form parameters
		String roomNo = request.getParameter("RoomNo");
		String roomSize = request.getParameter("RoomSize");

		try {
			// Load MySQL JDBC Driver
			Class.forName(driver);

			// Establish connection
			conn = DriverManager.getConnection(url, user, password);
			
			HttpSession session = request.getSession(false);
			userRole = (String) session.getAttribute("role");
			System.out.println(userRole);

			// Check if the room already exists
			 checkQuery = "SELECT COUNT(*) FROM rooms WHERE room_number = ?";
			pstmt = conn.prepareStatement(checkQuery);
			pstmt.setString(1, roomNo);
			rs = pstmt.executeQuery();
			rs.next();

			if (rs.getInt(1) > 0) {
				// Room already exists
				out.println("<script>alert('Room number already exists! Please choose a different one.');"
						+ "window.location='newroom.html';</script>");
			} else {
				// Insert new room
				String insertQuery = "INSERT INTO rooms (room_number, rs_id) VALUES (?, (SELECT rs_id FROM room_size WHERE room_size = ?))";
				pstmt = conn.prepareStatement(insertQuery);
				pstmt.setString(1, roomNo);
				pstmt.setString(2, roomSize);

				int rowsInserted = pstmt.executeUpdate();
				if (rowsInserted > 0 && userRole.equals("Admin")) {
							out.println("<script>alert('Room added successfully!');" + "window.location='admin';</script>");
					}
					else if(rowsInserted > 0 && userRole.equals("Manager")) {
						out.println("<script>alert('Room added successfully!');" + "window.location='backoffice_home.html';</script>");
						}
				 else {
					out.println("<script>alert('Error adding room. Please try again.');"
							+ "window.location='newroom.html';</script>");
				}
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			out.println("<script>alert('Room number already exists! Please choose a different one.');"
					+ "window.location='newroom.html';</script>");
		} catch (Exception e) {
			e.printStackTrace();
			out.println("<script>alert('Database error. Please try again later.');"
					+ "window.location='newroom.html';</script>");
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
