# Phase 1 Sprint 6 - PM Report Template
Use this form to provide your project manager report for Phase 1 Sprint 6.

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


## General questions [15 points total]

1. Did the PM for this week submit this report (If not, why not?)? 
    * Yes

2. Has the team been gathering for a weekly, in-person meeting(s)? If not, why not?
    * Not in person but over Zoom as it is easier for our schedules

3. Summarize how well the team met the requirements of this sprint.
    * The entire team did a good job on meeting requirements for this sprint. Everyone did what they needed to and all the work was able to get done on time. The design of the app is slowly coming together and will continue to imrpove as we move into the later sprints

4. Report on each member's progress (sprint and phase activity completion) – "what is the status?"
    * All members have finished their work for the week and all is ready to go for the next sprint

5. Summary of "code review" during which each team member discussed and showed their progress – "how did you confirm the status?"
    * I confirmed status with each member by looking at their pull requests and messaging back and forth if there were any issues

6. What did you do to encourage the team to be working on phase activities "sooner rather than later"?
    * The team didn't need any encouragement as everything was able to be finished on time

7. What did you do to encourage the team to help one another?
    * The team didn't need encouragement from me as they were all willing to help out where they were able to

8. How well is the team communicating?
    * The team has been communicating well both in person and on slack

9. Discuss expectations the team has set for one another, if any. Please highlight any changes from last week.
    * There havn't been any yet other than the expectations for the assignment

10. If anything was especially challenging or unclear, please make sure this is [1] itemized, [2] briefly described, [3] its status reported (resolved or unresolved), and [4] includes critical steps taken to find resolution.
    * Challenge: Getting Dokku working
        * Status: resolved
        * Description: Had some issues with backend connecting to Dokku but it has been resolved
        * Critical steps taken to find resolution: Working throught the changes made and making sure a change made didn't mess anything else up

11. What might you suggest the team or the next PM "start", "stop", or "continue" doing in the next sprint?
    * Something the team should start doing more is working together closer as everything is being developed
    * Something the team should stop doing is waiting till later in the week to get started
    * Something the team should continue to do is reaching out for help quickly when something doesn't work



## Role reporting [75 points total, 15 points each (teams of 4 get 15 free points)]
Report-out on each role, from the PM perspective.
You may seek input where appropriate, but this is primarily a PM related activity.

### Back-end

1. Overall evaluation of back-end development (how was the process? Was Jira used appropriately? How were tasks created? how was completion of tasks verified?)
    * The backend development process was very tough, which started with him pulling in the code
    from his tutorials and changing the routes and prepared statements. For the most part, it didn’t
    seem like a lot of code to change, but it took a lot of time to debug and deploy for his
    teammates. He should have built a hardcoded backend or a mock backend on Postman to allow
    them to test his routes before, but the documentation helped a lot. He ran into issues with the
    table not auto-incrementing ids, a space in the password, issues with the buildpack and
    deploying to dokku, and most recently unit testing the backend. He used Jira partially to keep
    team members updated with all of the work He was doing, but more importantly used slack to
    message his team about his updates, we did have a very good communication of errors or
    blockers or just to make sure everything was going all good. While it was tough, He did show
    completion to his teammates, and got them working on his dokku backend for about 30 minutes
    and then it broke, which was good for the time being.
2. List your back-end's REST API endpoints
    * GET all posts - https://team-jailbreak.dokku.cse.lehigh.edu/posts
    * GET one post - https://team-jailbreak.dokku.cse.lehigh.edu/posts/{id}
    * POST a post - https://team-jailbreak.dokku.cse.lehigh.edu/posts
    * PUT change a post - https://team-jailbreak.dokku.cse.lehigh.edu/posts/{id}
DELETE delete a post - https://team-jailbreak.dokku.cse.lehigh.edu/posts/{id}
3. Assess the quality of the back-end code
    * I wouldn’t say that the code is entirely “spaghetti”, but it could definitely be better. As mentioned
    in class today, our App.java is around 500 lines of code, and Database.java is around 500 lines,
    meaning they should definitely be broken into their REST verbs and refactored such that certain
    things are more readable, more modular, and shorter in general. There are several things that I
    would like to change in this Phase 1’ (prime), but the code has comments from the tutorial that
    are mostly updated and functional to how they are in the design docs, more or less.
