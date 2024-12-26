package edu.lehigh.cse216.teamjailbreak.backend;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.javalin.Javalin;
import java.util.ArrayList;
import io.javalin.testtools.JavalinTest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AppTest {
    /*
    private Javalin app;
    private Gson gson;
    private MockDataStore dataStore;

    private Javalin createTestApp() {
        app = Javalin.create(config -> {
            config.requestLogger.http((ctx, ms) -> {
                System.out.printf("%s%n", "=".repeat(42));
                System.out.printf("%s\t%s\t%s%nfull url: %s%n", 
                    ctx.scheme(), ctx.method().name(), ctx.path(), ctx.fullUrl());
            });
        });
        return app;
    }

    @BeforeEach
    public void setUp() {
        app = createTestApp();
        gson = new Gson();
        dataStore = new MockDataStore();
        setupRoutes();
    }

    @AfterEach
    public void tearDown() {
        if (app != null) {
            app.stop();
        }
    }

    private void setupRoutes() {
        // GET all posts route
        app.get("/posts", ctx -> {
            ctx.status(200);
            ctx.contentType("application/json");
            ArrayList<MockDataRowLite> posts = dataStore.readAll();
            StructuredResponse resp = new StructuredResponse("ok", null, posts);
            ctx.result(gson.toJson(resp));
        });

        // GET single post route
        app.get("/posts/{id}", ctx -> {
            int idx = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(200);
            ctx.contentType("application/json");
            
            MockDataRow data = dataStore.readOne(idx);
            StructuredResponse resp;
            if (data == null) {
                resp = new StructuredResponse("error", "Data with row id " + idx + " not found", null);
            } else {
                resp = new StructuredResponse("ok", null, data);
            }
            ctx.result(gson.toJson(resp));
        });

        // POST new post route
        app.post("/posts", ctx -> {
            ctx.status(200);
            ctx.contentType("application/json");
            SimpleRequest req = gson.fromJson(ctx.body(), SimpleRequest.class);
            
            int newId = dataStore.createEntry(req.title(), req.contents(), req.is_liked());
            StructuredResponse resp;
            if (newId == -1) {
                resp = new StructuredResponse("error", "error performing insertion", null);
            } else {
                resp = new StructuredResponse("ok", Integer.toString(newId), null);
            }
            ctx.result(gson.toJson(resp));
        });

        // PUT update post route
        app.put("/posts/{id}", ctx -> {
            int idx = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(200);
            ctx.contentType("application/json");
            SimpleRequest req = gson.fromJson(ctx.body(), SimpleRequest.class);
            
            MockDataRow result = dataStore.updateOne(idx, req.title(), req.contents(), req.is_liked());
            StructuredResponse resp;
            if (result == null) {
                resp = new StructuredResponse("error", "unable to update row " + idx, null);
            } else {
                resp = new StructuredResponse("ok", null, result);
            }
            ctx.result(gson.toJson(resp));
        });

        // DELETE post route
        app.delete("/posts/{id}", ctx -> {
            int idx = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(200);
            ctx.contentType("application/json");
            
            boolean result = dataStore.deleteOne(idx);
            StructuredResponse resp;
            if (!result) {
                resp = new StructuredResponse("error", "unable to delete row " + idx, null);
            } else {
                resp = new StructuredResponse("ok", null, null);
            }
            ctx.result(gson.toJson(resp));
        });

        // PUT route for toggling like status
        app.put("/posts/{id}/like", ctx -> {
            int idx = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(200);
            ctx.contentType("application/json");
            
            MockDataRow currentPost = dataStore.readOne(idx);
            if (currentPost == null) {
                ctx.result(gson.toJson(
                    new StructuredResponse("error", "Post with id " + idx + " not found", null)
                ));
                return;
            }
            
            // Toggle the like status
            MockDataRow result = dataStore.updateOne(
                idx,
                currentPost.title(),
                currentPost.contents(),
                !currentPost.is_liked()
            );
            
            if (result == null) {
                ctx.result(gson.toJson(
                    new StructuredResponse("error", "Unable to update like status", null)
                ));
            } else {
                ctx.result(gson.toJson(
                    new StructuredResponse("ok", null, result)
                ));
            }
        });
    }

    @Test
    public void testGetAllPosts() {
        JavalinTest.test(app, (server, client) -> {
            // First, add some test data
            dataStore.createEntry("Test Title 1", "Test Content 1", false);
            dataStore.createEntry("Test Title 2", "Test Content 2", false);

            var response = client.get("/posts");
            assertEquals(200, response.code());
            
            StructuredResponse resp = gson.fromJson(response.body().string(), StructuredResponse.class);
            assertEquals("ok", resp.status());
            assertNotNull(resp.data());
            
            ArrayList<MockDataRowLite> posts = gson.fromJson(
                gson.toJson(resp.data()),
                new TypeToken<ArrayList<MockDataRowLite>>(){}.getType()
            );
            assertEquals(2, posts.size());
        });
    }

    @Test
    public void testGetSinglePost() {
        JavalinTest.test(app, (server, client) -> {
            int newId = dataStore.createEntry("Test Title", "Test Content", false);
            
            var response = client.get("/posts/" + newId);
            assertEquals(200, response.code());
            
            StructuredResponse resp = gson.fromJson(response.body().string(), StructuredResponse.class);
            assertEquals("ok", resp.status());
            assertNotNull(resp.data());
        });
    }

    @Test
    public void testCreatePost() {
        JavalinTest.test(app, (server, client) -> {
            SimpleRequest newPost = new SimpleRequest("New Title", "New Content", false, 0);
            var response = client.post("/posts", gson.toJson(newPost));
            assertEquals(200, response.code());
            
            StructuredResponse resp = gson.fromJson(response.body().string(), StructuredResponse.class);
            assertEquals("ok", resp.status());
            assertNotNull(resp.message());
        });
    }

    @Test
    public void testUpdatePost() {
        JavalinTest.test(app, (server, client) -> {
            int newId = dataStore.createEntry("Original Title", "Original Content", false);
            SimpleRequest updatePost = new SimpleRequest("Updated Title", "Updated Content", false, 0);
            
            var response = client.put("/posts/" + newId, gson.toJson(updatePost));
            assertEquals(200, response.code());
            
            StructuredResponse resp = gson.fromJson(response.body().string(), StructuredResponse.class);
            assertEquals("ok", resp.status());
        });
    }

    @Test
    public void testDeletePost() {
        JavalinTest.test(app, (server, client) -> {
            // Create a post first
            int newId = dataStore.createEntry("Delete Me", "This will be deleted", false);
            
            // Attempt to delete it
            var response = client.delete("/posts/" + newId);
            assertEquals(200, response.code());
            
            StructuredResponse resp = gson.fromJson(response.body().string(), StructuredResponse.class);
            assertEquals("ok", resp.status());
            
            // Verify it's gone
            assertNull(dataStore.readOne(newId));
        });
    }

    @Test
    public void testLikePost() {
        JavalinTest.test(app, (server, client) -> {
            int newId = dataStore.createEntry("Test Title", "Test Content", false);
            
            var response = client.put("/posts/" + newId + "/like", "");
            assertEquals(200, response.code());
            
            StructuredResponse resp = gson.fromJson(response.body().string(), StructuredResponse.class);
            assertEquals("ok", resp.status());
            
            MockDataRow updatedPost = dataStore.readOne(newId);
            assertTrue(updatedPost.is_liked());
        });
    }
        */
}