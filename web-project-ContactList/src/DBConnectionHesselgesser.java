import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletContext;

public class DBConnectionHesselgesser {
   static Connection connection = null;
   static ServletContext servletContext;

   static void getDBConnection() {
      System.out.println("-------- MySQL JDBC Connection ------------");
      try {
         Class.forName("com.mysql.jdbc.Driver");
      } catch (ClassNotFoundException e) {
         System.out.println("Where is your MySQL JDBC Driver?");
         e.printStackTrace();
         return;
      }
      System.out.println("MySQL JDBC Driver Registered!");

      connection = null;
      try {
         UtilPropHesselgesser.loadProperty(servletContext);
         connection = DriverManager.getConnection(getURL(), getUserName(), getPassword());
      } catch (Exception e) {
         System.out.println("Connection Failed! Check output console");
         e.printStackTrace();
         return;
      }

      if (connection != null) {
         System.out.println("Connected to database!");
      } else {
         System.out.println("Failed to make connection!");
      }
   }

   static String getURL() {
      String url = UtilPropHesselgesser.getProp("url");
      //System.out.println("[DBG] URL: " + url);
      return url;
   }

   static String getUserName() {
      String usr = UtilPropHesselgesser.getProp("user");
      //System.out.println("[DBG] URL: " + usr);
      return usr;
   }

   static String getPassword() {
      String pwd = UtilPropHesselgesser.getProp("password");
      //System.out.println("[DBG] URL: " + pwd);
      return pwd;
   }

   public static void getDBConnection(ServletContext context) {
      servletContext = context;
      getDBConnection();
   }
}