# Phase 2 Sprint 9 - PM Report 

### Team Information:

* Number: 32 
* Name: Jailbreak
* Mentor: Emir Veziroglu, env225@lehigh.edu
* Weekly live & synchronous meeting: 
    * without mentor: Thursday (10/31) @ 4:30 PM
    * with mentor: Because of Election Day, we were unable to meet with Emir all together.

### Team Roles:

* Project Manger: <Dean Clark, dmc227@lehigh.edu>
    * Changed with start of new phase (Phase 2)
* Backend developer: <Illia Slipchenko, ils226@lehigh.edu>
* Admin developer: <Liam Messinger, ljm426@lehigh.edu>
* Web developer: <Anaya Rawoot, adr325@lehigh.edu>
* Mobile developer: <Jared Cooper, jac327@lehigh.edu>

### Essential links for this project:

* Team's Dokku URL(s)
    * <https://team-jailbreak.dokku.cse.lehigh.edu>
* Team's software repo (bitbucket)
    * <https://bitbucket.org/sml3/cse216_fa24_team_32>
* Team's Jira board
    * <https://lehigh-team-drfppe9i.atlassian.net/jira/software/projects/TC/boards/2>

Screenshot of Jira board:
![Jira Board for Sprint 9](../Jira_board_sprint09.png)

## General questions

1. Did the PM for this week submit this report (If not, why not?)? 
    Yes, I (Dean Clark) am submitting the report.

2. Has the team been gathering for a weekly, in-person meeting(s)? If not, why not?
    Yes, we have been meeting weekly to discuss the different aspects of the project.

3. Summarize how well the team met the requirements of this sprint.
    To put it simply, this was a tough sprint as anticipated by our team. That being said, we were able to implement all of the backend functionality that was required for the sprint, but because of time constraints, were unable to get everything to work for the fronted and mobile. As for Admin, all of the new tables were created, and the command line application was updated to be able to invalidate any user or post on the app. One of the only setbacks we have right now was on getting OAuth to work for the frontend and mobile, and a few other small updates on housing the profile information on the profile page of the app. Tests were being written for each of the components and documentation was also updated to reflect what was happening in this phase and sprint specifically, and all of the other problems were put as child issues in the Jira board to notify future team members and me about what is going on. 

4. Report on each member's progress (sprint and phase activity completion) – "what is the status?"
    <b>Admin:</b> Everything is up to date, there shouldn't be any tech debt currently. The JavaDocs have even been updated and the unit tests were shown in the video passing when running mvn test before running the command line program and showing the new features.
    <b>Backend:</b> It seems like all of the functionality is finished from the routes on Postman, and was shown running locally with the authentification only working for Lehigh accounts. I don't think that it was uploaded to Dokku yet, which will be added to tech debt, as well as updated documentation and unit tests to follow. However, for this being such a hard sprint, I think the bulk of the work has been done to properly set up OAuth and the new user information. This will be wrapped up within the next week. 
    <b>Frontend:</b> The sign in screen has been formed for the app, which I think was working, but the profile page still needs some work with it, and the upvotes and downvotes have been created and converted from what we had before. Additionally, documentation and unit tests could be updated, which I will talk to and work with them with to get done for the next sprint. 
    <b>Mobile:</b> Mobile seems to be in a pretty similar spot as frontend. I saw the sign in screen which was still having some issues, I know that the upvotes and downvotes were implemented properly and a page was made for the profile, but needs to connect to the backend. I think that I could also help with the documentation and/or tests, I know the tests are a little different than frontend but we should be good to go after the next week for Phase 2 prime.

5. Summary of "code review" during which each team member discussed and showed their progress – "how did you confirm the status?"
    For code reviews, I would check as on-demand, whenever anyone asks me to, as well as whenever a pull request was sent to me to review. For the most part, I would do this on my own time and address any problems with the code through BitBucket or messaging them. I watched over everyone's videos, Jira activity, and commit history because I wanted to understand what everyone was doing, and encouraged others to share their issues or their progress via Slack. 

6. What did you do to encourage the team to be working on phase activities "sooner rather than later"?
    Similarly to last time, I would send messages to my teammates regarding their progress, and in our synchronous meetings. Additionally, I did keep saying it was going to be a hard sprint, to try to help everyone understand what would be expected of them.