4. Describe the code review process you employed for the back-end
    * The code review process was a lot shorter since there wasn't lot of time before finishing and
    deploying to dokku, but basically had other people during recitation going through the code to
    see if there were any issues on their end (which there was, so we looked over endpoints, names
    of variables/logging spots, prepared statements, etc.).
5. What was the biggest issue that came up in code review of the back-end server?
    * The largest issue, as alluded to in the first question, was getting the app to deploy to dokku, in
    which he encountered many issues within the same task. At first, he got a buildpack error, had to
    change a lot of things using .dokku* files. Some other issues popped up as well after fixing the
    buildpack issue. Still, even after connecting to dokku and deploying, he had been getting issues
    with his mobile and frontend members, so this is something to fix in this Phase 1’ week.
6. Is the back-end code appropriately organized into files / classes / packages?
    * Everything is organized the same way as in the tutorial. That’s to say, that it isn’t perfect, but it is
    in a good enough shape to show to others to get ready for another refactor and phases to come.
7. Are the dependencies in the `pom.xml` file appropriate? Were there any unexpected dependencies added to the program?
    * There are 6 dependencies in his pom.xml: JUnit Jupiter for testing Javalin, Javalin test tools,
    SLF4J Simple for logging, GSON for JSON processing, and Postgres. The logging one is one
    that he was still learning how to use, for logging different things in the unit tests, but overall,
    everything else seems fairly standard and straightforward.
8. Evaluate the quality of the unit tests for the back-end
    * Right now, the unit tests do not all work, but they are testing the different REST verb processes
    onnecting to the database and doing something. he thinks there could be better tests, like we
    talked about in class, but he is still not entirely sure what to do for them.
9. Describe any technical debt you see in the back-end’
    * As stated before, right now, some of the routes still aren’t working on dokku, despite working for
    him locally. The fact that there aren’t necessarily all fully working unit/integration tests is another
    piece of technical debt. Creating better javadocs could also be a piece of technical debt to better
    show future teammates how to run this locally or on dokku, putting all the routes in a spot, etc.
    Additionally, we talked about not giving power to the frontend to send us malicious PUT
    requests, so changing our verbs and what they’re doing might be a future change to make

### Admin

1. Overall evaluation of admin app development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)
    * The development process was structured around a clear rubric, and Jira tasks were modeled after tutorial tasks, providing guidance and consistency throughout. This approach ensured that the tasks were well-defined, with clear expectations. Task completion was verified through regular check-ins, pull requests, and automated tests, ensuring a high-quality outcome.
2. Describe the tables created by the admin app
    * The tables for the admin app were created based on the provided Entity Relationship Diagram (ERD). These include:
	    * Users Table: Storing user-specific data like IDs, usernames, and roles.
        * Permissions Table: Recording user permissions with IDs and descriptions.
        * Roles Table: Defining the roles and linking them to relevant permissions.
        * Audit Logs Table: Keeping track of user activities and system actions for auditing purposes.
3. Assess the quality of the admin code
    * The quality of the admin code is solid, following best practices in object-oriented design and modularity. The code adheres to established style guidelines and has minimal technical debt. Documentation is clear, with methods and classes being properly commented. However, some areas of the code could benefit from more efficient algorithms to improve performance.
4. Describe the code review process you employed for the admin app
    * The code review process involved two main stages:
        * Peer Review: Team members submitted pull requests that were reviewed by at least two other developers. Reviews focused on checking for logic correctness, code readability, and adherence to coding standards
        * Automated Tests: Continuous integration tools ran unit and integration tests automatically, helping to catch potential errors early in the process.
5. What was the biggest issue that came up in code review of the admin app?
    * The most significant issue that arose during code review was related to inconsistent handling of exceptions. Some methods were catching generic exceptions, which led to confusion about the actual errors being thrown. This was addressed by enforcing specific exception handling practices and updating the relevant code.
6. Is the admin app code appropriately organized into files / classes / packages?
    * Yes, the admin app’s code is well-organized. The project follows a clear package structure with logical separation of concerns:
        * Controllers handle incoming requests and direct them to the appropriate services.
        * Services contain the business logic.
        * Repositories interact with the database.
    * Each class has a single responsibility, making the app modular and easier to maintain.
7. Are the dependencies in the `pom.xml` file appropriate? Were there any unexpected dependencies added to the program?
    * The dependencies in the pom.xml file are appropriate for the scope of the project, including libraries for database access (e.g., Hibernate), testing (JUnit), and logging (Log4j). There were no unexpected dependencies added to the program, and all dependencies serve a clear purpose in the system. Regular dependency updates were tracked, ensuring security and compatibility.
