# CS-320 Project One: Software Testing

## Project Overview

This project was completed for Southern New Hampshire University's **CS-320: Software Testing, Automation, and Quality Assurance** course. Project One focuses on testing three small Java services against their requirements: Contact, Task, and Appointment.

Each service stores objects in memory and supports operations identified below. The JUnit tests check valid operations, rejected inputs, field limits, and the state of objects after changes. This portfolio version preserves the original implementation and test files while adding a repeatable Maven build and JaCoCo coverage reporting.

## Services Implemented

### Contact Service

`Contact` stores an ID, first name, last name, phone number, and address.

| Field | Validation |
| --- | --- |
| Contact ID | Non-null, at most 10 characters, fixed after construction |
| First and last names | Each non-null and at most 10 characters |
| Phone | Non-null and exactly 10 digits; letters, spaces, and separators are rejected |
| Address | Non-null and at most 30 characters |

`ContactService` can add, delete, look up, and count contacts. It can update a matching contact's first name, last name, phone, or address. Updates use the model's validation and retain the previous value when rejected.

### Task Service

`Task` stores an ID, name, and description.

| Field | Validation |
| --- | --- |
| Task ID | Non-null, at most 10 characters, fixed after construction |
| Name | Non-null and at most 20 characters |
| Description | Non-null and at most 50 characters |

`TaskService` can add, delete, look up, and count tasks, and update a matching task's name or description. Rejected updates preserve the existing fields and ID.

### Appointment Service

`Appointment` stores an ID, date, and description, all fixed after construction.

| Field | Validation |
| --- | --- |
| Appointment ID | Non-null and at most 10 characters |
| Date | Non-null `java.util.Date`, not earlier than the current time when validated during construction |
| Description | Non-null and at most 50 characters |

`AppointmentService` can add, delete, look up, and count appointments. It has no update operation. The model copies the date both when receiving it and when returning it, so changing an external `Date` does not change the stored appointment.

### Shared behavior and constraints

- Each service owns an in-memory list. There is no database, user interface, or application entry point.
- IDs are unique within a service instance and are compared without regard to case. Duplicate adds return `false` and keep the original object.
- Successful adds, deletes, and updates return `true`. Missing delete or update targets return `false`; missing lookups return `null`. A null lookup, delete, or update ID matches nothing.
- Adding a null object or supplying an invalid model field throws `IllegalArgumentException`. For Contact and Task updates, a missing target returns `false` before the replacement value is validated.
- String validation imposes maximum lengths, not a non-empty requirement. Empty IDs, names, addresses, and descriptions are accepted where those fields exist; the phone still requires exactly 10 digits.
- Services store and return the supplied object instances. Contact and Task fields remain editable through their validated setters; appointment dates are protected by copies.

## Testing Approach

The suite uses JUnit Jupiter assertions and exception checks. Most tests are requirements-based, black-box checks of public constructors, getters, setters, and service methods. Task and Appointment tests also use reflection to check that their IDs are private and final and have no public setter.

The existing tests demonstrate:

- **Boundary value analysis:** IDs of 10 characters are accepted and 11 are rejected. Task names are tested at 20 and 21 characters; descriptions at 50 and 51. Contact addresses are tested at 30 and 31 characters.
- **Equivalence partitioning:** Contact phone tests distinguish valid 10-digit values from null, short, long, alphabetic, and separated values. Appointment tests distinguish future dates from past dates and null dates.
- **Positive and negative cases:** Successful adds, updates, and deletes are checked alongside duplicate IDs, missing IDs, and invalid values using `assertTrue`, `assertFalse`, and `assertThrows`.
- **State and identity checks:** `assertEquals` verifies field values and counts; `assertSame` verifies that duplicate adds retain the original object. Invalid setter and service updates are checked for state preservation.
- **Mutable-input protection:** Appointment tests change both the original date and a returned date, then verify that the appointment's date remains unchanged. Future-date fixtures are calculated relative to the test run rather than a fixed calendar date.

## Project Structure

```text
cs-320-software-testing/
|-- .gitignore
|-- pom.xml
|-- README.md
|-- src/
|   |-- Appointment.java
|   |-- AppointmentService.java
|   |-- Contact.java
|   |-- ContactService.java
|   |-- Task.java
|   `-- TaskService.java
|-- test/
|   |-- AppointmentServiceTest.java
|   |-- AppointmentTest.java
|   |-- ContactServiceTest.java
|   |-- ContactTest.java
|   |-- TaskServiceTest.java
|   `-- TaskTest.java
`-- target/                     # Generated locally; ignored by Git
    |-- classes/
    |-- test-classes/
    |-- surefire-reports/
    |-- jacoco.exec
    `-- site/jacoco/
        |-- index.html
        `-- jacoco.xml
