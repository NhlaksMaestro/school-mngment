FROM openjdk:11-slim as build
ARG MVNOPTS=
WORKDIR /usr/src/app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw $MVNOPTS com.coveo:fmt-maven-plugin:format
RUN ./mvnw $MVNOPTS install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:11-slim
VOLUME /tmp
ARG DEPENDENCY=/usr/src/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.exp.test.ExpTestApiApplication"]
