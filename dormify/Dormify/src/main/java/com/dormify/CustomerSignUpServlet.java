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

@WebServlet("/customersignup")
public class CustomerSignUpServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	final String driver = "com.mysql.cj.jdbc.Driver";
	final String url = "jdbc:mysql://localhost:3306/dormify";
	final String user = "root";
	final String password = "Basil@2024";

	String cusName;
	String cusPhone;
	String cusGender;
	String cusAddress;
	String role;

	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;
	RequestDispatcher dis = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

			 HttpSession session = request.getSession(false);
			  role = (String) session.getAttribute("role");
			 
			response.setContentType("text/html");

			cusName = request.getParameter("cusName").toLowerCase().trim();
			cusPhone = request.getParameter("cusPhone").trim();
			cusGender = request.getParameter("cusGender").trim();
			cusAddress = request.getParameter("cusAddress").toLowerCase().trim();
		
			// HttpSession session = request.getSession(false);
			//String name = (String) session.getAttribute("user");

			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			pst = con.prepareStatement("INSERT INTO customerbasic( cusName,cusPhone,cusGender,cusAddress) values(?,?,?,?)");
			pst.setString(1, cusName);
			pst.setString(2, cusPhone);
			pst.setString(3, cusGender);
			pst.setString(4, cusAddress);
			
			pst.executeUpdate();
				
				if(role.equals("Admin")) {
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
