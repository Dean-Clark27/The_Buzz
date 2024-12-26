package edu.lehigh.cse216.adr325.admin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.UUID;

import javax.xml.crypto.Data;

/**
 * Unit test for App and Database functionality.
 * 
 * This class contains several unit tests that check the functionality of the 
 * database operations, including creating tables, inserting rows, selecting rows,
 * and updating rows.
 */
public class AppTest extends TestCase {

    /** Holds the dynamic table name for testing */
    private String tableName;

    /**
     * Constructor for AppTest
     *
     * This constructor calls the super class TestCase's constructor with the name 
     * of the test case. It allows us to identify the name of each test case.
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Set up the test environment.
     * 
     * This method is called before every test and ensures that the database is clean
     * by dropping the table if it exists and recreating it. A unique table name is
     * generated for each test using a UUID.
     */
    public void setUp() {
        tableName = "tblData_" + UUID.randomUUID().toString().replace("-", "");
        Database db = Database.getDatabase();
        assertNotNull("Database connection should be established", db);
        db.dropTable(tableName);  // Drop the table if it exists
        db.createTable(tableName); // Re-create the table with a new name
        db.disconnect();
    }

    /**
     * Test for successful dropping of a table.
     * 
     * This test ensures that the 'dropTable()' method successfully drops the table
     */
    public void testCreateTable() {
        System.out.println("------------------------ testCreateTable ------------------------");

        Database db = Database.getDatabase();
        assertNotNull("Database connection should be established", db);

        // Try to create the table and check for exceptions
        try {
            db.dropTable(tableName);
        } catch (Exception e) {
            fail("Table creation failed with exception: " + e.getMessage());
        }

        db.disconnect();
    }

    /**
     * Test for inserting a row and selecting it from the database.
     * 
     * This test inserts a row into the dynamically created table, then retrieves the inserted row 
     * using the `selectOne()` method, and verifies that the data matches what was inserted.
     */
    public void testInsertAndSelectRow() {
        System.out.println("------------------------ testInsertAndSelectRow ------------------------");

        Database db = Database.getDatabase();
        assertNotNull("Database connection should be established", db);

        // Insert a row into the table
        Database.RowData row = new Database.PostRowData(1, "1", "Test Title", "Test Contents", true);
        int rowsInserted = db.insertRow(tableName, row);
        assertEquals("One row should be inserted", 1, rowsInserted);

        // Select the inserted row and verify its content
        Database.RowData returned_row = db.selectOne(tableName, "1");
        Database.PostRowData post_row = (Database.PostRowData) returned_row;
        assertNotNull("Inserted row should not be null", returned_row);
        assertEquals("The title should match the inserted value", "Test Title", post_row.title());
        assertEquals("The contents should match the inserted value", "Test Contents", post_row.contents());

        // Close
        db.dropTable(tableName);
        db.disconnect();
    }

    /**
     * Test for updating a row in the database.
     * 
     * This test first inserts a row, then updates its contents, and finally verifies
     * that the update was successful by checking the new post content.
     */
    public void testUpdateRow() {
        System.out.println("------------------------ testUpdateRow ------------------------");

        Database db = Database.getDatabase();
        assertNotNull("Database connection should be established", db);

        // Insert a row into the table to be updated later
        Database.RowData row = new Database.PostRowData(1, "1", "Test Title", "Test Contents", true);
        db.insertRow(tableName, row);

        // Update the row's contents
        Database.RowData updatedRow = new Database.PostRowData(1, "1", "Updated Title", "Updated Contents", true);
        int rowsUpdated = db.updateOne(tableName, "1", updatedRow);
        assertEquals("One row should be updated", 1, rowsUpdated);

        // Verify the row was updated correctly
        Database.RowData returned_row = db.selectOne(tableName, "1");
        Database.PostRowData post_row = (Database.PostRowData) returned_row;
        assertNotNull("Updated row should not be null", row);
        assertEquals("The title should be updated", "Updated Title", post_row.title());
        assertEquals("The contents should be updated", "Updated Contents", post_row.contents());

        // Close
        db.dropTable(tableName);
        db.disconnect();
    }

    /**
     * Test for deleting a row in the database.
     * 
     * This test inserts a row and then deletes it, verifying that the row is successfully removed.
     */
    public void testDeleteRow() {
        System.out.println("------------------------ testDeleteRow ------------------------");

        Database db = Database.getDatabase();
        assertNotNull("Database connection should be established", db);

        // Insert a row into the table to be deleted later
        Database.RowData row = new Database.PostRowData(1, "1", "Test Title", "Test Contents", true);
        db.insertRow(tableName, row);

        // Delete the row
        int rowsDeleted = db.deleteRow(tableName, "1");
        assertEquals("One row should be deleted", 1, rowsDeleted);

        // Verify that the row is deleted
        Database.RowData returned_row = db.selectOne(tableName, "1");
        assertNull("Deleted row should be null", returned_row);

        // Close
        db.dropTable(tableName);
        db.disconnect();
    }

    /**
     * Test for selecting all rows from the database.
     * 
     * This test inserts multiple rows into the table and then retrieves all rows using the `selectAll()`
     * method. It verifies that the number of rows returned matches the number of rows inserted.
     */
    public void testSelectAllRows() {
        System.out.println("------------------------ testSelectAllRows ------------------------");

        Database db = Database.getDatabase();
        assertNotNull("Database connection should be established", db);

        // Insert multiple rows into the table
        Database.RowData row1 = new Database.PostRowData(1, "1", "Test Title 1", "Test Contents 1", true);
        Database.RowData row2 = new Database.PostRowData(2, "2", "Test Title 2", "Test Contents 2", true);
        db.insertRow(tableName, row1);
        db.insertRow(tableName, row2);

        // Select all rows from the table
        ArrayList<Database.RowData> rows = db.selectAll(tableName);
        assertEquals("Two rows should be returned", 2, rows.size());

        // Close
        db.dropTable(tableName);
        db.disconnect();
    }

    /**
     * Test for invalidating a row in the database.
     * 
     * This test inserts a row and then invalidates it, verifying that the row is successfully invalidated.
     */
    public void testInvalidateRow() {
        System.out.println("------------------------ testInvalidateRow ------------------------");

        Database db = Database.getDatabase();
        assertNotNull("Database connection should be established", db);

        // Insert a row into the table to be invalidated later
        Database.RowData row = new Database.PostRowData(1, "1", "Test Title", "Test Contents", true);
        db.insertRow(tableName, row);

        // Invalidate the row
        int rowsInvalidated = db.invalidateRow(tableName, "1");
        assertEquals("One row should be invalidated", 1, rowsInvalidated);

        // Verify that the row is invalidated
        Database.RowData returned_row = db.selectOne(tableName, "1");
        Database.PostRowData post_row = (Database.PostRowData) returned_row;
        assertNotNull("Invalidated row should not be null", post_row);
        assertFalse("The row should be invalidated", post_row.valid());

        // Close
        db.dropTable(tableName);
        db.disconnect();
    }


    /* Validation test */
    /*Post is invalid, run validation procedure, verify valid */

    /*  */
}