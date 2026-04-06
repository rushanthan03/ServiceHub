FROM gradle AS BUILD_IMAGE

COPY . .

RUN ./gradlew clean build

RUN ls build
RUN ls build/libs
RUN ls -l
RUN chmod +x build
RUN chmod +x build/libs
RUN ls -l
RUN ls build/libs

FROM eclipse-temurin:21
COPY --from=build_image /build/libs/serviceHub-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "serviceHub-0.0.1-SNAPSHOT.jar"]
