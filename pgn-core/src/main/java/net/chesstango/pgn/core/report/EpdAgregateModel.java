package net.chesstango.pgn.core.report;

import net.chesstango.pgn.core.search.PGNSearchResult;
import net.chesstango.reports.search.board.BoardModel;
import net.chesstango.reports.search.evaluation.EvaluationModel;
import net.chesstango.reports.search.evaluation.iteration.EvaluationIterationModel;
import net.chesstango.reports.search.nodes.depth.NodesDepthModel;
import net.chesstango.reports.search.nodes.types.NodesTypesModel;

import net.chesstango.reports.search.pv.PrincipalVariationModel;
import net.chesstango.reports.search.pv.iteration.PrincipalVariationIterationModel;
import net.chesstango.reports.search.transposition.TranspositionModel;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public record EpdAgregateModel(List<PGNSearchResult> PGNSearchResults,
                               EpdSearchModel epdSearchModel,
                               BoardModel boardModel,
                               NodesDepthModel nodesDepthModel,
                               NodesTypesModel nodesTypesModel,
                               EvaluationIterationModel evaluationIterationModel,
                               PrincipalVariationModel principalVariationReportModel,
                               PrincipalVariationIterationModel principalVariationIterationReportModel,
                               EvaluationModel evaluationReportModel,
                               TranspositionModel transpositionModel) {
    public static EpdAgregateModel load(String suiteName, List<PGNSearchResult> PGNSearchResults) {
        EpdSearchModel epdSearchModel = new EpdSearchModel().collectStatistics(suiteName, PGNSearchResults);
        BoardModel boardModel = new BoardModel().collectStatistics(suiteName, PGNSearchResults.stream().map(PGNSearchResult::getSearchResult).toList());
        NodesDepthModel nodesDepthModel = new NodesDepthModel().collectStatistics(suiteName, PGNSearchResults.stream().map(PGNSearchResult::getSearchResult).toList());
        NodesTypesModel nodesTypesModel = new NodesTypesModel().collectStatistics(suiteName, PGNSearchResults.stream().map(PGNSearchResult::getSearchResult).toList());
        EvaluationIterationModel iterationEvaluationModel = new EvaluationIterationModel().collectStatistics(suiteName, PGNSearchResults.stream().map(PGNSearchResult::getSearchResult).toList());
        EvaluationModel evaluationReportModel = new EvaluationModel().collectStatistics(suiteName, PGNSearchResults.stream().map(PGNSearchResult::getSearchResult).toList());
        PrincipalVariationModel principalVariationReportModel = new PrincipalVariationModel().collectStatistics(suiteName, PGNSearchResults.stream().map(PGNSearchResult::getSearchResult).toList());
        PrincipalVariationIterationModel principalVariationIterationReportModel = new PrincipalVariationIterationModel().collectStatistics(suiteName, PGNSearchResults.stream().map(PGNSearchResult::getSearchResult).toList());
        TranspositionModel transpositionReportModel = new TranspositionModel().collectStatistics(suiteName, PGNSearchResults.stream().map(PGNSearchResult::getSearchResult).toList());
        return new EpdAgregateModel(PGNSearchResults, epdSearchModel, boardModel, nodesDepthModel, nodesTypesModel, iterationEvaluationModel, principalVariationReportModel, principalVariationIterationReportModel, evaluationReportModel, transpositionReportModel);
    }
}
