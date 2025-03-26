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


@WebServlet("/newuser")
public class NewUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	final String driver = "com.mysql.cj.jdbc.Driver";
	final String url = "jdbc:mysql://localhost:3306/dormify";
	final String user = "root";
	final String password = "Basil@2024";
	
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
			
			response.setContentType("text/html");
		
			
			
			
			userName=request.getParameter("UserName").toLowerCase().trim();
			userAddress=request.getParameter("Address").toLowerCase().trim();
			userRole=request.getParameter("Role").trim();
			userPhone=request.getParameter("Phone").toLowerCase().trim();
			userIdProof=request.getParameter("IdProof").toLowerCase().trim();
			userPass=request.getParameter("Password").toLowerCase().trim();
			

			//HttpSession session = request.getSession(false);
			//String name = (String) session.getAttribute("user");	
			
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			if(userRole.equals("Admin") || userRole.equals("Manager") ) {
				pst = con.prepareStatement("CALL add_user(?,?,?,?,?,?);"); 
				pst.setString(1, userName);
				pst.setString(2, userAddress);
				pst.setString(3, userPhone);
				pst.setString(4, userIdProof);
				pst.setString(5, userPass);
				pst.setString(6, userRole);
				pst.executeUpdate();
				dis = request.getRequestDispatcher("admin");
				dis.include(request, response);
				
				
			}
			else if(userRole.equals("None")) {
					pst = con.prepareStatement("INSERT INTO user( userName,userAddress,userPhone,UserIdProof)"
							+ "values(?,?,?,?)"); 
					pst.setString(1, userName);
					pst.setString(2, userAddress);
					pst.setString(3, userPhone);
					pst.setString(4, userIdProof);
					pst.executeUpdate();
					
					dis = request.getRequestDispatcher("admin");
					dis.include(request, response);
				
			}
			
			

			
			
			
			
			
		}catch(SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
	
	
}
