package net.chesstango.pgn.core.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chesstango.pgn.core.report.SummaryDiffModel;
import net.chesstango.pgn.core.report.SummaryDiffModelInput;
import net.chesstango.pgn.core.report.SummaryDiffReport;
import net.chesstango.pgn.core.report.SummaryModel;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class PGNSearchComparatorMain {

    public static void main(String[] args) {
        printSummaryLegends();

        PGNSearchComparatorMain PGNSearchComparatorMain = new PGNSearchComparatorMain("depth-6-2026-03-31-08-21-v1.6.0");
        PGNSearchComparatorMain.addSession("depth-6-2026-04-29-09-30-v1.7.0-SNAPSHOT");
        PGNSearchComparatorMain.addSession("depth-6-2026-05-01-15-31-v1.7.0-SNAPSHOT");
        //

        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\mate-w1.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\mate-b1.epd");

        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\mate-w2.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\mate-b2.epd");

        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\mate-w3.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\mate-b3.epd");

        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\Bratko-Kopec.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\Kaufman.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\wac-2018.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\sbd.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\Nolot.epd");

        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\STS1.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\STS2.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\STS3.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\STS4.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\STS5.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\STS6.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\STS7.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\STS8.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\STS9.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\STS10.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\STS11.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\STS12.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\STS13.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\STS14.epd");
        PGNSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\EPD\\database\\STS15.epd");
    }

    private static void printSummaryLegends() {
        String content = """
                Metric description:
                Duration     (ms): milliseconds spent in the search phase.
                Searches         : number of searches performed.
                Success       (%): percentage of successful moves.
                Coincidences  (%): percentage of coincidences between evaluations.
                Moves            : executed moves.
                Evaluations      : evaluations performed.
                 Collisions   (%): Different positions with same evaluation (Collisions).
                Max  Level       : Max depth reached.
                Vis  Nodes       : Visited nodes.
                Cutoff        (%): Cutoff percentage.
                PV Complete   (%): Principal variation complete percentage.
                TT ReadHits   (%): TT read hits percentage.
                TT OverWrites (%): TT overwrites percentage.
                """;
        System.out.println(content);
    }

    private final String baseLineSessionID;
    private final List<String> searchSessions = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Path suiteParentDirectory;
    private String suiteName;
    private SummaryModel baseLineSearchSummary;
    private List<SummaryModel> searchSummaryList;

    public PGNSearchComparatorMain(String baseLineSessionID) {
        this.baseLineSessionID = baseLineSessionID;
    }

    private void execute(String suiteFile) {
        loadSearchSummaries(suiteFile);
        printReport(System.out);
    }

    private void printReport(PrintStream out) {
        SummaryDiffModel reportModel = new SummaryDiffModel().collectStatistics(suiteName, new SummaryDiffModelInput(baseLineSearchSummary, searchSummaryList));

        new SummaryDiffReport()
                .withSummaryDiffReportModel(reportModel)
                .printReport(out);
    }


    private void loadSearchSummaries(String suiteFile) {
        Path suitePath = Paths.get(suiteFile);

        if (!Files.exists(suitePath)) {
            System.err.printf("file not found: %s\n", suiteFile);
            return;
        }

        suiteName = suitePath.getFileName().toString();

        suiteParentDirectory = suitePath.getParent();

        baseLineSearchSummary = loadSearchSummary(baseLineSessionID);

        if (baseLineSearchSummary == null) {
            System.err.printf("baseLineSearchSummary not found: %s\n", suiteName);
            return;
        }

        searchSummaryList = searchSessions.stream()
                .map(this::loadSearchSummary)
                .filter(Objects::nonNull)
                .toList();
    }

    private SummaryModel loadSearchSummary(String sessionID) {

        Path searchSummaryPath = suiteParentDirectory.resolve(sessionID).resolve(String.format("%s.json", suiteName));

        if (!Files.exists(searchSummaryPath)) {
            System.err.printf("file not found: %s\n", searchSummaryPath);
            return null;
        }

        try {
            return objectMapper.readValue(searchSummaryPath.toFile(), SummaryModel.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addSession(String sessionID) {
        searchSessions.add(sessionID);
    }
}