7. What did you do to encourage the team to help one another?
    Again, the frontend/mobile and backend need to work close together to get all of the routes to work together, so teamwork was sort of always expected. This group met on Tuesday to get some things working as a whole, which seemed to help, but besides this, I offered any help with anyone's issues. 

8. How well is the team communicating?
    For the most part, I think we have been communicating alright, there were a few problems that we figured out by talking, and our backend and frontend/mobile met by themselves to try to get some stuff agreed upon, but for the most part, we have been active enough on Slack but not too much. No issues, just as the Project Manager, I'd like to know what's going on a little bit more.

9. Discuss expectations the team has set for one another, if any. Please highlight any changes from last week.
    Nothing changed too much from last week, the main thing was just communicating more about updates about how everyone was doing. Checking over each other's work when doing pull request, to generally try to write everything in a way in which you would like to find it, both for the code and the documentation, and other positive practices. 

10. If anything was especially challenging or unclear, please make sure this is [1] itemized, [2] briefly described, [3] its status reported (resolved or unresolved), and [4] includes critical steps taken to find resolution.
    * Challenge: Get the frontend/mobile to prorperly handle accounts
        * Status: unresolved
        * Description: Both have a page for the authorization, but is not connecting to the backend properly, which is giving errors logging in which does not allow the profile information to populate either. 
        * Critical steps taken to find resolution: Get these groups talking to the backend about how exactly to format everything and getting it to work with their frontend code. Similarly, reaching out for help from any of the other members to help with keeping up with the sprint's tasks would be helpful. 


11. What might you suggest the team or the next PM "start", "stop", or "continue" doing in the next sprint?
    For "start", I think we should ask for more help or work more collaboratively. Especially since I do not have that many tasks for these sprints, I would love to help out and split up some work if I can to try to get everything done in a better or more optimal manner. 
    For "stop", I think we are pretty good, I don't think there is too much that we do right now that is destructive or bad for our group. Maybe stop allowing perfect to deter us from a good minimum viable, or just work on designing things as they come up, and not worry about over or under doing the minimum viable. Most of it is laid out in the phase description, or can be a design choice by our group, so worrying too much about small things that may get changed in the future is okay for now. 
    For "continue", we should continue asking questions of each other to gain more knowledge of what we are trying to do, or asking for help about a certain topic. 

## Role reporting

### Back-end

1. Overall evaluation of back-end development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)
  Jira was used to show subtasks, tasks were created in the same structure of phase 1 and the tutorials, and split up some of the files for OAuth which was helpful. Everything functionally was created and verified using Postman and the frontend/mobile team members seeing if it worked.  

2. List your back-end's REST API endpoints
A good amount of the API routes were not changed from phase 1. Those being Getting all the posts, Creating a new post, showing a singular post, liking a post and deleting a like. Next is a post request to /auth/validate/{idToken}, which is a POST route that returns user data upon successful token validation. We had a GET route for all of the users at /users. There is also a POST route to add a new user to the users database. There were also two POST routes that were very similar for adding an upvote and downvote which handles the issue of upvoting when upvoted, downvoting when downvoted, upvoting when downvoted, and downvoting when upvoted. These are at POST /posts/{post_id}/{user_id}/[upvote or downvote]. There should also be a GET to /users/{user_id} and a PUT to /users/{user_id} to get all of the user's information for themself as well as changing anything when asked to.

3. Assess the quality of the back-end code
It seems to be pretty good, I could follow everything, it had comments for important parts, it was functional and tried to reduce and reuse where it could, and had tests to ensure that it worked. 

4. Describe the code review process you employed for the back-end
As with all of the members, I reviewed the code when the pull request was made, paying close attention to the routes and the way that Illia was handling the different actions of OAuth safely. It looked pretty good, and I knew since he had done it before that the approach was correct for sure. 

5. What was the biggest issue that came up in code review of the back-end server?
Getting it to work with the frontend and mobile branches was an issue since they did not have a lot of time to work with it, but overall, everything else seemed to work out pretty well. 

6. Is the back-end code appropriately organized into files / classes / packages?\
Yes, as mentioned before, it is organized the same was as the phase 1 and tutorial code.

