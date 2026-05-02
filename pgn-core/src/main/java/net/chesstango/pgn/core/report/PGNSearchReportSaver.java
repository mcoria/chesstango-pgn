package net.chesstango.pgn.core.report;

import net.chesstango.pgn.core.search.PGNSearchResult;
import net.chesstango.reports.ReportToFile;

import java.nio.file.Path;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PGNSearchReportSaver {
    private final Path directory;

    private PGNAgregateModel pgnAgregateModel;
    private SummaryModel summaryModel;


    public PGNSearchReportSaver(Path directory) {
        this.directory = directory;
    }

    public void loadModel(String sessionId, List<PGNSearchResult> PGNSearchResults) {
        this.pgnAgregateModel = PGNAgregateModel.load(sessionId, PGNSearchResults);
        this.summaryModel = new SummaryModel().collectStatistics(sessionId, pgnAgregateModel);
    }

    public void saveReport(String suiteName) {
        ReportToFile reportToFile = new ReportToFile(directory);
        reportToFile.save(String.format("%s-report.txt", suiteName), new PGNAgregateReport()
                .setPgnAgregateModel(pgnAgregateModel)
        );
    }

    public void saveJson(String suiteName) {
        ReportToFile reportToFile = new ReportToFile(directory);
        reportToFile.save(String.format("%s.json", suiteName), new SummaryReport()
                .setReportModel(summaryModel)
        );
    }

}
