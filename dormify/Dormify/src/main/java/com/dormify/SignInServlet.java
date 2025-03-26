package com.dormify;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/signin")
public class SignInServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String driver = "com.mysql.cj.jdbc.Driver";
	final String url = "jdbc:mysql://localhost:3306/dormify";
	final String user = "root";
	final String password = "Basil@2024";

	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	RequestDispatcher dis = null;
	String userName = "";
	String userRole = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			
			 response.setContentType("text/html");
	          PrintWriter out = response.getWriter();
			
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);

			String uname = request.getParameter("uname");
			String upass = request.getParameter("upass");

	         // Fetch  login_user_name & role

			pst = con.prepareStatement("select login_user_name,login_role from user_login where " + "login_user_name=? "
					+ "and " + "login_password=?");

			pst.setString(1, uname);
			pst.setString(2, upass);

			rs = pst.executeQuery();

			while (rs.next()) {
				userName = rs.getString(1);
				userRole = rs.getString(2);
			}
			
			// Set session Attribute role and name
			HttpSession session = request.getSession();
			session.setAttribute("user", userName);
			session.setAttribute("role", userRole);

			if (userRole.equals("Admin")) {
				dis = request.getRequestDispatcher("admin");
				dis.include(request, response);
			} else if (userRole.equals("Manager")) {
				dis = request.getRequestDispatcher("backoffice_home.html");
				dis.include(request, response);
			}
			else {
				out.println("<script>alert('Name or Password Mismatch..!!');"
						+ "window.location='login.html';</script>");
				
			}

		} catch (IOException | SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}

	}

}
