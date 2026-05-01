package net.chesstango.pgn.core.report;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.pgn.core.search.PGNSearchResult;
import net.chesstango.reports.Printer;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EpdSearchPrinter implements Printer {

    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "EpdSearchReport";

    @Setter
    @Accessors(chain = true)
    private EpdSearchModel reportModel;


    @Override
    public EpdSearchPrinter print() {
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------%n");
        out.printf("EpdSearchReport: %s%n%n", reportModel.reportTitle);

        out.printf("Searches        : %d%n", reportModel.searches);
        out.printf("Success rate    : %d%%%n", reportModel.successRate);
        out.printf("Time (ms)       : %d%n%n", reportModel.duration);

        if (reportModel.failedEntries.isEmpty()) {
            out.println("\tall tests executed successfully !!!!");
        } else {
            for (String failedTest : reportModel.failedEntries) {
                out.printf("\t%s%n", failedTest);
            }
        }
        return this;
    }

    public EpdSearchPrinter withEdpEntries(List<PGNSearchResult> edpEntries) {
        this.reportModel = new EpdSearchModel().collectStatistics(reportTitle, edpEntries);
        return this;
    }

}
