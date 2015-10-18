FROM mikeneck/gradle

RUN \
  mkdir -p /home/gradle/.gradle/caches/modules-2/files-2.1 && \
  mkdir -p /home/gradle/.gradle/caches/modules-2/metadata-2.15 && \
  mkdir /home/gradle/project

VOLUME /home/gradle/.gradle/caches/modules-2/files-2.1
VOLUME /home/gradle/.gradle/caches/modules-2/metadata-2.15
VOLUME /home/gradle/project
WORKDIR /home/gradle/project

RUN \
  chown gradle /home/gradle/.gradle && \
  chown gradle /home/gradle/.gradle/caches && \
  chown gradle /home/gradle/.gradle/caches/modules-2 && \
  chown gradle /home/gradle/.gradle/caches/modules-2/files-2.1 && \
  chown gradle /home/gradle/.gradle/caches/modules-2/metadata-2.15 && \
  chown gradle /home/gradle/project

USER gradle
