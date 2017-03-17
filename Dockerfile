FROM centos
RUN \
  yum update -y && \
  yum install -y java-1.8.0-openjdk && \
  yum install -y wget && \
  mkdir -p /data/annotation-api \
  mkdir -p /data/annotation-api/work \
  mkdir -p /data/annotation-api/target

WORKDIR data/annotation-api

COPY start.sh /data/annotation-api
COPY target/annotation-0.0.1-SNAPSHOT.jar /data/annotation-api/target
COPY annotation-configuration.yml /data/annotation-api

EXPOSE 8087
EXPOSE 8088

ENV JAVA_HOME /usr/lib/jvm/jre-1.8.0-openjdk
CMD /data/annotation-api/start.sh