```

The original `src/` and `test/` directories and default Java package are retained. Maven is configured to use these directories directly. The tree shows the main generated outputs; Maven also creates its build metadata and a JAR under `target/`.

## Running the Tests

Prerequisites:

- **JDK 11**, including the Java compiler. The build uses `--release 11` to set the Java language, API, and class-file compatibility level.
- **Apache Maven 3.9.x**. Set `JAVA_HOME` to the JDK installation and add the Java and Maven `bin` directories to `PATH`.
- Network access to Maven Central for the first build to download the pinned plugins and test dependencies.

IntelliJ IDEA is not required. From the repository root, confirm the tools are available:

```shell
java -version
mvn -version
```

Compile the implementation:

```shell
mvn compile
```

Clean previous build output, compile the implementation and tests, and run the full test suite:

```shell
mvn clean test
```

Run the full build and tests, package the classes, and generate HTML and XML coverage reports:

```shell
mvn clean verify
```

Surefire discovers the six existing `*Test` classes using the JUnit Platform and fails the build if no tests are found. JaCoCo collects coverage during tests; its report runs in the `verify` phase. Open `target/site/jacoco/index.html` in a browser after verification. Machine-readable coverage is in `target/site/jacoco/jacoco.xml`, and individual test results are in `target/surefire-reports/`.

## Test Results and Coverage

Verified on **October 19, 2026**, using **Apache Maven 3.9.16**, **Eclipse Temurin JDK 11.0.32.1**, **JUnit Jupiter 5.14.0**, **Maven Surefire 3.6.0**, and **JaCoCo 0.8.15**.

All **95 tests passed**, with **0 failures, 0 errors, and 0 skipped tests**. The executed test names were compared with every `@Test` method in the source files; all 95 were discovered.

A clean copy containing only the source, tests, Maven configuration, README, and ignore rules also passed `mvn compile`, `mvn clean test`, and `mvn clean verify` using cached dependencies. Its coverage counts matched the repository build exactly, without relying on IDE metadata or existing build output.

| Test class | Tests passed |
| --- | ---: |
| ContactTest | 10 |
| ContactServiceTest | 15 |
| TaskTest | 19 |
| TaskServiceTest | 21 |
| AppointmentTest | 13 |
| AppointmentServiceTest | 17 |
| **Total** | **95** |

JaCoCo measured the following coverage for every implementation class. Counts are covered items divided by total items.

| Class | Instruction coverage | Line coverage | Branch coverage | Meets 80% minimum |
| --- | ---: | ---: | ---: | :---: |
| Contact | 100% (136/136) | 100% (44/44) | 100% (20/20) | Yes |
| ContactService | 100% (146/146) | 100% (46/46) | 100% (22/22) | Yes |
| Task | 100% (80/80) | 100% (26/26) | 100% (12/12) | Yes |
| TaskService | 100% (120/120) | 100% (37/37) | 100% (18/18) | Yes |
| Appointment | 100% (82/82) | 100% (21/21) | 100% (14/14) | Yes |
| AppointmentService | 100% (94/94) | 100% (27/27) | 100% (14/14) | Yes |

Every class exceeds the course's 80% expectation in all three reported metrics. No additional tests or production changes were needed. Coverage reports are generated locally and excluded from Git. These are measured results for this version of the project; full coverage does not mean every possible input or defect has been tested.

## What I Learned

This project helped me connect software requirements to repeatable checks. Testing values at and just beyond a limit makes the validation rules concrete, while null and invalid inputs show whether the code rejects bad data consistently. Checking the state after a rejected update is also important: an exception alone does not show that the original data was preserved. Automated tests give me a consistent way to check these behaviors again when making future changes.

## Technologies

- Java and the Java standard library
- [JUnit Jupiter](https://docs.junit.org/5.14.0/release-notes/) for automated unit tests
- [Apache Maven](https://maven.apache.org/) for compilation and the build lifecycle
- [Maven Surefire](https://maven.apache.org/surefire/maven-surefire-plugin/examples/junit-platform.html) for JUnit Platform test execution
- [JaCoCo](https://www.jacoco.org/jacoco/trunk/doc/maven.html) for instruction, line, and branch coverage reports

## Academic Context

This repository presents Project One coursework completed at **Southern New Hampshire University** for **CS-320: Software Testing, Automation, and Quality Assurance**. It demonstrates validation and unit testing for the Contact, Task, and Appointment services. The build configuration and documentation prepare the completed coursework for an academic GitHub portfolio.