7. Are the dependencies in the `pom.xml` file appropriate? Were there any unexpected dependencies added to the program?
No, all of the dependencies make sense to have in the pom.xml, and the only ones added are those for the Google API client, which were:
```
    <dependency>
      <groupId>com.google.api-client</groupId>
      <artifactId>google-api-client</artifactId>
      <version>1.32.1</version>
    </dependency>
    <dependency>
      <groupId>com.google.oauth-client</groupId>
      <artifactId>google-oauth-client</artifactId>
      <version>1.33.3</version>
    </dependency>
    <dependency>
      <groupId>com.google.http-client</groupId>
      <artifactId>google-http-client</artifactId>
      <version>1.43.0</version>
    </dependency>
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>gson</artifactId>
      <version>2.11.0</version>
    </dependency>
```
8. Evaluate the quality of the unit tests for the back-end
New tests were written to test the behavior of the new upvote/downvotes as well as the user profile information passed in. It seems like the ones already written were passed, and that more will be written in the next sprint to test all the new features from the sprint.
9. Describe any technical debt you see in the back-end
I think for the most part it is small things, as all the core functionality for a minimum viable is done at this time. There is some technical debt in the documentation department that remains from when I was on backend in phase 1, as well as some files that could still be cut down a bit and organized a bit differently so that they are a bit shorter, but overall, doing all that and writing a few more unit tests should be pretty good for the backend. 

### Admin

1. Overall evaluation of admin app development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)
    Jira was used properly as stated before, having a singular task for the one member and child tasks under it describing each of the core features needed by the member. The tasks were verified in my code review, where I cross-referenced the tables with our ERD (which we changed once during this sprint), as well as when the backend developer had to interact with the database when writing the backend code. 
2. Describe the tables created by the admin app
    We have 5 tables: First, we have the post table, with the id as the primary key, author id as a foreign key, both of which are ints. Then we have the title and contents, which are 100 and 300 character strings respectively, and a valid boolean to check if it has been invalidated by the admin.
    After this, we have the user table, which is connected to the post table having its id as the foreign key for the author id in the post table. it also holds values for email, username, gender identity, sexual orientation, and notes, which were all made to be 255 character strings. It also has the valid boolean to check if the user has been invalidated by the admin.
    We have a separate table for both upvotes and downvotes, but are very similar. The primary key is id, with a foreign key of the author id, and has another foreign key to the table post for its parent post id which takes from the id entity of the post table. 
    Finally, there is a comment table, which has its own id, the parent post id which is a foreign key to id in posts, contents which is a 300 character string to match the contents of a normal message, and an author foreign key to the author foreign key in posts.
3. Assess the quality of the admin code
    The admin code is well organized, in the same way as the backend code, the same as in phase 1 and the tutorial. There are many unit tests run before the CLI program initializes, and lots of comments throughout to describe what is happening.
4. Describe the code review process you employed for the admin app
    As with all of the members, I reviewed the code when the pull request was made, paying close attention to how the tables were created and the new features of invalidating a user or a post. 
5. What was the biggest issue that came up in code review of the admin app?
    There was a slight descrepency between admin and backend which led us to change part of our table, which was the only real bump in the road that I can remember. The rest seemed to be pretty good, both with initially coming up with the table, implementing it, and testing its proper functionality.
6. Is the admin app code appropriately organized into files / classes / packages?
    Yes, it is organized the same way as how we had it before. 
7. Are the dependencies in the `pom.xml` file appropriate? Were there any unexpected dependencies added to the program?
    The same dependencies as in Phase 1 were used, which is only 2: JUnit and Postgres.
8. Evaluate the quality of the unit tests for the admin app
    There are all of the tests from phase 1, as well as a new test to invalidate a row in the database, which does it for either a user or a post. This was all that was really added in this sprint, so I think that about covers the functionality that the command line program has at this moment. 
9. Describe any technical debt you see in the admin app
    Not much tech debt to be eliminated or addressed currently. The documentation is updated, the functionality is working for both phases 1 and 2, and there is a new test for all the new functionality.

### Web

1. Overall evaluation of Web development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)
    Jira was used properly, having a singular task for the frontend member and child tasks under it describing each of the core features needed by them. These tasks were checked to be completed when I ran my code review during the pull request, where I checked the mock UI to see if it had what we needed. 
