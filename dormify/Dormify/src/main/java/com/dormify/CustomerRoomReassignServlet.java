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

@WebServlet("/Reassignroom")
public class CustomerRoomReassignServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String driver = "com.mysql.cj.jdbc.Driver";
	final String url = "jdbc:mysql://localhost:3306/dormify";
	final String user = "root";
	final String password = "Basil@2024";
	
	int i=1;
	int cusID;
	int roomid;
	String userRole;

	Connection con = null;
	PreparedStatement pst = null;
	
	PreparedStatement pstTwo = null;
	ResultSet rsTwo = null;
	RequestDispatcher dis = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			HttpSession session = request.getSession(false);
			 String userRole = (String) session.getAttribute("role");
			
			cusID = Integer.parseInt(request.getParameter("cusID"));
			roomid=Integer.parseInt(request.getParameter("roomid"));

			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);

			pst = con.prepareStatement("call MoveCustomerToRoom(?,?)");
			pst.setInt(1, cusID);
			pst.setInt(2, roomid);
			pst.executeUpdate();
					
			if(userRole.equals("Admin")){
			dis = request.getRequestDispatcher("admin");
			dis.include(request, response);
			}else {
				dis = request.getRequestDispatcher("backoffice_home.html");
				dis.include(request, response);
			}

		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}

	}
}
