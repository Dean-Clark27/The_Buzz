package edu.lehigh.cse216.adr325.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.google.api.services.drive.Drive;

import edu.lehigh.cse216.adr325.admin.GoogleDriveService;
/**
 * App is our basic admin app.  For now, all it does is connect to the database
 * and then disconnect.
 */
public class App {
    /**
     * Default constructor for App
     */
    public App() {}

    /**
     * The list of valid table names
     */
    static final String[] TABLE_NAMES = {"User", "Post", "Upvote", "Downvote", "Comment"};

    /**
     * The main method
     * 
     * @param argv Command-line arguments
     */
    public static void main(String[] argv) {
        //simpleManualTests("Post");
        //mainCliLoop();
        
    }

    /**
     * Reads arguments from the environment and then uses those
     * arguments to connect to the database. Either DATABASE_URL should be set,
     * or the values of four other variables POSTGRES_{IP, PORT, USER, PASS, DBNAME}.
     * 
     * @param tableName the name of the table to be used in database operations.
     */
    public static void simpleManualTests(String tableName) {
        /* Holds connection to the database created from environment variables */
        Database db = Database.getDatabase();

        db.dropTable(tableName);  // Drop the table if it exists
        db.createTable(tableName);  // Create the table with the provided name
        db.insertRow(tableName, new Database.PostRowData(1, "1", "test title", "test post", true));  // Insert a row in the specific table
        db.updateOne(tableName, "1", new Database.PostRowData(1, "1", "test title", "test post", true));  // Update a row in the specific table

        ArrayList<Database.RowData> list_rd = db.selectAll(tableName);  // Select all rows
        System.out.println("Row data:");
        for (Database.RowData rd : list_rd) {
            System.out.println(">\t" + rd);
        }

        Database.RowData single_rd = db.selectOne(tableName, "1");  // Select a single row
        System.out.println("Single row: " + single_rd);

        db.deleteRow(tableName, "1");  // Delete the row in the specific table

        if (db != null)
            db.disconnect();
    }

