//    package gmartinc.examples;

// JDBC libraries
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// JDK libraries
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/** This class is an example of accessing a database with JDBC. It stores
 * data relating to a single record in a table called MUSIC_COLLECTION
 * 
 * @author Greg Martin
 * @date 03/01/2007
 * @course swe642
 */
public class MusicCollectionDatabase
{
  // instance variables
  private String artist;
  private String albumTitle;
  private String category;
  private String mediaType;
  private String description;
  private Date releaseDate;
   
  // DB connection properties
  private String driver = "oracle.jdbc.driver.OracleDriver";
  private String jdbc_url = "jdbc:oracle:thin:@apollo.vse.gmu.edu:1521:ite10g";

  // IMPORTANT: DO NOT PUT YOUR LOGIN INFORMATION HERE. INSTEAD, PROMPT USER FOR HIS/HER LOGIN/PASSWD 
  private String username = "XXX";
  private String password = "***";
  
  /**
   * Default constructor
   */
  public MusicCollectionDatabase() {}
  
  /** Method to get the connection for the database
   * @return java.sql.Connection object
   */
  private Connection getConnection() {
    // register the JDBC driver
    try {
      Class.forName(driver);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    
    // create a connection
    Connection connection = null;
    try {
      connection = DriverManager.getConnection (jdbc_url, username, password);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return connection;
  }
  
  /** Method to check if a table exists
   * @param connection java.sql.Connection object
   * @return true is the table exists, false otherwise
   * @throws SQLException
   */
  private boolean doesTableExist(Connection connection) throws SQLException {
    boolean bTableExists = false;
    
    // check the meta data of the connection
    DatabaseMetaData dmd = connection.getMetaData();
    ResultSet rs = dmd.getTables (null, null, "MUSIC_COLLECTION", null);
    while (rs.next()){
      bTableExists =  true;
    }
    rs.close(); // close the result set
    return bTableExists;    
  }

  /** Method to create the MUSIC_COLLECTION table
   * @param connection java.sql.Connection object
   * @throws SQLException
   */
  private void createTable(Connection connection) throws SQLException
  {
    // create the SQL for the table
    StringBuffer sbCreate = new StringBuffer();
    sbCreate.append(" CREATE TABLE MUSIC_COLLECTION "); 
    sbCreate.append(" ( ");
    sbCreate.append("     ARTIST VARCHAR(256), "); 
    sbCreate.append("     ALBUM_TITLE VARCHAR(256), "); 
    sbCreate.append("     CATEGORY VARCHAR(20),  ");
    sbCreate.append("     MEDIA_TYPE VARCHAR(20),  ");
    sbCreate.append("     DESCRIPTION VARCHAR(500),  ");
    sbCreate.append("     RELEASE_DATE DATE ");
    sbCreate.append(" ) ");
    
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

  
  /**
   * @return
   * @throws SQLException
   */
  public MusicCollectionDatabase[] loadAll() throws SQLException {
    // get the connection
    Connection connection = getConnection();
        
    // create the SELECT SQL
    StringBuffer sbSelect = new StringBuffer();
    sbSelect.append(" SELECT ARTIST, ALBUM_TITLE, CATEGORY, MEDIA_TYPE, DESCRIPTION, RELEASE_DATE FROM MUSIC_COLLECTION ");
    
    Statement statement = null;
    ResultSet rs = null;
    ArrayList collection = new ArrayList();
    try
    {
      // create the statement
      statement = connection.createStatement();
      // Insert the data
      rs = statement.executeQuery(sbSelect.toString());
      if (rs != null) {    
        // when the resultset is not null, there are records returned
        while (rs.next())
        {
          // loop through each result and store the data
          // as an MusicCollectionDatabase object
          MusicCollectionDatabase music = new MusicCollectionDatabase();
          music.setArtist(rs.getString("ARTIST"));
          music.setAlbumTitle(rs.getString("ALBUM_TITLE"));
          music.setCategory(rs.getString("CATEGORY"));
          music.setMediaType(rs.getString("MEDIA_TYPE"));
          music.setDescription(rs.getString("DESCRIPTION"));
          music.setReleaseDate(rs.getDate("RELEASE_DATE"));
          
          // store it in the list
          collection.add(music);
        }        
      }
    } catch (SQLException e)
    {
      throw e;
    } finally
    {  
      rs.close();
      statement.close();
      close(connection);
    }
    
    //   return the array
    return (MusicCollectionDatabase[])collection.toArray(new MusicCollectionDatabase[0]);  
  }

  /**
   * @throws SQLException
   */
  public void insertData () throws SQLException
  {
    // get the connection
    Connection connection = getConnection();
    
    // check if table exists
    if (!this.doesTableExist(connection))
    {
      // create the table
      System.out.println("MUSIC_COLLECTION Table doesn't exist. Creating it.....");
      createTable(connection);
    }
    
    // format the date
    SimpleDateFormat df = (SimpleDateFormat) DateFormat.getInstance();
    df.applyPattern("MM/dd/yyyy");
    String strReleaseDate = df.format(releaseDate);
    
    // create the INSERT SQL
    StringBuffer sbInsert = new StringBuffer();
    sbInsert.append(" INSERT INTO MUSIC_COLLECTION (ARTIST, ALBUM_TITLE, CATEGORY, MEDIA_TYPE, DESCRIPTION, RELEASE_DATE) ");
    sbInsert.append(" VALUES ");
    sbInsert.append(" ('" + artist + "', '" + albumTitle + "','" + category + "','" + mediaType + "', '" + description + "', TO_DATE('" + strReleaseDate + "', 'MM-DD-YYYY'))");

    // create the statement
    Statement statement = connection.createStatement();
    try
    {
      // Insert the data
      statement.executeUpdate (sbInsert.toString());
    } catch (SQLException e)
    {
      throw e;
    } finally
    {  
      statement.close();
      close(connection);
    }
  }

  /**
   * @param connection
   * @throws SQLException
   */
  public void close(Connection connection) throws SQLException
  {
    try
    {
      connection.close();
    } catch (SQLException e)
    {
      throw e;
    }
  }
  
  
  //
  // Getter and setter methods
  //

  public String getAlbumTitle() {
    return albumTitle;
  }

  public void setAlbumTitle(String albumTitle) {
    this.albumTitle = albumTitle;
  }
  
  public String getArtist() {
    return artist;
  }
  
  public void setArtist(String artist) {
    this.artist = artist;
  }
  
  public String getCategory() {
    return category;
  }
  
  public void setCategory(String category) {
    this.category = category;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public String getMediaType() {
    return mediaType;
  }
  
  public void setMediaType(String mediaType) {
    this.mediaType = mediaType;
  }
  
  public Date getReleaseDate() {
    return releaseDate;
  }
  
  public void setReleaseDate(Date releaseDate) {
    this.releaseDate = releaseDate;
  }
  
  /** Method to run from a command line
   * @param arg
   */
  public static void main (String arg[])
  {
    
    // create and insert first record
    System.out.println("Insert Album 1");
    MusicCollectionDatabase musicDB = new MusicCollectionDatabase();
    musicDB.setArtist("Bon Jovi");
    musicDB.setAlbumTitle("These Days");
    musicDB.setCategory("Rock n Roll");
    musicDB.setMediaType("CD");
    musicDB.setDescription("A pretty good rock album.");
    musicDB.setReleaseDate(new Date());
    try
    {
      musicDB.insertData();
    } catch (SQLException sqlException)
    {
      while (sqlException != null)
      {
        sqlException.printStackTrace();
        sqlException = sqlException.getNextException();
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
    
    // create and insert second record
    System.out.println("Insert Album 2");
    MusicCollectionDatabase musicDB2 = new MusicCollectionDatabase();
    musicDB2.setArtist("Rush");
    musicDB2.setAlbumTitle("Fly By Night");
    musicDB2.setCategory("Hard Rock");
    musicDB2.setMediaType("Record");
    musicDB2.setDescription("A reall good rock album.");
    musicDB2.setReleaseDate(new Date());
    try
    {
      musicDB2.insertData();
    } catch (SQLException sqlException)
    {
      while (sqlException != null)
      {
        sqlException.printStackTrace();
        sqlException = sqlException.getNextException();
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
    
    // List the records
    MusicCollectionDatabase[] db;
    try {
      db = musicDB2.loadAll();
      System.out.println("\nAlbum \t Artist \t Description");
      System.out.println("----- \t ------ \t -----------");
      System.out.println(db.length);
      for (int i = 0; i <db.length; i++) {
        MusicCollectionDatabase mdb = db[i];
        System.out.println(mdb.getAlbumTitle() + "\t" + mdb.getArtist() + "\t" + mdb.getDescription());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }    
  } //end main()
} 