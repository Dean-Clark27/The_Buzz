package edu.lehigh.cse216.teamjailbreak.backend;

import java.math.BigInteger;
// these imports allow us to connect to a database
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
// these imports are for interacting with a connected database
import java.sql.PreparedStatement;
import java.sql.ResultSet;
// standard imports like common data structures
import java.util.Map;
import java.util.ArrayList;

/**
 * Database has all our logic for connecting to and interacting with a database.
 */
public class Database {
    /** Connection to db. An open connection if non-null, null otherwise */
    private Connection mConnection;

    /**
     * The Database constructor is private: we only create Database objects 
     * through one or more static getDatabase() methods.
     */
    private Database() {
    }

    /**
     * Uses the presence of environment variables to invoke the correct 
     * overloaded `getDatabase` method. Either TEAM_DATABASE_URI should be set,
     * or the values of four other variables TEAM_{IP, PORT, USER, PASS, DBNAME}.
     * @return a valid Database object with active connection on success, null otherwise
     */
    static Database getDatabase(){
        // get the Postgres configuration from environment variables; 
        // you could name them almost anything
        Map<String, String> env = System.getenv();
        String ip     = env.get("TEAM_IP");
        String port   = env.get("TEAM_PORT");
        String user   = env.get("TEAM_USER");
        String pass   = env.get("TEAM_PASS");
        String dbname = env.get("TEAM_DBNAME");
        String dbUri  = env.get("TEAM_DATABASE_URI");

        System.out.printf("Using the following environment variables:%n%s%n", "-".repeat(45)); 
        System.out.printf("TEAM_IP=%s%n", ip);
        System.out.printf("TEAM_PORT=%s%n", port);
        System.out.printf("TEAM_USER=%s%n", user);
        System.out.printf("TEAM_PASS=%s%n", "omitted");
        System.out.printf("TEAM_DBNAME=%s%n", dbname );
        System.out.printf("TEAM_DATABASE_URI=%s%n", dbUri );

        if( dbUri != null && dbUri.length() > 0 ){
            return Database.getDatabase(dbUri);
        }else if( ip != null && port != null && dbname != null && user != null && pass != null ){
            return Database.getDatabase(ip, port, dbname, user, pass);
        }
        //else insufficient information to connect
        System.err.println( "Insufficient information to connect to database." );
        return null;
    }

    /**
     * Uses dbUri to create a connection to a database, and stores it in the returned Database object
     * @param dbUri the connection string for the database
     * @return null upon connection failure, otherwise a valid Database with open connection
     */
    static Database getDatabase( String dbUri ){
        // Connect to the database or fail
        if( dbUri != null && dbUri.length() > 0 ){ 
            // TEAM_DATABASE_URI appears to be set
            Database returned_val = new Database();            
            System.out.println( "Attempting to use provided TEAM_DATABASE_URI env var." );
            try {
                returned_val.mConnection = DriverManager.getConnection(dbUri);
                if (returned_val.mConnection == null) {
                    System.err.println("Error: DriverManager.getConnection() returned a null object");
                    return null; // we return null to indicate failure; callers should know this
                } else {
                    System.out.println(" ... successfully connected");
                }
            } catch (SQLException e) {
                System.err.println("Error: DriverManager.getConnection() threw a SQLException");
                e.printStackTrace();
                return null; // we return null to indicate failure; callers should know this
            }
            return returned_val; // the valid object with active connection
        }
        return null;
    }

    /**
     * Uses params to manually construct string for connecting to a database, 
     * and stores it in the returned Database object
     * @param ip not strictly an ip, can also be a host name
     * @param port default tends to be 5432 or something like it
     * @param dbname generally should be non null
     * @param user generally required
     * @param pass generally required
     * @return null upon connection failure, otherwise a valid Database with open connection
     */
    static Database getDatabase( String ip, String port, String dbname, String user, String pass ){
        if( ip != null && port != null && 
            dbname != null &&
            user != null && pass != null)
        {
            // TEAM_* variables appear to be set
            Database returned_val = new Database();
            System.out.println( "Attempting to use provided TEAM_{IP, PORT, USER, PASS, DBNAME} env var." );
            System.out.println("Connecting to " + ip + ":" + port);
            try {
                // Open a connection, fail if we cannot get one
                returned_val.mConnection = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" + port + "/" + dbname, user, pass);
                if (returned_val.mConnection == null) {
                    System.out.println("\n\tError: DriverManager.getConnection() returned a null object");
                    return null; // we return null to indicate failure; callers should know this
                } else {
                    System.out.println(" ... successfully connected");
                }
            } catch (SQLException e) {
                System.out.println("\n\tError: DriverManager.getConnection() threw a SQLException");
                e.printStackTrace();
                return null; // we return null to indicate failure; callers should know this
            }
            return returned_val; // the valid object with active connection
        }
        return null;
    }

