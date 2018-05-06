
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

	// DB connection properties
	private static String driver = "oracle.jdbc.driver.OracleDriver";
	private static String jdbc_url = "jdbc:oracle:thin:@apollo.vse.gmu.edu:1521:ite10g";
	private static String username;
	private static String password;
	private static int attSize = 0;
    private static String query = "";
    private static String SET = "";
    private static String WHERE = "";
    private static int SET_INT;
    private static int WHERE_INT;

    private static Connection connection;

	// Default Constructor
	public Project() {
	}

	// TODO: make password invisible when logging in if we have time
    // TODO: search database, ie. find reservations and other basic searches
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
				getTables(option);
				break;
			case "2":
				// Manipulate records
				getTables(option);
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

    // Select tables for viewing
    // FUNCTION COMPLETE
    // TODO: Implement option 3 for Searching DB
	private static void getTables(String prevOp) throws SQLException {
		if(prevOp.equals("1")) {
			Scanner scan = new Scanner(System.in);
			int choice = 0;
			System.out.println("Select the table you wish to view");
			do {
				System.out.println("Please choose from the following:");
				System.out.println(
						"\t(1) Customer\n\t(2) Hotel\n\t(3) Hotel Rooms\n\t(4) Reservations\n\t(5) Pricing Information\n\t(6) Rooms");
				System.out.print("-> ");
				String in = scan.nextLine();
				choice = Integer.parseInt(in);
			} while (choice < 0 || choice > 6);

			switch (choice) {
				case 1:
					getTuples(1);
					break;
				case 2:
					getTuples(2);
					break;
				case 3:
					getTuples(3);
					break;
				case 4:
					getTuples(4);
					break;
				case 5:
					getTuples(5);
					break;
				case 6:
					getTuples(6);
					break;
				default:
					break;
			}
			System.out.println("Returning to main menu\n");
		}

		else if(prevOp.equals("2")) {
			Scanner scan = new Scanner(System.in);
			int in = 0;
			do {
				manipMenu();
				System.out.println("\nPlease select an option. (1, 2 or 3)");
				System.out.print("-> ");
				String input = scan.nextLine();
				in = Integer.parseInt(input);
			} while (in < 0 || in > 4);

			switch (in) {
				case 1:
					manipData(1); // Add Tuples
					break;
				case 2:
					manipData(2); // Delete Tuple
					break;
				case 3:
					manipData(3); // Update Tuple
					break;
				default:
					break;
			}
		}
	} // End getTables

	// Function for the user to manipulate data in the DB
    // FUNCTION COMPLETE
	public static void manipData(int tableNumber) throws SQLException {
		Scanner scan = new Scanner(System.in);
		String table;
		String att;
		int tmp;
        int choice;

		// Insert a tuple
		if(tableNumber == 1) {
            // Loop as long as user inputs incorrect option for selecting table
            do {
                System.out.println("Please select a table to insert into");
                System.out.println(
                        "\t(1) Customer\n\t(2) Hotel\n\t(3) Hotel_Rooms\n\t(4) Reservation\n\t(5) Price_Info\n\t(6) Room\n\t(7) Date_List");
                System.out.print("-> ");
                table = scan.nextLine();
                tmp = Integer.parseInt(table);
                table = setTQ(tmp);
            }while(tmp <= 0 || tmp > 7);

            insertInto(tmp);
            // Execute query
            Statement statement = null;
            statement = connection.createStatement();
            try {
                StringBuffer sbCreate = new StringBuffer();
                sbCreate.append("Insert into " + table + " Values(" + query + ")");
                statement.executeUpdate(sbCreate.toString());
                System.out.println("Tuple Added");
            }catch (SQLException e) {
                throw e;
            }
            finally {
                statement.close();
            }
		} // End Insert

		// Remove Tuple
		else if(tableNumber == 2) {
            // Loop as long as user inputs incorrect option for selecting table
            do {
                System.out.println("Please select a table to delete from.");
                System.out.println(
                        "\t(1) Customer\n\t(2) Hotel\n\t(3) Hotel_Rooms\n\t(4) Reservation\n\t(5) Price_Info\n\t(6) Room\n\t(7) Date_List");
                System.out.print("-> ");
                table = scan.nextLine();
                tmp = Integer.parseInt(table);
                table = setTQ(tmp);
            }while(tmp <= 0 || tmp > 7);

		    // Loop as long as user inputs incorrect option for selection attribute
		    do {
                System.out.println("Delete " + table + " by what attribute?");
                viewAttributes(tmp);
                System.out.print("\n-> ");
                att = scan.nextLine();
                choice = Integer.parseInt(att);
                att = setTuQ(tmp, choice);
            }while(choice <= 0 || choice > attSize);

		    // Get user input for what they want deleted
			System.out.print("Delete " + att + " that equals?\n-> ");
			String info = scan.nextLine();

			// Execute query
			Statement statement = null;
			statement = connection.createStatement();
			try {
				StringBuffer sbCreate2 = new StringBuffer();
				sbCreate2.append("Delete from " + table + " Where " + att + " = '" + info + "'");
				statement.executeUpdate(sbCreate2.toString());
			}catch (SQLException e) {
				throw e;
			}
			finally {
				statement.close();
			}

		} // End Remove

		// Update a tuple
		else if(tableNumber == 3) {
            do {
                System.out.println("Please select a table to update");
                System.out.println(
                        "\t(1) Customer\n\t(2) Hotel\n\t(3) Reservation\n\t(4) Price_Info\n\t(5) Room");
                System.out.print("-> ");
                table = scan.nextLine();
                tmp = Integer.parseInt(table);
                switch(tmp) {
                    case 1:
                        table = "Customer";
                    case 2:
                        table = "Hotel";
                    case 3:
                        table = "Reservation";
                    case 4:
                        table = "Price_Info";
                    case 5:
                        table = "Room";
                }
            }while(tmp <= 0 || tmp > 5);

            updateInto(tmp);
            // Execute query
            Statement statement = null;
            statement = connection.createStatement();
            try {
                StringBuffer sbCreate = new StringBuffer();
                sbCreate.append("UPDATE " + table + " SET " + query + " WHERE " + WHERE);
                statement.executeUpdate(sbCreate.toString());
            }catch (SQLException e) {
                throw e;
            }
            finally {
                statement.close();
            }

		} // End Update

	} // End manipData

    // Function used to obtain information for updating tuples
    // TODO: Add ability to update remaining tables
    private static void updateInto(int table) {
        String C_ID = "";
        String C_Name = "";
        int Age = 0;
        String Gender = "";
        int GC = 0;
        String Res_Num = "";
        int Party_Size = 0;
        int Total = 0;
        int day_in = 0;
        int month_in = 0;
        int year_in = 0;
        int day_out = 0;
        int month_out = 0;
        int year_out = 0;
        String H_Name = "";
        int Branch_ID = 0;
        String R_Type = "";
        String City = "";
        String Street_Name = "";
        int Street_Num = 0;
        String State = "";
        String Phone_Number = "";
        Scanner scan = new Scanner(System.in);
        int input = 0;

        switch (table) {
            //TODO: FIX THIS
            case 1: // Customer
                System.out.printf("\t(1) Age\n");
                System.out.printf("\t(2) Gender\n");
                System.out.printf("\t(3) Name\n");
                System.out.println("Update which Customer attribute?");
                System.out.print("-> ");
                input = scan.nextInt();
                switch(input) {
                    case 1:
                        System.out.print("Set new Age: ");
                        Age = scan.nextInt();
                        SET_INT = Age;
                        System.out.println("For what customer? (Enter Customer_ID)");
                        System.out.print("-> ");
                        C_ID = scan.nextLine();
                        C_ID = scan.nextLine();
                        WHERE = "C_ID = '" + C_ID + "' ";
                        query = "Age = " + SET_INT;
                        break;
                    case 2:
                        System.out.println("Set new Gender: ");
                        System.out.print("-> ");
                        Gender = scan.nextLine();
                        Gender = scan.nextLine();
                        SET = Gender;
                        System.out.println("For what customer? (Enter Customer_ID)");
                        System.out.print("-> ");
                        C_ID = scan.nextLine();
                        WHERE = "C_ID = '" + C_ID + "' ";
                        query = "Gender = '" + SET + "' ";
                        break;
                    case 3:
                        System.out.println("Set new Name: ");
                        System.out.print("-> ");
                        C_Name = scan.nextLine();
                        C_Name = scan.nextLine();
                        SET = C_Name;
                        System.out.println("For what customer? (Enter Customer_ID)");
                        System.out.print("-> ");
                        C_ID = scan.nextLine();
                        WHERE = "C_ID = '" + C_ID + "'";
                        query = "C_Name = '" + SET + "' ";
                        break;
                    default:
                        System.out.println("Invalid choice, please select again.");
                        System.out.printf("\t(1) Age\n");
                        System.out.printf("\t(2) Gender\n");
                        System.out.printf("\t(3) Name\n");
                        System.out.println("Update which Customer attribute?");
                        System.out.print("-> ");
                        input = scan.nextInt();
                }
                break;
            case 2: // Hotel
                System.out.printf("\t(1) Hotel Address\n");
                System.out.printf("\t(2) Phone Number\n");
                System.out.println("Update which Hotel attribute?");
                System.out.print("-> ");
                input = scan.nextInt();
                switch(input) {
                    case 1:
                        System.out.print("Set new City : ");
                        City = scan.nextLine();
                        City = scan.nextLine();
                        System.out.print("Set new Street Name : ");
                        Street_Name = scan.nextLine();
                        System.out.print("Set new Street Number : ");
                        Street_Num = scan.nextInt();
                        System.out.print("Set new State (Acronym): ");
                        State = scan.nextLine();
                        State = scan.nextLine();
                        System.out.println("For what Hotel?");
                        System.out.print("-> ");
                        H_Name = scan.nextLine();
                        System.out.println("Which Branch ID?");
                        System.out.print("-> ");
                        Branch_ID = scan.nextInt();
                        WHERE = "Branch_ID = " + Branch_ID;
                        query = "City = '" + City + "', Street_Name = '" + Street_Name +"', Street_Num = " + Street_Num + ", State = '" + State + "'";
                        break;
                    case 2:
                        System.out.print("Set new Phone Number : ");
                        Phone_Number = scan.nextLine();
                        Phone_Number = scan.nextLine();
                        SET = Phone_Number;
                        System.out.println("For what Hotel?");
                        System.out.print("-> ");
                        H_Name = scan.nextLine();
                        System.out.println("Which Branch ID?");
                        System.out.print("-> ");
                        Branch_ID = scan.nextInt();
                        WHERE = "Branch_ID = " + Branch_ID;
                        query = "Phone_Number = '" + SET + "' ";
                        break;
                    default:
                        System.out.printf("\t(1) Hotel Address\n");
                        System.out.printf("\t(2) Phone Number\n");
                        System.out.println("Update which Hotel attribute?");
                        System.out.print("-> ");
                        input = scan.nextInt();
                }
                break;
                //TODO: CANT UPDATE RESERVATION
            case 3: // Reservation
                System.out.print("\nPlease enter your Reservation ID: ");
                Res_Num = scan.nextLine();
                WHERE = "Res_Num = '"+Res_Num+"'";
                System.out.printf("\t(1) Check in/out dates\n");
                System.out.printf("\t(2) Hotel\n");
                System.out.printf("\t(3) Room Type\n");
                System.out.println("Update Reservation by which attribute?");
                System.out.print("-> ");
                input = scan.nextInt();
                switch(input) {
                    case 1:
                        System.out.print("Set new Check in date: ");
                        System.out.print("\nMonth (1-12) -> ");
                        month_in = scan.nextInt();
                        System.out.print("\nDay (1-31) -> ");
                        day_in = scan.nextInt();
                        System.out.print("\n\nSet new Check out date: ");
                        System.out.print("\nMonth (1-12) -> ");
                        month_out = scan.nextInt();
                        System.out.print("\nDay (1-31) -> ");
                        day_out = scan.nextInt();
                        query = "month_in = "+month_in+", day_in = "+day_in+", month_out = "+month_out+", day_out = "+day_out;
                        break;
                    case 2:
                        System.out.print("Set new Hotel: ");
                        H_Name = scan.nextLine();
                        H_Name = scan.nextLine();
                        System.out.print("Set Hotel Branch Number: ");
                        Branch_ID = scan.nextInt();
                        query = "H_Name = '"+H_Name+"', Branch_ID = " + Branch_ID;
                        break;
                    case 3:
                        System.out.print("Set new Room Type: ");
                        R_Type = scan.nextLine();
                        R_Type = scan.nextLine();
                        query = "R_Type = '"+R_Type+"'";
                        break;
                    default:
                        System.out.println("Invalid choice, please select again.");
                        System.out.printf("\t(1) Check in/out dates\n");
                        System.out.printf("\t(2) Hotel\n");
                        System.out.printf("\t(3) Room Type\n");
                        System.out.println("Update Reservation by which attribute?");
                        System.out.print("-> ");
                        input = scan.nextInt();
                }
                break;
            //TODO: CANT UPDATE PRICE_INFO
            case 4: // Price_Info
                System.out.println("Set new room price: ");
                System.out.print("-> ");
                SET_INT = scan.nextInt();
                System.out.println("For what room type?");
                System.out.print("-> ");
                R_Type = scan.nextLine();
                R_Type = scan.nextLine();
                WHERE = "R_Type = '" + R_Type + "'";
                query = "Price = " + SET_INT;
                break;
            case 5: // Room
                System.out.println("Set new room capacity: ");
                System.out.print("-> ");
                SET_INT = scan.nextInt();
                System.out.println("For what room type?");
                System.out.print("-> ");
                R_Type = scan.nextLine();
                R_Type = scan.nextLine();
                WHERE = "R_Type = '" + R_Type + "'";
                query = "Capacity = " + SET_INT;
                break;

        } // end Switch
    } // end updateInto

    // Function used to assist in inserting tuples into tables
    // TODO: Fix inserting into reservation
    private static void insertInto(int table) throws SQLException {
	    String C_ID = "";
	    String C_Name = "";
	    int Age = 0;
	    String Gender = "";
	    int GC = 0;
        String Res_Num = "";
        int Party_Size = 0;
        int Total = 0;
        int day_in = 0;
        int month_in = 0;
        int year_in = 0;
        int day_out = 0;
        int month_out = 0;
        int year_out = 0;
        String H_Name = "";
        int Branch_ID = 0;
        String R_Type = "";
        String City = "";
        String Street_Name = "";
        int Street_Num = 0;
        String Phone_Number = "";
        String State = "";
        int Capacity = 0;
        int Price = 0;
        Scanner scan = new Scanner(System.in);

	    switch(table) {
            case 1: // Customer
                viewAttributes(1);
                while(C_ID.length() != 5) {
                    System.out.println("Please enter Customer ID. (Length must be 5 characters)");
                    System.out.print("Customer ID -> ");
                    C_ID = scan.nextLine();
                }
                while((C_Name.length() < 1) || (C_Name.length() > 20)) {
                    System.out.println("Please enter Customer Name. (Limit 20 Characters)");
                    System.out.print("Customer Name -> ");
                    C_Name = scan.nextLine();
                }
                while(Age < 18 || Age > 100) {
                    System.out.println("Please enter Customer Age.");
                    System.out.print("Customer Age -> ");
                    Age = scan.nextInt();
                }
                while(GC < 1 || GC > 2) {
                    System.out.println("Please enter Customer Gender. (1 for Male, 2 for Female)");
                    System.out.print("Customer Gender -> ");
                    GC = scan.nextInt();
                    if(GC == 1)
                        Gender = "Male";
                    else if(GC == 2)
                        Gender = "Female";
                }
                query = "'" + C_ID + "', '" + C_Name + "', " + Age + ", '" + Gender + "'";
                break;
            case 2: // Hotel
                System.out.print("Hotel Name: ");
                H_Name = scan.nextLine();
                System.out.print("Hotel Branch Number: ");
                Branch_ID = scan.nextInt();
                System.out.print("City: ");
                City = scan.nextLine();
                City = scan.nextLine();
                System.out.print("Street Name: ");
                Street_Name = scan.nextLine();
                System.out.print("Street Number: ");
                Street_Num = scan.nextInt();
                System.out.print("State: ");
                State = scan.nextLine();
                State = scan.nextLine();
                System.out.print("Phone Number: ");
                Phone_Number = scan.nextLine();
                query = Branch_ID+", '"+H_Name+"', '"+City+"', '"+Street_Name+"', "+Street_Num+", '"+State+"', '"+Phone_Number+"'";
                break;
            case 3: // Hotel_Room
                System.out.print("Hotel Name: ");
                H_Name = scan.nextLine();
                System.out.print("Hotel Branch Number: ");
                Branch_ID = scan.nextInt();
                System.out.print("Room Type: ");
                R_Type = scan.nextLine();
                R_Type = scan.nextLine();
                System.out.print("Number of rooms: ");
                Capacity = scan.nextInt();
                query = Branch_ID+", '"+H_Name+"', '"+R_Type+"', "+Capacity;
                break;
            case 4: // Reservation
                viewAttributes(4);
                while(Res_Num.length() != 7) {
                    System.out.println("\nPlease enter Reservation Number. (Length must be 7 characters)");
                    System.out.print("Reservation Number -> ");
                    Res_Num = scan.nextLine();
                }
                while(C_ID.length() != 5) {
                    System.out.println("\nPlease enter Customer ID. (Length must be 5 characters)");
                    System.out.print("Customer ID -> ");
                    C_ID = scan.nextLine();
                }
                while(Party_Size < 1 || Party_Size > 6) {
                    System.out.println("\nPlease enter number in your party.");
                    System.out.print("Party Size -> ");
                    Party_Size = scan.nextInt();
                }
                while(day_in < 1 || day_in > 31 || month_in < 1 || month_in > 12 || year_in != 2018) {
                    System.out.print("\nCheck in month (number) -> ");
                    month_in = scan.nextInt();
                    System.out.println(month_in + "/day/" + "year");
                    System.out.println("\nWhat is your check in date?");
                    System.out.print("Check in day -> ");
                    day_in = scan.nextInt();
                    System.out.println(month_in + "/" + day_in + "/year");
                    System.out.print("\nCheck in year (current year) -> ");
                    year_in = scan.nextInt();
                    System.out.println("\nYour Check in date: " + month_in + "/" + day_in + "/" + year_in + "\n");
                }
                while(day_out < 1 || day_out > 31 || month_out < 1 || month_out > 12 || year_out != 2018) {
                    System.out.println("What is your check out date?");
                    System.out.print("\nCheck out month (number) -> ");
                    month_out = scan.nextInt();
                    System.out.println(month_out + "/day/" + "year");
                    System.out.print("\nCheck out day -> ");
                    day_out = scan.nextInt();
                    System.out.println(month_out + "/" + day_out + "/year");
                    System.out.print("\nCheck out year (current year) -> ");
                    year_out = scan.nextInt();
                    System.out.println("\nYour Check out date: " + month_out + "/" + day_out + "/" + year_out);
                }
                while(!H_Name.equals("Hilton") && !H_Name.equals("Hyatt") && !H_Name.equals("HansEtFonz")) {
                    System.out.println("\nSelect your hotel. (Hilton, Hyatt, HansEtFonz)");
                    System.out.print("Hotel -> ");
                    String tmp = scan.nextLine();
                    H_Name = scan.nextLine();
                }
                if(H_Name.equals("Hilton")) {
                    while(Branch_ID != 1000 && Branch_ID != 1001 && Branch_ID != 1002) {
                        System.out.println("\nSelect your Branch. (1000, 1001, 1002)");
                        System.out.print("Branch ID -> ");
                        Branch_ID = scan.nextInt();
                    }
                }
                else if(H_Name.equals("Hyatt")) {
                    while(Branch_ID != 1003 && Branch_ID != 1004 && Branch_ID != 1005) {
                        System.out.println("\nSelect your Branch. (1003, 1004, 1005)");
                        System.out.print("Branch ID -> ");
                        Branch_ID = scan.nextInt();
                    }
                }
                else if(H_Name.equals("HansEtFonz")) {
                    while(Branch_ID != 1006 && Branch_ID != 1007 && Branch_ID != 1008) {
                        System.out.println("\nSelect your Branch. (1006, 1007, 1008)");
                        System.out.print("Branch ID -> ");
                        Branch_ID = scan.nextInt();
                    }
                }
                while(!R_Type.equals("King") && !R_Type.equals("Suite") && !R_Type.equals("Queen") && !R_Type.equals("Twin")) {
                    System.out.println("\nSelect your Room Type. (King, Suite, Queen, Twin)");
                    System.out.print("Room Type -> ");
                    String tmp = scan.nextLine();
                    R_Type = scan.nextLine();
                }
                Total = 0;
                query = "'" + Res_Num + "', '" + C_ID + "', " + Party_Size + ", " + Total + ", "+ month_in + ", " + day_in + ", " + year_in + ", " + month_out + ", " + day_out + ", " + year_out + ", '" + H_Name + "', " + Branch_ID + ", '" + R_Type + "'";
                break;
            case 5: // Pricing
                System.out.print("Hotel Name: ");
                H_Name = scan.nextLine();
                System.out.print("Hotel Branch Number: ");
                Branch_ID = scan.nextInt();
                System.out.print("Room Type: ");
                R_Type = scan.nextLine();
                R_Type = scan.nextLine();
                System.out.print("Month (1-12): ");
                month_in = scan.nextInt();
                System.out.print("Day (1-31): ");
                day_in = scan.nextInt();
                System.out.print("Year: ");
                year_in = scan.nextInt();
                System.out.print("Price: ");
                Price = scan.nextInt();
                System.out.print("Number of rooms: ");
                Capacity = scan.nextInt();
                query = "'"+H_Name+"', "+Branch_ID+", '"+R_Type+"', "+month_in+", "+day_in+", "+year_in+", "+Price+", "+Capacity;
                break;
            case 6: // Room
                System.out.print("Room Type: ");
                R_Type = scan.nextLine();
                System.out.print("Room Capacity: ");
                Capacity = scan.nextInt();
                query = "'"+R_Type+"', "+Capacity;
                break;

            case 7: // Dates
                System.out.print("Month (1-12): ");
                month_in = scan.nextInt();
                System.out.print("Day (1-31): ");
                day_in = scan.nextInt();
                System.out.print("Year: ");
                year_in = scan.nextInt();
                query = month_in+", "+day_in+", "+year_in;
                break;
        }
    } // End insertInto

    // Function used to to assist with delete query
    // FUNCTION COMPLETE
    private static String setTQ(int tbl) {
        switch(tbl) {
            case 1:
                return "Customer";
            case 2:
                return "Hotel";
            case 3:
                return "Hotel_Room";
            case 4:
                return "Reservation";
            case 5:
                return "Price_Info";
            case 6:
                return "Room";
            case 7:
                return "Date_List";
        }
        return "error";
    } // End setTQ

    // Function used to to assist with delete query
    // FUNCTION COMPLETE
    private static String setTuQ(int tbl, int atr) {
        switch(tbl) {
            case 1:
                switch(atr) {
                    case 1:
                        return "C_ID";
                    case 2:
                        return "C_Name";
                    case 3:
                        return "Age";
                    case 4:
                        return "Gender";
                }
                break;
            case 2:
                switch(atr) {
                    case 1:
                        return "Branch_ID";
                    case 2:
                        return "H_Name";
                    case 3:
                        return "City";
                    case 4:
                        return "Street_Name";
                    case 5:
                        return "Street_Num";
                    case 6:
                        return "State";
                    case 7:
                        return "Phone_Number";
                }
                break;
            case 3:
                switch(atr) {
                    case 1:
                        return "Branch_ID";
                    case 2:
                        return "H_Name";
                    case 3:
                        return "R_Type";
                    case 4:
                        return "Quantity";
                }
                break;
            case 4:
                switch(atr) {
                    case 1:
                        return "Res_Num";
                    case 2:
                        return "C_ID";
                    case 3:
                        return "Party_Size";
                    case 4:
                        return "Total";
                    case 5:
                        return "day_in";
                    case 6:
                        return "month_in";
                    case 7:
                        return "year_in";
                    case 8:
                        return "day_out";
                    case 9:
                        return "month_out";
                    case 10:
                        return "year_out";
                    case 11:
                        return "H_Name";
                    case 12:
                        return "Branch_ID";
                    case 13:
                        return "R_Type";
                }
                break;
            case 5:
                switch(atr) {
                    case 1:
                        return "H_Name";
                    case 2:
                        return "Branch_ID";
                    case 3:
                        return "R_Type";
                    case 4:
                        return "Date_day";
                    case 5:
                        return "Date_month";
                    case 6:
                        return "Date_year";
                    case 7:
                        return "Price";
                    case 8:
                        return "Num_Avail";
                }
                break;
            case 6:
                switch(atr) {
                    case 1:
                        return "R_Type";
                    case 2:
                        return "Capacity";
                }
                break;
            case 7:
                switch(atr) {
                    case 1:
                        return "Date_day";
                    case 2:
                        return "Date_month";
                    case 3:
                        return "Date_year";
                }
                break;
        }
        return "error";
    } // End setTuQ

    // View the Attributes of the corresponding table
    // FUNCTION COMPLETE
    private static void viewAttributes(int Entity) {
	    switch(Entity) {
            case 1:
                System.out.println("\n\t(1) C_ID\n\t(2) C_Name\n\t(3) Age\n\t(4) Gender");
                attSize = 4;
                break;
            case 2:
                System.out.println("\n\t(1) Branch_ID\n\t(2) H_Name\n\t(3) City\n\t(4) Street_Name\n\t(5) Street_Num\n\t(6) State\n\t(7) Phone_Number");
                attSize = 7;
                break;
            case 3:
                System.out.println("\n\t(1) Branch_ID\n\t(2) H_Name\n\t(3) R_Type\n\t(4) Quantity");
                attSize = 4;
                break;
            case 4:
                System.out.println("\n\t(1) Res_Num\n\t(2) C_ID\n\t(3) Party_Size\n\t(4) Total\n\t(5) day_in\n\t(6) month_in\n\t(7) year_in");
                System.out.println("\t(8) day_out\n\t(9) month_out\n\t(10) year_out\n\t(11) H_Name\n\t(12) Branch_ID\n\t(13) R_Type");
                attSize = 13;
                break;
            case 5:
                System.out.println("\n\t(1) H_Name\n\t(2) Branch_ID\n\t(3) R_Type\n\t(4) Date_day\n\t(5) Date_month\n\t(6) Date_year\n\t(7) Price\n\t(8) Num_Avail");
                attSize = 8;
                break;
            case 6:
                System.out.println("\n\t(1) R_Type\n\t(2) Capacity");
                attSize = 2;
                break;
            case 7:
                System.out.println("\n\t(1) Date_day\n\t(2) Date_month\n\t(3) Date_year");
                attSize = 3;
                break;
        }
    } // End viewAttributes

	// View table tuples
    // FUNCTION COMPLETE
	private static void getTuples(int tableNumber)  {

		Scanner scan = new Scanner(System.in);
		String table = "";
		switch (tableNumber) {
			case 1: table="CUSTOMER"; 	break;
			case 2: table="HOTEL";		break;
			case 3: table="HOTEL_ROOM";	break;
			case 4: table="RESERVATION";break;
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

                    System.out.printf("|%10s | %10d | %d/%d/%d | %10s | $%10d | %10d|\n", h_name, b_id, day, month, year, room_t, price, num_avail);

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
	} // End getTuples

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
				statement.executeUpdate(sbCreate.toString());
            }
            fileReader.close();
        } catch (IOException e) {
            System.out.println("uploadTables Exception here.");
            e.printStackTrace();
        }

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
				statement.executeUpdate(sbCreate.toString());
			}
			fileReader.close();
		} catch (IOException e) {
			System.out.println("uploadTables Exception here.");
			e.printStackTrace();
		}
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

	// Print the main menu for the user
	// FUNCTION COMPLETE
	public static void menu() {
		System.out.println("\n(1) View Content");
		System.out.println("(2) Manipulate Records");
		System.out.println("(3) Search Database");
		System.out.println("(4) Quit");
	}

	// Print the menu for manipulating tables
	// FUNCTION COMPLETE
	public static void manipMenu() {
		System.out.println("\n(1) Add Tuple");
		System.out.println("(2) Remove Tuple");
		System.out.println("(3) Update Tuple");
	}

} // End Project