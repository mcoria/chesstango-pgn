package net.chesstango.pgn.core.report;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.pgn.core.search.PGNSearchResult;
import net.chesstango.reports.Report;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PGNSearchReport implements Report {

    private PrintStream out;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "PGNSearchReport";

    @Setter
    @Accessors(chain = true)
    private PGNSearchModel reportModel;

    @Override
    public PGNSearchReport printReport(PrintStream output) {
        out = output;
        new PGNSearchPrinter()
                .setReportTitle(reportTitle)
                .setReportModel(reportModel)
                .setOut(out)
                .print();
        return this;
    }

    public PGNSearchReport withEdpEntries(List<PGNSearchResult> edpEntries) {
        this.reportModel = new PGNSearchModel().collectStatistics(reportTitle, edpEntries);
        return this;
    }

}
