package air;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * Servlet implementation class MainPage
 */
@WebServlet("/MainPage")
public class MainPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainPage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter(); 
		HttpSession session = request.getSession(true);
		dbConnection db = new dbConnection();
		con = db.getConnection(request);
		if(session.getAttribute("id") == null) {
			printHead(out);
			printBody(out);
			printBottom(out);
		}else {
			if(session.getAttribute("role").equals("admin")) response.sendRedirect(request.getContextPath()+"/AdminPage");
			if(session.getAttribute("role").equals("instructor")) response.sendRedirect(request.getContextPath()+"/InstructorPage");
			if(session.getAttribute("role").equals("client")) response.sendRedirect(request.getContextPath()+"/ClientPage");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(true);
		String operation = request.getParameter("operation");
		if(operation.equals("login")) {
			login(out,request,session,response);
			response.sendRedirect(request.getContextPath()+"/MainPage");
		}
	}
	
	private void login(PrintWriter out, HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		String sql = "SELECT *,COUNT(*) as amount FROM users WHERE email = '"+request.getParameter("email")+"';";
		try {
	        stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			rs.next();
			if(Integer.parseInt(rs.getString("amount"))!=0) {
				if(rs.getString("pwd").equals(request.getParameter("password"))) {
					session.setAttribute("id", rs.getString("id"));
					session.setAttribute("role", rs.getString("role"));
					session.setAttribute("name", rs.getString("name"));
					session.setAttribute("surname", rs.getString("surname"));
					session.setAttribute("email", rs.getString("email"));
				}else {
					out.println("<script>alert(\"" + "Incorrect password!!" + "\");</script>");
				}
			}else {
				out.println("<script>alert(\"" + "User doesn't exist!!" + "\");</script>");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printBody(PrintWriter out) {
		  out.println("<div class=\"d-flex align-items-center justify-content-center\" style=\"height: 100vh;\">");
		  out.println("<form method='post' action='MainPage'>");
		  out.println("<input type='hidden' name='operation' value='login'>");
		  out.println("  <div class=\"form-group\">");
		  out.println("    <label for=\"exampleInputEmail1\">Email address</label>");
		  out.println("    <input type=\"email\" name='email' class=\"form-control\" id=\"exampleInputEmail1\" aria-describedby=\"emailHelp\" placeholder=\"Enter email\">");
		  out.println("  </div>");
		  out.println("  <div class=\"form-group\">");
		  out.println("    <label for=\"exampleInputPassword1\">Password</label>");
		  out.println("    <input type=\"password\" name='password' class=\"form-control\" id=\"exampleInputPassword1\" placeholder=\"Password\">");
		  out.println("  </div>");
		  out.println("  <button type=\"submit\" class=\"btn btn-primary\">Enter</button>");
		  out.println("  <p>Don't have an account? <a href=\"RegisterPage\">Register now</a></p>");
		  out.println("</form>");
		  out.println("  </div>");
	}
	
	public void printHead(PrintWriter out) {
	    out.println("<!DOCTYPE html>");
	    out.println("<html lang=\"en\" data-bs-theme=\"dark\">");
	    out.println("  <head>");
	    out.println("    <meta charset=\"utf-8\">");
	    out.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
	    out.println("    <title>AIS</title>");
	    out.println("<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL\" crossorigin=\"anonymous\"></script>\r\n"
	    		+ "");
	    out.println("    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet' integrity='sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN' crossorigin='anonymous'>");
	    out.println("  </head>");
	    out.println("  <body>");
	    out.println("<div class=\"container\">");
	}

	
	public void printBottom(PrintWriter out) {
		out.println("  </div>");
		out.println("    <script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js' integrity=\"sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL\" crossorigin=\"anonymous\"></script>\r\n"
				+ "  </body>\r\n"
				+ "</html>");
	}

}
