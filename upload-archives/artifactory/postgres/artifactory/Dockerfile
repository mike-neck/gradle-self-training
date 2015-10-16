FROM mikeneck/jdk8

RUN \
  groupadd -r repo && \
  useradd -m -g repo artifactory

WORKDIR /home/artifactory

RUN \
  curl -L \
    http://bit.ly/Hqv9aj \
    -o artifactory.zip

RUN \
  unzip -q artifactory.zip -d tmp && \
  rm artifactory.zip && \
  mv tmp/artifactory-oss-4.1.3/ artifactory && \
  rmdir tmp/ && \
  mkdir artifactory/data && \
  mkdir artifactory/backup && \
  chown -R artifactory /home/artifactory/artifactory

ADD postgresql.properties /home/artifactory/artifactory/etc/storage.properties
ADD run.sh /home/artifactory/run.sh

RUN \
  curl -L \
    https://jdbc.postgresql.org/download/postgresql-9.4-1204.jdbc4.jar \
    -o postgresql-9.4-1204.jdbc4.jar

RUN \
  mv postgresql-9.4-1204.jdbc4.jar \
    /home/artifactory/artifactory/tomcat/lib/postgresql-9.4-1204.jdbc4.jar && \
  chown artifactory /home/artifactory/run.sh && \
  chmod 744 /home/artifactory/run.sh

RUN \
  chown artifactory /home/artifactory/artifactory/etc/storage.properties

USER artifactory

EXPOSE 8081
ENV ARTIFACTORY_HOME /home/artifactory/artifactory

VOLUME /home/artifactory/artifactory/data
VOLUME /home/artifactory/artifactory/logs
VOLUME /home/artifactory/artifactory/backup

ENTRYPOINT ["./run.sh"]
