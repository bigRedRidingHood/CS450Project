
// JDBC libraries
// JDBC Packages
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Project {

	// Instance Variables

	// DB connection properties
	private static String driver = "oracle.jdbc.driver.OracleDriver";
	private static String jdbc_url = "jdbc:oracle:thin:@apollo.vse.gmu.edu:1521:ite10g";
	private static String username;
	private static String password;

	private static Connection connection;

	// Default Constructor
	public Project() {
	}

	public static void main(String arg[]) throws SQLException, ClassNotFoundException {
		Scanner scan = new Scanner(System.in);
		System.out.println("Please Enter your credentials");
		System.out.print("Username: ");
		username = scan.nextLine();
		System.out.print("Password: ");
		password = scan.nextLine();
		boolean Quit = true;
        getConnection();
		uploadTables();
		//uploadTuples();

		while (Quit) {
			menu();
			System.out.print("Please select an option. (1, 2, 3 or 4)\n-> ");
			String option;
			option = scan.nextLine();
			switch (option) {
			case "1":
				// View Tables

				getTables();
				break;
			case "2":
				// Manipulate records
				break;
			case "3":
				// Search Database
				break;
			case "4":
				// Quit Program

				System.out.println("Connection closed");
				Quit = false;
				break;
			default:
				System.out.println("Invalid input, please try again.");
			} // End Switch
		} // End Program Loop
		 close();
	} // End Main

	private static void getTables() {
		Scanner scan = new Scanner(System.in);
		int choice = 0;
		// Customer, Hotel, Room, Hotel_Room, Reservation, Room_Type, Price_Info
		System.out.println("Select the table you wish to view");
		do {
			System.out.println("Please choose from the following:");
			System.out.println(
					"\t(1) Customer\n\t(2) Hotel\n\t(3) Hotel Rooms\n\t(4) Reservations\n\t(5) Room Types\n\t(6) Pricing Information\n\t(7) Rooms");
			String in = scan.nextLine();
			choice = Integer.parseInt(in);
		} while (choice < 0 || choice > 7);

		switch (choice) {
		case 1:
			getTuples(1);	break;
		case 2:
			getTuples(2);	break;
		case 3:
			getTuples(3);	break;
		case 4:
			getTuples(4);	break;
		case 5:
			getTuples(5);	break;
		case 6:
			getTuples(6);	break;
		case 7:
			getTuples(7);	break;
		default:
			break;
		}
		System.out.println("Returning to main menu\n");
	}

//****** HEY MICHAEL COME HERE AND WORK ON THIS, DO RESERVATION & PRICE_INFO TABLES

	private static void getTuples(int tableNumber) /*throws SQLException */ {
//		System.out.println("tableNumber: " + tableNumber);

		Scanner scan = new Scanner(System.in);
		String table = "";
		switch (tableNumber) {
			case 1: table="CUSTOMER"; 	break;
			case 2: table="HOTEL";		break;
			case 3: table="HOTEL_ROOMS";	break;
			case 4: table="RESERVATIONS";break;
			case 5: table="ROOM_TYPES";	break;
			case 6: table="PRICING";		break;
			case 7: table="ROOMS";		break;
		}
		//Limit 5 will limit the number of tuples to show.

		String query="SELECT * FROM " + table + " WHERE ROWNUM < 6";

		System.out.println("\tDisplay full list?\n\t\t(1) Yes.\n\t\t(2) No.");
		int c = Integer.parseInt(scan.nextLine());
		if(c < 1 || c>2)
			do {
				System.out.println("Please select either Yes (1) or No (2)");
			}while(c!=1 || c!=2);

		
		if(c==1) 
			query = "SELECT * FROM " + table; 
		//TODO: Code to execute query
		 
		try {
			Statement st = connection.createStatement();
			if(tableNumber == 1) {
				ResultSet rs = st.executeQuery(query);
				while (rs.next()) {
					String CID = rs.getString("C_ID");
					String name = rs.getString("C_NAME");
					int age = rs.getInt("AGE");
					String gender = rs.getString("GENDER");
	
					System.out.printf("| %10s | %10s | %10d | %10s |\n", CID, name, age, gender);
				}
			}
			else if(tableNumber == 2) {
				ResultSet rs = st.executeQuery(query);
				while (rs.next()) {
					int b_ID = rs.getInt("Branch_ID");
					String hotelName = rs.getString("H_Name");
					String city = rs.getString("City");
					String Street_Name = rs.getString("Street_Name");
					int Street_Num = rs.getInt("Street_Num");
					String number = rs.getString("Phone_Number");

					System.out.printf("| %10s | %6d | %6d %15s %10s | %10s|\n", hotelName, b_ID, Street_Num, Street_Name, city, number);
				}
			}
		} catch (Exception e) {
			 System.err.println("Got an exception! ");
		     System.err.println(e.getMessage());
		 }
		
//		System.out.println("The selected query is:\n\t" + query);
		
	}

	// Make connection with DB
	private static void getConnection() {
		// register the JDBC driver
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// Create a connection

		try {

			connection = DriverManager.getConnection(jdbc_url, username, password);
			System.out.println("Connection successful");
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
		while (rs.next()) {
			TableExists = true;
		}
		rs.close(); // close the result set
		return TableExists;
	}

	// Call our sql file that will create all the table
	private static void uploadTables() throws SQLException {
		// create the SQL for the table

		//StringBuffer sbCreate = new StringBuffer();
		Statement statement = null;
		statement = connection.createStatement();

        try {
            File file = new File("src/Phase2.sql");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            System.out.println("Read from file " + file);
            while ((line = bufferedReader.readLine()) != null) {
				StringBuffer sbCreate = new StringBuffer();
                sbCreate.append(line.toString().replace(';',' '));
                //sbCreate.append("\n");
				statement.executeUpdate(sbCreate.toString());
            }
            fileReader.close();
        } catch (IOException e) {
            System.out.println("uploadTables Exception here.");
            e.printStackTrace();
        }

		// create the table
//		Statement statement = null;
//		try {
//			statement = connection.createStatement();
//			//System.out.println(sbCreate.toString());
//			statement.executeUpdate(sbCreate.toString());
//            System.out.println("Tables uploaded");
//		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			statement.close();
		}
	}

	// Call our sql file that will insert tuples
	private static void uploadTuples() throws SQLException {
		// create the SQL for the table
		StringBuffer sbCreate = new StringBuffer();
		sbCreate.append("@Phase2_Test_Data.sql");

		// create the table
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sbCreate.toString());
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
	public static void close() throws SQLException {
		try {
			connection.close();
			System.out.println("close connection");
		} catch (SQLException e) {
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

}