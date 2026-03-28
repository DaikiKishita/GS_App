FROM eclipse-temurin:17-jdk

ENV TZ=Asia/Tokyo

WORKDIR /workspace

EXPOSE 8080

CMD ["./gradlew", "bootRun"]