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
		            "<head><title>" + title + "</title></head>\n" + //
		            "<body bgcolor=\"#f0f0f0\">\n" + //
		            "<h1 align=\"center\">" + title + "</h1>\n");

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
			         
			         out.println( "You added the following contact:" + firstName + " " + lastName + " " + email + " " + phone + " " + address + " " + birthday + "<br><br>");
		         }
		         //After potential insertion pull up the whole contact list to display
		         String tableName = "ContactList";
		         
		         String selectSQL = "SELECT * FROM " + tableName;
		         preparedStatement = connection.prepareStatement(selectSQL);
		         ResultSet rs = preparedStatement.executeQuery();
		         
		         //output modified table
		         out.println("Here is the new modified database: <br><ul>");
		         while (rs.next()) {
		            int id = rs.getInt("id");
		            String dbfirstName = rs.getString("first").trim();
		            String dblastName = rs.getString("last").trim();
		            String dbEmail = rs.getString("email").trim();
		            String dbPhone = rs.getString("phone").trim();
		            String dbAddress = rs.getString("address").trim();
		            String dbBirthday = rs.getString("birthday").trim();
		            
		            out.println("<li>");
					out.println("ID: " + id + ", ");
					out.println("User: " + dbfirstName + ", ");
					out.println("User: " + dblastName + ", ");
					out.println("Email: " + dbEmail + ", ");
					out.println("Phone: " + dbPhone + ", ");
					out.println("Address: " + dbAddress + ", ");
					out.println("Birthday: " + dbBirthday + ", ");
					out.println("</li>");
		         }
		         out.println("</ul>");
		         
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