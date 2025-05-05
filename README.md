Bragi Movie Db
==================

An app that shows a list of movies filtered by genre

## How to build the app

1. Clone this branch
    ```
    git clone git@github.com:milanJ/BragiMovieDb.git --branch main
    ```
2. Open the cloned repository in Android Studio.
3. In terminal run `./gradlew assemble` task.
4. After the task finishes running, release and debug APK files will be located in the `/app/build/outputs/apk` directory.

## General architecture

The app is based on MVVM architecture. It was built using Hilt, Coroutines, Material Design and Jetpack Compose.
It is separated into 7 modules: `app`, `core-data`, `core-testing`, `core-ui`, `feature-filters`, `feature-movies` and `test-app`.

 - `app` is the main application module and it contains its `MainActivity` and `MyApplication` classes. And sets-up navigation.
 - `core-data` module contains the data layer of the app.
 - `core-testing` module contains the common classes used for testing in other modules.
 - `core-ui` module contains the common UI code.
 - `feature-filters` module contains the Filters screen.
 - `feature-movies` module contains the Movies screen.
 - `test-app` module is supposed to contain integration and e2e tests.

## Breakdown of libraries used

The application is using the following libraries:
 - **Kotlin Coroutines** for asynchronous programming.
 - **Jetpack Compose** to build UI.
 - **Material Design 3** to style the UI.
 - **Hilt** for dependency injection.
 - **Navigation Compose** to handle the navigation between the parts of UI.
 - **AndroidX Paging 3** and **Paging Compose** to handle the loading of paged data.
 - **AndroidX Lifecycle** to react to changes in the lifecycle status of components.
 - **Coil** to load images.
 - **Gson** to parse JSON data.
 - **OkHttp** and **Retrofit** to handle network communication.
 - **AndroidX Test**  for testing.
