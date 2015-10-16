FROM mikeneck/jdk8

RUN \
  groupadd -r repo && \
  useradd -m -g repo artifactory

USER artifactory
WORKDIR /home/artifactory

RUN \
  curl -L \
    http://bit.ly/Hqv9aj \
    -o artifactory.zip

RUN \
  unzip -q artifactory.zip -d tmp && \
  rm artifactory.zip && \
  mv tmp/artifactory-oss-4.1.3/ artifactory && \
  rmdir tmp/
RUN \
  mkdir artifactory/data && \
  mkdir artifactory/backup

EXPOSE 8081
ENV ARTIFACTORY_HOME /home/artifactory/artifactory

VOLUME /home/artifactory/artifactory/data
VOLUME /home/artifactory/artifactory/logs
VOLUME /home/artifactory/artifactory/backup

ENTRYPOINT ["artifactory/bin/artifactory.sh"]
