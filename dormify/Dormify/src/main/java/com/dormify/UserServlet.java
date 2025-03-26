package com.dormify;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/dormify";
    final String user = "root";
    final String password = "Basil@2024";

    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int i = 1;

        try {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            HttpSession session = request.getSession(false);
            String name = (session != null) ? (String) session.getAttribute("user") : "Guest";
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            
         // Fetch total User details
            pst = con.prepareStatement("select u.userid,userName,login_password,login_role,userAddress,userPhone,UserIdProof from user u  left join user_login ul on  u.userid=ul.userId");
            rs = pst.executeQuery();

            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset='ISO-8859-1'>");
            out.println("<title>User Management</title>");
            out.println("<style>");
            out.println("body { background: linear-gradient(to right top, #0b2e64, #00446d, #00545c, #026041, #54662e); font-family: Arial, sans-serif; color: white; margin: 0; padding: 20px; text-align: center; }");
            out.println(".container { width: 80%; margin: auto; background: rgba(255, 255, 255, 0.1); padding: 20px; border-radius: 10px; box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.3); }");
            out.println("h1 span { color: #ffcc00; }");
            out.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
            out.println("th, td { border: 1px solid white; padding: 10px; text-align: center; }");
            out.println("th { background: rgba(255, 255, 255, 0.2); }");
            out.println("a { color: white; text-decoration: none; }");
            out.println("a:hover { text-decoration: underline; }");
            out.println(".button { display: inline-block; padding: 10px 20px; background: #00545c; color: white; border-radius: 5px; text-decoration: none; margin: 10px; }");
            out.println(".button:hover { background: #026041; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<h1>Welcome <span>Admin</span>, " + name + "</h1>");
            out.println("<a href='admin' class='button'>Home</a>");
            out.println("<a href='newuser.html' class='button'>New User</a>");
            out.println("<h2>All Users Details</h2>");
            out.println("<table>");
            out.println("<tr><th>SINO</th><th>USER NAME</th><th>PASSWORD</th><th>ROLE</th><th>ADDRESS</th><th>PHONE</th><th>ID PROOF</th><th>EDIT</th><th>DELETE</th></tr>");
            
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + i + "</td>");
                out.println("<td>" + rs.getString(2) + "</td>");
                out.println("<td>" + rs.getString(3) + "</td>");
                out.println("<td>" + rs.getString(4) + "</td>");
                out.println("<td>" + rs.getString(5) + "</td>");
                out.println("<td>" + rs.getString(6) + "</td>");
                out.println("<td>" + rs.getString(7) + "</td>");
                out.println("<td><a href='edituser?eid=" + rs.getInt(1) + "'>Edit</a></td>");
                out.println("<td><a href='deleteuser?did=" + rs.getInt(1) + "' onclick=\"return confirm('Are you sure you want to delete this user?');\">Delete</a></td>");
                out.println("</tr>");
                i++;
            }
            
            out.println("</table>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

            out.close();
        } catch (IOException | SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
