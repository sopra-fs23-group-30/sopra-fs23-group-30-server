# upsearch.ch
upsearch.ch is an open-source web platform designed to alleviate the tedious and exhausting process that students face when searching for a flat in highly sought-after areas like Zurich. Our project aims to simplify the entire experience for both seekers and listers, eliminating the need for repetitive self-information and providing a streamlined hub for managing applications and listings.

Key Features:

- Streamlined Flat Search: We provide an intuitive and user-friendly interface that simplifies the search process, enabling students to find suitable flats quickly and efficiently.
- Automated Profile Management: Say goodbye to repeatedly entering the same information about yourself. Our platform automates profile creation, saving you time and effort.
- Centralized Application Hub: Manage all your flat applications in one place, allowing you to track their status, communicate with listers, and stay organized throughout the process.
- Enhanced Listing Management: Listers can easily create and manage property listings, eliminating the hassle of repetitive data entry and providing a clear overview of all listings.

```
Join us in revolutionizing the flat search experience for students in high-demand locations with upsearch.ch, and contribute to our open-source project to make finding a flat hassle-free and more enjoyable for everyone.
```

## Technologies 
- FrontEnd Framework [React](https://react.dev/).
- FrontEnd Styling Framework [Tailwind CSS](https://tailwindcss.com/)
- Backend Framework [JPA](https://tailwindcss.com/](https://spring.io/projects/spring-boot)
- WebApp Hosting [Google Cloud](https://cloud.google.com/)
- Database Hosting & Blob Storage [Microsoft Azure](https://azure.microsoft.com/)


## High-level components 




## Launch & Deployment:

## Illustrations

## Roadmap

## Authors and acknowledgment

## License

-   Documentation: https://docs.spring.io/spring-boot/docs/current/reference/html/index.html
-   Guides: http://spring.io/guides
    -   Building a RESTful Web Service: http://spring.io/guides/gs/rest-service/
    -   Building REST services with Spring: https://spring.io/guides/tutorials/rest/

## Setup this Template with your IDE of choice
Download your IDE of choice (e.g., [IntelliJ](https://www.jetbrains.com/idea/download/), [Visual Studio Code](https://code.visualstudio.com/), or [Eclipse](http://www.eclipse.org/downloads/)). Make sure Java 17 is installed on your system (for Windows, please make sure your `JAVA_HOME` environment variable is set to the correct version of Java).

### IntelliJ
1. File -> Open... -> SoPra server template
2. Accept to import the project as a `gradle project`
3. To build right click the `build.gradle` file and choose `Run Build`

### VS Code
The following extensions can help you get started more easily:
-   `vmware.vscode-spring-boot`
-   `vscjava.vscode-spring-initializr`
-   `vscjava.vscode-spring-boot-dashboard`
-   `vscjava.vscode-java-pack`

**Note:** You'll need to build the project first with Gradle, just click on the `build` command in the _Gradle Tasks_ extension. Then check the _Spring Boot Dashboard_ extension if it already shows `soprafs23` and hit the play button to start the server. If it doesn't show up, restart VS Code and check again.

## Building with Gradle
You can use the local Gradle Wrapper to build the application.
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

You can verify that the server is running by visiting `localhost:8080` in your browser.

### Test

```bash
./gradlew test
```

### Development Mode
You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

## API Endpoint Testing with Postman
We recommend using [Postman](https://www.getpostman.com) to test your API Endpoints.

## Debugging
If something is not working and/or you don't know what is going on. We recommend using a debugger and step-through the process step-by-step.

To configure a debugger for SpringBoot's Tomcat servlet (i.e. the process you start with `./gradlew bootRun` command), do the following:

1. Open Tab: **Run**/Edit Configurations
2. Add a new Remote Configuration and name it properly
3. Start the Server in Debug mode: `./gradlew bootRun --debug-jvm`
4. Press `Shift + F9` or the use **Run**/Debug "Name of your task"
5. Set breakpoints in the application where you need it
6. Step through the process one step at a time

## Testing
Have a look here: https://www.baeldung.com/spring-boot-testing
