# IHE XDS and openEHR Integration Prototype Test Client

**Disclaimer:** This code is research code and documents the prototype developed as part of my master's thesis.

The prototype client allows to execute two exemplary functions. First, an openEHR composition can be submitted using the openEHR REST API. Second, a combination of ITI-18 and ITI-43 can be invoked to query and retrieve that very same composition again. 

The prototype server is required to run, so the client can connect to it. All configurations of ports and endpoints are static and meant for local use only.

## Origin
Forked from [IPF iheclient tutorial](https://github.com/oehf/ipf/tree/d0f35cfad926f616c74ea542fdca4c906234afe8/tutorials/iheclient).

## Dependencies

- Java 1.8
- Maven

## Run

1. run `mvn package -Dmaven.test.failure.ignore=true`
2. run `java -jar target/ipf-tutorials-iheclient-3.6-SNAPSHOT.jar` 