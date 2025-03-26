package com.dormify;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    try {
	        response.setContentType("text/html");
	        PrintWriter out = response.getWriter();

	        HttpSession session = request.getSession();
	        session.invalidate();

	        out.println("<html>");
	        out.println("<head>");
	        out.println("<title>Logout</title>");
	        out.println("<style>");
	        out.println("body {"
	                + "background: linear-gradient(to right top, #0b2e64, #00446d, #00545c, #026041, #54662e);"
	                + "font-family: Arial, sans-serif; color: white; margin: 0; height: 100vh;"
	                + "display: flex; align-items: center; justify-content: center; text-align: center;"
	                + "}");
	        out.println(".container {"
	                + "width: 400px; background: rgba(255, 255, 255, 0.1); padding: 30px;"
	                + "border-radius: 10px; box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.3);"
	                + "}");
	        out.println("h2 { margin-bottom: 20px; }");
	        out.println(".nav-container a {"
	                + "display: inline-block; background: rgba(255, 255, 255, 0.2); color: white;"
	                + "padding: 15px; margin-top: 20px; text-decoration: none; font-size: 18px;"
	                + "border-radius: 8px; transition: background 0.3s;"
	                + "}");
	        out.println(".nav-container a:hover { background: #00545c; }");
	        out.println("</style>");
	        out.println("</head>");
	        out.println("<body>");
	        out.println("<div class='container'>");
	        out.println("<h2>You are successfully logged out!</h2>");
	        out.println("<div class='nav-container'>");
	        out.println("<a href='index.html'>Login</a>");
	        out.println("</div>");
	        out.println("</div>");
	        out.println("</body>");
	        out.println("</html>");

	        out.close();
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	}
}