    ////////////////////////// CREATE TABLE //////////////////////////
    /** precompiled SQL statement for creating the table in our database */
    private PreparedStatement mCreateTable;
    /** the SQL for creating the table in our database */
    private static final String SQL_CREATE_TABLE = 
            "CREATE TABLE IF NOT EXISTS Message (" + 
            " id SERIAL PRIMARY KEY," + 
            " title VARCHAR(100) NOT NULL," +
            " contents VARCHAR(300) NOT NULL)" + 
            " is_liked BOOLEAN DEFAULT false";
    // NB: we can easily get ourselves in trouble here by typing the
    //     SQL incorrectly.  We really should have things like "Message"
    //     as constants, and then build the strings for the statements
    //     from those constants.

    /** safely performs mCreateTable = mConnection.prepareStatement(SQL_CREATE_TABLE); */
    private boolean init_mCreateTable(){
        // return true on success, false otherwise
        try {
            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table 
            // creation/deletion, so multiple executions will cause an exception
            mCreateTable = mConnection.prepareStatement(SQL_CREATE_TABLE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mCreateTable");
            System.err.println("Using SQL: " + SQL_CREATE_TABLE);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
    * Create Message.  If it already exists, this will print an error
    */
    void createTable() {
        if( mCreateTable == null )  // not yet initialized, do lazy init
            init_mCreateTable();    // lazy init
        try {
            System.out.println( "Database operation: createTable()" );
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //////////////////////////  DROP TABLE  //////////////////////////
    /** ps to delete table tbldata from the database */
    private PreparedStatement mDropTable;
    /** the SQL for mDropTable */
    private static final String SQL_DROP_TABLE_TBLDATA = "DROP TABLE Message";

    /** safely performs mDropTable = mConnection.prepareStatement("DROP TABLE Message"); */
    private boolean init_mDropTable(){
        // return true on success, false otherwise
        try {
            mDropTable = mConnection.prepareStatement( SQL_DROP_TABLE_TBLDATA );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mDropTable");
            System.err.println("Using SQL: " + SQL_DROP_TABLE_TBLDATA);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Remove Message from the database.  If it does not exist, this will print
     * an error.
     */
    void dropTable() {
        if( mDropTable == null )  // not yet initialized, do lazy init
            init_mDropTable();    // lazy init
        try {
            System.out.println( "Database operation: dropTable()" );
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     //////////////////////////  INSERT POST //////////////////////////
    /** ps to instert into tbldata a new row with next auto-gen id and the two given values */
    private PreparedStatement mInsertOne;
    /** the SQL for mInsertOne */
    private static final String SQL_INSERT_ONE_TBLDATA = 
        "INSERT INTO Post (author, title, contents, valid) VALUES (?, ?, ?, ?) RETURNING id";

    /** safely performs mInsertOne = mConnection.prepareStatement("INSERT INTO Message VALUES (default, ?, ?, ?)"); */
    private boolean init_mInsertOnePost(){
        // return true on success, false otherwise
        try {
            mInsertOne = mConnection.prepareStatement( SQL_INSERT_ONE_TBLDATA );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mInsertOne");
            System.err.println("Using SQL: " + SQL_INSERT_ONE_TBLDATA);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Insert a row into the database
     * @param title The title for this new row
     * @param contents The contents body for this new row
     * @param is_liked The status of being liked or not for this new row
     * @return The number of rows that were inserted
     */
    int insertPostRow(int author, String title, String contents, boolean valid) {
        int insertedId = -1;
        if( mInsertOne == null )  // not yet initialized, do lazy init
            init_mInsertOnePost();    // lazy init
        try {
            System.out.println( "Database operation: insertRow(Int, String, String, Boolean)" );
            mInsertOne.setInt(1, author);
            mInsertOne.setString(2, title);
            mInsertOne.setString(3, contents);
            mInsertOne.setBoolean(4, valid);
           
            // Use executeQuery() instead of executeUpdate() to get the returned id.
            ResultSet rs = mInsertOne.executeQuery();
            if (rs.next()) {
                insertedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insertedId;
    }

    //////////////////////////  UPDATE ONE 2-ARG  //////////////////////////
    /** ps to replace the Message in Message for specified row with given value */
    private PreparedStatement mUpdateOne_3arg;
    /** the SQL for mUpdateOne */
    private static final String SQL_UPDATE_ONE_TBLDATA_3ARG = 
            "UPDATE Post" + 
            " SET title = ?, contents = ?, valid = ?" + 
            " WHERE id = ?";

    /** safely performs mUpdateOne = mConnection.prepareStatement("UPDATE Message SET title = ?, contents = ?, is_liked = ? WHERE id = ?"); */
    private boolean init_mUpdateOne_2arg(){
        // return true on success, false otherwise
        try {
            mUpdateOne_3arg = mConnection.prepareStatement( SQL_UPDATE_ONE_TBLDATA_3ARG );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mUpdateOne");
            System.err.println("Using SQL: " + SQL_UPDATE_ONE_TBLDATA_3ARG);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Update the title, contents and liked status for a row in the database
     * @param id The id of the row to update
     * @param title The new post title
     * @param contents The new post contents
     * @param is_liked The liked status of the post
     * @return The number of rows that were updated.  -1 indicates an error.
     */
    int updateOne(int id, String title, String contents, boolean valid) {
        if( mUpdateOne_3arg == null )  // not yet initialized, do lazy init
            init_mUpdateOne_2arg();    // lazy init
        int res = -1;
        try {
            System.out.println( "Database operation: updateOne(int id, String subject, String Message)" );
            mUpdateOne_3arg.setString(1, title);
            mUpdateOne_3arg.setString(2, contents);
            mUpdateOne_3arg.setBoolean(3, valid);
            mUpdateOne_3arg.setInt(4, id);
            res = mUpdateOne_3arg.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    //////////////////////////  DELETE  //////////////////////////
    /** ps for deleting a row from Message */
    private PreparedStatement mDeleteOne;
    /** the SQL for mDeleteOne */
    private static final String SQL_DELETE_ONE = 
            "DELETE FROM Post" + 
            " WHERE id = ?";

    /** safely performs mDeleteOne = mConnection.prepareStatement(SQL_DELETE_ONE); */
    private boolean init_mDeleteOne(){
        // return true on success, false otherwise
        try {
            mDeleteOne = mConnection.prepareStatement( SQL_DELETE_ONE );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mDeleteOne");
            System.err.println("Using SQL: " + SQL_DELETE_ONE);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Delete a row by ID
     * @param id The id of the row to delete
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteRow(int id) {
        if( mDeleteOne == null )  // not yet initialized, do lazy init
            init_mDeleteOne();    // lazy init
        int res = -1;
        try {
            System.out.println( "Database operation: deleteRow(int)" );
            mDeleteOne.setInt(1, id);
            res = mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    //////////////////////////  SELECT ALL  //////////////////////////
    /** ps to return all rows from Message, but only the id and subject columns */
    private PreparedStatement mSelectAll;
    /** the SQL for mSelectAll */
    private static final String SQL_SELECT_ALL_TBLDATA = 
            "SELECT id, author, title, contents, valid" + 
            " FROM Post;";

    /** safely performs mSelectAll = mConnection.prepareStatement("SELECT id, subject FROM Message"); */
    private boolean init_mSelectAll(){
        // return true on success, false otherwise
        try {
            mSelectAll = mConnection.prepareStatement( SQL_SELECT_ALL_TBLDATA );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectAll");
            System.err.println("Using SQL: " + SQL_SELECT_ALL_TBLDATA);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Query the database for a list of all subjects and their IDs
     * @return All rows, as an ArrayList; note that Message is intentionally not returned
     */
    ArrayList<PostRowData> selectAll() {
        if( mSelectAll == null )  // not yet initialized, do lazy init
            init_mSelectAll();    // lazy init        
        ArrayList<PostRowData> res = new ArrayList<PostRowData>();
        try {
            System.out.println( "Database operation: selectAll()" );
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int author = rs.getInt("author");
                String title = rs.getString("title");
                String contents = rs.getString("contents");
                boolean valid = rs.getBoolean("valid");
                PostRowData data = new PostRowData(id, author, title, contents, valid);
                res.add(data);
            }
            rs.close(); // remember to close the result set
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //////////////////////////  SELECT ONE  //////////////////////////
    /** ps to return row from Message with matching id */
    private PreparedStatement mSelectOne;
    /** the SQL for mSelectOne */
    private static final String SQL_SELECT_ONE_TBLDATA = 
            "SELECT *" + 
            " FROM Post" +
            " WHERE id = ?;";

    /** safely performs mSelectOne = mConnection.prepareStatement("SELECT * from Message WHERE id=?"); */
    private boolean init_mSelectOne(){
        // return true on success, false otherwise
        try {
            mSelectOne = mConnection.prepareStatement( SQL_SELECT_ONE_TBLDATA );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectOne");
            System.err.println("Using SQL: " + SQL_SELECT_ONE_TBLDATA);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Get all data for a specific row, by ID
     * @param id The id of the row being requested
     * @return The data for the requested row, or null if the ID was invalid
     */
    PostRowData selectOne(int row_id) {
        if( mSelectOne == null )  // not yet initialized, do lazy init
            init_mSelectOne();    // lazy init
        PostRowData data = null;
        try {
            System.out.println( "Database operation: selectOne(int)" );
            mSelectOne.setInt(1, row_id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                int author = rs.getInt("author");
                String title = rs.getString("title");
                String contents = rs.getString("contents");
                boolean valid = rs.getBoolean("valid");
                data = new PostRowData(id, author, title, contents, valid);
            }
            rs.close();  // remember to close the result set
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }


    //////////////////////////  SELECT ALL USERS  //////////////////////////
    /** ps to return all rows from Message, but only the id and subject columns */
    private PreparedStatement selectAllUsers;
    /** the SQL for mSelectAll */
    private static final String SQL_SELECT_ALL_USERS = 
            "SELECT *" + 
            " FROM \"User\";";

    /** safely performs mSelectAll = mConnection.prepareStatement("SELECT id, subject FROM Message"); */
    private boolean init_selectAllUsers(){
        // return true on success, false otherwise
        try {
            selectAllUsers = mConnection.prepareStatement( SQL_SELECT_ALL_USERS );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: selectAllUsers");
            System.err.println("Using SQL: " + SQL_SELECT_ALL_USERS);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Query the database for a list of all subjects and their IDs
     * @return All rows, as an ArrayList; note that Message is intentionally not returned
     */
    ArrayList<UserRowData> selectAllUsers() {
        if( selectAllUsers == null )  // not yet initialized, do lazy init
            init_selectAllUsers();    // lazy init        
        ArrayList<UserRowData> res = new ArrayList<UserRowData>();
        try {
            System.out.println( "Database operation: selectAllUsers()" );
            ResultSet rs = selectAllUsers.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String email = rs.getString("email");
                String username = rs.getString("username");
                String gender_identity = rs.getString("gender_identity");
                String sexual_orientation = rs.getString("sexual_orientation");
                String notes = rs.getString("notes");
                boolean valid = rs.getBoolean("valid");
                UserRowData data = new UserRowData(id, email, username, gender_identity, sexual_orientation, notes, valid);
                res.add(data);
            }
            rs.close(); // remember to close the result set
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //////////////////////////  SELECT ONE USER  //////////////////////////
    /** ps to return row from Message with matching id */
    private PreparedStatement selectUser;
    /** the SQL for mSelectOne */
    private static final String SQL_SELECT_USER = 
            "SELECT *" + 
            " FROM \"User\"" +
            " WHERE id = ?;";

    /** safely performs mSelectOne = mConnection.prepareStatement("SELECT * from Message WHERE id=?"); */
    private boolean init_selectUser(){
        // return true on success, false otherwise
        try {
            selectUser = mConnection.prepareStatement( SQL_SELECT_USER );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: selectUser");
            System.err.println("Using SQL: " + SQL_SELECT_USER);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Get all data for a specific row, by ID
     * @param id The id of the row being requested
     * @return The data for the requested row, or null if the ID was invalid
     */
    UserRowData selectUser(String user_id) {
        if( selectUser == null )  // not yet initialized, do lazy init
            init_selectUser();    // lazy init
            UserRowData data = null;
        try {
            System.out.println( "Database operation: selectUser(int)" );
            selectUser.setString(1, user_id);
            ResultSet rs = selectUser.executeQuery();
            if (rs.next()) {
                String id = rs.getString("id");
                String email = rs.getString("email");
                String username = rs.getString("username");
                String gender_identity = rs.getString("gender_identity");
                String sexual_orientation = rs.getString("sexual_orientation");
                String notes = rs.getString("notes");
                boolean valid = rs.getBoolean("valid");
                data = new UserRowData(id, email, username, gender_identity, sexual_orientation, notes, valid);
            }
            rs.close();  // remember to close the result set
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    //////////////////////////  INSERT USER //////////////////////////
    /** ps to instert into tbldata a new row with next auto-gen id and the two given values */
    private PreparedStatement insertUser;
    /** the SQL for insertUser */
    private static final String SQL_INSERT_USER = 
        "INSERT INTO \"User\" (id, email, username, gender_identity, sexual_orientation, notes, valid) VALUES (?, ?, ?, ?, ?, ?, ?)";

    /** safely performs mInsertOne = mConnection.prepareStatement("INSERT INTO Message VALUES (default, ?, ?, ?)"); */
    private boolean init_insertUser(){
        // return true on success, false otherwise
        try {
            insertUser = mConnection.prepareStatement( SQL_INSERT_USER );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: insertUser");
            System.err.println("Using SQL: " + SQL_INSERT_USER);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Insert a row into the database
     * @param title The title for this new row
     * @param contents The contents body for this new row
     * @param is_liked The status of being liked or not for this new row
     * @return The number of rows that were inserted
     */
    int insertUserRow(String id, String email, String username, String gender_identity, String sexual_orientation, String notes, boolean valid) {
        int insertedId = -1;
        if( insertUser == null )  // not yet initialized, do lazy init
            init_insertUser();    // lazy init
        try {
            System.out.println("Database operation: insertUser(String id, String email, String username, String gender_identity, String sexual_orientation, String notes, boolean valid)" );
            insertUser.setString(1, id);
            insertUser.setString(2, email);
            insertUser.setString(3, username);
            insertUser.setString(4, gender_identity);
            insertUser.setString(5, sexual_orientation);
            insertUser.setString(6, notes);
            insertUser.setBoolean(7, valid);
           
            // Use executeQuery() instead of executeUpdate() to get the returned id.
            ResultSet rs = insertUser.executeQuery();
            if (rs.next()) {
                insertedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insertedId;
    }

    //////////////////////////  DELETE USER //////////////////////////
    /** ps for deleting a row from Message */
    private PreparedStatement deleteUser;
    /** the SQL for mDeleteOne */
    private static final String SQL_DELETE_USER = 
            "DELETE FROM \"User\"" + 
            " WHERE id = ?";

    
    private boolean init_deleteUser(){
        // return true on success, false otherwise
        try {
            deleteUser = mConnection.prepareStatement( SQL_DELETE_USER );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: deleteUser");
            System.err.println("Using SQL: " + SQL_DELETE_USER);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Delete a row by ID
     * @param id The id of the row to delete
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteUser(String id) {
        if( deleteUser == null )  // not yet initialized, do lazy init
            init_deleteUser();    // lazy init
        int res = -1;
        try {
            System.out.println( "Database operation: deleteUser(int)" );
            deleteUser.setString(1, id);
            res = deleteUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    //////////////////////////  INSERT UPVOTE //////////////////////////
    /** ps to instert into tbldata a new row with next auto-gen id and the two given values */
    private PreparedStatement insertUpvote;
    /** the SQL for insertUser */
    private static final String SQL_INSERT_UPVOTE = 
        "INSERT INTO upvote (author, parent_post) VALUES (?, ?) RETURNING id";

    /** safely performs mInsertOne = mConnection.prepareStatement("INSERT INTO Message VALUES (default, ?, ?, ?)"); */
    private boolean init_insertUpvote(){
        // return true on success, false otherwise
        try {
            insertUpvote = mConnection.prepareStatement( SQL_INSERT_UPVOTE );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: insertUpvote");
            System.err.println("Using SQL: " + SQL_INSERT_UPVOTE);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Insert a row into the database
     * @param title The title for this new row
     * @param contents The contents body for this new row
     * @param is_liked The status of being liked or not for this new row
     * @return The number of rows that were inserted
     */
    int insertUpvote(int author, int parent_post) {
        int insertedId = -1;
        if( insertUpvote == null )  // not yet initialized, do lazy init
            init_insertUpvote();    // lazy init
        try {
            System.out.println( "Database operation: insertUpvote(int author, int parent_post)" );
            insertUpvote.setInt(1, author);
            insertUpvote.setInt(2, parent_post);
           
            // Use executeQuery() instead of executeUpdate() to get the returned id.
            ResultSet rs = insertUpvote.executeQuery();
            if (rs.next()) {
                insertedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insertedId;
    }
    //////////////////////////  DELETE UPVOTE //////////////////////////
    /** ps for deleting a row from Message */
    private PreparedStatement deleteUpvote;
    /** the SQL for mDeleteOne */
    private static final String SQL_DELETE_UPVOTE = 
            "DELETE FROM Upvote" + 
            " WHERE author = ? and parent_post = ?";

    /** safely performs mDeleteOne = mConnection.prepareStatement(SQL_DELETE_ONE); */
    private boolean init_deleteUpvote(){
        // return true on success, false otherwise
        try {
            mDeleteOne = mConnection.prepareStatement( SQL_DELETE_UPVOTE );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: deleteUpvote");
            System.err.println("Using SQL: " + SQL_DELETE_UPVOTE);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Delete a row by ID
     * @param id The id of the row to delete
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteUpvote(int author, int parent_post) {
        if( deleteUpvote == null )  // not yet initialized, do lazy init
            init_deleteUpvote();    // lazy init
        int res = -1;
        try {
            System.out.println( "Database operation: deleteUpvote(int)" );
            deleteUpvote.setInt(1, author);
            deleteUpvote.setInt(2, parent_post);
            res = deleteUpvote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    //////////////////////////  INSERT DOWNVOTE //////////////////////////
    /** ps to instert into tbldata a new row with next auto-gen id and the two given values */
    private PreparedStatement insertDownvote;
    /** the SQL for insertUser */
    private static final String SQL_INSERT_DOWNVOTE = 
        "INSERT INTO upvote (author, parent_post) VALUES (?, ?) RETURNING id";

    /** safely performs mInsertOne = mConnection.prepareStatement("INSERT INTO Message VALUES (default, ?, ?, ?)"); */
    private boolean init_insertDownvote(){
        // return true on success, false otherwise
        try {
            insertDownvote = mConnection.prepareStatement( SQL_INSERT_DOWNVOTE );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: insertDownvote");
            System.err.println("Using SQL: " + SQL_INSERT_DOWNVOTE);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Insert a row into the database
     * @param title The title for this new row
     * @param contents The contents body for this new row
     * @param is_liked The status of being liked or not for this new row
     * @return The number of rows that were inserted
     */
    int insertDownvote(int author, int parent_post) {
        int insertedId = -1;
        if( insertDownvote == null )  // not yet initialized, do lazy init
            init_insertDownvote();    // lazy init
        try {
            System.out.println( "Database operation: insertDownvote(int author, int parent_post)" );
            insertDownvote.setInt(1, author);
            insertDownvote.setInt(2, parent_post);
           
            // Use executeQuery() instead of executeUpdate() to get the returned id.
            ResultSet rs = insertDownvote.executeQuery();
            if (rs.next()) {
                insertedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insertedId;
    }

    //////////////////////////  DELETE DOWNVOTE //////////////////////////
    /** ps for deleting a row from Message */
    private PreparedStatement deleteDownvote;
    /** the SQL for mDeleteOne */
    private static final String SQL_DELETE_DOWNVOTE = 
            "DELETE FROM Downvote" + 
            " WHERE author = ? and parent_post = ?";

    /** safely performs mDeleteOne = mConnection.prepareStatement(SQL_DELETE_ONE); */
    private boolean init_deleteDownvote(){
        // return true on success, false otherwise
        try {
            deleteDownvote = mConnection.prepareStatement( SQL_DELETE_DOWNVOTE );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: deleteDownvote");
            System.err.println("Using SQL: " + SQL_DELETE_DOWNVOTE);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Delete a row by ID
     * @param id The id of the row to delete
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteDownvote(int author, int parent_post) {
        if( deleteDownvote == null )  // not yet initialized, do lazy init
            init_deleteDownvote();    // lazy init
        int res = -1;
        try {
            System.out.println( "Database operation: deleteDownvote(int)" );
            deleteDownvote.setInt(1, author);
            deleteDownvote.setInt(2, parent_post);
            res = deleteDownvote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    //////////////////////////  INSERT COMMENT //////////////////////////
    /** ps to instert into tbldata a new row with next auto-gen id and the two given values */
    private PreparedStatement insertComment;
    /** the SQL for insertUser */
    private static final String SQL_INSERT_COMMENT = 
        "INSERT INTO comment (author, parent_post, contents) VALUES (?, ?, ?) RETURNING id";

    /** safely performs mInsertOne = mConnection.prepareStatement("INSERT INTO Message VALUES (default, ?, ?, ?)"); */
    private boolean init_insertComment(){
        // return true on success, false otherwise
        try {
            insertComment = mConnection.prepareStatement( SQL_INSERT_COMMENT );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: insertComment");
            System.err.println("Using SQL: " + SQL_INSERT_COMMENT);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Insert a row into the database
     * @param title The title for this new row
     * @param contents The contents body for this new row
     * @param is_liked The status of being liked or not for this new row
     * @return The number of rows that were inserted
     */
    int insertComment(int author, int parent_post, String contents) {
        int insertedId = -1;
        if( insertComment == null )  // not yet initialized, do lazy init
            init_insertComment();    // lazy init
        try {
            System.out.println( "Database operation: insertComment(int author, int parent_post, String contents)" );
            insertDownvote.setInt(1, author);
            insertDownvote.setInt(2, parent_post);
            insertDownvote.setString(2, contents);
           
            // Use executeQuery() instead of executeUpdate() to get the returned id.
            ResultSet rs = insertComment.executeQuery();
            if (rs.next()) {
                insertedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insertedId;
    }

    //////////////////////////  DELETE COMMENT //////////////////////////
    /** ps for deleting a row from Message */
    private PreparedStatement deleteComment;
    /** the SQL for mDeleteOne */
    private static final String SQL_DELETE_COMMENT = 
            "DELETE FROM Comment" + 
            " WHERE id = ?";

    /** safely performs mDeleteOne = mConnection.prepareStatement(SQL_DELETE_ONE); */
    private boolean init_deleteComment(){
        // return true on success, false otherwise
        try {
            deleteComment = mConnection.prepareStatement( SQL_DELETE_COMMENT );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: deleteComment");
            System.err.println("Using SQL: " + SQL_DELETE_COMMENT);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Delete a row by ID
     * @param id The id of the row to delete
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteComment(int id) {
        if( deleteComment == null )  // not yet initialized, do lazy init
            init_deleteComment();    // lazy init
        int res = -1;
        try {
            System.out.println( "Database operation: deleteComment(int)" );
            deleteComment.setInt(1, id);
            res = deleteComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * RowData is like a struct in C: we use it to hold data, and we allow 
     * direct access to its fields.  In the context of this Database, RowData 
     * represents the data we'd see in a row.
     * 
     * We make RowData a static class of Database because we don't really want
     * to encourage users to think of RowData as being anything other than an
     * abstract representation of a row of the database.  RowData and the 
     * Database are tightly coupled: if one changes, the other should too.
     */
    public static record PostRowData (int id, int author, String title, String contents, boolean valid) {}
    public static record CommentRowData (int id, int message, String author, String contents) {}
    public static record UserRowData (String id, String email, String username, String gender_identity, String sexual_orientation, String notes, boolean valid) {}
    public static record VoteRowData (int id, int author, int message) {}
    
    /**
     * Close the current connection to the database, if one exists.
     * 
     * NB: The connection will always be null after this call, even if an 
     *     error occurred during the closing operation.
     * 
     * @return true if the connection was cleanly closed, false otherwise
     */
    boolean disconnect(){
        if( mConnection != null ){ 
            // for this simple example, we disconnect from the db when done
            System.out.print("Disconnecting from database.");
            try {
                mConnection.close();
            } catch (SQLException e) {
                System.err.println("\n\tError: close() threw a SQLException");
                e.printStackTrace();
                mConnection = null; // set it to null rather than leave broken
                return false;
            }
            System.out.println(" ...  connection successfully closed");
            mConnection = null; // connection is gone, so null this out
            return true;
        } 
        // else connection was already null
        System.err.print("Unable to close connection: Connection was null");
        return false;
    }

}