FROM openjdk:8-jdk as shioharabuild
WORKDIR /app
COPY . .
RUN ./gradlew build

FROM openjdk:8-jdk
WORKDIR /app
ENV JAVA_OPTS=${JAVA_OPTS:-'-Xmx512m'}
ENV DEBUG_OPTS=${DEBUG_OPTS}
ENV PORT=${PORT:-2710}
ENV spring.datasource.url=${SPRING_DATASOURCE_URL:-'jdbc:h2:file:./store/db/shioharaDB'}
ENV spring.datasource.username=${SPRING_DATASOURCE_USERNAME:-'sa'}
ENV spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:-''}
ENV spring.datasource.driver-class-name=${SPRING_DATASOURCE_DRIVER-CLASS-NAME:-'org.h2.Driver'}
ENV spring.jpa.properties.hibernate.dialect=${SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT:-'org.hibernate.dialect.H2Dialect'}

RUN useradd -ms /bin/bash java
RUN sh -c 'mkdir -p /app/store'

COPY --from=shioharabuild  /app/build/libs/viglet-shiohara.jar /app/app.jar
RUN sh -c 'touch /app/app.jar'
RUN sh -c 'chown -R java /app'

VOLUME /tmp
VOLUME /app/store

USER java

EXPOSE ${PORT:-2710}

CMD java ${JAVA_OPTS} ${DEBUG_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar