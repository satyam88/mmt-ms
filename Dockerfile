FROM tomcat:9.0.52-jre11-openjdk-slim
COPY ./target/mmt*.jar /usr/local/tomcat/webapps
EXPOSE  8080 9090
USER mmt-ms
WORKDIR /usr/local/tomcat/webapps
CMD ["catalina.sh", "run"]