2. Describe the different models and other templates used to provide the web front-end's user interface
    Since we are using React, we used our mock UI to come up with different components that would match our profile and Google sign in process.  
3. Assess the quality of the Web front-end code
    Everything is laid out very nice on the frontend side, and nothing seems too long for each file or directory. Everything is rendered in app.jsx, which is then exported and used in main.jsx. In a subfolder, there are components for each React component with the proper Pascal Case (which is the standard for React naming) title, which are all smaller pieces of the picture in making the frontend to be what it is. 
4. Describe the code review process you employed for the Web front-end
    Similar to all the other members, I reviewed all of the code when the pull request was made to me, paying close attention to each of the new components and how they were interacting with the calls to the backend routes for the API so that I could see if there were (or where) the errors were coming from when signing in.
5. What was the biggest issue that came up in code review of the Web front-end?
    Currently, the frontend could not connect to accounts. This was the largest challenge that was faced during this sprint, despite getting the upvotes and downvotes and profile page working, without that verification, the information wouldn't load in, so a temporary override mode where the user didn't need to be signed in was made. 
6. Is the Web front-end code appropriately organized into files / classes / packages?
    To my knowledge of React (I have been learning it during this phase since I am PM and will need it for either frontend or mobile next phase) yes, all of the components are in their own folder with the proper Pascal Case, and the app is being built and exported to the main.jsx, which is actually running it, which can be done by using npx run dev, or with Vite. 
7. Are the dependencies in the `package.json` file appropriate? Were there any unexpected dependencies added to the program?
    All of the new dependencies from phase 2 seem to be coming from Google's API, which seems to be correct. 
8. Evaluate the quality of the unit tests for the Web front-end
    The tests seem to be rendering separate components and asserting their output, which seems correct to me. If the OAuth was working properly, I think these would be pretty good tests to show if each part was working.
9. Describe any technical debt you see in the Web front-end
    I think the biggest thing at the moment is to get the login with Lehigh google account to work, since all the UI components are there and can be accessed if they hit the right routes and parse the JSON it receives right. Besides this, a litte overhaul with the documentation and a few new tests would be perfect to have to perfect everyhting and eliminate tech debt going forward.

### Mobile

1. Overall evaluation of Mobile development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)
    Jira was used in the same way as each of the other group members, with a singular task for mostly everything that the person had to do, with subtasks for each small feature that needed to be added for the minimum viable. I checked over all of these when conducting my code review during the pull request, which is where I checked it up against the routes that we decided on for the backend as well as the mock UI that was created.
2. Describe the activities that comprise the Mobile app
    We are using ExpoGo, which means we can use React Native to render our frontend onto our app. This means that the subfolders and project as a whole looks very similar to the way we are doing things on frontend. The components are laid out in a similar way, there is a subdirectory for OAuth specific files, and then a subdirectory for the app, which puts everything and exports in a file called _layout.tsx.
3. Assess the quality of the Mobile code
    Everything seems to be pretty well organized and in manageable pieces, which is something that I am enjoying about React, since all the components are separated out and can be used anywhere by simply importing then rendering it to the DOM, whihch is pretty cool. They're short files, have comments to explain anything uncertain, and are set up in a way that I think most developers can follow.
4. Describe the code review process you employed for the Mobile front-end
    Similarly to how I did for everyone else, I reviewed all of the code formally when I got a pull request, where I looked at how each new component was created and handling of OAuth using the new routes to see where there were any issues. 
5. What was the biggest issue that came up in code review of the Mobile front-end?
    Similarly to our frontend, the OAuth section of logging into our accout was not working on mobile either. An unauthorized user mode was added to account for this so we could still see the login page and profile, but could not actually connect to it. 
6. Is the Mobile front-end code appropriately organized into files / classes / packages?
    It is separated into the same structure as the frontend essentially, which is to say that it is well organized in the way that React code should be. 
7. Are the dependencies in the `pubspec.yaml` (or build.gradle) file appropriate? Were there any unexpected dependencies added to the program?
    Since we use ExpoGo which uses React Native to run our app, we have a package.json. The dependencies are pretty much the same as what is in the frontend's package.json, which only really added the Google OAuth API stuff, so not much else was added from what I can tell. 
