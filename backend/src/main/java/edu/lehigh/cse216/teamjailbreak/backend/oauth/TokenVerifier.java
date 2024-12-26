package edu.lehigh.cse216.teamjailbreak.backend.oauth;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import edu.lehigh.cse216.teamjailbreak.backend.UserData;

import com.google.api.client.http.javanet.NetHttpTransport;

import java.util.Collections;
import java.util.Map;

public class TokenVerifier {
    private static final Map<String, String> env = System.getenv();
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String CLIENT_ID = env.get("OAUTH_CLIENT_ID");
    
    public static UserData verifyToken(String idTokenString) {
        
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JSON_FACTORY)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Get user information from the payload
                String userId = payload.getSubject();
                String email = payload.getEmail();
                boolean emailVerified = payload.getEmailVerified();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");

                System.out.println("User ID: " + userId);
                System.out.println("Email: " + email);
                System.out.println("Email Verified: " + emailVerified);
                System.out.println("Name: " + name);
                System.out.println("Picture URL: " + pictureUrl);

                return new UserData(userId, email, emailVerified, name, pictureUrl);
            } else {
                System.out.println("Invalid ID token.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
                return null;
    }
}

  
