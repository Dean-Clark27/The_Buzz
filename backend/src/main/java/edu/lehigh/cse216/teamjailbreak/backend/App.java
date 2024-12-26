package edu.lehigh.cse216.teamjailbreak.backend;


// Javalin package for creating HTTP GET, PUT, POST, etc routes
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Import Google's JSON library
import com.google.gson.*;

import edu.lehigh.cse216.teamjailbreak.backend.oauth.TokenVerifier;


/**
 * A Javalin-based HTTP server. There are different mains that demonstrate different functionality.
 */
public class App
{
    /** The default port our webserver uses. We set it to Javalin's default, 8080 */
    public static final int DEFAULT_PORT_WEBSERVER = 8080;
    public static final HashMap<String, UserData> session = new HashMap<>();
    /**
    * Safely gets integer value from named env var if it exists, otherwise returns default
    *
    * @envar      The name of the environment variable to get.
    * @defaultVal The integer value to use as the default if envar isn't found
    *
    * @returns The best answer we could come up with for a value for envar
    */
    static int getIntFromEnv(String envar, int defaultVal) {
        String envValue = System.getenv(envar);
        if (envValue == null || envValue.trim().isEmpty()) {
            System.out.printf("Environment variable %s not set, using default value %d%n", envar, defaultVal);
            return defaultVal;
        }
        try {
            return Integer.parseInt(envValue.trim());
        } catch (NumberFormatException e) {
            System.err.printf("ERROR: Could not parse %s value '%s' as integer, using default of %d%n", envar, envValue, defaultVal);
            return defaultVal;
        }
    }
       
    /** Not particularly elegant, but we can activate different mains by commenting/uncommenting */
    public static void main( String[] args ){
        //main_helloworld(args);
        //main_inMemory_datastore(args);
        main_uses_database(args);
    }


