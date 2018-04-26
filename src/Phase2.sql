-- Students: Michael Villalobos & Hanish Moola
-- Project Phase 2
-- Instructor: Dr. Jessica Lin

drop table Customer cascade constraints;
drop table Hotel cascade constraints;
drop table Room cascade constraints;
drop table Hotel_Room cascade constraints;
drop table Reservation cascade constraints;
drop table Date_List cascade constraints;
drop table Price_Info cascade constraints;

create table Customer(
	C_ID varchar2(5), 
	C_Name varchar2(15), 
	Age integer, 
	Gender varchar2(7), 
	Primary key(C_ID)
	);

create table Hotel(
	Branch_ID integer, 
	H_Name varchar2(20), 
	City varchar2(15), 
	Street_Name varchar2(15), 
	Street_Num integer, 
	State varchar2(2), 
	Phone_Number integer, 
	Primary key(H_Name, Branch_ID)
	);

create table Room(
	R_Type varchar2(10), 
	Capacity integer,
	Primary key(R_Type)
	--Unique (Price),
	--Unique (Capacity)
	);

create table Hotel_Room(
	Branch_ID integer, 
	H_Name varchar2(20), 
	R_Type varchar2(10),
	Quantity integer,
	Primary Key (Branch_ID, H_Name, R_Type),
	Foreign Key (Branch_ID, H_Name) references Hotel(Branch_ID, H_Name),
	Foreign Key (R_Type) references Room(R_Type)
	);

create table Reservation(
	Res_Num varchar2(7),
	C_ID varchar2(5),
	Party_Size integer, 
	Total integer,
	day_in integer,
	month_in integer,
	year_in integer,
	day_out integer,
	month_out integer,
	year_out integer,
	H_Name varchar2(20),
	Branch_ID integer,
	R_Type varchar2(10), 
	Primary key (Res_Num),
	Foreign key (C_ID) references Customer(C_ID),
	Foreign key (day_in, month_in, year_in) references Date_List(Date_day, Date_month, Date_year),
	Foreign key (day_out, month_out, year_out) references Date_List(Date_day, Date_month, Date_year),
	Foreign key (H_Name, Branch_ID) references Hotel(H_Name, Branch_ID),
	Foreign key (R_Type) references Room(R_Type),
    --Unique (Party_Size),	
	Foreign key (Price) references Price_Info(Price)
	);

create table Date_List(
	Date_day integer,
	Date_month integer,
	Date_year integer,
	Primary key(Date_day, Date_month, Date_year)
	);

create table Price_Info(
	H_Name varchar2(20),
	Branch_ID integer,
	R_Type varchar2(10),
	Date_day integer,
	Date_month integer,
	Date_year integer,
	Price integer,
	Num_Avail integer,
	Primary key (H_Name, Branch_ID, R_Type, Date_day, Date_month, Date_year),
	Foreign key (H_Name, Branch_ID) references Hotel(H_Name, Branch_ID),
	Foreign key (R_Type) references Room(R_Type),
	Foreign key (Date_day, Date_month, Date_year) references Date_List(Date_day, Date_month, Date_year)
	);