# CSE 216 Team Repo - Backend
### Details
* Semester: Fall 2024
* Team Number: 32
* Team Name: jailbreak
* Bitbucket Repository: 
    * On The Web:https://bitbucket.org/sml3/cse216_fa24_team_32
    * Git Clone: 'git clone git@bitbucket.org:sml3/cse216_fa24_team_32.git'
* Jira:
    * https://lehigh-team-drfppe9i.atlassian.net/jira/software/projects/TC/boards/2
* Backend URL:
    * https://team-jailbreak.dokku.cse.lehigh.edu
* Postman Link:
    * https://app.getpostman.com/join-team?invite_code=9158d15a2b15aaaa4d72dc22fec45468&target_code=6607d18e2501668d2f7eef1b5fd8ade5
* API Routes:
![API Routes](../docs/APIRoutes.png)

### Functionality:
Currently, the backend is being hosted locally and on dokku, where the information is being hosted at /posts. Using AJAX calls within a REST API, we can perform the following actions: 
* Using a GET request, we can get all the posts or a singular post given a certain ID number, which is automatically generated from the database.
* Using a POST request, upload a new post into the database with a title and contents, initializing being not liked.
* Using a PUT request to the /posts/{id}/like route, flip the value of the like in the app.
* Using a DELETE request, either delete an entire post or a specific like on a post using the /posts/{id}/like route again (same as PUT request).
These requests are currently done by anonymous users.
### Running on Dokku:
**Creating an app (only needs to be done once)-** `dokku apps:create team-32`

**Gets all of the environment variables from Dokku-** `ssh -i ~/.ssh/id_ed25519 -t dokku@dokku.cse.lehigh.edu 'config:export team-jailbreak'`
    
**Gets recent log of requests to our dokku backend-** `ssh -i ~/.ssh/id_ed25519 -t dokku@dokku.cse.lehigh.edu 'logs team-jailbreak'`

**Create the App-** `ssh -i ~/.ssh/id_ed25519 -t dokku@dokku.cse.lehigh.edu 'apps:create team-jailbreak'`

**Start the App-** `ssh -i ~/.ssh/id_ed25519 -t dokku@dokku.cse.lehigh.edu 'ps:start team-jailbreak'`

**Restart the App (without rebuild)-** `ssh -i ~/.ssh/id_ed25519 -t dokku@dokku.cse.lehigh.edu 'ps:restart team-jailbreak'`

**Stop the App-** `ssh -i ~/.ssh/id_ed25519 -t dokku@dokku.cse.lehigh.edu 'ps:stop team-jailbreak'`

## Running Locally:
* Run the shell script we have to deploy it (this will run the proper mvn package)
* Run `$ mvn exec:java`
* go to http://localhost:[PORT_NUMBER], and use either Curl/Postman for 
    testing routes

## Phase 1: Dean Clark
### Unit Tests for Backend:
1. Posting a new idea
    - Create a mock HTTP POST request to /ideas (or whatever we call this) with the JSON payload of a new idea
    - Show that the server responds with the correct status code and that it shows up in the database
    - We could further do testing to make sure a new post starts at 1 like, that it is a unique message, etc.

2. Getting all ideas
    - Create a mock HTTP GET request to /ideas
    - Show that that server responds with the correct status code and gets all ideas
    - Check that the response from the server is an array with JSON array of all the ideas that matches the intended structure

3. Liking an idea
    - Create a mock HTTP PUT request to /ideas/:id/like (or whatever the correct routing is to a specific like value)
    - Show that the server responds with the correct status code
    - Check to make sure the like count goes up in the database (maybe query it in the test or check Supabase)

4. Updating an idea
    - Create a mock HTTP PUT request to a specific idea (/ideas/:id or similar) with a new title or description
    - Show that the server responds with a correct status code
    - Check to see if the new message is created in the database

5. Deleting an idea
    - Create a mock HTTP DELETE request to an existing idea (/ideas/:id or similar)
    - Show that the server responds with a correct status and there is a message there to be deleted
    - Check in the database to see if the message was removed

6. Test invalid JSON input
    - Create a mock HTTP POST request to /ideas with an unfinished JSON
    - Show that the server responds with a bad status code showing that it was invalid
    - Show that the message is returned somewhere and that the idea isn't added to the database

7. Test for getting nonexistant idea
    - Create a mock HTTP Get request to a certain message (/ideas/:id) where the id does not exist
    - Show that the server responds with a bad status code showing that the idea could not be found
    - Show that the message is returned somewhere and no idea is returned anywhere

## Phase 2: Illia Slichenko

1. Upvoting a neutral idea
    - Create a mock HTTP POST request to /ideas/:id/like 
    - Show that the server responds with the correct status code
    - Check to make sure the like bias goes up by one

2. Upvoting upvoted idea
    - Create a mock HTTP POST request to /ideas/:id/like
    - Show that the server responds with the correct status code
    - Check to make sure the like bias goes back to neutral

3. Upvoting downvoted idea
    - Create a mock HTTP POST request to /ideas/:id/like
    - Show that the server responds with the correct status code
    - Check to make sure the like bias goes down by one

4. Downvoting a neutral idea
    - Create a mock HTTP POST request to /ideas/:id/dislike
    - Show that the server responds with the correct status code
    - Check to make sure the like bias goes down by one

5. Downvoting upvoted idea
    - Create a mock HTTP POST request to /ideas/:id/dislike
    - Show that the server responds with the correct status code
    - Check to make sure the like bias goes up by one

6. Downvoting downvoted idea
    - Create a mock HTTP POST request to /ideas/:id/dislike
    - Show that the server responds with the correct status code
    - Check to make sure the like bias goes back to neutral