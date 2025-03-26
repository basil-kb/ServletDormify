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

@WebServlet("/edituser")
public class EditUserBasicServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/dormify";
    final String user = "root";
    final String password = "Basil@2024";

    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int id;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String name = (session != null) ? (String) session.getAttribute("user") : "Guest";

            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);

            id = Integer.parseInt(request.getParameter("eid"));

            pst = con.prepareStatement("select u.userid,userName,login_password,login_role,userAddress,userPhone,UserIdProof from user u left join user_login ul on u.userid=ul.userId where u.userid=" + id);
            rs = pst.executeQuery();

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset='ISO-8859-1'>");
            out.println("<title>Edit User</title>");
            out.println("<style>");
            out.println("body { background: linear-gradient(to right top, #0b2e64, #00446d, #00545c, #026041, #54662e); font-family: Arial, sans-serif; color: white; margin: 0; padding: 20px; text-align: center; }");
            out.println(".container { width: 50%; margin: auto; background: rgba(255, 255, 255, 0.1); padding: 20px; border-radius: 10px; box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.3); }");
            out.println("h1 span { color: #ffcc00; }");
            out.println("form { display: flex; flex-direction: column; align-items: center; }");
            out.println("input, select { padding: 10px; margin: 10px; border-radius: 5px; border: none; }");
            out.println(".button { display: inline-block; padding: 10px 20px; background: #00545c; color: white; border-radius: 5px; text-decoration: none; margin: 10px; }");
            out.println(".button:hover { background: #026041; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<h1>Welcome <span>Admin</span>, " + name + "</h1>");
            out.println("<a href='admin' class='button'>Home</a>");
            out.println("<h2>Edit User Basic Details</h2>");
            out.print("<form name='edit-user_basic' action='updateuser' method='post'>");
            while (rs.next()) {
                out.print("<input type='hidden' name='userId' value='" + rs.getInt(1) + "'><br>");
                out.print("<label>Name </label><input type='text' name='userName' value='" + rs.getString(2) + "'/><br>");
                out.print("<label>Address </label><textarea name='userAddress'>" + rs.getString(5) +"</textarea><br>");
                out.print("<label>Role </label>");
                out.print("<select name='Role'>");
                out.print("<option value='Admin'>Admin</option>");
                out.print("<option value='Manager'>Manager</option>");
                out.print("<option value=''>NA</option>");
                out.print("<option value='" + rs.getString(4) + "' selected>" + rs.getString(4) + "</option>");
                out.print("</select><br>");
                out.print("<label>Phone No </label><input type='text' placeholder='91-1111-233-233' pattern='^[91]{2}-[0-9]{4}-[0-9]{3}-[0-9]{3}' name='userPhone' value='" + rs.getString(6) + "'><small>Format: 91-1111-233-233</small><br>");
                out.print("<label>IdProof </label><input type='text' name='userIdProof' value='" + rs.getString(7) + "'><br>");
                out.print("<label>Password </label><input type='text' name='userPass' value='" + rs.getString(3) + "'><br>");
            }
            out.print("<input type='submit' value='UPDATE' class='button'>");
            out.print("</form>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            out.close();

        } catch (IOException | SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
