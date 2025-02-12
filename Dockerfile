FROM eclipse-temurin:21-jdk-alpine
RUN apk --no-cache add curl
ARG XMX_DEFAULT=2048m
ARG XMS_DEFAULT=1024m
ARG XX_DEFAULT=+UseParallelGC
ARG EXECUTABLE_APP_PATH=./app.jar
ENV EXECUTABLE_APP_PATH=$EXECUTABLE_APP_PATH
ENV TZ=America/Asuncion
RUN apk add --no-cache tzdata
RUN ln -snf /usr/share/zoneinfo/${TZ} /etc/localtime && echo ${TZ} > /etc/timezone
COPY target/itti-ms-template-spring-boot.jar app.jar
ENV MISC_JAVA_OPTS=""
ENV XMX=${XMX_DEFAULT}
ENV XMS=${XMS_DEFAULT}
ENV XX=${XX_DEFAULT}
ARG ENV
EXPOSE 8080
ENTRYPOINT java ${MISC_JAVA_OPS} -Djava.security.egd=file:/dev/./urandom -Xmx${XMX} -Xms${XMS} -XX:${XX} -jar ${EXECUTABLE_APP_PATH}