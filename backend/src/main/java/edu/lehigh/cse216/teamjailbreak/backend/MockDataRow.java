package edu.lehigh.cse216.teamjailbreak.backend;

import java.util.Date;

/**
 * MockDataRow holds a row of information.  A row of information consists of
 * an identifier, strings for a "title" and "contents", and a creation date.
 * Being a java record, all fields are final (cannot be changed)
 * 
 * We will ultimately be converting instances of this object into JSONdirectly, 
 * so it is convenient that all record fields are public. This is ok because
 * all fields are immutable. 
 */
public record MockDataRow( int id, String title, String contents, boolean is_liked, Date created ){

    /**
     * We override the default constructor to set a null created to the current time
     * @param id The unique identifier associated with this element. Can be null
     * @param title The title for this row of data. Can be null
     * @param contents The contents for this row of data. Can be null
     * @param is_liked The status of liking a message for each post. Can only be liked (true) or not (false).
     * @param created The creation date for this row of data; if null, sets it to current system clock time
     */
    public MockDataRow {
        if(created == null)
            created = new Date();
    }

    /**
     * For convenience, because of the immutability of records, we also provide a 
     * Builder that lets you incrementally initialize the object, and then build() it
     * when ready to have an instance of MockDataRow.
     * @return instance on which all public fields are set; build() it when done.
     */
    public Builder builder(){return new Builder( this );}

    /** because record fields are final, we have a builder */
    public static class Builder{
        public int id;
        public String title;
        public String contents;
        public boolean is_liked;
        public Date created;

        private Builder(){}
        private Builder( MockDataRow mdr ){
            this.id = mdr.id;
            this.title = mdr.title;
            this.contents = mdr.contents;
            this.is_liked = mdr.is_liked;
            this.created = mdr.created;
        }
        
        /** returns a new MockDataRow instance with currently set values of builder */
        public MockDataRow build(){
            return new MockDataRow(id, title, contents, is_liked, created);
        }
    }
}
