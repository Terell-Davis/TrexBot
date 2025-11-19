FROM gradle:9.2.1-jdk21

WORKDIR /app
COPY . .

RUN ./gradlew clean installShadowDist
RUN rm -rf bin
RUN mv build/install/TrexBot-shadow/bin bin
RUN rm -rf lib
RUN mv build/install/TrexBot-shadow/lib lib

COPY .env bin/.env

ENTRYPOINT ["bash", "bin/TrexBot"]
