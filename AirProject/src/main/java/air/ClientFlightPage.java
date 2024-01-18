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
 * Servlet implementation class ClientFlightPage
 */
@WebServlet("/ClientFlightPage")
public class ClientFlightPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClientFlightPage() {
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
			response.sendRedirect(request.getContextPath()+"/MainPage");
		}else {
			printHead(out);
			printBody(out,session);
			printBottom(out);
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
		if(operation.equals("logout")) {
			session.invalidate();
			response.sendRedirect(request.getContextPath()+"/ClientFlightPage");
			return;
		}
		if(operation.equals("add")) {
			addFlight(out,request,session,response);
			response.sendRedirect(request.getContextPath()+"/ClientFlightPage");
			return;
		}
	}
	
	private void addFlight(PrintWriter out, HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		 if(request.getParameter("from").compareTo(request.getParameter("to"))<0) {
			 String check = "SELECT COUNT(*) as amount FROM flights\r\n"
			 		+ "WHERE date_from >= '"+request.getParameter("from")+"' AND date_to <= '"+request.getParameter("to")+"' AND (pilot_id = "+request.getParameter("instructor")+" OR plane_id = "+request.getParameter("plane")+");\r\n"
			 		+ "";
			 int am;
			 try {
			     stmt = con.createStatement();
			     rs = stmt.executeQuery(check);
			     rs.next();
			     am = rs.getInt("amount");
			     System.out.println(am);
			     if(am==0) {
					 String sql = "INSERT INTO `flights` (`id`, `user_id`, `pilot_id`, `plane_id`, `date_from`, `date_to`) VALUES (NULL, '"+session.getAttribute("id")+"', '"+request.getParameter("instructor")+"', '"+request.getParameter("plane")+"', '"+request.getParameter("from")+"', '"+request.getParameter("to")+"')";
				     stmt = con.createStatement();
				     stmt.executeUpdate(sql);

			     }else {
			    	 System.out.println("You can't!");
			     }
				 } catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				 }

		 
	}
	}
	
	public void printBody(PrintWriter out,HttpSession session) {
		out.println("<h1>Hello, "+session.getAttribute("role")+" "+session.getAttribute("name") +"</h1>");
		out.println("<h1>Add flights</h1>");
		out.println("<h3>Choose Instructor</h3>");
		out.println("      <form method='POST' action='ClientFlightPage'>");
		out.println("		   <input type='hidden' name='operation' value='add'>");
		try {
			out.println("  <div class=\"col-md-2\">");
			out.println("    <label for=\"inputInstructor\" class=\"form-label\">Category</label>");
			out.println("    <select id=\"inputInstructor\" name=\"instructor\" class=\"form-select\" >");
			String sql = "SELECT * FROM users WHERE role='instructor' and active = 0";
	        stmt = con.createStatement();
	        rs = stmt.executeQuery(sql);
	        while(rs.next()) {
			out.println("      <option value=" + rs.getInt("id") + ">"+rs.getString("name")+"</option>");

	        }
			out.println("    </select>");
			out.println("  </div>");
		} catch (SQLException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
		out.println("<h3>Choose Plane</h3>");
		try {
			out.println("  <div class=\"col-md-2\">");
			out.println("    <label for=\"inputPlane\" class=\"form-label\">Category</label>");
			out.println("    <select id=\"inputPlane\" name=\"plane\" class=\"form-select\">");
			String sql = "SELECT * FROM planes WHERE active = 0";
	        stmt = con.createStatement();
	        rs = stmt.executeQuery(sql);
	        while(rs.next()) {
			out.println("      <option value="+ rs.getInt("id") +">"+rs.getString("name")+"</option>");
	        }
			out.println("    </select>");
			out.println("  </div>");
		} catch (SQLException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
		out.println("<h3>Choose Date</h3>");
		out.println("<label for=\"from\">From:</label>\r\n"
				+ "  <input type=\"date\" id=\"from\" name=\"from\" required>");
		out.println("<label for=\"to\">To:</label>\r\n"
				+ "  <input type=\"date\" id=\"to\" name=\"to\" required>");
		out.println("        <td><button type=\"submit\" class=\"btn btn-primary\">Add</button></td>");
		out.println("      </form>");
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
	    out.println("<nav class=\"navbar navbar-expand-lg bg-body-tertiary sticky-top\">");
	    out.println("  <div class=\"container-fluid\">");
	    out.println("    <a class=\"navbar-brand\" href=\"#\">AIS</a>");
	    out.println("    <button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#navbarNav\" aria-controls=\"navbarNav\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">");
	    out.println("      <span class=\"navbar-toggler-icon\"></span>");
	    out.println("    </button>");
	    out.println("    <div class=\"collapse navbar-collapse\" id=\"navbarNav\">");
	    out.println("      <ul class=\"navbar-nav\">");
	    out.println("        <li class=\"nav-item\">");
	    out.println("          <a class=\"nav-link\" href=\"AdminPage\">Main</a>");
	    out.println("        </li>");
	    out.println("        <li class=\"nav-item\">");
	    out.println("          <a class=\"nav-link\" href=\"ClientFlightPage\">Manage flights</a>");
	    out.println("        </li>");
	    out.println("        <li class=\"nav-item\">");
	    out.println("          <a class=\"nav-link\" href=\"ClientHistoryPage\">History flights</a>");
	    out.println("        </li>");
	    out.println("<form action=\"ClientFlightPage\" method=\"post\">");
	    out.println("    <input type=\"hidden\" name=\"operation\" value=\"logout\">");
	    out.println("    <li class=\"nav-item\">");
	    out.println("        <button type=\"submit\" class=\"nav-link\" style=\"border: none; background: none; cursor: pointer;\">Logout</button>");
	    out.println("    </li>");
	    out.println("</form>");
	    out.println("      </ul>");
	    out.println("    </div>");
	    out.println("  </div>");
	    out.println("</nav>");
	    out.println("<div class=\"container\">");
	}
	public void printBottom(PrintWriter out) {
		out.println("  </div>");
		out.println("    <script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js' integrity=\"sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL\" crossorigin=\"anonymous\"></script>\r\n"
				+ "  </body>\r\n"
				+ "</html>");
	}

}
