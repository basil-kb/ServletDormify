package com.dormify;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/newroom")
public class NewRoomServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String driver = "com.mysql.cj.jdbc.Driver";
	final String url = "jdbc:mysql://localhost:3306/dormify";
	final String user = "root";
	final String password = "Basil@2024";

	String RoomNo;
	String RoomSize;
	String name;

	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;
	RequestDispatcher dis = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

			response.setContentType("text/html");

			HttpSession session = request.getSession(false);
			name = (String) session.getAttribute("user");

			RoomNo = request.getParameter("RoomNo").trim();
			RoomSize = request.getParameter("RoomSize").trim();

			// HttpSession session = request.getSession(false);
			// String name = (String) session.getAttribute("user");

			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			pst = con.prepareStatement(
					"INSERT INTO rooms (room_number, rs_id) VALUES(?, (SELECT rs_id FROM room_size WHERE room_size = ?))");
			pst.setString(1, RoomNo);
			pst.setString(2, RoomSize);

			pst.executeUpdate();

			if (name.equals("Admin")) {
				dis = request.getRequestDispatcher("admin");
				dis.include(request, response);
			} else {
				dis = request.getRequestDispatcher("backoffice_home.html");
				dis.include(request, response);
			}

		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

}
