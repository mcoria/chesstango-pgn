package net.chesstango.pgn.master;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.pgn.core.report.PGNSearchReportSaver;
import net.chesstango.pgn.worker.PGNSearchResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
@Slf4j
public class PGNSearchMainReader {

    public static void main(String[] args) {
        Path sessionDirectory = Path.of("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\depth-6-2026-05-01-15-31-v1.7.0-SNAPSHOT");

        Stream<PGNSearchResponse> epdSearchResponseStream = readEpdSearchResponses(sessionDirectory);

        epdSearchResponseStream
                .parallel()
                .forEach(epdSearchResponse -> {
                    PGNSearchReportSaver epdSearchReportSaver = new PGNSearchReportSaver(sessionDirectory);

                    epdSearchReportSaver.loadModel(epdSearchResponse.getSessionId(), epdSearchResponse.getPGNSearchResults());

                    CompletableFuture<Void> saveReport = CompletableFuture.supplyAsync(() -> {
                        epdSearchReportSaver.saveReport(epdSearchResponse.getSearchId());
                        return null;
                    });

                    CompletableFuture<Void> saveJson = CompletableFuture.supplyAsync(() -> {
                        epdSearchReportSaver.saveJson(epdSearchResponse.getSearchId());
                        return null;
                    });

                    CompletableFuture<Void> combinedSave = CompletableFuture.allOf(saveReport, saveJson);

                    log.info("Saving reports {}", epdSearchResponse.getSearchId());

                    combinedSave.join();
                });

        log.info("Work completed");
    }

    private static Stream<PGNSearchResponse> readEpdSearchResponses(Path sessionDirectory) {
        File directory = sessionDirectory.toFile();

        log.info("Loading PGNSearchResponse from {}", directory.getAbsolutePath());

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".ser"));

        log.info("Found {} ", Arrays.toString(files));

        assert files != null;

        return Stream
                .of(files)
                .map(file -> {
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                        log.info("Deserializing file: {}", file.getName());
                        return (PGNSearchResponse) ois.readObject();
                    } catch (Exception e) {
                        log.error("Failed to deserialize file: " + file, e);
                        return null;
                    }
                }).filter(Objects::nonNull);
    }
}
