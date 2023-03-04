./gradlew bootJar
cp build/libs/hw3-1.0-SNAPSHOT.jar .
docker build -f Dockerfile -t exchange-application:latest .
rm hw3-1.0-SNAPSHOT.jar
