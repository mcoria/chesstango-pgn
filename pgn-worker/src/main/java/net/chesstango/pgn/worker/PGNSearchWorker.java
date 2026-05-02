package net.chesstango.pgn.worker;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.pgn.core.search.PGNSearch;
import net.chesstango.pgn.core.search.PGNSearchResult;
import net.chesstango.pgn.core.search.SearchSupplier;
import net.chesstango.gardel.epd.EPD;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
@Slf4j
class PGNSearchWorker implements Function<PGNSearchRequest, PGNSearchResponse> {

    @Override
    public PGNSearchResponse apply(PGNSearchRequest epdSearchRequest) {
        log.info("[{}] Running EPD search entries={}, depth={}, timeOut={}", epdSearchRequest.getSessionId(), epdSearchRequest.getEpdList().size(), epdSearchRequest.getDepth(), epdSearchRequest.getTimeOut());
        PGNSearch PGNSearch = new PGNSearch()
                .setDepth(epdSearchRequest.getDepth());

        if (epdSearchRequest.getTimeOut() > 0) {
            PGNSearch.setTimeOut(epdSearchRequest.getTimeOut());
        }

        SearchSupplier searchSupplier = new SearchSupplier();

        Stream<EPD> epdStream = epdSearchRequest.getEpdList().stream();

        List<PGNSearchResult> PGNSearchResults = PGNSearch.run(searchSupplier, epdStream);

        log.info("[{}] Completed EPD search entries={}, depth={}, timeOut={}", epdSearchRequest.getSessionId(), epdSearchRequest.getEpdList().size(), epdSearchRequest.getDepth(), epdSearchRequest.getTimeOut());

        return new PGNSearchResponse()
                .setPGNSearchResults(PGNSearchResults)
                .setSessionId(epdSearchRequest.getSessionId())
                .setSearchId(epdSearchRequest.getSearchId());
    }
}
