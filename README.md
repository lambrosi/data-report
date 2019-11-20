# Data Report
The purpose of this application is to watch certain directory, read sales files and extract reports from them.

## Execution flow
This diagram was made to illustrate the flow of execution. The <defaul_directory> is **user.home** environment variable:
<p align="center">
  <img width="731" height="831" src="https://i.imgur.com/Avz8okJ.png">
</p>

## Technical decisions
#### Separation of concerns
Two distinct services were created for this application: directory-monitor-service and file-processor-service. The first service monitor given directory (configured via applicaton.yml) and send to RabbitMQ the names of the files. The second service actually processes the file, generating a file with totalizers and send the data to Kafka, for future viewing on Kibana.

With the separate application and a messaging system connecting the two, there would be no loss of information if the file processing service were down. When it became up again, the messages would be consumed from Rabbit. Also, in a possible volume increase of files being made available in the folder, can easily scale the file processing service in isolation.

#### Monitoring
One concern when developing was to have a clear way to view the data as well as view the application logs. It is important to have an easy view of what is happening internally in execution for easy troubleshooting.

With that in mind, was used the ELK Stack. Thus, a filebeat installed on the machine where the applications are located monitors the log files and sends this data to logstash.

For business data, when the processing service completes report generation, after generating the final file a message is sent to Kafka containing the totalizers. A logstash input has been configured to read these events from Kafka. Having this data available in Kibana later, it is possible for the business area to create dashboards.

Two indexes were created for these views:
*   "file-processed" for processed business data
*   "application-log" for the logs of the two services

#### Test
Unit tests and mutation tests were implemented to ensure that possible paths were covered. Pitest was used for mutation tests.
To run them, in the service folder enter
```sh
<file-processor-service>:~$ ./gradlew clean test
<file-processor-service>:~$ ./gradlew clean pitest
```

## How to run
#### Build a jar file with Gradle
This command will build file-processor-service application
```sh
file-processor-service:~$ ./gradlew clean build
```
This command will build directory-monitor-service application
```sh
directory-monitor-service:~$ ./gradlew clean build
```
#### Increase the virtual memory if needed
Elasticsearch requires that the value of the max_map_count variable be at least 262144.
```sh
:~$ sysctl -w vm.max_map_count=262144
```
#### Run docker-compose
This command will raise the containers needed to run the entire application.
```sh
:~$ docker-compose up
```
