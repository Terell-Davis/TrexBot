FROM openjdk:21

RUN mkdir "TrexBot"
WORKDIR /TrexBot
COPY ./bin /TrexBot/
Copy ./lib /TrexBot/