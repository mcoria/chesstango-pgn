package net.chesstango.pgn.core.search;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.epd.EPD;
import net.chesstango.search.SearchResult;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
@Accessors(chain = true)
@Getter
@Setter
public class PGNSearchResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 2L;

    private final EPD epd;

    private final SearchResult searchResult;

    public PGNSearchResult(EPD epd, SearchResult searchResult) {
        this.epd = epd;
        this.searchResult = searchResult;
    }

    public String getEPDText() {
        return epd.getText();
    }

    public int getBottomMoveCounter() {
        return searchResult.getBottomMoveCounter();
    }

    public String getBestMove() {
        Move bestMove = searchResult.getBestMove();
        return bestMove.coordinateEncoding();
    }

    public boolean isSearchSuccess() {
        return epd.isMoveSuccess(getBestMove());
    }
}
