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
  public void setB_ID(){
  	this.B_ID = B_ID;
  }
  public void setStreet_Num(){
  	this.Street_Num = Street_Num;
  }
  public void setPhone_Num(){
  	this.Phone_Num = Phone_Num;
  }
  public void setCapacity(){
  	this.Capacity = Capacity;
  }
  public void setPrice(){
  	this.Price = Price;
  }
  public void setAge(){
  	this.Age = Age;
  }
  public void setDate_Day(){
  	this.Date_Day = Date_Day;
  }
  public void setDate_Month(){
  	this.Date_Month = Date_Month;
  }
  public void setDate_Year(){
  	this.Date_Year = Date_Year;
  }
  public void setReservation_Num(){
  	this.Reservation_Num = Reservation_Num;
  }
  public void setParty_Size(){
  	this.Party_Size = Party_Size;
  }
  public void setQuantity(){
  	this.Quantity = Quantity;
  } 
  public void setGender(){
  	this.Gender = Gender;
  }
  public void setC_ID(){
  	this.C_ID = C_ID;
  }
  public void setC_Name(){
  	this.C_Name = C_Name;
  }
  public void setRoom_Type(){
  	this.Room_Type = Room_Type;
  } 
  public void setHotel_Name(){
  	this.Hotel_Name = Hotel_Name;
  }
  public void setCity(){
  	this.City = City;
  }
  public void setStreet_Name(){
  	this.Street_Name = Street_Name;
  }
  public void setState(){
  	this.State = State;
  }

  // Getter Methods
  public int getB_ID(){
  	return B_ID;
  }
  public int getStreet_Num(){
  	return Street_Num;
  }
  public int getPhone_Num(){
  	return Phone_Num;
  }
  public int getCapacity(){
  	return Capacity;
  }
  public int getPrice(){
  	return Price;
  }
  public int getAge(){
  	return Age;
  }
  public int getDate_Day(){
  	return Date_Day;
  }
  public int getDate_Month(){
  	return Date_Month;
  }
  public int getDate_Year(){
  	return Date_Year;
  }
  public int getReservation_Num(){
  	return Reservation_Num;
  }
  public int getParty_Size(){
  	return Party_Size;
  }
  public int getQuantity(){
  	return Quantity;
  }
  public String getGender(){
  	return Gender;
  }
  public String getC_ID(){
  	return C_ID;
  }
  public String getC_Name(){
  	return C_Name;
  }
  public String getRoom_Type(){
  	return Room_Type;
  } 
  public String getHotel_Name(){
  	return Hotel_Name;
  }
  public String getCity(){
  	return City;
  }
  public String getStreet_Name(){
  	return Street_Name;
  }
  public String getState(){
  	return State;
  }


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