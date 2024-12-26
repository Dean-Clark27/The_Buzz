# Phase 1' Sprint 7 - PM Report Template
Use this form to provide your project manager report for Phase 1' (Prime).

<!-- PM: When editing this template for your submission, you may remove this section -->
## Instructions
Be as thorough and complete as possible, while being brief/concise. Please give detailed answers.

Submit one report per team. This should be submitted by the designated PM, except in approved circumstances. The report should be created as a markdown file (and converted to pdf if required).

In addition to uploading to coursesite, version control this in the `master` branch under the `docs` folder.

## Team Information [10 points total]

### Team Information:

* Number: 32
* Name: Jailbreak
* Mentor: <Emir Veziroglu, env225@lehigh.edu>
* Weekly live & synchronous meeting:
    * without mentor: Thursday 4:30pm
    * with mentor: Recitation Time (Tuesday 7:15pm - 9:55pm)

### Team Roles:

* Project Manger: <Jared Cooper, jac327@lehigh.edu>
    * Has this changed from last week (if so, why)?
        * No
* Backend developer: <Dean Clark, dmc227@lehigh.edu>
* Admin developer: <Anaya Rawoot, adr325@lehigh.edu>
* Web developer: <Illia Slipchenko, ils226@lehigh.edu>
* Mobile developer: <Liam Messinger, ljm426@lehigh.edu>

### Essential links for this project:

* Team's Dokku URL(s)
    * <https://team-32.dokku.cse.lehigh.edu>
* Team's software repo (bitbucket)
    * <https://bitbucket.org/sml3/cse216_fa24_team_32>
* Team's Jira board
    * <https://lehigh-team-drfppe9i.atlassian.net/jira/software/projects/TC/boards/2>


## Beginning of Phase 1' [20 points total]
Report out on the Phase 1 backlog and any technical debt accrued during Phase 1.

1. What required Phase 1 functionality was not implemented and why? 
    <!-- PM: When editing this template for your submission, you may remove this guidance -->
    * These high-priority items are to be added to the Phase 1' backlog that appears on your Jira board.
    * This should be a list of items from the Phase 1 backlog that were not "checked off".
    * List this based on a component-by-component basis, as appropriate.
        * Admin-cli
            * None
        * Backend
            * Getting Dokku up and connected to the frontend
        * Mobile FE
            * None
        * Web FE
            * None

2. What technical debt did the team accrue during Phase 1?
    <!-- PM: When editing this template for your submission, you may remove this guidance -->
    * These items are to be added to the Phase 1' backlog that appears on your Jira board.
    * List this based on a component-by-component basis, as appropriate.
        * Admin-cli
            * Error Handling, Hardcoded SQL, Scalability, Command-Line Interface (CLI)
        * Backend
            * The structure of the files
        * Mobile FE
            * None
        * Web FE
            * Automatic unit tests, App.jsx refactoring


## End of Phase 1' [20 points total]
Report out on the Phase 1' backlog as it stands at the conclusion of Phase 1'.

1. What required Phase 1 functionality still has not been implemented or is not operating properly and why?
    * None

2. What technical debt remains?
    * Backened
        * The structure of the files 

3. If there was any remaining Phase 1 functionality that needed to be implemented in Phase 1', what did the PM do to assist in the effort of getting this functionality implemented and operating properly?
    * No the app currently has all the functionality needed

4. Describe how the team worked together in Phase 1'. Were all members engaged? Was the work started early in the week or was there significant procrastination?
    * All team members worked well during the phase and started early and were consistently on task

5. What might you suggest the team or the next PM "start", "stop", or "continue" doing in the next Phase?
    * Something the team should start doing is having more meetings and not just seeing eachother twice a week
    * Something the team should stop doing is trying to do too much and just making sure what is needed to get done gets done
    * Something the team should continue to do is reaching out for help quickly when something doesn't work

## Role reporting [50 points total]
Report-out on each team members' activity, from the PM perspective (you may seek input where appropriate, but this is primarily a PM related activity).
**In general, when answering the below you should highlight any changes from last week.**

### Back-end
What did the back-end developer do during Phase 1'?
1. Overall evaluation of back-end development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)
    * It was very hard, but having the extra week to finalize any issues that were having regarding
    changing routes, deploying to dokku, or with frontend/React and mobile. Jira was used to group
    big tasks and keep track of progress, but more emphasis was placed on Slack communication and
    in-person question asking and debugging for issues. Tasks were verified by using Postman to
    check locally if the routes work given a certain JSON body (if needed), by getting confirmation
    from frontend and mobile that routes were working, and by asking PM/other team members
    about design docs to see any changes/testing ideas.
2. List your back-end's REST API endpoints
    * Show all posts
    * Create new post
    * Show single post
    * Like a post
    * Edit post
    * Delete post
    * Remove a like
3. Assess the quality of the back-end code
    * The code is in better shape than the initial implement sprint, but has some design choices that are
    less than desirable. First of all, because of the amount of changing routes, App.java and
    Database.java are still massive, over 500 lines of code each, with a couple overridden requests to
    different routes, such as a POST to /posts and to /posts/{id}/like which add a new post to the app
    and like a post respectively. The docs folder has been added and updated to reflect the new
    routes, and the files are in the tutorial format for the most part to work with Maven. That being
    said, CORS not being able to work with mobile was an issue (brought up to our mentor), and a
    few different design choices would lead to both more security and readability issues.
4. Describe the code review process you employed for the back-end
    * For code review, it was mostly a do-as-you-go and do-as-you-need process, where we shared our
    code with each other when we ran into issues to bounce ideas or be able to describe common
    issues that needed to be addressed, with a final review before pushing to the pre-main and
    approving a pull request. That being said, for the most part, we were independent in our process,
    since we had good documentation and communication to figure out what we needed on our own.
5. What was the biggest issue that came up in code review of the back-end server?
    * The largest issue was deploying to dokku and the configuration changing in the deploy-dokku
    branch as well as coordinating with the frontend and mobile team members to get all of the
    routes working in the right way, since we kept finding small things that were different that would
    cause different errors. 
6. Is the back-end code appropriately organized into files / classes / packages?
    * Yes, the code is still in the proper format for Maven, having a source and target, with main and
    test code separated out, and the pom.xml configuring tests and the app itself.
7. Are the dependencies in the `pom.xml` file appropriate? Were there any unexpected dependencies added to the program?
    * Yes, the dependencies are the same as before, and all have some sort of purpose in building or
    configuring the project to work in Maven
8. Evaluate the quality of the unit tests for the back-end
    * There are 6 different tests that are being run on the backend. All of which are using the mock
    (in-data) database. First, there is a test to get all posts in the proper JSON format. The next test
    uses a GET request to only get a test of a certain id with the proper JSON body, comparing the
    output to the expected output. To create a post, we test the POST request sending a title,
    contents, default false is liked boolean. To test the PUT, we have an update test with the title and
    contents. To test the DELETE, we have a delete post test to verify that a test is created and
    deleted in the mock database. Finally, we test liking the post, which only changes the liked value
    from false to true. Each of the tests work for their routes, but another test for each of the routes,
    connecting to the database, and checking how the backend handles invalid JSON could all be
    good choices for more tests, because the more tests we have, the better.
9. Describe any technical debt you see in the back-end
    * There are a few different areas where technical debt can be seen in the backend. The big one
    already mentioned is the structure of the actual files in the backend, since App.java and
    Datbase.java are 500+ line code monsters, and despite having comments, are unreadable.
    Splitting these into their own respective requests will not change actual functionality, but will
    help a lot to allow others to read the code which will help in the future to catch errors and catch
    up quicker. Additionally, creating and updating more JavaDoc documentation is important again
    to fill everyone in with how to run everything. Finally, adding additional unit/integration tests to
    test the code to make sure it works as expected is important, and something that should be done
    going forward and seen as tech debt

### Admin
What did the admin front-end developer do during Phase 1'?
1. Overall evaluation of admin app development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)
    * The development process of the admin app followed structured steps, from connecting to the database to providing a command-line interface for basic table operations (e.g., create, delete, insert, update). The use of Jira was appropriate for task management, especially as the tasks were modeled after tutorial examples of how to break down tasks based on a rubric. Tasks were created with clear objectives, and completion was verified through implementation and subsequent testing (both manually and using unit tests). However, improvements could be made by creating more granular tasks and tracking edge cases more comprehensively.
