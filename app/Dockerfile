FROM gradle:8.9-jdk21

WORKDIR /app

COPY / .

RUN ./gradlew installDist

export JDBC_DATABASE_URL=jdbc:postgresql://db:5432/postgres?password=password&user=postgres

CMD ./build/install/app/bin/app