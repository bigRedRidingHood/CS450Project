
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
		uploadTuples();

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
					"\t(1) Customer\n\t(2) Hotel\n\t(3) Hotel Rooms\n\t(4) Reservations\n\t(5) Pricing Information\n\t(6) Rooms");
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
		//case 7:
		//	getTuples(7);	break;
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
			case 3: table="HOTEL_ROOM";	break;
			case 4: table="RESERVATION";break;
			//case 5: table="ROOM_TYPE";	break;
			case 5: table="PRICE_INFO";	break;
			case 6: table="ROOM";		break;
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
			System.out.println(table + " Table:");
			if(tableNumber == 1) {
				System.out.printf(" %s  %14s  %8s  %14s \n", "CID", "C_Name", "Age", "Gender");
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
				System.out.printf(" %s %6s %11s %40s\n", "Hotel_Name", "B_ID", "Address", "Phone_Number");
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
			else if(tableNumber == 3) {
				System.out.printf(" %s %13s %11s %11s\n", "Branch_ID", "Hotel_Name", "Room_Type", "Quantity");
				ResultSet rs = st.executeQuery(query);
				while (rs.next()) {
					int b_ID = rs.getInt("Branch_ID");
					String hotelName = rs.getString("H_Name");
					String room_t = rs.getString("R_Type");
					int quantity = rs.getInt("Quantity");

					System.out.printf("| %10s | %10d | %10s | %8d|\n", hotelName, b_ID, room_t, quantity);
				}
			}
			else if(tableNumber == 4) {
				System.out.printf(" %s %9s %18s %11s %9s %16s %10s %20s %15s\n", "Res_Num", "C_ID", "Hotel_Name", "Branch_id", "R_Type", "Party_Size", "Check_In", "Check_Out", "Total");
				ResultSet rs = st.executeQuery(query);
				while (rs.next()) {
                    String res_num = rs.getString("Res_Num");
                    String c_id = rs.getString("C_ID");
                    int party_size = rs.getInt("Party_Size");
                    int total = rs.getInt("Total");

                    int day_in = rs.getInt("day_in");
                    int month_in = rs.getInt("month_in");
                    int year_in = rs.getInt("year_in");

                    int day_out = rs.getInt("day_out");
                    int month_out = rs.getInt("month_out");
                    int year_out = rs.getInt("year_out");

                    String hotel_name = rs.getString("H_Name");
                    int b_id = rs.getInt("Branch_ID");
                    String room_t = rs.getString("R_Type");

                    System.out.printf("| %10s | %10s | %10s | %10d | %10s | %10d | %10d/%d/%d | %10d/%d/%d | $%10d |\n", res_num, c_id, hotel_name, b_id, room_t, party_size, month_in, day_in, year_in, month_out, day_out, year_out, total);
				}
			}
			else if(tableNumber == 5) {
			    System.out.printf(" %s %10s %7s %15s %8s %16s\n", "Hotel_Name", "Branch_ID", "Date", "Room_Type", "Price", "Num_Avail");
				ResultSet rs = st.executeQuery(query);
				while (rs.next()) {
                    String h_name = rs.getString("H_Name");
                    int b_id = rs.getInt("Branch_ID");
                    String room_t = rs.getString("R_Type");
                    int day = rs.getInt("Date_Month");
                    int month = rs.getInt("Date_Day");
                    int year = rs.getInt("Date_Year");
                    int price = rs.getInt("Price");
                    int num_avail = rs.getInt("Num_Avail");

                    System.out.printf("|%10s | %10d | %d/%d/%d | %10s | $%10d | %10d|\n", h_name, b_id, month, day, year, room_t, price, num_avail);

                }
			}
			else if(tableNumber == 6) {
                System.out.printf(" %s %16s\n", "Room_Type", "Room_Capacity");
				ResultSet rs = st.executeQuery(query);
				while (rs.next()) {
                    String room_t = rs.getString("R_Type");
                    int capacity = rs.getInt("Capacity");

                    System.out.printf("| %10s | %10d |\n", room_t, capacity);
				}
			}

		} catch (Exception e) {
			 System.err.println("Got an exception! ");
		     System.err.println(e.getMessage());
		 }
		
//		System.out.println("The selected query is:\n\t" + query);
		
	}

	// Make connection with DB
	// FUNCTION COMPLETE
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
	// FUNCTION COMPLETE
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
	// FUNCTION COMPLETE
	private static void uploadTuples() throws SQLException {
		// create the SQL for the table
		Statement statement = null;
		statement = connection.createStatement();

		try {
			File file = new File("src/Phase2_Test_Data.sql");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			System.out.println("Load from file " + file);
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
	}

	// Function for the user to insert data into the DB
	//TODO: Implement this function for user to insert data
	public void insertData() {

	}

	// Close the Connection
	// FUNCTION COMPLETE
	public static void close() throws SQLException {
		try {
			connection.close();
			System.out.println("close connection");
		} catch (SQLException e) {
			throw e;
		}
	}

	// Print the menu for the user
	// FUNCTION COMPLETE
	public static void menu() {
		System.out.println("\n(1) View Content");
		System.out.println("(2) Manipulate Records");
		System.out.println("(3) Search Database");
		System.out.println("(4) Quit");
	}

}