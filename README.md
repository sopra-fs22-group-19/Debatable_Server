# It's Debatable Server Repository
![logo](https://github.com/sopra-fs22-group-19/Debatable_Client/blob/master/src/images/logo2.png)

## Introduction
It's debatable is an online debate platform where users can debate with their friends and family. They can have debate on existing debate topics or they can create their own debate topics. Users can also see their ongoing debates, ended debates, debates which are waiting to be started, etc.

Once both participants join the debate room, participant 1 can start the debate. Each participant will get 30 seconds to defend their side. Debates arguments can be translated in 5 different laguages namely Chinese, English, German, Greek, Spanish. 

If second participant joined as a guest user, they do not have any access to any feature of It's debatable except participanting on the debate they have invited to. However, they do have right to end the debate.

## Technologies
We used Java in the backend of It's debatable i.e. in this repository. The other frameworks which we used in backend are Spring, Rest, Heroku (for deployment), SonarCloud (code quality). We used Deepl api for translation. We used Javascript, HTML, CSS, SCSS and React for [It's debatable Client](https://github.com/sopra-fs22-group-19/Debatable_Client). We had weekly Scrums and the implementation period was divided into 2 sprints.

## High-level Components

### Database: Postgres
We use a Postgres Database hosted in Heroku. You can find the schema we used for our application in [DB schema.png](./documentation/DB%20schema.png)

### Translation API
We used the translation API from [DeepL](https://www.deepl.com/pro#developer), whose free tier for developers allowed us to translate up to 500,000 characters / month and includes a good offer of languages

### UserService/Controller and DebateService/Controller
We have two main classes to handle everything in our application:

 * [UserService.java](./src/main/java/ch/uzh/ifi/hase/soprafs22/service/UserService.java) / [UserController.java](./src/main/java/ch/uzh/ifi/hase/soprafs22/controller/UserController.java):
 Everything related to the handling of users like their creation/editing and authentication
 * [DebateService.java](./src/main/java/ch/uzh/ifi/hase/soprafs22/service/DebateService.java) / [DebateController.java](./src/main/java/ch/uzh/ifi/hase/soprafs22/controller/DebateController.java):
  Handle everything related to the execution of the debate as the creation of possible debate topics, debate rooms where users can discuss, and enforcing the rules of when can a user that is registered to the room intervene and post a message to it.

In the Controller classes you will find how the API requests are handled and on the Service class you will find the methods of how many of these actions are implemented. 

### WSMsgDebateRoomController

The classes [WSMsgDebateRoomController](./src/main/java/ch/uzh/ifi/hase/soprafs22/controller/WSDebateRoomController.java).java and [WebSocketConfig](./src/main/java/ch/uzh/ifi/hase/soprafs22/config/WebsocketConfig.java).java are used to handle the messages sent during a debate. To have a faster and more functional app, we have the messages be sent and broadcast to other users via WebSockets.
WSMsgDebateRoomController.java handles the actions taken when a message is received and before it is broadcasted (like saving it to the Database). WebSocketConfig.java handles how to subscribe to a specific channel for a debate.

### Class Diagram
To have a better picture of the different classes in our system, please have a look at the [class diagram](./documentation/Class%20Diagram.pdf).
Note that there is a good opportunity for refactoring by encapsulating better the different methods associated to the DebateService and separate/break it into different services associated to other entities like [DebateSpeaker.java](./src/main/java/ch/uzh/ifi/hase/soprafs22/entity/DebateSpeaker.java) and [DebateTopic.java](./src/main/java/ch/uzh/ifi/hase/soprafs22/entity/DebateTopic.java).
In its current state, our code more or less has one master class in [DebateService.java](./src/main/java/ch/uzh/ifi/hase/soprafs22/service/DebateService.java)

## Launch & Deployment

### Building with Gradle

You can use the local Gradle Wrapper to build the application.
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

#### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

### Test

```bash
./gradlew test
```

### Environment Variables

You need to create the following environment variables in local or Github Secrets and in your Heroku instance for the app.
#### Database

| Env var name  | Value |
|---------------|-------|
| DB_NAME       |       |
| DB_PWD        |       |
| DB_SERVER_URL |       |
| DB_USERNAME   |       |


#### Translation API

When you create an account in Deepl as a developer, it should generate for you the following to values for you to make requests to the app. 
Again, you have to declare these as local variables, both when running from local or deploying to Heroku.

| Env var name  | Value |
|---------------|-------|
| TRANSLATION_API_HOST       |       |
| TRANSLATION_API_KEY        |       |

### Example requests for the backend
We included a non-exhaustive list of requests ([Test APIs with postman.json](./documentation/Test%20APIs%20with%20postman.json)) you can import into Postman and use it to test the resources from the backend.

## Illustrations
Users can either login or register to the platform. They will see a home page. Users can join the debates using exiting debate topis as shown in the image below:


![Topics](https://github.com/sopra-fs22-group-19/Debatable_Client/blob/master/src/images/topics.png)


Users can filter the debates by clicking the filter button above. They can then choose any filter. Debates will be filtered accordingly. 


![Filter](https://github.com/sopra-fs22-group-19/Debatable_Client/blob/master/src/images/filter.png)


Users can also create a new debate topic using Create debate in the navigation bar.


![create](https://github.com/sopra-fs22-group-19/Debatable_Client/blob/master/src/images/create_debate.png)


Once users join the debate from topics, they will be redirected to debate room. Where they will see a invite button to invite second participant for the debate. They need to send the link to second participant. Second participant can join the debate by either login, register, or join as a guest. If user joins as guest, they don't have access to anything other than participating on that particular debate. Once second participant join, first participant can start the debate. Once the debate started, it will be chance of first participant to write their argument in 30 seconds time period. Participants can either post their answers by clicking enter button in their keyboard or clicking send button. If participant could not post their argument in 30 seconds, argument will posted automatically after 30 seconds. 


![debate_Started](https://github.com/sopra-fs22-group-19/Debatable_Client/blob/master/src/images/started_debate.png)

Both the participants can translate their or other participant's arguments in 5 different languages using translate button in the argument box. Both the participants can end the debate anytime using the end debate button.

![translation](https://github.com/sopra-fs22-group-19/Debatable_Client/blob/master/src/images/translate_msg.png)

Once debate is ended a logged in user will be directed to home page. Guest users will be redirected to login page.

Users can also see awaited debates, ongoing debates, ended debates, and started debates on clicking to My Debates on navigation bar. 

![mydebates](https://github.com/sopra-fs22-group-19/Debatable_Client/blob/master/src/images/mydebates.png)


## Roadmap
Following are some features which would be nice additions:
### Debate viewers
It would be a nice addition to add viewers who can watch the debates.

### Debate Winner
Currently, there is no winner of the debates. Using the above feature and by upvoting and downvoting the arguments, debate winner can be decided.

### Debate Moderator
It would be nice if we can have a debate moderator who can try to keep the debating participants stick to the topic and can stop them from deviating.

### Live video mode of debating
In the current implementation, users type their arguments in the debate room. It would be nicer to have debates with people on live video mode.

### Backend Specific
Separate/break the DebateService into different services associated to other entities like [DebateSpeaker.java](./src/main/java/ch/uzh/ifi/hase/soprafs22/entity/DebateSpeaker.java) and [DebateTopic.java](./src/main/java/ch/uzh/ifi/hase/soprafs22/entity/DebateTopic.java).
This will help encapsulate better the actions of the different entities in the app (DebateRoom, DebateTopic, and DebateSpeaker). In its current state, our code more or less has one master class in [DebateService.java](./src/main/java/ch/uzh/ifi/hase/soprafs22/service/DebateService.java)

## Authors
* [Pablo Bolaños](https://github.com/pabsbo)
* [Juan Bermeo](https://github.com/JdbermeoUZH)
* [Chenfei Ma](https://github.com/chenfeimauzh)
* [Orestis Oikonomou](https://github.com/oroikono)
* [Rupal Saxena](https://github.com/rupalsaxena)

## Acknowledgement
We would like to thank Prof. Dr. Thomas Fritz and his team of Software Praktikum (SoPra) - FS22 of University of Zurich for all the support. 

## License
MIT License

Copyright (c) [year] [fullname]

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

