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

@WebServlet("/updatecustomer")
public class UpdateCustomerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	final String driver = "com.mysql.cj.jdbc.Driver";
	final String url = "jdbc:mysql://localhost:3306/dormify";
	final String user = "root";
	final String password = "Basil@2024";
	int cusId;
	String cusName;
	String cusAddress;
	String cusPhone;
	String cusGender;
	
	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;
	RequestDispatcher dis = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			
			cusId = Integer.parseInt(request.getParameter("cusId"));
			cusName=request.getParameter("cusName").toLowerCase().trim();
			cusAddress=request.getParameter("cusAddress").toLowerCase().trim();
			cusPhone=request.getParameter("cusPhone").toLowerCase().trim();
			cusGender=request.getParameter("cusGender");
			
			
			pst = con.prepareStatement("UPDATE customerbasic SET cusName = ?, cusPhone = ?, cusGender = ?, cusAddress = ? WHERE cusId = ?;"
					+ "");
			pst.setInt(5, cusId);
			pst.setString(1, cusName);
			pst.setString(4, cusAddress);
			pst.setString(2, cusPhone);
			pst.setString(3, cusGender);
			
			

			pst.executeUpdate();
			con.close();
			
			dis = request.getRequestDispatcher("admin");
			dis.include(request, response);
			
			
		} catch (IOException |SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	

		
	}
}
