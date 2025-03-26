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

@WebServlet("/updateroom")
public class UpdateRoomServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	final String driver = "com.mysql.cj.jdbc.Driver";
	final String url = "jdbc:mysql://localhost:3306/dormify";
	final String user = "root";
	final String password = "Basil@2024";
	
	
	String roomNumber;
	String RoomSize;
	int room_id;
	

	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;
	RequestDispatcher dis = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			
			room_id=Integer.parseInt(request.getParameter("room_id"));
			roomNumber=request.getParameter("roomNumber").trim();
			RoomSize=request.getParameter("RoomSize").trim();
			
			pst = con.prepareStatement("update rooms set room_number=?,rs_id=(select rs_id from room_size where room_size=?) where room_id=?");
			
			pst.setString(1, roomNumber);
			pst.setString(2, RoomSize);
			pst.setInt(3, room_id);
			

			pst.executeUpdate();
			con.close();
			
			dis = request.getRequestDispatcher("admin");
			dis.include(request, response);
			
			
		} catch (IOException |SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	

		
	}
}
