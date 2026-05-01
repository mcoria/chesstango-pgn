FROM ghcr.io/graalvm/graalvm-community:25
COPY target/chesstango-pgn-worker.jar /tmp
CMD ["java","-jar","/tmp/chesstango-epd-worker.jar"]