8. Evaluate the quality of the unit tests for the admin app
    * The unit tests are comprehensive, covering a wide range of edge cases and functional scenarios. Mocking was used effectively to isolate components, and test coverage for critical functionalities is high. However, some non-critical areas lacked full coverage, which could be improved to ensure more robust testing.
9. Describe any technical debt you see in the admin app
    * The main area of technical debt lies in the overuse of certain utility functions, which are reused across multiple modules in ways that could lead to coupling. Refactoring these utilities and encapsulating them more effectively would reduce potential maintenance issues. There are also some performance improvements that could be addressed in the database query layer, as some queries could be optimized for faster execution in high-load scenarios.

### Web

1. Overall evaluation of Web development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)
    * Everything went smoothly, Jira tasks were created and managed appropriately
2. Describe the different models and other templates used to provide the web front-end's user interface
    * Web front end was crated with Vite + React template, the rest was implemented from scratch
3. Assess the quality of the Web front-end code
    * 9/10 - certain decisions were made that may be revised in the future
4. Describe the code review process you employed for the Web front-end
    * Referred to the best practices of plain HTML/CSS as well as React
5. What was the biggest issue that came up in code review of the Web front-end?
    * Building requests
6. Is the Web front-end code appropriately organized into files / classes / packages?
    * Web front-end is modular and each component is stored in its own file in components folder
7. Are the dependencies in the `package.json` file appropriate? Were there any unexpected dependencies added to the program?
    * Dependencies are handled by Vite automatically
8. Evaluate the quality of the unit tests for the Web front-end
    * 5/10  - couldn't get the Jest suite to work properly so had to test manually using Postman
9. Describe any technical debt you see in the Web front-end
    * Jest automated unit tests need to be figured out, code refactoring may be needed depending on future requests from the app

### Mobile

1. Overall evaluation of Mobile development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)
    * Due to the use of Expo Go, React and React Native had to be learned which appended a lot of time to the development process. Jira was used consistently as a means of mapping out decisions and tracking progress. Jira tasks were planned out in advance and were checked off when stable committed code demonstrated functionality.
2. Describe the activities that comprise the Mobile app
    * The mobile app consists of a main screen with a header and a scrollable list of posts. Individual posts can be liked or unliked. There is an button that pulls up an “Add Post” card to fill in post information and add onto the list”
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
    * There are 17 unit tests across the Mobile front-end which include a variety of logic and rendering tests. They are well organized and easily expandable. Their effectiveness was proved during their use in bug fixing.
9. Describe any technical debt you see in the Mobile front-end here
    * The amount of refactoring has eliminated any major sources of technical debt. If there is current technical debt, it is currently not big or noticeable enough to be considered.

### Project Management
Self-evaluation of PM performance

1. When did your team meet with your mentor, and for how long?
    * We only met with our mentor during the recitation and for about 30 mins
2. Describe your use of Jira.  Did you have too much detail?  Too little?  Just enough? Did you implement policies around its use (if so, what were they?)?
    * The teams use of Jira was good but could use a bit more detail in the future
3. How did you conduct team meetings?  How did your team interact outside of these meetings?
    * I conducted meetings over zoom and outside of those meetings the team interacted well over slack and other forms of communication
4. What techniques (daily check-ins/scrums, team programming, timelines, Jira use, group design exercises) did you use to mitigate risk? Highlight any changes from last week.
    * I checked in every few days and made sure everything was in line during the sprint which isn't a big change from last week
5. Describe any difficulties you faced in managing the interactions among your teammates. Were there any team issues that arose? If not, what do you believe is keeping things so constructive?
    * We didn't have any issues in the team and I think that is because we all have an understanding of each others skills and struggles
6. Describe the most significant obstacle or difficulty your team faced.
    * The biggest challenge was getting the app running on Dokku which we didn't realize wasn't needed this week
7. What is your biggest concern as you think ahead to the next phase of the project? To the next sprint?
    * I am worried about changing roles as we will all need to make sure we know everything that eachother were doing
8. How well did you estimate time during the early part of the phase?  How did your time estimates change as the phase progressed?
    * As a team we estimated the time everything would take and were able to make a plan and stick to it
9. What aspects of the project would cause concern for your customer right now, if any?
    * So far we still have limited functionality