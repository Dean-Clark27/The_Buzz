package edu.lehigh.cse216.teamjailbreak.backend;

/**
 * MockDataRowLite is for communicating back a subset of the information in a
 * MockDataRow.  Specifically, we only send back the id and title. Note that
 * in order to keep the client code as consistent as possible, we ensure 
 * that the field names in MockDataRowLite match the corresponding names in
 * MockDataRow. As with MockDataRow, we plan to convert MockDataRowLite objects to
 * JSON, so we need to make their fields public.
 * 
 * @param mId see MockDataRow.mId
 * @param mTitle see DataRow.mTitle
 */
public record MockDataRowLite( int idd, String title, String contents, boolean is_liked ) { 
    /**
     * Create a DataRowLite by copying fields from a DataRow
     * @param data The MockDataRow on which to base the lite version (without date and content)
     */
    public MockDataRowLite(MockDataRow data) {
        this( data.id(), data.title(), data.contents(), data.is_liked() );
    }
}