8. Evaluate the quality of the unit tests for the Mobile front-end here
    There are tests for the components and for the physical OAuth connection itself. The components are mainly being rendered out and asserted based on their output as well as with synthetic post information, whereas the Auth tests work by trying to connect to the Google OAuth server. 
9. Describe any technical debt you see in the Mobile front-end here
    Again, the largest piece of technical debt is going to be getting the OAuth to work with the frontend, since it was working for the backend tests. Similarly, this would allow the profile information to populate instead of being blank. The only other thing that would need to be fixed at that point is the documentation to be updated sinec Liam already did an amazing job during phase 1 telling us how to actually set it up. 

### Project Management
Self-evaluation of PM performance

1. When did your team meet with your mentor, and for how long?
Since it was Election Day, we were unable to meet all together with Emir, but asked questions and met with him if we needed anything specific from him. 

2. Describe your use of Jira.  Did you have too much detail?  Too little?  Just enough? Did you implement policies around its use (if so, what were they?)?
For Jira, we for the most part keep it simple. Creating a singular task for each role and then creating child tasks for each of the required features or documentation or testing is how I do things. As of right now, I had not been writing out absolutely every subtask, which felt a little bit overkill and also people would not initially update them anyways so there was no point, however I did just enough along with talking to members to see what exactly was happening.  

3. How did you conduct team meetings?  How did your team interact outside of these meetings?
For team meetings, I would sort of facilitate a conversation between group members to see what their expectations were, what could be done together, when we could meet again, and any other questions people had. For the most part, though, everyone felt free to talk during the meetings, asking questions to each other and trying to get everything organized to be done for the next sprint.

4. What techniques (daily check-ins/scrums, team programming, timelines, Jira use, group design exercises) did you use to mitigate risk? Highlight any changes from last week.
    I tried not to overwhelm people with messages, but I would check up every day or so to see how everyone is doing, as well as putting stuff on Jira to see where everyone was with their work. Again, as for pair programming and code reviews, I would do this on-demand, not requiring but being open for anyone to come to me if they needed anything done.

5. Describe any difficulties you faced in managing the interactions among your teammates. Were there any team issues that arose? If not, what do you believe is keeping things so constructive?
There were no issues among teammmates, whenever anyone needed something of the other people, we would talk to each other and try to resolve it as soon as we could. Part of that is because we are able to honestly and openly communicate to each other, and part of that is that we are trying to be as resourceful as possible when it comes to what information we already have and using different resources to learn all of these new topics. Whether it be the internet, other teams, our mentor, or professors, we all want everything to work properly and having been putting off things, so when we run into issues, which can be earlier on due to how we do things, we ask the appropriate source and get it resolved before it snowballs to affect another person in the group.

6. Describe the most significant obstacle or difficulty your team faced.
The largest obstacle was most likely faced by our team was that on backend, as getting OAuth to work with the new type of upvotes and downvotes and all the new routes to connect to Google and also safely get that information back in JSON was the hardest. 

7. What is your biggest concern as you think ahead to the next phase of the project? To the next sprint?
Similarly to the question below, the biggest concern is that there are not authorization/security risks with logging into peoples' Lehigh emails, since we don't want to expose anyone's data, as well as their upvotes and downvotes directly to anyone else, which is why they shouldn't be in the JSON. Moving onto the next phase of the project, since we don't know what it is of yet, I don't know, but probably the largest risk is backend, since it is the mediator between the database and the frontend to access all of it, and needs to have everything working properly to ensure the app runs properly.

8. How well did you estimate time during the early part of the phase?  How did your time estimates change as the phase progressed?
I think we always anticipate it to take less time than it actually will, which is understandable, but makes it so that we need to spend more time in the second half of the sprint scrambling to get stuff done instead of polishing things, but overall, almost all the functionality got done for this week anyways, so wasn't a huge cause for concern.

9. What aspects of the project would cause concern for your customer right now, if any?
As of right now, the only real cause for concern would be getting the mobile and frontend app to connect to OAuth properly, since there were errors doing so, and getting the proper information for the profile and posts that people make. Besides that, everything else should be relatively in place, and the slight issues with that should be fixed by the end of the next sprint. 