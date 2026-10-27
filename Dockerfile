FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY Main.java ./
COPY modelo ./modelo
COPY controlador ./controlador
COPY vista ./vista
RUN javac -encoding UTF-8 -d out Main.java modelo/*.java controlador/*.java vista/*.java
VOLUME /app/data
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8", "-cp", "out", "Main"]
