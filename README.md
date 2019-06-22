# Backend Test

Design and implement a RESTful API (including data model and the backing implementation) for money transfers between accounts.

## Explicit requirements:
1. You can use Java or Kotlin.
2. Keep it simple and to the point (e.g. no need to implement any authentication).
3. Assume the API is invoked by multiple systems and services on behalf of end users.
4. You can use frameworks/libraries if you like (except Spring), but don't forget about requirement #2 and keep it simple and avoid heavy frameworks.
5. The datastore should run in-memory for the sake of this test.
6. The final result should be executable as a standalone program (should not require a pre-installed container/server).
7. Demonstrate with tests that the API works as expected.

## Implicit requirements:
1. The code produced by you is expected to be of high quality.
2. There are no detailed requirements, use common sense.

Please put your work on github or bitbucket.

## Running

Using the Gretty (Gradle Plugin) run de command bellow:

- **Unix**: `./gradlew appRun`
- **Windows**: `gradle.bat appRun`

## Build final project

Build the standalone project, run:

`./gradlew buildProduct`

This command will create a directory on *build/output/account-management*
that contains the standalone application with scripts for run it
(e.g. *run.sh, run.bat, start.sh, start.bat ...*).

