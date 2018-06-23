# Simple Base64 encoded data diff

### Description
Web application that computes the linear (byte by byte) difference between base64 encoded data. The data is provided to the system as a pair of left and right data identified by an ID. By using this ID the difference between those parts is computed.  

### Endpoints
The application provides 2 http endpoints that accepts JSON base64 encoded binary data and one endpoint that shows the computed difference.

###### POST endpoints 
* The first endpoint accepts the left part of the data pair and the ID that identifies it: 
    * \<host\>/v1/diff/\<ID\>/left
* The second endpoint accepts the left part of the data pair and the ID that identifies it: 
    * \<host\>/v1/diff/\<ID\>/right

*The expected JSON at the endpoints is:*
```
{
    "data" : "the base64 encoded data"
}
```

**NOTE:** *Every time the user posts new data with existing ID the old data is replaced*

###### GET endpoint 
* The endpoint that provides the JSON with the results of the difference is: 
    * \<host\>/v1/diff/\<ID\>

**The result can be:**

* When the two provided data are equal
```
{
    "equalityStatus": "equal"
}
```

* When the two provided data are of different size
```
{
    "equalityStatus": "not equal size"
}
```

* When the two provided data are different but of same size we provide insights of where this data differs: offset (index) of the byte (**first byte is 1 and not 0**) and the length of non-similar bytes  
```
{
    "equalityStatus": "not equal",
    "insights": [
        {
            "offset": 5,
            "length": 1
        }
    ]
}
```

**NOTE:** *Not provided data (null) and empty data are considered equal. The exceptions are passed to the caller of the endpoint with the relative message. I.e.: if ID not present in the system is requested the exception states that no such data exists.*

### Running the app
The application requires Java 8+ to run. It is build on top of Spring Boot 2. For the sake of simplicity it uses file based H2 DB for production and in-memory H2 (with enabled H2 console) for development (dev profile) You can start the app by running:
```
gradlew bootRun
```

### Building the app
The application is build using gradle. Once build you can go in the `/build/libs` folder and execute the generated [app_name-version.jar].
I.e:
```
java -jar simple-diff-0.0.1-SNAPSHOT.jar
```

### Troubleshooting
Since the application runs on port 80 (privileged port in Linux) make sure you have privileges, run the application on other port > 1024 or run with dev profile (using port 8080):
I.e:
```
gradlew bootRun -Dspring.profiles.active=dev
```
When running on Java 9+ environments be sure to uncomment the JAXB dependencies in the [build.gradle](build.gradle)