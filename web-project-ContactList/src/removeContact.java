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

@WebServlet("/RemoveContact")
public class removeContact extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public removeContact() {
		   super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		      String[] removeList = request.getParameterValues("contactList");
		      

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
		            "<body bgcolor=\"#f0f0f0\">\n" + //
		            "<h1 align=\"center\">" + title + "</h1>\n");

		      //Connect to sql stuff
		      Connection connection = null;
		        
		      try {
		         DBConnectionHesselgesser.getDBConnection(getServletContext());
		         connection = DBConnectionHesselgesser.connection;
		         
		         // Create query and insert if there is a thing to insert
		         
		         if (removeList.length > 0) {
		        	 out.println("<table style=\"width:100%\">");
			         out.println("<tr><th>First Name</th><th>Last Name</th><th>Email</th><th>Phone Number</th><th>Address</th><th>Birthday</th></tr>");
			         out.println("You removed the following contacts:");
		        	 for (int i = 0; i < removeList.length; i++) {
		        		//First grab item to be removed so it can be displayed after removal
			        	String searchSql = "SELECT * FROM ContactList WHERE ID LIKE ?";
				        PreparedStatement preparedStmt = connection.prepareStatement(searchSql);
				        preparedStmt.setString(1, removeList[i]);
				        ResultSet rs = preparedStmt.executeQuery();
				        
				        rs.next(); // Only one result is possible since ID is a primary key
				         
			            String dbfirstName = rs.getString("first").trim();
			            String dblastName = rs.getString("last").trim();
			            String dbEmail = rs.getString("email").trim();
			            String dbPhone = rs.getString("phone").trim();
			            String dbAddress = rs.getString("address").trim();
			            String dbBirthday = rs.getString("birthday").trim();
				         
			            String removed = String.format("<tr><td>%-30s</td><td>%-30s</td><td>%-30s</td><td>%-30s</td><td>%-30s</td><td>%-5s</td></tr>", dbfirstName, dblastName, dbEmail, dbPhone, dbAddress, dbBirthday);
			            
			            //Then remove the item
			            String deleteSql = "DELETE FROM ContactList WHERE ID = ?";
				        preparedStmt = connection.prepareStatement(deleteSql);
				        preparedStmt.setString(1, removeList[i]);
				        preparedStmt.execute();
			            
			            out.println(removed);
			            rs.close();
		        	 }
		        	 out.println("</table>");
		         } else {
		        	 out.println("You did not select any contacts to remove!");
		         }
		         //Link back to original page and last of html required stuff
		         out.println("<a href=/web-project-ContactList/addContact.html>Add Contact</a><br>");
		         out.println("</body></html>");
		         
		         //close everything
		         
		         connection.close();
		         
		      } catch (SQLException se) {
		         se.printStackTrace();
		      } catch (Exception e) {
		         e.printStackTrace();
		      } finally {
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