2. Describe the tables created by the admin app
    * The admin app creates a dynamically named table (tblData) with three columns:
	    * id: A serial primary key that auto-increments.
	    * subject: A VARCHAR field with a maximum length of 50, representing the subject of a row.
	    * message: A VARCHAR field with a maximum length of 500, representing the body message of a row.
    The dynamic nature of the table naming allows for flexibility in creating different tables during testing or production.
3. Assess the quality of the admin code
    * The code is well-structured and clearly separates responsibilities. The admin logic is encapsulated within the App class, and the database logic is cleanly managed within the Database class. Code readability is high, with descriptive method names and thoughtful error handling, though some improvements could be made to manage exceptions more consistently (e.g., avoiding repetitive catch blocks). Furthermore, some methods could benefit from refactoring to reduce the size of try-catch blocks and ensure more graceful exception management.
4. Describe the code review process you employed for the admin app
    * The code review process involved multiple steps:
	    * Automated Testing: Unit tests for each CRUD operation were developed to ensure functionality.
	    * Manual Code Inspection: Code was reviewed for adherence to coding standards, including naming conventions, class/method structure, and proper use of Java features.
	    * Test Coverage: All major database operations were tested, ensuring coverage for creating, selecting, updating, and deleting data.
    The code was reviewed by peers, with a focus on maintainability, readability, and performance considerations.
5. What was the biggest issue that came up in code review of the admin app?
    * The biggest issue identified during the code review was the lack of more advanced error handling and rollback mechanisms in case of SQL failures. The app currently terminates the connection upon a SQL exception, which might not be ideal for real-world production environments. Implementing transactions for multi-step operations and adding retries for transient failures would improve reliability.
6. Is the admin app code appropriately organized into files / classes / packages?
    * The code is appropriately organized into classes (App, Database, and RowData) and packages. The use of static inner classes, like RowData, effectively encapsulates row-related data and reduces external dependencies. However, the App class could be broken down further by abstracting command-line parsing logic into its own class to improve separation of concerns and maintainability.
7. Are the dependencies in the `pom.xml` file appropriate? Were there any unexpected dependencies added to the program?
    * The program likely uses standard dependencies for JDBC to connect to PostgreSQL databases and JUnit for testing. There were no unexpected dependencies mentioned in the provided code. However, a thorough check would involve reviewing the actual pom.xml file to ensure only necessary libraries were included. If new dependencies were introduced without reason, they could pose challenges in terms of project size and security vulnerabilities.
8. Evaluate the quality of the unit tests for the admin app
    * The unit tests are effective in covering the basic CRUD operations for the database. They utilize a dynamic table naming strategy to avoid conflicts during testing, ensuring that each test runs in isolation. The tests check for both expected functionality (e.g., correct data retrieval after insertion) and failure cases (e.g., null data). However, the tests could be expanded to cover edge cases, such as invalid input handling and testing performance with larger datasets.
9. Describe any technical debt you see in the admin app
    * The following aspects of technical debt were observed:
	    * Error Handling: While the app handles some exceptions, it lacks robust transaction management or retry logic,which could cause issues in high-concurrency environments.
	    * Hardcoded SQL: There are hardcoded SQL statements scattered throughout the code, which may cause maintenance challenges if the table schema changes. Abstracting SQL queries into constants or using an ORM would improve flexibility.
	    * Scalability: The current design is suitable for basic CRUD operations but may struggle with performance and concurrency issues if scaled up. Refactoring the database connection management and implementing connection pooling would be necessary for better scalability.
	    * Command-Line Interface (CLI): The CLI parsing could be refactored to a more structured approach, allowing better extensibility and more user-friendly command processing.

### Web
What did the web front-end developer do during Phase 1'?
1. Overall evaluation of Web development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)
    * Web development went well with some minor issues coming up and being promptly resolved. Jira was used to managed the said issues.
2. Describe the different models and other templates used to provide the web front-end's user interface
    * Nothing new was used apart from React + Vite template.
3. Assess the quality of the Web front-end code
    * 9/10 - Needs some refactoring.
4. Describe the code review process you employed for the Web front-end
    * Went over the code and did thorough testing to make sure everything works as expected.
