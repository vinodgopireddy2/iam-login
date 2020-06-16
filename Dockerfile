FROM anapsix/alpine-java:latest

COPY --from=docker.elastic.co/beats/filebeat:7.3.1 /usr/share/filebeat/ /usr/share/filebeat/
RUN mkdir -p /etc/filebeat
COPY ./filebeat.yml /etc/filebeat/

WORKDIR /var/iam

RUN mkdir -p lib/jars
COPY ./target/dependency/jetty-runner.jar lib/jars
COPY ./target/iam-login.war lib/

COPY ./start.sh .
RUN chmod +x start.sh

CMD ./start.sh
