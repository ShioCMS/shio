FROM openjdk:8-jdk as shiobuild
WORKDIR /app
COPY . .
RUN ./gradlew build

FROM openjdk:8-jre-alpine
WORKDIR /app
ENV JAVA_OPTS=${JAVA_OPTS:-'-Xmx512m'}
ENV DEBUG_OPTS=${DEBUG_OPTS}
ENV PORT=${PORT:-2710}

RUN adduser -D -g '' java

RUN sh -c 'mkdir -p /app/store'

COPY --from=shiobuild  /app/build/libs/viglet-shio.jar /app/app.jar
RUN sh -c 'touch /app/app.jar'
RUN sh -c 'chown -R java /app'

VOLUME /tmp
VOLUME /app/store

USER java

EXPOSE ${PORT:-2710}

CMD java ${JAVA_OPTS} ${DEBUG_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar