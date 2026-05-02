package net.chesstango.pgn.core.report;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.engine.Tango;
import net.chesstango.pgn.core.search.PGNSearchResult;
import net.chesstango.reports.Report;
import net.chesstango.reports.search.board.BoardReport;
import net.chesstango.reports.search.evaluation.EvaluationReport;
import net.chesstango.reports.search.evaluation.iteration.EvaluationIterationReport;
import net.chesstango.reports.search.nodes.depth.NodesDepthReport;
import net.chesstango.reports.search.nodes.types.NodesTypesReport;
import net.chesstango.reports.search.pv.PrincipalVariationReport;
import net.chesstango.reports.search.pv.iteration.PrincipalVariationIterationReport;
import net.chesstango.reports.search.transposition.TranspositionReport;

import java.io.PrintStream;
import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class PGNAgregateReport implements Report {

    private PGNAgregateModel pgnAgregateModel;

    @Override
    public PGNAgregateReport printReport(PrintStream out) {

        out.printf("Version: %s\n", Tango.ENGINE_VERSION);

        new PGNSearchReport()
                .setReportModel(pgnAgregateModel.epdSearchModel())
                .printReport(out);

        new BoardReport()
                .setReportModel(pgnAgregateModel.boardModel())
                .printReport(out);

        new NodesDepthReport()
                .setReportModel(pgnAgregateModel.nodesDepthModel())
                .withCutoffStatistics()
                .withNodesVisitedStatistics()
                .printReport(out);

        new NodesTypesReport()
                .setReportModel(pgnAgregateModel.nodesTypesModel())
                .printReport(out);

        new EvaluationIterationReport()
                .setReportModel(pgnAgregateModel.evaluationIterationModel())
                .printReport(out);

        new PrincipalVariationReport()
                .setReportModel(pgnAgregateModel.principalVariationReportModel())
                .printReport(out);

        new PrincipalVariationIterationReport()
                .setReportModel(pgnAgregateModel.principalVariationIterationReportModel())
                .printReport(out);

        new EvaluationReport()
                .setReportModel(pgnAgregateModel.evaluationReportModel())
                //.withExportEvaluations()
                .withEvaluationsStatistics()
                .printReport(out);

        new TranspositionReport()
                .setTranspositionModel(pgnAgregateModel.transpositionModel())
                .printReport(out);

        return this;
    }

    public PGNAgregateReport withEpdSearchResults(String suiteName, List<PGNSearchResult> PGNSearchResults) {
        this.pgnAgregateModel = PGNAgregateModel.load(suiteName, PGNSearchResults);
        return this;
    }

}
