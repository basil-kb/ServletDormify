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

@WebServlet("/package")
public class PackageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String driver = "com.mysql.cj.jdbc.Driver";
	final String url = "jdbc:mysql://localhost:3306/dormify";
	final String user = "root";
	final String password = "Basil@2024";

	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;
	RequestDispatcher dis = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			int i = 1;
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			HttpSession session = request.getSession(false);
			String name = (session != null) ? (String) session.getAttribute("user") : "Guest";

			out.println("<html>");
			out.println("<head>");
			out.println("<meta charset='ISO-8859-1'>");
			out.println("<title>Package Details</title>");
			out.println("<style>");
			out.println("body { background-color: #f4f4f4; font-family: Arial, sans-serif; text-align: center; margin: 0; padding: 20px; }");
			out.println(".container { width: 80%; margin: auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.2); }");
			out.println("h1 { color: #333; }");
			out.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
			out.println("th, td { padding: 10px; border: 1px solid #ddd; text-align: center; }");
			out.println("th { background: #007bff; color: white; }");
			out.println("tr:nth-child(even) { background: #f9f9f9; }");
			out.println(".button { display: inline-block; padding: 10px 20px; background: #007bff; color: white; border-radius: 5px; text-decoration: none; margin: 10px; font-weight: bold; }");
			out.println(".button:hover { background: #0056b3; }");
			out.println("</style>");
			out.println("</head>");
			out.println("<body>");
			out.println("<div class='container'>");

			out.println("<h1>Welcome, " + name + "</h1>");
			out.println("<a href='admin' class='button'>Home</a>");

			out.println("<h2>Package Details</h2>");

			out.println("<table>");
			out.println("<tr>");
			out.println("<th>SINO</th>");
			out.println("<th>Package Type</th>");
			out.println("<th>Room Size</th>");
			out.println("<th>Package Price</th>");
			out.println("</tr>");

			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			
			pst = con.prepareStatement("SELECT p.package_type, pp.packagePrice, rs.room_size "
					+ "FROM packageprice pp JOIN package p ON pp.package_id = p.package_id "
					+ "JOIN room_size rs ON pp.rs_id = rs.rs_id ORDER BY p.package_type");
			rs = pst.executeQuery();

			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + i + "</td>");
				out.println("<td>" + rs.getString(1) + "</td>");
				out.println("<td>" + rs.getString(3) + "</td>");
				out.println("<td>" + rs.getInt(2) + "/--</td>");
				out.println("</tr>");
				i++;
			}

			out.println("</table>");
			out.println("</div>");
			out.println("</body>");
			out.println("</html>");
			out.close();

			dis.include(request, response);

		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
}
