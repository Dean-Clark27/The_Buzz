# The Buzz (Former CSE 216 Team Repo) 
## Details
* Semester: Fall 2024
* Team Number: 32
* Team Name: jailbreak
* Repository:
    * On The Web:https://github.com/Dean-Clark27/The_Buzz (former https://bitbucket.org/sml3/cse216_fa24_team_32)
    * Git Clone: 'git clone https://github.com/Dean-Clark27/The_Buzz.git'
* Jira:
    * https://lehigh-team-drfppe9i.atlassian.net/jira/software/projects/TC/boards/2
* Backend URL:
    * https://team-jailbreak.dokku.cse.lehigh.edu
* API Routes:
![API Routes](docs/)
![API Routes](docs/)
* Supabase Database:
    * DATABASE_URI = 
    jdbc:postgresql://aws-0-us-east-1.pooler.supabase.com:5432/postgres?user=postgres.setpdkyicbyderfipqpy&password=[YOUR-PASSWORD]

    * On The Web: 
    postgresql://postgres.setpdkyicbyderfipqpy:[YOUR-PASSWORD]@aws-0-us-east-1.pooler.supabase.com:5432/postgres

## Team Members/Roles:
* Mentor: Emir Veziroglu, env225@lehigh.edu
* Weekly live & synchronous meeting: 
    * Zoom Virtual Meeting: Thursdays, 4:30 PM
    * In Person Meeting: Tuesdays, 7:00 PM 
* Project Manger: <Dean Clark, dmc227@lehigh.edu>
* Backend developer: <Illia Slipchenko, ils226@lehigh.edu>
* Admin developer: <Liam Messinger, ljm426@lehigh.edu>
* Web developer: <Anaya Rawoot, adr325@lehigh.edu>
* Mobile developer: <Jared Cooper, jac327@lehigh.edu>

## Functionality:
### Phase 1:
* Backend: 
    * Currently, the backend is being hosted locally and on dokku, where the information is being hosted at /posts. Using AJAX calls within a REST API, we can perform the following actions: 
    * Using a GET request, we can get all the posts or a singular post given a certain ID number, which is automatically generated from the database.
    * Using a POST request, upload a new post into the database with a title and contents, initializing being not liked.
    * Using a PUT request to the /posts/{id}/like route, flip the value of the like in the app.
    * Using a DELETE request, either delete an entire post or a specific like on a post using the /posts/{id}/like route again (same as PUT request).
    * These requests are currently done by anonymous users.
    * Writes meaningful tests to ensure the security and proper functionality of the new features.

* Mobile: 
    * The app runs through ExpoGo app for Apple/Android devices and uses React Native instead of Flutter
    * The app is a singular page app (SPA) showing all of the posts that are connected to the database, using the requests through the backend.
    * Has a picture at the nav bar area, showing all the posts scrolling down with titles, bodies, a heart button which is on or off.
    * Writes meaningful tests to ensure the security and proper functionality of the new features.

* Web:
    * The app is a singular page app (SPA) using React components showing all of the posts that are connected to the database, using the requests through the backend.
    * Has a picture at the nav bar area, showing all the posts scrolling down with titles, bodies, a heart button which is on or off.
    * Writes meaningful tests to ensure the security and proper functionality of the new features.

* Admin:
    * Created the databse and test data for each table to be interacted with the backend. 
    * Creates a Java admin command-line program to interact with, drop the table, edit posts, etc. 
    * Writes meaningful tests to ensure the security and proper functionality of the new features.

### Phase 2:
* Backend: 
    * OAuth 2.0 was implemented during this phase, allowing users to login using their Lehigh Google account to create an account on the website.
    * Comments (only singular comments, you cannot reply to them), upvotes, and downvotes were added in lieu of the heart button from the prior phase.
    * Redirects user to the Google authorized sign-in page for users to get an API token for users to be authorized
    * Profiles consist of a user's email, username, gender identity, sexual orientation, and notes about the user was added. 
    * Writes meaningful tests to ensure the security and proper functionality of the new features.

* Mobile: 
    * Adds a profile page to the app, using a button to see the details of the user.
    * Shows only the user their email, username, gender identity, sexual orientation, and notes.
    * Shows upvotes/downvotes, and comments under the original
    * Writes meaningful tests to ensure the security and proper functionality of the new features.

* Web:
    * Adds a profile page to the app, using a button to see the details of the user.
    * Shows only the user their email, username, gender identity, sexual orientation, and notes.
    * Shows upvotes/downvotes, and comments under the original
    * Writes meaningful tests to ensure the security and proper functionality of the new features.

* Admin:
    * Created all of the new necessary tables with different relationships for all of the data and prepopulates it with test data that the backend/web/mobile people can test hitting
    * The CLI can invalidate a user or a post 
    * Writes meaningful tests to ensure the security and proper functionality of the new features.

## Instructions to Run different Components:
#### [Backend Instructions](backend/README.md) 
#### [Mobile Instuctions](buzz-mobile-app/README.md)
#### [Frontend Instructions](web/README.md)
#### [Admin Instructions](admin-cli/README.md)
