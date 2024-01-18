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
 * Servlet implementation class AdminPlanePage
 */
@WebServlet("/AdminPlanePage")
public class AdminPlanePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminPlanePage() {
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
		if(operation.equals("delete")) {
			deletePlane(out,request,session,response);
			response.sendRedirect(request.getContextPath()+"/AdminPlanePage");
		}
		if(operation.equals("add")) {
			addPlane(out,request,session,response);
			response.sendRedirect(request.getContextPath()+"/AdminPlanePage");
		}
		if(operation.equals("edit")) {
			updatePlane(out,request,session,response);
			response.sendRedirect(request.getContextPath()+"/AdminPlanePage");
		}
		if(operation.equals("logout")) {
			session.invalidate();
			response.sendRedirect(request.getContextPath()+"/AdminPlanePage");
			return;
		}
	}
	
	private void updatePlane(PrintWriter out, HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		 String sql = "UPDATE planes SET name = '"+request.getParameter("name")+"', active = '"+request.getParameter("active")+"' WHERE id = '"+request.getParameter("id")+"';";
		 try {
		     stmt = con.createStatement();
		     stmt.executeUpdate(sql);
			 } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
	}
	private void deletePlane(PrintWriter out, HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		String check = "SELECT COUNT(*) as amount FROM flights WHERE plane_id =  "+request.getParameter("id")+";";
		try {
		     stmt = con.createStatement();
		     rs = stmt.executeQuery(check);
		     rs.next();
		     if(rs.getInt("amount")==0) {
		    	 String sql = "DELETE FROM planes WHERE id = '"+request.getParameter("id")+"';";
		    	 stmt = con.createStatement();
			     stmt.executeUpdate(sql);
		     }else {
		    	 System.out.println(rs.getInt("amount"));
		     }
			 } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			} 
	}
	private void addPlane(PrintWriter out, HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		 String sql = "INSERT INTO `planes` (`id`, `name`,`active`) VALUES (NULL, '"+request.getParameter("name")+"','"+request.getParameter("active")+"')";
		 try {
	     stmt = con.createStatement();
	     stmt.executeUpdate(sql);
		 } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
	public void printBody(PrintWriter out,HttpSession session) {
		out.println("<h1>Hello, "+session.getAttribute("role")+" "+session.getAttribute("name") +"</h1>");
		out.println("<h2>Manage planes</h2>");
		  out.println("<table class=\"table\">");
		  out.println("  <thead>");
		  out.println("    <tr>");
		  out.println("      <th scope=\"col\">ID</th>");
		  out.println("      <th scope=\"col\">Name</th>");
		  out.println("      <th scope=\"col\">Active</th>");
		  out.println("      <th scope=\"col\">Edit</th>");
		  out.println("      <th scope=\"col\">Delete</th>");
		  out.println("    </tr>");
		  out.println("  </thead>");
		  out.println("  <tbody>");
		  String sql = "SELECT * FROM planes;";
			try {
		        stmt = con.createStatement();
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					  out.println("    <tr>");
					  out.println("      <form method='POST' action='AdminPlanePage'>");
					  out.println("		   <input type='hidden' name='operation' value='edit'>");
					  out.println("		   <input type='hidden' name='id' value='"+rs.getString("id")+"'>");
					  out.println("        <th scope=\"row\">"+rs.getString("id")+"</th>");
					  out.println("        <td><input type=\"text\" name=\"name\" value="+rs.getString("name")+"></td>");
					  out.println("        <td><input type=\"number\" name=\"active\" value="+rs.getString("active")+" min=\"0\" max=\"1\"></td>");
					  out.println("        <td><button type=\"submit\" class=\"btn btn-primary\">Edit</button></td>");
					  out.println("      </form>");
					  out.println("      <form method='POST' action='AdminPlanePage'>");
					  out.println("		   <input type='hidden' name='operation' value='delete'>");
					  out.println("		   <input type='hidden' name='id' value='"+rs.getString("id")+"'>");
					  out.println("        <td><button type=\"submit\" class=\"btn btn-danger\">Delete</button></td>");
					  out.println("      </form>");
					  out.println("    </tr>");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  out.println("  </tbody>");
		  out.println("</table>");
		  
		  out.println("<h3>Add subject</h3>");
		  out.println("<form class=\"form-inline\" method='POST' action='AdminPlanePage'>");
		  out.println("	<input type='hidden' name='operation' value='add'>");
		  out.println("  <div class=\"form-group mb-2\">");
		  out.println("  </div>");
		  out.println("  <div class=\"form-group mx-sm-3 mb-2\">");
		  out.println("    <label for=\"inputPassword2\" class=\"sr-only\">Name</label>");
		  out.println("    <input type=\"text\" name='name' class=\"form-control\" id=\"inputPassword2\" placeholder=\"Name\">");
		  out.println("  </div>");
		  out.println("  <div class=\"form-group mx-sm-3 mb-2\">");
		  out.println("    <label for=\"inputPassword2\" class=\"sr-only\">Active</label>");
		  out.println("    <input type=\"number\" name='active' class=\"form-control\" id=\"inputPassword2\" placeholder=\"Active\" min=\"0\" max=\"1\">");
		  out.println("  </div>");
		  out.println("  <button type=\"submit\" class=\"btn btn-primary mb-2\">Add Plane</button>");
		  out.println("</form>");
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
	    out.println("          <a class=\"nav-link\" href=\"AdminPlanePage\">Manage planes</a>");
	    out.println("        </li>");
	    out.println("        <li class=\"nav-item\">");
	    out.println("          <a class=\"nav-link\" href=\"AdminInstructorPage\">Manage instructor</a>");
	    out.println("        </li>");
	    out.println("<form action=\"AdminPlanePage\" method=\"post\">");
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