5. What was the biggest issue that came up in code review of the Web front-end?
    * The need to refactor the code to refresh the page on change
6. Is the Web front-end code appropriately organized into files / classes / packages?
    * Yes
7. Are the dependencies in the `package.json` file appropriate? Were there any unexpected dependencies added to the program?
    * All dependencies are managed by Vite automatically
8. Evaluate the quality of the unit tests for the Web front-end 
    * 5/10 - still unable to run Jest, requires manual testing
9. Describe any technical debt you see in the Web front-end
    * Automatic unit tests, App.jsx refactoring

### Mobile
What did the mobile front-end developer do during Phase 1'?
1. Overall evaluation of Mobile development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)
    * Due to the use of Expo Go, React and React Native had to be learned which appended a lot of time to the development process. Jira was used consistently as a means of mapping out decisions and tracking progress. Jira tasks were planned out in advance and were checked off when stable committed code demonstrated functionality.
2. Describe the activities that comprise the Mobile app
    * The mobile app consists of a main screen with a header and a scrollable list of posts. Individual posts can be liked or unliked. There is an button that pulls up an “Add Post” card to fill in post information and add onto the list.
3. Assess the quality of the Mobile code
    * The quality of the code is quite high. Lots of refactoring was done on the overall code and the structure is modular in nature. No individual file surpasses 125 lines of code.
4. Describe the code review process you employed for the Mobile front-end
    * The code review process was basic. A pull request was made from the mobile branch with an appropriate title and description.
5. What was the biggest issue that came up in code review of the Mobile front-end?
    * No issues came up during the code review process.
6. Is the Mobile front-end code appropriately organized into files / classes / packages?
    * The Mobile front-end code is organized into the main app, assets, and components in an attempt at improving readability. 
7. Are the dependencies in the `pubspec.yaml` (or build.gradle) file appropriate? Were there any unexpected dependencies added to the program?
    * The dependencies can be found in the file “package.json”. None of the dependencies are unexpected. All of the dependencies are for Expo, React, or React Native paper. There is an additional dependency of Jest for unit testing purposes.
8. Evaluate the quality of the unit tests for the Mobile front-end here
    * There are 18 unit tests across the Mobile front-end which include a variety of logic and rendering tests. They are well organized and easily expandable. Their effectiveness was proved during their use in bug fixing.
9. Describe any technical debt you see in the Mobile front-end here
    * The amount of refactoring has eliminated any major sources of technical debt. If there is current technical debt, it is currently not big or noticeable enough to be considered currently.

### Project Management
Self-evaluation of PM performance
1. When did your team meet with your mentor, and for how long?
    * We met with our mentor during the recitation time and for about 30 mins.
2. Describe your use of Jira.  Did you have too much detail?  Too little?  Just enough? Did you implement policies around its use (if so, what were they?)?
    * Jira use was good but each task could probably use a bit more detail so that it is easier to read for everyone.
3. How did you conduct team meetings?  How did your team interact outside of these meetings?
    * Team meetings were once a week and were used as a group check in. Outside of the meetings slack was used to make sure everyone was on task and good to finish in time.
4. What techniques (daily check-ins/scrums, team programming, timelines, Jira use, group design exercises) did you use to mitigate risk? Highlight any changes from last week.
    * I checked in regularly and make sure that jira was also up to date. Didn't feel that any changes needed to be made from last week.
5. Describe any difficulties you faced in managing the interactions among your teammates. Were there any team issues that arose? If not, what do you believe is keeping things so constructive?
    * No difficulties faced and I believe that is because as a team we all understand each others strengths and weaknesses.
6. Describe the most significant obstacle or difficulty your team faced.
    * The most significant obstacle was getting dokku connected to the front end which took the entire sprint but in the end we were able to get it done.
7. What is your biggest concern as you think ahead to the next phase of the project? To the next sprint? What steps can the team take to reduce your concern?
    * The biggest concern is switching roles as everyone is going to be doing something new and work off of other peoples code from before.
8. How well did you estimate time during the early part of the phase?  How did your time estimates change as the phase progressed?
    * We estimated out time well during the early part of the phase and into the later parts as well. 
9. What aspects of the project would cause concern for your customer right now, if any?
    * In our current state I don't believe there are any major concerns. 