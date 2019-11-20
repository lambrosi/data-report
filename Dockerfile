FROM openjdk:8-jre

RUN apt-get update && apt-get install wget

USER root

RUN wget "https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-6.5.0-linux-x86_64.tar.gz" \
    && tar -xzvf filebeat-6.5.0-linux-x86_64.tar.gz \
    && rm filebeat-6.5.0-linux-x86_64.tar.gz 

COPY file-processor-service/build/libs/fileprocessor-0.0.1-SNAPSHOT.jar /usr/services/file-processor-service/app.jar
COPY directory-monitor-service/build/libs/directorymonitor-0.0.1-SNAPSHOT.jar /usr/services/directory-monitor-service/app.jar
COPY monitoring-config/filebeat.yml /filebeat-6.5.0-linux-x86_64/filebeat.yml

CMD ./filebeat-6.5.0-linux-x86_64/filebeat -c /filebeat-6.5.0-linux-x86_64/filebeat.yml & java -jar /usr/services/file-processor-service/app.jar & java -jar /usr/services/directory-monitor-service/app.jar