    /**
     * A simple webserver that uses an IN-MEMORY datastore.
     * Uses default port; customizes the logger.
     *
     * Supports GET to /posts to retrieve all posts (without their body)
     * Supports POST to /posts to create a new post
     * Supports {GET, PUT, DELETE} to /posts/{id} to do associated action on post with given id
     */
    public static void main_inMemory_datastore( String[] args ){
    // our javalin app on which most operations must be performed
    // our javalin app on which most operations must be performed
    Javalin app = Javalin
    .create(
        config -> {
            config.requestLogger.http(
                (ctx, ms) -> {
                    System.out.printf( "%s%n", "=".repeat(42) );
                    System.out.printf( "%s\t%s\t%s%nfull url: %s%n", ctx.scheme(), ctx.method().name(), ctx.path(), ctx.fullUrl() );                                
                }
            );
            // set up static file serving. See: https://javalin.io/documentation#staticfileconfig
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";                   // change to host files on a subpath, like '/assets'
                String static_location_override = System.getenv("STATIC_LOCATION");
                if (static_location_override == null) { // serve from jar; files located in src/main/resources/public
                    staticFiles.directory = "/public";                  // the directory where your files are located
                    staticFiles.location = Location.CLASSPATH;          // Location.CLASSPATH (jar)
                } else { // serve from filesystem
                    System.out.println( "Overriding location of static file serving using STATIC_LOCATION env var: " + static_location_override );
                    staticFiles.directory = static_location_override;   // the directory where your files are located
                    staticFiles.location = Location.EXTERNAL;           // Location.EXTERNAL (file system)
                }
                staticFiles.precompress = false;                   // if the files should be pre-compressed and cached in memory (optimization)
                // staticFiles.aliasCheck = null;                  // you can configure this to enable symlinks (= ContextHandler.ApproveAliases())
                // staticFiles.headers = Map.of(...);              // headers that will be set for the files
                // staticFiles.skipFileFunction = req -> false;    // you can use this to skip certain files in the dir, based on the HttpServletRequest
                // staticFiles.mimeTypes.add(mimeType, ext);       // you can add custom mimetypes for extensions
            });
        }
    );    


    /* if ("True".equalsIgnoreCase(System.getenv("CORS_ENABLED"))) {
        final String acceptCrossOriginRequestsFrom = "*";
        final String acceptedCrossOriginRoutes = "GET,PUT,POST,DELETE,OPTIONS";
        final String supportedRequestHeaders = "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin";
        enableCORS(app, acceptCrossOriginRequestsFrom, acceptedCrossOriginRoutes, supportedRequestHeaders);
    }
    */ 

        // gson provides us a way to turn JSON into objects, and objects into JSON.
        //
        // NB: it must be final, so that it can be accessed from our lambdas
        //
        // NB: Gson is thread-safe.  See
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();


        // dataStore holds all of the data that has been provided via HTTP requests
        //
        // NB: every time we shut down the server, we will lose all data, and
        //     every time we start the server, we'll have an empty dataStore,
        //     with IDs starting over from 0.
        final MockDataStore dataStore = new MockDataStore();
        
        app.get("/", ctx -> {
            StructuredResponse resp = new StructuredResponse("ok", null, "Server is up and running. Hi!:)");
            ctx.result(gson.toJson(resp));
        });

        // POST route that returns user data upon successful token validation. 
        app.post("/auth/validate/{idToken}", ctx -> {
            String idTokeString = ctx.pathParam("idToken");

            ctx.status(200);
            ctx.contentType("application/json");

            Map<String, Object> responseData = new HashMap<>();

            UserData data = TokenVerifier.verifyToken(idTokeString);
            responseData.put("uId", data.getUserId());
            responseData.put("email", data.getEmail());
            responseData.put("email_verified", data.isEmailVerified());
            responseData.put("name", data.getName());
            responseData.put("picture_url", data.getPictureUrl());
            System.out.print(responseData);
            StructuredResponse resp = new StructuredResponse("ok", null, data);
            ctx.result(gson.toJson(resp));
        });
        


        // GET route that returns all post titles and Ids.  All we do is get
        // the data, embed it in a StructuredResponse, turn it into JSON, and
        // return it.  If there's no data, we return "[]", so there's no need
        // for error handling.
        app.get( "/posts", ctx -> {
            ctx.status( 200 ); // status 200 OK
            ctx.contentType( "application/json" ); // MIME type of JSON
            StructuredResponse resp = new StructuredResponse( "ok" , null, dataStore.readAll() );
            ctx.result( gson.toJson( resp ) ); // return JSON representation of response
        } );


        // GET route that returns everything for a single row in the MockDataStore.
        // The "{id}" suffix in the first parameter to get() becomes
        // ctx.pathParam("id"), so that we can get the requested row ID.  If
        // "{id}" isn't a number, Javalin will reply with a status 500 Internal
        // Server Error.  Otherwise, we have an integer, and the only possible
        // error is that it doesn't correspond to a row with data.
        app.get( "/posts/{id}", ctx -> {
            // NB: the {} syntax "/posts/{id}" does not allow slashes ('/') as part of the parameter
            // NB: the <> syntax "/posts/<id>" allows slashes ('/') as part of the parameter
            int idx = Integer.parseInt( ctx.pathParam("id") );
           
            // NB: even on error, we return 200, but with a JSON object that describes the error.
            ctx.status( 200 ); // status 200 OK
            ctx.contentType( "application/json" ); // MIME type of JSON


            MockDataRow data = dataStore.readOne(idx);
            StructuredResponse resp = null;
            if (data == null) { // row not found, so return an error response
                resp = new StructuredResponse("error", "Data with row id " + idx + " not found", null);
            } else { // we found it, so just return the data
                resp = new StructuredResponse("ok", null, data);
            }
           
            ctx.result( gson.toJson( resp ) ); // return JSON representation of response
        } );


        // POST route for adding a new element to the MockDataStore.  This will read
        // JSON from the body of the request, turn it into a SimpleRequest
        // object, extract the title and post, insert them, and return the
        // ID of the newly created row.
        app.post("/posts", ctx -> {
            // NB: even on error, we return 200, but with a JSON object that describes the error.
            ctx.status( 200 ); // status 200 OK
            ctx.contentType( "application/json" ); // MIME type of JSON
            StructuredResponse resp = null;


            // get the request json from the ctx body, turn it into SimpleRequest instance
            // NB: if gson.Json fails, expect server reply with status 500 Internal Server Error
            PostRequest req = gson.fromJson(ctx.body(), PostRequest.class);
           
            // NB: add to MockDataStore; createEntry checks for null title and post
            int newId = dataStore.createEntry(req.title(), req.contents(), req.valid());
            if (newId == -1) {
                resp = new StructuredResponse("error", "error performing insertion (title or post null?)", null);
            } else {
                resp = new StructuredResponse("ok", Integer.toString(newId), null);
            }
            ctx.result( gson.toJson( resp ) ); // return JSON representation of response
        });


        // PUT route for updating a row's liked status 
        app.put("/posts/{id}", ctx -> {
            // If we can't get an ID or can't parse the JSON, javalin sends a status 500
            int idx = Integer.parseInt( ctx.pathParam("id") );


            // NB: even on error, we return 200, but with a JSON object that describes the error.
            ctx.status( 200 ); // status 200 OK
            ctx.contentType( "application/json" ); // MIME type of JSON
            StructuredResponse resp = null;


            // get the request json from the ctx body, turn it into SimpleRequest instance
            // NB: if gson.Json fails, expect server reply with status 500 Internal Server Error
            PostRequest req = gson.fromJson(ctx.body(), PostRequest.class);


            // NB: update entry in MockDataStore; updateOne checks for null title and post and invalid ids
            MockDataRow result = dataStore.updateOne(idx, req.title(), req.contents(), req.valid());
            if (result == null) {
                resp = new StructuredResponse("error", "unable to update row " + idx, null);
            } else {
                resp = new StructuredResponse("ok", null, result);
            }
            ctx.result( gson.toJson( resp ) ); // return JSON representation of response
        });


        // DELETE route for removing a row from the MockDataStore
        app.delete("/posts/{id}", ctx -> {
            // If we can't get an ID or can't parse the JSON, javalin sends a status 500
            int idx = Integer.parseInt( ctx.pathParam("id") );


            // NB: even on error, we return 200, but with a JSON object that describes the error.
            ctx.status( 200 ); // status 200 OK
            ctx.contentType( "application/json" ); // MIME type of JSON
            StructuredResponse resp = null;


            // NB: we won't concern ourselves too much with the quality of the
            //     post sent on a successful delete
            boolean result = dataStore.deleteOne(idx);
            if (!result) {
                resp = new StructuredResponse("error", "unable to delete row " + idx, null);
            } else {
                resp = new StructuredResponse("ok", null, null);
            }
            ctx.result( gson.toJson( resp ) ); // return JSON representation of response
        });
       
        // don't forget: nothing happens until we `start` the server
        app.start(getIntFromEnv("PORT", DEFAULT_PORT_WEBSERVER) );
    }



    // ************ USES DATABASE ************ //


    public static void main_uses_database( String[] args ){
        // our javalin app on which most operations must be performed
        Javalin app = Javalin
                .create(
                    config -> {
                        config.requestLogger.http(
                            (ctx, ms) -> {
                                System.out.printf( "%s%n", "=".repeat(42) );
                                System.out.printf( "%s\t%s\t%s%nfull url: %s%n", ctx.scheme(), ctx.method().name(), ctx.path(), ctx.fullUrl() );                                
                            }
                        );
                        // set up static file serving. See: https://javalin.io/documentation#staticfileconfig
                        config.staticFiles.add(staticFiles -> {
                            staticFiles.hostedPath = "/";                   // change to host files on a subpath, like '/assets'
                            String static_location_override = System.getenv("STATIC_LOCATION");
                            if (static_location_override == null) { // serve from jar; files located in src/main/resources/public
                                staticFiles.directory = "/public";                  // the directory where your files are located
                                staticFiles.location = Location.CLASSPATH;          // Location.CLASSPATH (jar)
                            } else { // serve from filesystem
                                System.out.println( "Overriding location of static file serving using STATIC_LOCATION env var: " + static_location_override );
                                staticFiles.directory = static_location_override;   // the directory where your files are located
                                staticFiles.location = Location.EXTERNAL;           // Location.EXTERNAL (file system)
                            }
                            staticFiles.precompress = false;                   // if the files should be pre-compressed and cached in memory (optimization)
                            // staticFiles.aliasCheck = null;                  // you can configure this to enable symlinks (= ContextHandler.ApproveAliases())
                            // staticFiles.headers = Map.of(...);              // headers that will be set for the files
                            // staticFiles.skipFileFunction = req -> false;    // you can use this to skip certain files in the dir, based on the HttpServletRequest
                            // staticFiles.mimeTypes.add(mimeType, ext);       // you can add custom mimetypes for extensions
                        });
                    }
                );

            /* 
            if ("True".equalsIgnoreCase(System.getenv("CORS_ENABLED"))) {
                final String acceptCrossOriginRequestsFrom = "*";
                final String acceptedCrossOriginRoutes = "GET,PUT,POST,DELETE,OPTIONS";
                final String supportedRequestHeaders = "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin";
                enableCORS(app, acceptCrossOriginRequestsFrom, acceptedCrossOriginRoutes, supportedRequestHeaders);
            }
            */

        // gson provides us a way to turn JSON into objects, and objects into JSON.
        //
        // NB: it must be final, so that it can be accessed from our lambdas
        //
        // NB: Gson is thread-safe.  See
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();


        // Get the database object
        Database db = Database.getDatabase();
        if (db == null) {
            System.out.println("Error: Failed to get database. Exiting.");
            System.exit(1);
        }

        app.post("/auth/validate/{idToken}", ctx -> {
            String idTokeString = ctx.pathParam("idToken");

            ctx.status(200);
            ctx.contentType("application/json");

            UserData data = TokenVerifier.verifyToken(idTokeString);
            System.out.print(data);
            StructuredResponse resp = new StructuredResponse("ok", null, data);
            if (session.get(data.getUserId()) == null) {
                session.put(data.getUserId(), new UserData(data.getUserId(), data.getEmail(), data.isEmailVerified(), data.getName(), data.getPictureUrl()));
                db.insertUserRow(data.getUserId(), data.getEmail(), data.getName(), "", "", "", true);
            } else {
                System.out.println("user already logged in");
            }

            System.out.println(session);
            ctx.result(gson.toJson(resp));
        });

        app.post("/users/{userID}/logout", ctx -> {
            String userID = ctx.pathParam("userID");

            ctx.status(200);
            ctx.contentType("application/json");

            UserData user = session.get(userID);
            
            db.deleteUser(userID);
            session.remove(userID);

            StructuredResponse resp = new StructuredResponse("ok", null, new Gson().toJson(user));
            ctx.result(gson.toJson(resp));
        });

        // GET route that returns all post titles and Ids.  All we do is get
        // the data, embed it in a StructuredResponse, turn it into JSON, and
        // return it.  If there's no data, we return "[]", so there's no need
        // for error handling.
        app.get( "/posts", ctx -> {
            // Ensure db.selectAll() is working and returning valid data
            ctx.status(200);
            ctx.contentType("application/json");
            ArrayList<Database.PostRowData> posts = db.selectAll();
            ArrayList<Map<String, Object>> responseData = new ArrayList<>();
            for (Database.PostRowData post : posts) {
                Map<String, Object> postData = new HashMap<>();
                postData.put("id", post.id());
                postData.put("author", post.author());
                postData.put("title", post.title());
                postData.put("contents", post.contents());
                postData.put("valid", post.valid());
                responseData.add(postData);
            }
            StructuredResponse resp = new StructuredResponse("ok", null, responseData);
            ctx.result(gson.toJson(resp));
        });  

        // GET route that returns everything for a single row in the Database.
        // The "{id}" suffix in the first parameter to get() becomes
        // ctx.pathParam("id"), so that we can get the requested row ID.  If
        // "{id}" isn't a number, Javalin will reply with a status 500 Internal
        // Server Error.  Otherwise, we have an integer, and the only possible
        // error is that it doesn't correspond to a row with data.
        app.get( "/posts/{id}", ctx -> {
            // NB: the {} syntax "/posts/{id}" does not allow slashes ('/') as part of the parameter
            // NB: the <> syntax "/posts/<id>" allows slashes ('/') as part of the parameter
            int idx = Integer.parseInt( ctx.pathParam("id") );
           
            // NB: even on error, we return 200, but with a JSON object that describes the error.
            ctx.status( 200 ); // status 200 OK
            ctx.contentType( "application/json" ); // MIME type of JSON


            Database.PostRowData data = db.selectOne(idx);
            StructuredResponse resp;
            if (data == null) {
                resp = new StructuredResponse("error", "Data with row id " + idx + " not found", null);
            } else {
                Map<String, Object> postData = new HashMap<>();
                postData.put("id", data.id());
                postData.put("author", data.author());
                postData.put("title", data.title());
                postData.put("contents", data.contents());
                postData.put("valid", data.valid());
                resp = new StructuredResponse("ok", null, postData);
            }
            ctx.result( gson.toJson( resp ) ); // return JSON representation of response
        } );

        app.get( "/users/{id}", ctx -> {
            // NB: the {} syntax "/posts/{id}" does not allow slashes ('/') as part of the parameter
            // NB: the <> syntax "/posts/<id>" allows slashes ('/') as part of the parameter
            String idx = ctx.pathParam("id");
           
            // NB: even on error, we return 200, but with a JSON object that describes the error.
            ctx.status( 200 ); // status 200 OK
            ctx.contentType( "application/json" ); // MIME type of JSON


            Database.UserRowData data = db.selectUser(idx);
            StructuredResponse resp;
            if (data == null) {
                resp = new StructuredResponse("error", "Data with row id " + idx + " not found", null);
            } else {
                Map<String, Object> userData = new HashMap<>();
                userData.put("id", data.id());
                userData.put("email", data.email());
                userData.put("username", data.username());
                userData.put("gender_identity", data.gender_identity());
                userData.put("sexual_orientation", data.sexual_orientation());
                userData.put("valid", data.valid());
                resp = new StructuredResponse("ok", null, userData);
            }
            ctx.result( gson.toJson( resp ) ); // return JSON representation of response
        } );

        // GET route that returns all users
        app.get( "/users", ctx -> {
            // Ensure db.selectAll() is working and returning valid data
            ctx.status(200);
            ctx.contentType("application/json");
            ArrayList<Database.UserRowData> users = db.selectAllUsers();
            ArrayList<Map<String, Object>> responseData = new ArrayList<>();
            for (Database.UserRowData user : users) {
                Map<String, Object> userData = new HashMap<>();
                userData.put("id", user.id());
                userData.put("email", user.email());
                userData.put("username", user.username());
                userData.put("gender_identity", user.gender_identity());
                userData.put("sexual_orientation", user.sexual_orientation());
                userData.put("valid", user.valid());
                responseData.add(userData);
            }
            StructuredResponse resp = new StructuredResponse("ok", null, responseData);
            ctx.result(gson.toJson(resp));
        });  


        // POST route for adding a new element to the Database.  This will read
        // JSON from the body of the request, turn it into a SimpleRequest
        // object, extract the title and post, insert them, and return the
        // ID of the newly created row.
        app.post("/posts", ctx -> {
            // NB: even on error, we return 200, but with a JSON object that describes the error.
            ctx.status( 200 ); // status 200 OK
            ctx.contentType( "application/json" ); // MIME type of JSON
            StructuredResponse resp = null;
            //int idx = Integer.parseInt( ctx.pathParam("id") );
            // get the request json from the ctx body, turn it into SimpleRequest instance
            // NB: if gson.Json fails, expect server reply with status 500 Internal Server Error
            PostRequest req = gson.fromJson(ctx.body(), PostRequest.class);
           
            // NB: add to Database; createEntry checks for null title and post
            int newId = db.insertPostRow(req.author(), req.title(), req.contents(), req.valid());
            if (newId == -1) {
                resp = new StructuredResponse("error", "error performing insertion", null);
            } else {
                resp = new StructuredResponse("ok", Integer.toString(newId), null);
            }
           
            ctx.result( gson.toJson( resp ) ); // return JSON representation of response
        });
        /*
        // POST route for adding a new user to the Database. 
        app.post("/users", ctx -> {
            // NB: even on error, we return 200, but with a JSON object that describes the error.
            ctx.status( 200 ); // status 200 OK
            ctx.contentType( "application/json" ); // MIME type of JSON
            StructuredResponse resp = null;
            //int idx = Integer.parseInt( ctx.pathParam("id") );
            // get the request json from the ctx body, turn it into SimpleRequest instance
            // NB: if gson.Json fails, expect server reply with status 500 Internal Server Error
            UserRequest req = gson.fromJson(ctx.body(), UserRequest.class);
            System.out.println(req);
            // NB: add to Database; createEntry checks for null title and post
            int newId = db.insertUserRow(req.email(), req.username(), req.gender_identity(), req.sexual_orientation(), req.notes(), req.valid());
            if (newId == -1) {
                resp = new StructuredResponse("error", "error performing insertion", null);
            } else {
                resp = new StructuredResponse("ok", Integer.toString(newId), null);
            }
           
            ctx.result( gson.toJson( resp ) ); // return JSON representation of response
        });
        */
        // POST route for adding an upvote 
        app.post("/posts/{post_id}/{user_id}/upvote", ctx -> {
            // NB: even on error, we return 200, but with a JSON object that describes the error.
            ctx.status( 200 ); // status 200 OK
            ctx.contentType( "application/json" ); // MIME type of JSON

            int parent_post = Integer.parseInt( ctx.pathParam("post_id") );
            int author = Integer.parseInt( ctx.pathParam("user_id") );

            StructuredResponse resp = null;
            //int idx = Integer.parseInt( ctx.pathParam("id") );
            // get the request json from the ctx body, turn it into SimpleRequest instance
            // NB: if gson.Json fails, expect server reply with status 500 Internal Server Error
            VoteRequest req = gson.fromJson(ctx.body(), VoteRequest.class);
            System.out.println(req);
            // NB: add to Database; createEntry checks for null title and post
            int newId = db.insertUpvote(author, parent_post);
            if (newId == -1) {
                resp = new StructuredResponse("error", "error performing insertion", null);
            } else {
                resp = new StructuredResponse("ok", Integer.toString(newId), null);
            }
           
            ctx.result( gson.toJson( resp ) ); // return JSON representation of response
        });
        // POST route for adding a new user to the Database. 
        app.post("/posts/{post_id}/{user_id}/downvote", ctx -> {
            // NB: even on error, we return 200, but with a JSON object that describes the error.
            ctx.status( 200 ); // status 200 OK
            ctx.contentType( "application/json" ); // MIME type of JSON

            int parent_post = Integer.parseInt( ctx.pathParam("post_id") );
            int author = Integer.parseInt( ctx.pathParam("user_id") );

            StructuredResponse resp = null;
            //int idx = Integer.parseInt( ctx.pathParam("id") );
            // get the request json from the ctx body, turn it into SimpleRequest instance
            // NB: if gson.Json fails, expect server reply with status 500 Internal Server Error
            VoteRequest req = gson.fromJson(ctx.body(), VoteRequest.class);
            System.out.println(req);
            // NB: add to Database; createEntry checks for null title and post
            int newId = db.insertDownvote(author, parent_post);
            if (newId == -1) {
                resp = new StructuredResponse("error", "error performing insertion", null);
            } else {
                resp = new StructuredResponse("ok", Integer.toString(newId), null);
            }
           
            ctx.result( gson.toJson( resp ) ); // return JSON representation of response
        });
        









        /*
        // Replace the existing PUT /posts/{id}/like route with this POST route
        app.post("/posts/{id}/like", ctx -> {
            int idx = Integer.parseInt(ctx.pathParam("id"));
            
            ctx.status(200);
            ctx.contentType("application/json");
            StructuredResponse resp;

            // First, get the current post data
            Database.RowData currentPost = db.selectOne(idx);
            if (currentPost == null) {
                resp = new StructuredResponse("error", "Post with id " + idx + " not found", null);
                ctx.result(gson.toJson(resp));
                return;
            }

            // Set is_liked to true, preserving other fields
            int result = db.updateOne(
                idx, 
                currentPost.title(), 
                currentPost.contents(), 
                true  // Explicitly set to true for liking
            );

            if (result == -1) {
                resp = new StructuredResponse("error", "Unable to like post " + idx, null);
            } else {
                // Return the updated post data
                Database.RowData updatedPost = db.selectOne(idx);
                resp = new StructuredResponse("ok", "Post " + idx + " successfully liked", updatedPost);
            }
            
            ctx.result(gson.toJson(resp));
        });

        // Update the existing PUT /posts/{id} route for general post editing
        app.put("/posts/{id}", ctx -> {
            int idx = Integer.parseInt(ctx.pathParam("id"));
            
            ctx.status(200);
            ctx.contentType("application/json");
            StructuredResponse resp;

            // Get the current post first to verify it exists
            Database.RowData currentPost = db.selectOne(idx);
            if (currentPost == null) {
                resp = new StructuredResponse("error", "Post with id " + idx + " not found", null);
                ctx.result(gson.toJson(resp));
                return;
            }

            // Parse the update request
            SimpleRequest req = gson.fromJson(ctx.body(), SimpleRequest.class);
            
            // Validate the update data
            if (req.title() == null || req.title().isEmpty() || 
                req.contents() == null || req.contents().isEmpty()) {
                resp = new StructuredResponse("error", "Title and contents cannot be empty", null);
                ctx.result(gson.toJson(resp));
                return;
            }

            // Update the post while preserving the current like status
            int result = db.updateOne(
                idx,
                req.title(),
                req.contents(),
                currentPost.valid()  // Preserve the current like status
            );

            if (result == -1) {
                resp = new StructuredResponse("error", "Unable to update post " + idx, null);
            } else {
                // Return the updated post data
                Database.RowData updatedPost = db.selectOne(idx);
                resp = new StructuredResponse("ok", "Post " + idx + " successfully updated", updatedPost);
            }
            
            ctx.result(gson.toJson(resp));
        });

        // DELETE route for removing a row from the Database
        app.delete("/posts/{id}", ctx -> {
            // If we can't get an ID or can't parse the JSON, javalin sends a status 500
            int idx = Integer.parseInt( ctx.pathParam("id") );
       
            // NB: even on error, we return 200, but with a JSON object that describes the error.
            ctx.status( 200 ); // status 200 OK
            ctx.contentType( "application/json" ); // MIME type of JSON
            StructuredResponse resp = null;
       
            // NB: we won't concern ourselves too much with the quality of the
            //     post sent on a successful delete
            int result = db.deleteRow(idx);
            if ( result == -1 ) {
                resp = new StructuredResponse("error", "unable to delete row " + idx, null);
                }
            else {
                resp = new StructuredResponse("ok", null, "deleted row " + idx);
            }
            ctx.result( gson.toJson( resp ) ); // return JSON representation of response
        });

        // DELETE route for removing a like on a post
        app.delete("/posts/{id}/like", ctx -> {
            int idx = Integer.parseInt(ctx.pathParam("id"));
                    
            ctx.status(200);
            ctx.contentType("application/json");
            StructuredResponse resp;

            // First, get the current post data
            Database.RowData currentPost = db.selectOne(idx);
            if (currentPost == null) {
                resp = new StructuredResponse("error", "Post with id " + idx + " not found", null);
                ctx.result(gson.toJson(resp));
                return;
            }

            // Only update if the post is currently liked
            if (!currentPost.valid()) {
                resp = new StructuredResponse("ok", "Post " + idx + " is already unliked", currentPost);
                ctx.result(gson.toJson(resp));
                return;
            }

            // Set is_liked to false while preserving other fields
            int result = db.updateOne(
                idx, 
                currentPost.title(), 
                currentPost.contents(), 
                false  // Explicitly set to false instead of toggling
            );

            if (result == -1) {
                resp = new StructuredResponse("error", "Unable to unlike post " + idx, null);
            } else {
                // Return the updated post data
                Database.RowData updatedPost = db.selectOne(idx);
                resp = new StructuredResponse("ok", "Post " + idx + " successfully unliked", updatedPost);
            }
            
            ctx.result(gson.toJson(resp));
        }); 
        */
        
        // don't forget: nothing happens until we `start` the server
        app.start(getIntFromEnv("PORT", DEFAULT_PORT_WEBSERVER) );
    }

