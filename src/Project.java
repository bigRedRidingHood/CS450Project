// JDBC libraries
// JDBC Packages
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Project {

	// Instance Variables
	private int B_ID;
	private int Street_Num;
	private int Phone_Num;
	private int Capacity;
	private int Price;
	private int Age;
	private int Date_Day;
	private int Date_Month;
	private int Date_Year;
	private int Reservation_Num;
	private int Party_Size;
	private int Quantity;
	private String Gender;
	private String C_ID;
	private String C_Name;
	private String Room_Type; 
	private String Hotel_Name;
	private String City;
	private String Street_Name;
	private String State;

    // DB connection properties
    private static String driver = "oracle.jdbc.driver.OracleDriver";
    private static String jdbc_url = "jdbc:oracle:thin:@apollo.vse.gmu.edu:1521:ite10g";
    private static String username;
    private static String password;

    private static Connection connection;
    // Default Constructor
    public Project() {}

    private static void getConnection() {
   		// register the JDBC driver
   		try {
    	  Class.forName(driver);
    	} catch (ClassNotFoundException e) {
    	  e.printStackTrace();
    	}
    
    	// Create a connection
    	
    	try {
    		
    	  connection  = DriverManager.getConnection (jdbc_url, username, password);
    	  System.out.println("Connection successful" + connection);
    	} catch (SQLException e) {
    	  e.printStackTrace();
    	}
    	//return connection;
  	} 

  // Method to check if a table exists
  // return true is the table exists, false otherwise
  private boolean doesTableExist(Connection connection, String table_name) throws SQLException {
    boolean TableExists = false;
    
    // check the meta data of the connection
    DatabaseMetaData dmd = connection.getMetaData();
    ResultSet rs = dmd.getTables(null, null, table_name, null);
    while (rs.next()){
      TableExists = true;
    }
    rs.close(); // close the result set
    return TableExists;    
  }


  // Call our sql file that will create all the table
  private static void uploadTables() throws SQLException
  {
    // create the SQL for the table
    StringBuffer sbCreate = new StringBuffer();
    sbCreate.append("@Phase2.sql");
    System.out.println("Tables uploaded");
    // create the table
    Statement statement = null;
    try {
      statement = connection.createStatement();
      statement.executeUpdate (sbCreate.toString());
    } catch (SQLException e) {
      throw e;
    } finally {
      statement.close();
    }
  }

  // Call our sql file that will insert tuples
  private void uploadTuples(Connection connection) throws SQLException
  {
    // create the SQL for the table
    StringBuffer sbCreate = new StringBuffer();
    sbCreate.append("@Phase2_Test_Data.sql");
    
    // create the table
    Statement statement = null;
    try {
      statement = connection.createStatement();
      statement.executeUpdate (sbCreate.toString());
    } catch (SQLException e) {
      throw e;
    } finally {
      statement.close();
    }
  }

  // Function for the user to insert data into the DB
  public void insertData() {

  }

  // Close the Connection
  public void close(Connection connection) throws SQLException {
    try
    {
      connection.close();
    } catch (SQLException e)
    {
      throw e;
    }
  }

  // Setter Methods
 


  // Print the menu for the user
  public static void menu() {
  	System.out.println("\n(1) View Content");
  	System.out.println("(2) Manipulate Records");
  	System.out.println("(3) Search Database");
  	System.out.println("(4) Quit");
  }

  // Main Function
  public static void main (String arg[]) throws SQLException, ClassNotFoundException {
  	Scanner scan = new Scanner(System.in);
  	System.out.println("Please Enter your credentials");
  	System.out.print("Username: ");
  	username = scan.nextLine();
  	System.out.print("Password: ");
  	password = scan.nextLine();
  	boolean Quit = true;
  	
  	while(Quit) {
  		menu();
  		System.out.print("Please select an option. (1, 2, 3 or 4)\n-> ");
  		String option;
  		option = scan.nextLine();
  		switch(option) {
  			case "1":
  				// View Tables
  				getConnection();
  				
  				break;
  			case "2":
  				// Manipulate records
  				break;
  			case "3":
  				// Search Database
  				break;
  			case "4":
  				// Quit Program
  				Quit = false;
  				break;
  			default:
  				System.out.println("Invalid input, please try again.");
  		} // End Switch
  	} // End Program Loop

  } // End Main

}