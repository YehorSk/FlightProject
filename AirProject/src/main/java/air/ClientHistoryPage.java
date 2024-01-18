package air;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ClientHistoryPage
 */
@WebServlet("/ClientHistoryPage")
public class ClientHistoryPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClientHistoryPage() {
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
			response.sendRedirect(request.getContextPath()+"/AdminPage");
			return;
		}
	}
	public void printBody(PrintWriter out,HttpSession session) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
		out.println("<h1>Hello, "+session.getAttribute("role")+" "+session.getAttribute("name") +"</h1>");
		out.println("<h1>History flights</h1>");
		out.println("<table class=\"table\">");
		  out.println("  <thead>");
		  out.println("    <tr>");
		  out.println("      <th scope=\"col\">ID</th>");
		  out.println("      <th scope=\"col\">Plane_id</th>");
		  out.println("      <th scope=\"col\">Pilot_id</th>");
		  out.println("      <th scope=\"col\">From</th>");
		  out.println("      <th scope=\"col\">To</th>");
		  out.println("    </tr>");
		  out.println("  </thead>");
		  out.println("  <tbody>");
		  String sql = "SELECT * FROM flights WHERE user_id ="+session.getAttribute("id")+";";
			try {
		        stmt = con.createStatement();
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					  out.println("    <tr>");

					  out.println("        <th scope=\"row\">"+rs.getString("id")+"</th>");
					  out.println("        <th scope=\"row\">"+rs.getString("plane_id")+"</th>");
					  out.println("        <th scope=\"row\">"+rs.getString("pilot_id")+"</th>");
					  if(formattedDate.compareTo(rs.getString("date_to"))>0) {
						  out.println("        <th scope=\"row\" style='color:red;'>"+rs.getString("date_from")+"</th>");
						  out.println("        <th scope=\"row\" style='color:red;'>"+rs.getString("date_to")+"</th>"); 
					  }else {
						  out.println("        <th scope=\"row\" style='color:green;'>"+rs.getString("date_from")+"</th>");
						  out.println("        <th scope=\"row\" style='color:green;'>"+rs.getString("date_to")+"</th>"); 
					  }
					  
					  

					  out.println("    </tr>");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  out.println("  </tbody>");
		  out.println("</table>");
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
	    out.println("<form action=\"ClientHistoryPage\" method=\"post\">");
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