    /**
     * Entry point for our admin command-line interface program.
     * 
     * Runs a loop that gets a request from the user and processes it
     * using our Database class.
     */
    public static void mainCliLoop() {
        // Get a fully-configured connection to the database, or exit immediately
        Database db = Database.getDatabase();
        if (db == null) {
            System.err.println("Unable to make database object, exiting.");
            System.exit(1);
        }

        // Create the tables if they don't exist (in the correct order)
        for (String tableName : TABLE_NAMES) {
            db.createTable(tableName);
        }

        // Start our basic command-line interpreter:
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Get the user's request, and do it
            //
            // NB: for better testability, each action should be a separate
            //     function call
            char action = prompt(in);
            if (action == '?') { // ---------- Print the menu ----------
                menu();
            } else if (action == 'q') { // ---------- Quit the program ----------
                break;
            } else if (action == 'T') { // ---------- Create a table ----------
                // Get the table name from the user
                String tableName = getString(in, "Enter the table name");
                // Check if the table name is in TABLE_NAMES
                if (!validTableName(tableName)) {
                    continue;
                }
                // Create the table using the provided name
                db.createTable(tableName); 
            } else if (action == 'd') { // ---------- Drop a table ----------
                // Get the table name from the user
                String tableName = getString(in, "Enter the table name");
                // Check if the table name is in TABLE_NAMES
                if (!validTableName(tableName)) {
                    continue;
                }
                // Drop the table using the provided name
                db.dropTable(tableName);
            } else if (action == 'D') { // ---------- Drop all tables ----------
                // Drop all tables in reverse order of creation
                for (int i = TABLE_NAMES.length - 1; i >= 0; i--) {
                    db.dropTable(TABLE_NAMES[i]);
                }
            } else if (action == '1') { // ---------- Query for a specific row ----------
                // Get the table name from the user
                String tableName = getString(in, "Enter the table name");
                // Check if the table name is in TABLE_NAMES
                if (!validTableName(tableName)) {
                    continue;
                }
                // Get the row ID from the user
                String id = getString(in, "Enter the row ID");
                if (id.isEmpty()) {
                    continue;
                }
                // Select the row using the provided table name and row ID
                Database.RowData res = db.selectOne(tableName, id);
                printRowData(res);
            } else if (action == '*') { // ---------- Query for all rows ----------
                // Get the table name from the user
                String tableName = getString(in, "Enter the table name");
                // Check if the table name is in TABLE_NAMES
                if (!validTableName(tableName)) {
                    continue;
                }
                // Select all rows using the provided table name
                ArrayList<Database.RowData> res = db.selectAll(tableName);
                if (res == null) {
                    continue;
                }
                System.out.println("  Current Database Contents");
                System.out.println("  -------------------------");
                for (Database.RowData rd : res) {
                    printRowData(rd);
                }
            } else if (action == '-') { // ---------- Delete a row ----------
                // Get the table name from the user
                String tableName = getString(in, "Enter the table name");
                // Check if the table name is in TABLE_NAMES
                if (!validTableName(tableName)) {
                    continue;
                }
                // Get the row ID from the user
                String id = getString(in, "Enter the row ID");
                if (id.isEmpty()) {
                    continue;
                }
                // Delete the row
                int res = db.deleteRow(tableName, id);
                if (res == -1) {
                    continue;
                }
                System.out.println("  " + res + " rows deleted");
            } else if (action == '+') { // ---------- Insert a new row ----------
                // Get the table name from the user
                String tableName = getString(in, "Enter the table name");
                // Check if the table name is in TABLE_NAMES
                if (!validTableName(tableName)) {
                    continue;
                }
                // Prompt the user for different fields based on the table name
                Database.RowData rd = getRowInfo(in, tableName);
                // Insert the row
                int res = db.insertRow(tableName, rd);
                System.out.println(res + " rows added");
            } else if (action == '~') { // ---------- Update a row ----------
                // Get the table name from the user
                String tableName = getString(in, "Enter the table name");
                // Check if the table name is in TABLE_NAMES
                if (!validTableName(tableName)) {
                    continue;
                }
                // Get the row ID from the user
                String id = getString(in, "Enter the row ID");
                if (id.isEmpty()) {
                    continue;
                }
                // Prompt the user for different fields based on the table name
                Database.RowData rd = getRowInfo(in, tableName);
                // Update the row
                int res = db.updateOne(tableName, id, rd);
                if (res == -1) {
                    continue;
                }
                System.out.println("  " + res + " rows updated");
            } else if (action == 'P') { // ---------- Populate the database with test data ----------
                // Populate the database with test data
                db.populate();
            } else if (action == 'i') { // ---------- Invalidate a Post or User ----------
                // Get the table name from the user
                String tableName = getString(in, "Enter the table name");
                // Check if the table name is "Post" or "User"
                if (!tableName.equals("Post") && !tableName.equals("User")) {
                    System.out.println("Invalid table name, try: Post, User");
                    continue;
                }
                // Get the row ID from the user
                String id = getString(in, "Enter the row ID");
                if (id.isEmpty()) {
                    continue;
                }
                // Invalidate the row
                int res = db.invalidateRow(tableName, id);
                if (res == -1) {
                    continue;
                }
                System.out.println("  " + res + " rows invalidated");
            } else if (action == 'v') { // ---------- Validate a Post or User ----------
                // Get the table name from the user
                String tableName = getString(in, "Enter the table name");
                // Check if the table name is "Post" or "User"
                if (!tableName.equals("Post") && !tableName.equals("User")) {
                    System.out.println("Invalid table name, try: Post, User");
                    continue;
                }
                // Get the row ID from the user
                String id = getString(in, "Enter the row ID");
                if (id.isEmpty()) {
                    continue;
                }
                // Validate the row
                int res = db.validateRow(tableName, id);
                if (res == -1) {
                    continue;
                }
                System.out.println("  " + res + " rows validated");
            } else {
                System.out.println("Invalid Command");
            }
        }
        // Always remember to disconnect from the database when the program exits
        db.disconnect();
    }

    /**
     * Print the menu for our program.
     */
    static void menu() {
        System.out.println("Main Menu");
        System.out.println("  [T] Create table");
        System.out.println("  [d] Drop table");
        System.out.println("  [D] Drop all tables");
        System.out.println("  [1] Query for a specific row");
        System.out.println("  [*] Query for all rows");
        System.out.println("  [-] Delete a row");
        System.out.println("  [+] Insert a new row");
        System.out.println("  [~] Update a row");
        System.out.println("  [P] Populate the database with test data");
        System.out.println("  [i] Invalidate a Post or User");
        System.out.println("  [v] Validate a Post or User");
        System.out.println("  [q] Quit Program");
        System.out.println("  [?] Help (this message)");
    }

    /**
     * Ask the user to enter a menu option; repeat until we get a valid option.
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * 
     * @return The character corresponding to the chosen menu option
     */
    static char prompt(BufferedReader in) {
        // The valid actions:
        String actions = "TdD1*-+~Pivq?";

        // We repeat until a valid single-character option is selected        
        while (true) {
            System.out.print("[" + actions + "] :> ");
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1) {
                continue;
            }
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    /**
     * Ask the user to enter a String message.
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The string that the user provided.  May be "".
     */
    static String getString(BufferedReader in, String message) {
        String s;
        try {
            System.out.print(message + " :> ");
            s = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return s;
    }

    /**
     * Ask the user to enter an integer.
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The integer that the user provided.  On error, it will be -1
     */
    static int getInt(BufferedReader in, String message) {
        int i = -1;
        try {
            System.out.print(message + " :> ");
            i = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * Check if the table name is valid.
     * 
     * @param tableName The table name to check
     * 
     * @return true if the table name is valid, false otherwise
     */
    static boolean validTableName(String tableName) {
        // Loop through the table names
        for (String name : TABLE_NAMES) {
            if (tableName.equals(name)) {
                return true;
            }
        }
        System.out.println("Invalid table name, try: " + String.join(", ", TABLE_NAMES));
        return false;
    }

    /**
     * Check the type of row data and print appropriately
     * PostRowData: id, author, title, contents, valid
     * UserRowData: id, email, username, gender_identity, sexual_orientation, notes, valid
     * UpvoteRowData: id, author, parent_post
     * DownvoteRowData: id, author, parent_post
     * CommentRowData: id, parent_post, contents, author
     * 
     * @param rd The row data to print
     */
    static void printRowData(Database.RowData rd) {
        if (rd == null) {
            return;
        }
        // Check and print the rest of the data
        if (rd instanceof Database.PostRowData) {
            Database.PostRowData mrd = (Database.PostRowData) rd;
            System.out.println("  [" + mrd.id() + "] ");
            System.out.println(mrd.title());
            System.out.println("  --> " + mrd.contents());
            System.out.println("  --> Valid : " + mrd.valid());
        } else if (rd instanceof Database.UserRowData) {
            Database.UserRowData urd = (Database.UserRowData) rd;
            System.out.println("  [" + urd.id() + "] ");
            System.out.println(urd.email() + " : " + urd.username());
            System.out.println("  --> " + urd.gender_identity());
            System.out.println("  --> " + urd.sexual_orientation());
            System.out.println("  --> " + urd.notes());
            System.out.println("  --> Valid : " + urd.valid());
        } else if (rd instanceof Database.UpvoteRowData) {
            Database.UpvoteRowData urd = (Database.UpvoteRowData) rd;
            System.out.println("  [" + urd.id() + "] ");
            System.out.println("Author - " + urd.author() + " : Post - " + urd.parent_post());
        } else if (rd instanceof Database.DownvoteRowData) {
            Database.DownvoteRowData drd = (Database.DownvoteRowData) rd;
            System.out.println("  [" + drd.id() + "] ");
            System.out.println("Author - " + drd.author() + " : Post - " + drd.parent_post());
        } else if (rd instanceof Database.CommentRowData) {
            Database.CommentRowData crd = (Database.CommentRowData) rd;
            System.out.println("  [" + crd.id() + "] ");
            System.out.println(crd.contents());
        }
    }

    /**
     * Get the row data based on the table name.
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param tableName The table name to get the row data for
     * @return The row data for the table
     */
    private static Database.RowData getRowInfo(BufferedReader in, String tableName) {
        Database.RowData rd = null;
        if (tableName.equals("Post")) {
            String author = getString(in, "Enter the author ID");
            String title = getString(in, "Enter the title");
            String contents = getString(in, "Enter the contents");
            rd = new Database.PostRowData(-1, author, title, contents, true);
        } else if (tableName.equals("User")) {
            String id = getString(in, "Enter the ID");
            String email = getString(in, "Enter the email");
            String username = getString(in, "Enter the username");
            String notes = getString(in, "Enter the notes");
            rd = new Database.UserRowData(id, email, username, "", "", notes, true);
        } else if (tableName.equals("Upvote")) {
            String author = getString(in, "Enter the author ID");
            int parent_post = getInt(in, "Enter the post ID");
            rd = new Database.UpvoteRowData(-1, author, parent_post);
        } else if (tableName.equals("Downvote")) {
            String author = getString(in, "Enter the author ID");
            int parent_post = getInt(in, "Enter the post ID");
            rd = new Database.DownvoteRowData(-1, author, parent_post);
        } else if (tableName.equals("Comment")) {
            int parent_post = getInt(in, "Enter the post ID");
            String contents = getString(in, "Enter the contents");
            String author = getString(in, "Enter the author ID");
            rd = new Database.CommentRowData(-1, parent_post, contents, author);
        } else {
            System.out.println("Unknown error");
        }
        return rd;
    }
}
