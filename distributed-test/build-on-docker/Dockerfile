FROM mikeneck/gradle

RUN \
  mkdir /home/gradle/.gradle && \
  mkdir /home/gradle/project

VOLUME /home/gradle/.gradle
VOLUME /home/gradle/project
WORKDIR /home/gradle/project

RUN \
  chown gradle /home/gradle/.gradle && \
  chown gradle /home/gradle/project

USER gradle

ENTRYPOINT ["gradle", "build"]
