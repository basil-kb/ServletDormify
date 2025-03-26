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

@WebServlet("/updateuser")
public class UpdateUserBasicServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	final String driver = "com.mysql.cj.jdbc.Driver";
	final String url = "jdbc:mysql://localhost:3306/dormify";
	final String user = "root";
	final String password = "Basil@2024";
	int userId;
	String userName;
	String userAddress;
	String userRole;
	String userPhone;
	String userIdProof;
	String userPass;

	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;
	RequestDispatcher dis = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			
			userId = Integer.parseInt(request.getParameter("userId"));
			userName=request.getParameter("userName").toLowerCase().trim();
			userAddress=request.getParameter("userAddress").toLowerCase().trim();
			userRole=request.getParameter("Role");
			System.out.println(userRole);
			userPhone=request.getParameter("userPhone").toLowerCase().trim();
			userIdProof=request.getParameter("userIdProof");
			userPass=request.getParameter("userPass");
			
			pst = con.prepareStatement("CALL edit_user(?,?,?,?,?,?,?)");
			pst.setInt(1, userId);
			pst.setString(2, userName);
			pst.setString(3, userAddress);
			pst.setString(4, userPhone);
			pst.setString(5, userIdProof);
			pst.setString(6, userPass);
			pst.setString(7, userRole);
			

			pst.executeUpdate();
			con.close();
			
			dis = request.getRequestDispatcher("admin");
			dis.include(request, response);
			
			
		} catch (IOException |SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	

		
	}
}
