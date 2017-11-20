package testSQLite;
import java.sql.*;

public class Client {
  public static void main( String args[] ) {
      Connection c = null;
      Statement stmt = null;
      
      try {
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite:test.db");
         System.out.println("Opened database successfully");

         stmt = c.createStatement();
         String sql = "CREATE TABLE COMPANY " +
                        "(ASIN 				TEXT PRIMARY KEY     NOT NULL," +
                        " TITLE				TEXT    NOT NULL, " + 
                        " AUTHOR         	TEXT	NOT NULL, " + 
                        " MARKETPLACE    	TEXT, " + 
                        " UNITS_SOLD     	INT," +
                        " UNITS_REFUNDED		INT," + 
                        " NET_UNITS_SOLD		INT," +
                        " ROYALTY_TYPE		TEXT," +
                        " TRANSACTION_TYPE	TEXT," +
                        " CURRENCY			TEXT," +
                        " AVG_LIST_PRICE		REAL," +
                        " AVG_OFFER_PRICE	REAL," +
                        " AVG_DELIVERY_COST	REAL," +
                        " ROYALTY			REAL)"; 
         stmt.executeUpdate(sql);
         stmt.close();
         c.close();
      } catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
      System.out.println("Table created successfully");
   }
}