FROM ghcr.io/graalvm/graalvm-ce:ol7-java17-22.3.1

RUN yum -y update && \
    yum -y install python3 && \
    yum clean all

COPY ./build/libs/compiler-0.0.1-SNAPSHOT.jar /compiler.jar

WORKDIR /

ENTRYPOINT ["java", "-jar", "/compiler.jar"]