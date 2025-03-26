package com.dormify;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/selectpack")
public class CustomerSelectPackServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String driver = "com.mysql.cj.jdbc.Driver";
	final String url = "jdbc:mysql://localhost:3306/dormify";
	final String user = "root";
	final String password = "Basil@2024";

	int cusId;
	int packPId;

	Connection con = null;
	PreparedStatement pst = null;
	
	
	RequestDispatcher dis = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			cusId = Integer.parseInt(request.getParameter("customerid"));
			packPId = Integer.parseInt(request.getParameter("packPid"));

			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);

			pst = con.prepareStatement("INSERT INTO customer_packages(cusId,package_price_id) values(?,?)");
			pst.setInt(1, cusId);
			pst.setInt(2, packPId);
			System.out.println(pst);
			pst.executeUpdate();
			
				
			
			
			
			
			

			dis = request.getRequestDispatcher("admin");
			dis.include(request, response);

		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}

	}
}
