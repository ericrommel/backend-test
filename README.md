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

## Documentation
This is a summary of the API.

# Endpoints

| Resource                                             | GET                                             | POST                  | PUT                      | DELETE                                                                        |
|------------------------------------------------------|-------------------------------------------------|-----------------------|--------------------------|-------------------------------------------------------------------------------|
| /accounts                                            | Returns a list of accounts                      | Create a new account  | Not allowed              | Not allowed                                                                   |
| /accounts/{accountNumber}                            | Returns a specific account                      |  Not allowed          | Not allowed              | Not allowed                                                                   |
| /clients                                             | Returns a list of clients                       | Create a new client   | Not allowed              | Not allowed                                                                   |
| /clients/{id}                                        | Returns a specific clients                      |  Not allowed          | Update a specific client | Delete a specific client only if this client doesn't have accounts registered |
| /transactions/transfers                              | Returns a list of transfers                     | Create a new transfer | Not allowed              | Not allowed                                                                   |
| /transactions/transfers/{transactionId}              | Returns a specific transfer                     | Not allowed           | Not allowed              | Not allowed                                                                   |
| /transactions/transfers/from/{dateTransaction}       | Returns transfers starting from a specific date | Not allowed           | Not allowed              | Not allowed                                                                   |
| /transactions/transfers/account/from/{accountNumber} | Returns transfers from a specific account       | Not allowed           | Not allowed              | Not allowed                                                                   |
| /transactions/transfers/account/to/{accountNumber}   | Returns transfers to a specific account         | Not allowed           | Not allowed              | Not allowed                                                                   |

## Step by Step to execution
Here you can find the steps for testing the API, using a REST client (e.g. Postman or Insomnia) and Gradle test option.

# Using a REST client
At doc folder, you can use the exported JSON file with all endpoints set before at Insomnia application. You can use the Insomnia application and import this file there.
For other REST client, you can create the endpoits based on the endpoints descripted above.

# Using the Gradle Test option
All resources tested can be checked using the Gradle. Here the steps:


