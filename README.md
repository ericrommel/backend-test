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

## Technologies used:
- [Java 8](https://docs.oracle.com/javase/8/docs/api/) - Development.
- [Jersey RESTful](https://jersey.github.io/) - framework).
- [Jetty](https://www.eclipse.org/jetty/documentation/) - servlet container.
- [h2database](https://www.h2database.com/) - in-memory database.
- [Gradle](https://gradle.org/) - Build automation system.
- [GIT](https://git-scm.com/) - version control.
- [IntelliJ](https://www.jetbrains.com/idea/) - IDE.
- [Insomnia](https://insomnia.rest/) - REST client.
- [Windows 7](https://www.microsoft.com/) - SO environment.


## Documentation
This is a summary of the API.

### Installing and Running
Here you can find the steps for installing, running and testing the API

#### Installing
First, you need a copy of the source code, you can download it [here](https://github.com/ericrommel/backend-test) or clone the project. To clone the project you need [Git](https://git-scm.com) and then open a terminal application and enter the following commands:

```shell
$ cd desired/path/
$ git clone git@github.com:ericrommel/backend-test.git
```

#### Running
There are two ways to running the server. After the server is running, you can use a REST client or Browser to test the API.

**1. Using the final project built (executable as a standalone program):**
This command will create a directory on *build/output/account-management* that contains the standalone application with scripts for run it (e.g. *run.sh, run.bat, start.sh, start.bat ...*).

```shell
$ gradlew buildProduct
```

**NOTE:**
- ***Unix***: `./gradlew buildProduct`
- ***Windows***: `gradle buildProduct`

After that, execute one of the script runners:
```shell
cd build/output/account-management
$ start.bat
```

**NOTE:**
- These files were already created and you can use them from there.

**2. Using the Gretty (Gradle Plugin) run de command bellow at the terminal application:**

```shell
$ gradle appRun
```

**NOTE:**
- ***Unix***: `./gradlew appRun`
- ***Windows***: `gradle appRun`


#### Using a REST client
Note: At doc folder, you can use an exported JSON file with all endpoints set before by Insomnia application. You can use that application and import this file there.
For other REST clients, you can create all endpoits based on the endpoints descripted above. You can use any browser that you want also.


#### Tests
All resource tests can be checked using the Gradle. Back to main folder and then, execute the command below.

```shell
$ gradle test
```

The result file can be checked at \build\reports\tests\test\index.html:


## Limitations

In order to keep the api simple and to the point, some limitations were defined:
- Client information as simple as possible: name and some document id.
- As requested, only transfer transactions were provided.
- There is no banks involved. Assume that all accounts have the same bank.
- As all these data are sensible, the DELETE operations are not allowed for almost all entities. It could used DEACTIVATED status but, this was out of scope at this moment.
- Same to PUT operations, except to client.
- It is not possible to adding a future transfer. All of them are made always 'TODAY'


## Endpoints
The endpoints can be checked also at the \docs\resources.xlsx. They were copied below.

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

### POST /clients

#### Summary

Create a client in the system.

#### Payload

- **name**: the client name.
- **document_id**: the client document id.

#### Example of sent data

```json
{
    "name": "Eric Rommel",
    "document_id": "123456"
}
```

#### Reply

- 201 (Created).

#### Example of received data

`No body returned for response`

### PUT /clients/{id}

#### Summary

Update name or document_id of the client.

#### Payload

- **name**: the client name.
- **document_id**: the client document id.

#### Example of sent data

```json
{
    "name": "New Name",
    "document_id": "987654"
}
```

#### Reply

- 200 (OK).

#### Example of received data

```json
{
    "id": 1,
    "name": "New Name",
    "document_id": "987654"
}
```

### POST /accounts

#### Summary

Create an account.

#### Payload

- **client_id**: the client's identification registered for the account.
- **balance**: account's balance in Euros.

#### Example of sent data

```json
{
    "client_id": 1,
    "balance": 1000
}
```

#### Reply

- 201 (Created).

#### Example of received data

`No body returned for response`

### POST /transactions/transfers

#### Summary

Create a transfer.

#### Payload

- **from**: account number that the money will leave.
- **to**: account number that the money will arrive.
- **amount**: amount that will be sent

#### Example of sent data

```json
{
	"from": 1,
	"to": 2,
	"amount": 16
}
```

#### Reply

- 201 (Created).

#### Example of received data

`No body returned for response`

## Author
- [Eric Dantas](https://github.com/ericrommel)

## Contacts
- [LinkedIn](https://www.linkedin.com/in/ericrommel)
- Phone number: +36 70 462-7040
- Skype: eric.rommel
