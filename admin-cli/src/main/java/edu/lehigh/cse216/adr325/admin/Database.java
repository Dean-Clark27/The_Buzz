package edu.lehigh.cse216.adr325.admin;

// these imports allow us to connect to a database
import java.sql.Connection;
import java.sql.DriverManager;
// these imports are for interacting with a connected database
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
// standard imports like common data structures
import java.util.Map;

/**
 * Database has all our logic for connecting to and interacting with a database.
 */
public class Database {
    /**
     * RowData is like a struct in C: we use it to hold data, and we allow 
     * direct access to its immutable fields. In the context of this Database, 
     * RowData represents the data we'd see in a row.
     * 
     * We make RowData a static class of Database because we don't really want
     * to encourage users to think of RowData as being anything other than an
     * abstract representation of a row of the database.  RowData and the 
     * Database are tightly coupled: if one changes, the other should too.
     */
    public interface RowData {}
    /**
     * PostRowData is a RowData object that represents a row in the Post table
     * 
     * @param id The id of the post
     * @param author The id of the author of the post
     * @param title The title of the post
     * @param contents The contents of the post
     * @param valid Whether the post is valid or not
     */
    public static record PostRowData (
        int id, 
        String author, 
        String title, 
        String contents,
        boolean valid
    ) implements RowData {}
    /**
     * UserRowData is a RowData object that represents a row in the User table
     * 
     * @param id The id of the user
     * @param email The email of the user
     * @param username The username of the
     * @param gender_identity The gender identity of the user
     * @param sexual_orientation The sexual orientation of the user
     * @param notes Any notes about the user
     * @param valid Whether the user is valid or not
     */
    public static record UserRowData (
        String id,
        String email,
        String username,
        String gender_identity,
        String sexual_orientation,
        String notes,
        boolean valid
    ) implements RowData {}
    /**
     * UpvoteRowData is a RowData object that represents a row in the Upvote table
     * 
     * @param id The id of the upvote
     * @param author The id of the author of the upvote
     * @param parent_post The id of the post that was upvoted
     */
    public static record UpvoteRowData (
        int id,
        String author,
        int parent_post
    ) implements RowData {}
    /**
     * DownvoteRowData is a RowData object that represents a row in the Downvote table
     * 
     * @param id The id of the downvote
     * @param author The id of the author of the downvote
     * @param parent_post The id of the post that was downvoted
     */
    public static record DownvoteRowData (
        int id,
        String author,
        int parent_post
    ) implements RowData {}
    /**
     * CommentRowData is a RowData object that represents a row in the Comment table
     * 
     * @param id The id of the comment
     * @param parent_post The id of the post that was commented on
     * @param contents The contents of the comment
     * @param author The id of the author of the comment
     */
    public static record CommentRowData (
        int id,
        int parent_post,
        String contents,
        String author
    ) implements RowData {}
    /**
     * A helper function to get the values from a RowData object and add them to an ArrayList
     * 
     * @param values The ArrayList to add the values to
     * @param row The RowData object to get the values from
     * @param tableName The name of the table to get the values for
     */
    boolean getRowDataValues(ArrayList<Object> values, RowData row, String tableName) {
        switch (tableName) {
            case "Post":
                // Fall through to default case
            default:
                PostRowData postData = (PostRowData) row;
                values.add(postData.author());
                values.add(postData.title());
                values.add(postData.contents());
                break;
            case "User":
                UserRowData userData = (UserRowData) row;
                values.add(userData.id());
                values.add(userData.email());
                values.add(userData.username());
                values.add(userData.gender_identity());
                values.add(userData.sexual_orientation());
                values.add(userData.notes());
                break;
            case "Upvote":
                UpvoteRowData upvoteData = (UpvoteRowData) row;
                values.add(upvoteData.author());
                values.add(upvoteData.parent_post());
                break;
            case "Downvote":
                DownvoteRowData downvoteData = (DownvoteRowData) row;
                values.add(downvoteData.author());
                values.add(downvoteData.parent_post());
                break;
            case "Comment":
                CommentRowData commentData = (CommentRowData) row;
                values.add(commentData.parent_post());
                values.add(commentData.contents());
                values.add(commentData.author());
                break;
        }
        return true;
    }

    /** Connection to db. An open connection if non-null, null otherwise */
    private Connection mConnection;

    //////////////////////////  CREATE TABLE //////////////////////////
    /** precompiled SQL statement for creating the table in our database */
    private PreparedStatement mCreateTable;
    /**
     * Create a table in the database. Only call with a sanitized table name!!!
     * 
     * @param tableName The name of the table to create
     */
    void createTable(String tableName) {
        // Assign SQL_CREATE_TABLE based on the table name
        String SQL_CREATE_TABLE = "";
        switch (tableName) {
            case "Post":
                SQL_CREATE_TABLE = 
                    "CREATE TABLE IF NOT EXISTS Post (" +
                    " id SERIAL PRIMARY KEY," +
                    " author VARCHAR(50) REFERENCES \"User\"(id)," +
                    " title VARCHAR(50) NOT NULL," +
                    " contents VARCHAR(500) NOT NULL," +
                    " valid BOOLEAN NOT NULL DEFAULT TRUE);";
                break;
            default: // For testing purposes, we can create a table with a different name
                SQL_CREATE_TABLE = 
                    "CREATE TABLE IF NOT EXISTS " + tableName + " (" + 
                    " id SERIAL PRIMARY KEY," +
                    " author VARCHAR(50)," +
                    " title VARCHAR(50) NOT NULL," +
                    " contents VARCHAR(500) NOT NULL," +
                    " valid BOOLEAN NOT NULL DEFAULT TRUE);";
                break;
            case "User":
                SQL_CREATE_TABLE = 
                    "CREATE TABLE IF NOT EXISTS \"User\" (" + 
                    " id VARCHAR(255) PRIMARY KEY," +
                    " email VARCHAR(50) NOT NULL," +
                    " username VARCHAR(50) NOT NULL," +
                    " gender_identity VARCHAR(50) NOT NULL," +
                    " sexual_orientation VARCHAR(50) NOT NULL," +
                    " notes VARCHAR(255) NOT NULL," +
                    " valid BOOLEAN NOT NULL DEFAULT TRUE);";
                break;
            case "Upvote":
                SQL_CREATE_TABLE =
                    "CREATE TABLE IF NOT EXISTS Upvote (" +
                    " id SERIAL PRIMARY KEY," +
                    " author VARCHAR(50) REFERENCES \"User\"(id)," +
                    " parent_post INT REFERENCES Post(id));";
                break;
            case "Downvote":
                SQL_CREATE_TABLE =
                    "CREATE TABLE IF NOT EXISTS Downvote (" +
                    " id SERIAL PRIMARY KEY," +
                    " author VARCHAR(50) REFERENCES \"User\"(id)," +
                    " parent_post INT REFERENCES Post(id));";
                break;
            case "Comment":
                SQL_CREATE_TABLE =
                    "CREATE TABLE IF NOT EXISTS Comment (" +
                    " id SERIAL PRIMARY KEY," +
                    " parent_post INT REFERENCES Post(id)," +
                    " contents VARCHAR(500) NOT NULL," +
                    " author VARCHAR(50) REFERENCES \"User\"(id));";
                break;
        }

        try { // create the table
            mCreateTable = mConnection.prepareStatement(SQL_CREATE_TABLE);
            System.out.println( "Database operation: createTable()" );
            mCreateTable.execute();
        } catch (SQLException e) {
            System.err.println("Error creating table: " + tableName);
            e.printStackTrace();
        } finally { // close the PreparedStatement
            if (mCreateTable != null) {
                try {
                    mCreateTable.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //////////////////////////  DROP TABLE  //////////////////////////
    /** ps to delete table from the database */
    private PreparedStatement mDropTable;

    /**
     * Remove table from the database.  If it does not exist, this will print
     * an error.
     * 
     * @param tableName The name of the table to drop
     */
    void dropTable(String tableName) {
        // Assign SQL_DROP_TABLE based on the table name
        String SQL_DROP_TABLE = "";
        switch (tableName) {
            case "Post":
                // Fall through to default case
            default:
                SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + tableName;
                break;
            case "User":
                SQL_DROP_TABLE = "DROP TABLE IF EXISTS \"User\"";
                break;
            case "Upvote":
                SQL_DROP_TABLE = "DROP TABLE IF EXISTS Upvote";
                break;
            case "Downvote":
                SQL_DROP_TABLE = "DROP TABLE IF EXISTS Downvote";
                break;
            case "Comment":
                SQL_DROP_TABLE = "DROP TABLE IF EXISTS Comment";
                break;
        }

        try { // drop the table
            mDropTable = mConnection.prepareStatement(SQL_DROP_TABLE);
            System.out.println( "Database operation: dropTable()" );
            mDropTable.execute();
            System.out.println("Table " + tableName + " dropped successfully.");
        } catch (SQLException e) {
            System.err.println("Error dropping table: " + tableName);
            e.printStackTrace();
        } finally { // close the PreparedStatement
            if (mDropTable != null) {
                try {
                    mDropTable.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //////////////////////////  INSERT  //////////////////////////
    /** ps to insert into table a new row with next auto-gen id and the two given values */
    private PreparedStatement mInsertOne;
    /**
     * Insert a row into the database
     * 
     * @param tableName The name of the table to insert into
     * @param row The data to insert
     * @return The number of rows that were inserted
     */
    // Insert a row into a dynamically named table
    int insertRow(String tableName, RowData row) {
        // Assign SQL_INSERT_ONE based on the table name
        String SQL_INSERT_ONE = "";
        switch (tableName) {
            case "Post":
                // Fall through to default case
            default:
                SQL_INSERT_ONE = 
                    "INSERT INTO " + tableName + " (author, title, contents) VALUES (?, ?, ?);";
                break;
            case "User":
                SQL_INSERT_ONE = 
                    "INSERT INTO \"User\" (id, email, username, gender_identity, sexual_orientation, notes) VALUES (?, ?, ?, ?, ?, ?);";
                break;
            case "Upvote":
                SQL_INSERT_ONE = 
                    "INSERT INTO Upvote (author, parent_post) VALUES (?, ?);";
                break;
            case "Downvote":
                SQL_INSERT_ONE = 
                    "INSERT INTO Downvote (author, parent_post) VALUES (?, ?);";
                break;
            case "Comment":
                SQL_INSERT_ONE = 
                    "INSERT INTO Comment (parent_post, contents, author) VALUES (?, ?, ?);";
                break;
        }

        try { // insert the row
            // Convert the RowData object to an ArrayList of values
            ArrayList<Object> values = new ArrayList<>();
            if (!getRowDataValues(values, row, tableName)) {
                return 0;
            }
            // Initialize the PreparedStatement with the dynamic table name
            mInsertOne = mConnection.prepareStatement(SQL_INSERT_ONE);
            for (int i = 0; i < values.size(); i++) { // set the values in the PreparedStatement
                mInsertOne.setObject(i + 1, values.get(i));
            }
            System.out.println("Database operation: insertRow() on table " + tableName);
            return mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally { // close the PreparedStatement
            if (mInsertOne != null) {
                try {
                    mInsertOne.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //////////////////////////  UPDATE ONE  //////////////////////////
    /** ps to replace the data in a row in table */
    private PreparedStatement mUpdateOne;

    /**
     * Update the data for a row by ID
     * 
     * @param tableName The name of the table to update
     * @param id The id of the row to update
     * @param row The new data for the row
     * @return The number of rows that were updated. -1 indicates an error.
     */
    int updateOne(String tableName, String id, RowData row) {
        // Assign SQL_UPDATE_ONE based on the table name
        String SQL_UPDATE_ONE = "";
        switch (tableName) {
            case "Post":
                // Fall through to default case
            default:
                SQL_UPDATE_ONE = 
                    "UPDATE " + tableName + " SET author = ?, title = ?, contents = ? WHERE id = ?;";
                break;
            case "User":
                SQL_UPDATE_ONE = 
                    "UPDATE \"User\" SET email = ?, username = ?, gender_identity = ?, sexual_orientation = ?, notes = ? WHERE id = ?;";
                break;
            case "Upvote":
                SQL_UPDATE_ONE = 
                    "UPDATE Upvote SET author = ?, parent_post = ? WHERE id = ?;";
                break;
            case "Downvote":
                SQL_UPDATE_ONE = 
                    "UPDATE Downvote SET author = ?, parent_post = ? WHERE id = ?;";
                break;
            case "Comment":
                SQL_UPDATE_ONE = 
                    "UPDATE Comment SET parent_post = ?, contents = ?, author = ? WHERE id = ?;";
                break;
        }

        try { // update the row
            // Convert the RowData object to an ArrayList of values
            ArrayList<Object> values = new ArrayList<>();
            if (!getRowDataValues(values, row, tableName)) {
                return -1;
            }
            // Initialize the PreparedStatement with the dynamic table name
            mUpdateOne = mConnection.prepareStatement(SQL_UPDATE_ONE);
            int startIndex = 0;
            if ("User".equals(tableName)) {
                startIndex = 1; // Skip the id for the User table
            }
            for (int i = startIndex; i < values.size(); i++) { // set the values in the PreparedStatement
                mUpdateOne.setObject(i + 1 - startIndex, values.get(i));
            }
            if ("User".equals(tableName)) {
                mUpdateOne.setString(values.size() + 1, id);
            } else {
                mUpdateOne.setInt(values.size() + 1, Integer.parseInt(id));
            }
            System.out.println("Database operation: updateOne() on table " + tableName);
            return mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally { // close the PreparedStatement
            if (mUpdateOne != null) {
                try {
                    mUpdateOne.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //////////////////////////  DELETE  //////////////////////////
    /** ps for deleting a row from table */
    private PreparedStatement mDeleteOne;

    /**
     * Delete a row by ID
     * 
     * @param tableName The name of the table to delete from
     * @param id The id of the row to delete
     * @return The number of rows that were deleted. -1 indicates an error.
     */
    int deleteRow(String tableName, String id) {
        // Assign SQL_DELETE_ONE based on the table name
        String SQL_DELETE_ONE = "";
        switch (tableName) {
            case "Post":
                // Fall through to default case
            default:
                SQL_DELETE_ONE = "DELETE FROM " + tableName + " WHERE id = ?";
                break;
            case "User":
                SQL_DELETE_ONE = "DELETE FROM \"User\" WHERE id = ?";
                break;
            case "Upvote":
                SQL_DELETE_ONE = "DELETE FROM Upvote WHERE id = ?";
                break;
            case "Downvote":
                SQL_DELETE_ONE = "DELETE FROM Downvote WHERE id = ?";
                break;
            case "Comment":
                SQL_DELETE_ONE = "DELETE FROM Comment WHERE id = ?";
                break;
        }

        try { // delete the row
            mDeleteOne = mConnection.prepareStatement(SQL_DELETE_ONE);
            if ("User".equals(tableName)) {
                mDeleteOne.setString(1, id);
            } else {
                mDeleteOne.setInt(1, Integer.parseInt(id));
            }
            System.out.println("Database operation: deleteRow() on table " + tableName);
            return mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally { // close the PreparedStatement
            if (mDeleteOne != null) {
                try {
                    mDeleteOne.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //////////////////////////  SELECT ALL  //////////////////////////
    /** ps to return all rows from table, but only the id and subject columns */
    private PreparedStatement mSelectAll;

    /**
     * Query the database for a list of all subjects and their IDs
     * 
     * @param tableName The name of the table to query
     * @return All rows, as an ArrayList; null if an error occurred
     */
    ArrayList<RowData> selectAll(String tableName) {
        // Prepare the SQL statement with the dynamic table name
        String sqlSelectAll = "";
        switch (tableName) {
            case "Post":
                // Fall through to default case
            default:
                sqlSelectAll = "SELECT * FROM " + tableName + ";";
                break;
            case "User":
                sqlSelectAll = "SELECT * FROM \"User\";";
                break;
            case "Upvote":
                sqlSelectAll = "SELECT * FROM Upvote;";
                break;
            case "Downvote":
                sqlSelectAll = "SELECT * FROM Downvote;";
                break;
            case "Comment":
                sqlSelectAll = "SELECT * FROM Comment;";
                break;
        }
        
        ArrayList<RowData> res = new ArrayList<RowData>();
        try { // select all rows
            if (mConnection == null || mConnection.isClosed()) {
                System.err.println("Database connection is not available.");
                return null;
            }
            
            // Initialize the PreparedStatement with the dynamic table name
            mSelectAll = mConnection.prepareStatement(sqlSelectAll);
    
            System.out.println("Database operation: selectAll() on table " + tableName);
            
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                RowData row = null;
                // Create a new RowData object based on the table name
                switch (tableName) {
                    case "Post":
                        // Fall through to default case
                    default:
                        row = new PostRowData(
                            rs.getInt("id"),
                            rs.getString("author"),
                            rs.getString("title"),
                            rs.getString("contents"),
                            rs.getBoolean("valid")
                        );
                        break;
                    case "User":
                        row = new UserRowData(
                            rs.getString("id"),
                            rs.getString("email"),
                            rs.getString("username"),
                            rs.getString("gender_identity"),
                            rs.getString("sexual_orientation"),
                            rs.getString("notes"),
                            rs.getBoolean("valid")
                        );
                        break;
                    case "Upvote":
                        row = new UpvoteRowData(
                            rs.getInt("id"),
                            rs.getString("author"),
                            rs.getInt("parent_post")
                        );
                        break;
                    case "Downvote":
                        row = new DownvoteRowData(
                            rs.getInt("id"),
                            rs.getString("author"),
                            rs.getInt("parent_post")
                        );
                        break;
                    case "Comment":
                        row = new CommentRowData(
                            rs.getInt("id"),
                            rs.getInt("parent_post"),
                            rs.getString("contents"),
                            rs.getString("author")
                        );
                        break;
                }
                // Add the RowData object to the ArrayList
                res.add(row);
            }
            rs.close(); // Close the result set
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally { // close the PreparedStatement
            if (mSelectAll != null) {
                try {
                    mSelectAll.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //////////////////////////  SELECT ONE  //////////////////////////
    /** ps to return row from table with matching id */
    private PreparedStatement mSelectOne;

    /**
     * Get all data for a specific row, by ID
     * 
     * @param tableName The name of the table to query
     * @param row_id The id of the row being requested
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowData selectOne(String tableName, String row_id) {
        // Prepare the SQL statement with the dynamic table name
        String selectSQL = "";
        switch (tableName) {
            case "Post":
                // Fall through to default case
            default:
                selectSQL = "SELECT * FROM " + tableName + " WHERE id = ?;";
                break;
            case "User":
                selectSQL = "SELECT * FROM \"User\" WHERE id = ?;";
                break;
            case "Upvote":
                selectSQL = "SELECT * FROM Upvote WHERE id = ?;";
                break;
            case "Downvote":
                selectSQL = "SELECT * FROM Downvote WHERE id = ?;";
                break;
            case "Comment":
                selectSQL = "SELECT * FROM Comment WHERE id = ?;";
                break;
        }

        RowData data = null;
        try { // select the row
            // Initialize the PreparedStatement with the dynamic table name
            mSelectOne = mConnection.prepareStatement(selectSQL);
            if ("User".equals(tableName)) {
                mSelectOne.setString(1, row_id);
            } else {
                mSelectOne.setInt(1, Integer.parseInt(row_id));
            }
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                // Create a new RowData object based on the table name
                switch (tableName) {
                    case "Post":
                        // Fall through to default case
                    default:
                        data = new PostRowData(
                            rs.getInt("id"),
                            rs.getString("author"),
                            rs.getString("title"),
                            rs.getString("contents"),
                            rs.getBoolean("valid")
                        );
                        break;
                    case "User":
                        data = new UserRowData(
                            rs.getString("id"),
                            rs.getString("email"),
                            rs.getString("username"),
                            rs.getString("gender_identity"),
                            rs.getString("sexual_orientation"),
                            rs.getString("notes"),
                            rs.getBoolean("valid")
                        );
                        break;
                    case "Upvote":
                        data = new UpvoteRowData(
                            rs.getInt("id"),
                            rs.getString("author"),
                            rs.getInt("parent_post")
                        );
                        break;
                    case "Downvote":
                        data = new DownvoteRowData(
                            rs.getInt("id"),
                            rs.getString("author"),
                            rs.getInt("parent_post")
                        );
                        break;
                    case "Comment":
                        data = new CommentRowData(
                            rs.getInt("id"),
                            rs.getInt("parent_post"),
                            rs.getString("contents"),
                            rs.getString("author")
                        );
                        break;
                }
            }
            rs.close();  // remember to close the result set
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally { // close the PreparedStatement
            if (mSelectOne != null) {
                try {
                    mSelectOne.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //////////////////////////  INVALIDATE  //////////////////////////
    /** ps to invalidate a row in table */
    private PreparedStatement mInvalidateOne;

    /**
     * Invalidate a row by ID
     * 
     * @param tableName The name of the table to invalidate from
     * @param id The id of the row to invalidate
     * @return The number of rows that were invalidated. -1 indicates an error.
     */
    int invalidateRow(String tableName, String id) {
        // Assign SQL_INVALIDATE_ONE based on the table name
        String SQL_INVALIDATE_ONE = "";
        switch (tableName) {
            case "Post":
                // Fall through to default case
            default:
                SQL_INVALIDATE_ONE = "UPDATE " + tableName + " SET valid = FALSE WHERE id = ?";
                break;
            case "User":
                SQL_INVALIDATE_ONE = "UPDATE \"User\" SET valid = FALSE WHERE id = ?";
                break;
        }

        try { // invalidate the row
            mInvalidateOne = mConnection.prepareStatement(SQL_INVALIDATE_ONE);
            if ("User".equals(tableName)) {
                mInvalidateOne.setString(1, id);
            } else {
                mInvalidateOne.setInt(1, Integer.parseInt(id));
            }
            System.out.println("Database operation: invalidateRow() on table " + tableName);
            return mInvalidateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally { // close the PreparedStatement
            if (mInvalidateOne != null) {
                try {
                    mInvalidateOne.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //////////////////////////  VALIDATE  //////////////////////////
    /** ps to validate a row in table */
    private PreparedStatement mValidateOne;

    /**
     * Validate a row by ID
     * 
     * @param tableName The name of the table to validate from
     * @param id The id of the row to validate
     * @return The number of rows that were validated. -1 indicates an error.
     */
    int validateRow(String tableName, String id) {
        // Assign SQL_VALIDATE_ONE based on the table name
        String SQL_VALIDATE_ONE = "";
        switch (tableName) {
            case "Post":
                // Fall through to default case
            default:
                SQL_VALIDATE_ONE = "UPDATE " + tableName + " SET valid = TRUE WHERE id = ?";
                break;
            case "User":
                SQL_VALIDATE_ONE = "UPDATE \"User\" SET valid = TRUE WHERE id = ?";
                break;
        }

        try { // validate the row
            mValidateOne = mConnection.prepareStatement(SQL_VALIDATE_ONE);
            if ("User".equals(tableName)) {
                mValidateOne.setString(1, id);
            } else {
                mValidateOne.setInt(1, Integer.parseInt(id));
            }
            System.out.println("Database operation: validateRow() on table " + tableName);
            return mValidateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally { // close the PreparedStatement
            if (mValidateOne != null) {
                try {
                    mValidateOne.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //////////////////////////  CONNECT & DISCONNECT  //////////////////////////

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


    /**
     * The Database constructor is private: we only create Database objects 
     * through one or more static getDatabase() methods.
     */
    private Database() {
    }

    /**
     * Uses dbUri to create a connection to a database, and stores it in the returned Database object
     * @param dbUri the connection string for the database
     * @return null upon connection failure, otherwise a valid Database with open connection
     */
    static Database getDatabase( String dbUri ){
        // Connect to the database or fail
        if( dbUri != null && dbUri.length() > 0 ){ 
            // DATABASE_URI appears to be set
            Database returned_val = new Database();            
            System.out.println( "Attempting to use provided DATABASE_URI env var." );
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
            // POSTGRES_* variables appear to be set
            Database returned_val = new Database();
            System.out.println( "Attempting to use provided POSTGRES_{IP, PORT, USER, PASS, DBNAME} env var." );
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

    /**
     * Uses the presence of environment variables to invoke the correct 
     * overloaded `getDatabase` method. Either DATABASE_URL should be set,
     * or the values of four other variables POSTGRES_{IP, PORT, USER, PASS, DBNAME}.
     * 
     * @return a valid Database object with active connection on success, null otherwise
     */
    static Database getDatabase(){
        // get the Postgres configuration from environment variables; 
        // you could name them almost anything
        Map<String, String> env = System.getenv();
        String ip     = env.get("POSTGRES_IP");
        String port   = env.get("POSTGRES_PORT");
        String user   = env.get("POSTGRES_USER");
        String pass   = env.get("POSTGRES_PASS");
        String dbname = env.get("POSTGRES_DBNAME");
        String dbUri  = env.get("DATABASE_URI");

        System.out.printf("Using the following environment variables:%n%s%n", "-".repeat(45)); 
        System.out.printf("POSTGRES_IP=%s%n", ip);
        System.out.printf("POSTGRES_PORT=%s%n", port);
        System.out.printf("POSTGRES_USER=%s%n", user);
        System.out.printf("POSTGRES_PASS=%s%n", "omitted");
        System.out.printf("POSTGRES_DBNAME=%s%n", dbname );
        System.out.printf("DATABASE_URI=%s%n", dbUri );

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
     * Method to populate the database with some initial data
     */
    void populate() {
        // Create the tables if they don't exist already
        createTable("User");
        createTable("Post");
        createTable("Upvote");
        createTable("Downvote");
        createTable("Comment");

        // Add to the User table (id, email, username, g_id, s_o, notes)
        insertRow("User", new UserRowData("1", "user1@example.com", "user1", "", "", "example note1", true));
        insertRow("User", new UserRowData("2", "user2@example.com", "user2", "", "", "example note2", true));
        insertRow("User", new UserRowData("3", "user3@example.com", "user3", "", "", "example note3", true));

        // Add to the Post table (id, author, title, contents)
        insertRow("Post", new PostRowData(-1, "1", "Post 1", "This is post 1", true));
        insertRow("Post", new PostRowData(-1, "2", "Post 2", "This is post 2", true));
        insertRow("Post", new PostRowData(-1, "3", "Post 3", "This is post 3", true));

        // Add to the Upvote table (id, author, parent_post)
        insertRow("Upvote", new UpvoteRowData(-1, "1", 1));
        insertRow("Upvote", new UpvoteRowData(-1, "1", 2));
        insertRow("Upvote", new UpvoteRowData(-1, "2", 2));
        insertRow("Upvote", new UpvoteRowData(-1, "2", 3));

        // Add to the Downvote table (id, author, parent_post)
        insertRow("Downvote", new DownvoteRowData(-1, "1", 3));
        insertRow("Downvote", new DownvoteRowData(-1, "3", 1));
        insertRow("Downvote", new DownvoteRowData(-1, "3", 3));

        // Add to the Comment table (id, parent_post, contents, author)
        insertRow("Comment", new CommentRowData(-1, 1, "Comment 1", "1"));
        insertRow("Comment", new CommentRowData(-1, 1, "Comment 2", "2"));
        insertRow("Comment", new CommentRowData(-1, 3, "Comment 3", "2"));
        insertRow("Comment", new CommentRowData(-1, 2, "Comment 4", "3"));
    }
}