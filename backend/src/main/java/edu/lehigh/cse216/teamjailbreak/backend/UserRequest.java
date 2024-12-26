package edu.lehigh.cse216.teamjailbreak.backend;

public record UserRequest( String email, String username, String gender_identity, String sexual_orientation, String notes, boolean valid) {}
