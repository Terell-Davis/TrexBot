FROM gradle:9.2.1-jdk21

WORKDIR /app
COPY . .

RUN gradle clean installShadowDist

RUN mv build/install/TrexBot-shadow/bin bin
RUN rm -rf lib
RUN mv build/install/TrexBot-shadow/lib lib

ENTRYPOINT ["bash", "bin/TrexBot"]

