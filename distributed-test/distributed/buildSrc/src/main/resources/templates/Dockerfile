FROM mikeneck/gradle

USER root

RUN \
  mkdir -p /home/gradle/.gradle/caches/modules-2/files-2.1 && \
  mkdir -p /home/gradle/.gradle/caches/modules-2/metadata-2.15 && \
  mkdir -p /home/gradle/project/build/dist-test

VOLUME /home/gradle/.gradle/caches/modules-2/files-2.1
VOLUME /home/gradle/.gradle/caches/modules-2/metadata-2.15
VOLUME /home/gradle/project/build/dist-test

WORKDIR /home/gradle/project

ADD src/ /home/gradle/project/src
ADD buildSrc/ /home/gradle/project/buildSrc
ADD build.gradle /home/gradle/project/build.gradle
ADD settings.gradle /home/gradle/project/settings.gradle
ADD gradle.properties /home/gradle/project/gradle.properties

RUN \
  mkdir -p /home/gradle/project/buildSrc/.gradle && \
  chown -R gradle -R /home/gradle/.gradle && \
  chown -R gradle /home/gradle/project && \
  chown -R gradle /home/gradle/project/build && \
  chown -R gradle /home/gradle/project/build/dist-test

USER gradle
