package net.chesstango.pgn.core.report;

import net.chesstango.pgn.core.search.PGNSearchResult;
import net.chesstango.reports.Model;
import net.chesstango.search.SearchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EpdSearchModel implements Model<List<PGNSearchResult>> {
    String reportTitle;

    int searches;
    int success;
    int successRate;
    List<String> failedEntries;

    long duration;


    @Override
    public EpdSearchModel collectStatistics(String reportTitle, List<PGNSearchResult> epdEntries) {
        List<SearchResult> searchResults = epdEntries.stream().map(PGNSearchResult::getSearchResult).toList();

        this.reportTitle = reportTitle;

        this.searches = epdEntries.size();

        this.success = (int) epdEntries.stream().filter(PGNSearchResult::isSearchSuccess).count();

        this.successRate = ((100 * this.success) / this.searches);

        this.duration = searchResults.stream().mapToLong(SearchResult::getTimeSearching).sum();

        this.failedEntries = new ArrayList<>();

        epdEntries.stream()
                .filter(edpEntry -> !edpEntry.isSearchSuccess())
                .forEach(edpEntry ->
                        this.failedEntries.add(
                                String.format("Fail [%s] - best move found %s",
                                        edpEntry.getEPDText(),
                                        edpEntry.getBestMove()
                                )
                        ));

        return this;
    }
}
