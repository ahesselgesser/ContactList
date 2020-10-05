import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ContactList")
public class ContactList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public ContactList() {
		   super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		      String email = request.getParameter("email");
		      String phone = request.getParameter("phone");
		      String firstName = request.getParameter("firstName");
		      String address = request.getParameter("address");
		      String lastName = request.getParameter("lastName");
		      String birthday = request.getParameter("birthday");
		      

		      response.setContentType("text/html");
		      PrintWriter out = response.getWriter();
		      
		      //HTML output basic stuff
		      String title = "Contact List";
		      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
		            "transitional//en\">\n"; //
		      out.println(docType + //
		            "<html>\n" + //
		            "<head><style>table, th, td {border: 1px solid black;border-collapse: collapse;}"
		            + "</style><title>" + title + "</title></head>\n" + //
		            "<body>\n" + //
		            "<h1>" + title + "</h1>\n");

		      //Connect to sql stuff
		      Connection connection = null;
		      PreparedStatement preparedStatement = null;
		      
		      //
		      
		      
		      try {
		         DBConnectionHesselgesser.getDBConnection(getServletContext());
		         connection = DBConnectionHesselgesser.connection;
		         
		         // Create query and insert if there is a thing to insert
		         if (firstName != null || lastName != null) {
		        	 String insertSql = " INSERT INTO ContactList (id, FIRST, LAST, EMAIL, PHONE, ADDRESS, BIRTHDAY) values (default, ?, ?, ?, ?, ?, ?)";
			         PreparedStatement preparedStmt = connection.prepareStatement(insertSql);
			         preparedStmt.setString(1, firstName);
			         preparedStmt.setString(2, lastName);
			         preparedStmt.setString(3, email);
			         preparedStmt.setString(4, phone);
			         preparedStmt.setString(5, address);
			         preparedStmt.setString(6, birthday);
			         preparedStmt.execute();
			         
			         out.println( "You added the following contact: " + firstName + "<br><br>");
		         }
		         //After potential insertion pull up the whole contact list to display
		         String tableName = "ContactList";
		         
		         String selectSQL = "SELECT * FROM " + tableName;
		         preparedStatement = connection.prepareStatement(selectSQL);
		         ResultSet rs = preparedStatement.executeQuery();
		         
		         //output modified table
		         out.println("Here is the contact list: <br>");
		         out.println("<form action=\"RemoveContact\" method=\"POST\">");
		         out.println("<table style=\"width:100%\">");
		         out.println("<tr><th>Select</th><th>First Name</th><th>Last Name</th><th>Email</th><th>Phone Number</th><th>Address</th><th>Birthday</th></tr>");
		         while (rs.next()) {
		        	int dbID = rs.getInt("id");
		            String dbfirstName = rs.getString("first").trim();
		            String dblastName = rs.getString("last").trim();
		            String dbEmail = rs.getString("email").trim();
		            String dbPhone = rs.getString("phone").trim();
		            String dbAddress = rs.getString("address").trim();
		            String dbBirthday = rs.getString("birthday").trim();
		            
		            out.println("<tr>");
		            out.printf("<td><input type=\"checkbox\" name=\"contactList\" value=\"%d\"</td>", dbID);
		            out.printf("<td>%-30s</td><td>%-30s</td><td>%-30s</td><td>%-30s</td><td>%-30s</td><td>%-5s</td>", dbfirstName, dblastName, dbEmail, dbPhone, dbAddress, dbBirthday);
					out.println("</tr>");
		         }
		         out.println("</table>");
		         out.println("<input type=\"submit\" value=\"Delete Selected\" />");
		         out.println("</form>");
		         //Link back to original page and last of html required stuff
		         out.println("<a href=/web-project-ContactList/addContact.html>Add Contact</a><br>");
		         out.println("</body></html>");
		         
		         //close everything
		         rs.close();
		         preparedStatement.close();
		         connection.close();
		         
		      } catch (SQLException se) {
		         se.printStackTrace();
		      } catch (Exception e) {
		         e.printStackTrace();
		      } finally {
		         try {
		            if (preparedStatement != null)
		               preparedStatement.close();
		         } catch (SQLException se2) {
		         }
		         try {
		            if (connection != null)
		               connection.close();
		         } catch (SQLException se) {
		            se.printStackTrace();
		         }
		      }
		   }
		   
		   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			      doGet(request, response);
			   }


}