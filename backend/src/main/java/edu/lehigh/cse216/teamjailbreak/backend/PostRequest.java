package edu.lehigh.cse216.teamjailbreak.backend;

/**
 * SimpleRequest provides a format for clients to present title and message 
 * strings to the server.
 * @param title The title being provided by the client.
 * @param contents The message being provided by the client.
 * @param is_liked The message is liked by an anonymous user or not
 */
public record PostRequest( String title, int author, String contents, boolean valid) {}