/**
 * Reads arguments from the environment and then uses those
 * arguments to connect to the database. Either DATABASE_URI should be set,
 * or the values of four other variables POSTGRES_{IP, PORT, USER, PASS, DBNAME}.
 */


public static void simpleManualTests( String[] argv ){
    /* holds connection to the database created from environment variables */
    
    Database db = Database.getDatabase();
    /*

    db.dropTable();
    db.createTable();
    db.insertRow("test subject", "test post", false);
    db.updateOne(1,"title" ,"updated test post", true);


    ArrayList<Database.RowData> list_rd = db.selectAll();
    System.out.println( "Row data:" );
    for( Database.RowData rd : list_rd )
        System.out.println( ">\t" + rd );


    Database.RowData single_rd = db.selectOne(1);
    System.out.println( "Single row: " + single_rd );
   
    db.deleteRow(1);
    */
    if( db != null )
        db.disconnect();
    }
}

    /**
 * Set up CORS headers for the OPTIONS verb, and for every response that the
 * server sends.  This only needs to be called once.
 *
 * @param app the Javalin app on which to enable cors; create() already called on it
 * @param origin The server that is allowed to send requests to this server
 * @param methods The allowed HTTP verbs from the above origin
 * @param headers The headers that can be sent with a request from the above
 *                origin
 
private static void enableCORS(Javalin app, String origin, String methods, String headers) {
    System.out.println("!!! CAUTION: ~~~ ENABLING CORS ~~~ !!!");
    app.options( "/*", ctx -> {
        String accessControlRequestHeaders = ctx.req().getHeader( "Access-Control-Request-Headers");
        if (accessControlRequestHeaders != null) {
            ctx.res().setHeader("Access-Control-Allow-Headers", accessControlRequestHeaders);
        }
       
        String accessControlRequestMethod = ctx.req().getHeader("Access-Control-Request-Method");
        if (accessControlRequestMethod != null) {
            ctx.res().setHeader("Access-Control-Allow-Methods", accessControlRequestMethod);
        }
    });


    // 'before' is a decorator, which will run before any get/post/put/delete.
    // In our case, it will put three extra CORS headers into the response
    app.before( handler -> {
        handler.header("Access-Control-Allow-Origin", origin);
        handler.header("Access-Control-Request-Method", methods);
        handler.header("Access-Control-Allow-Headers", headers);
    });
} */ 