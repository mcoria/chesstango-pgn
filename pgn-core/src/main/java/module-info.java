module net.chesstango.pgn.core {
    exports net.chesstango.pgn.core.report;
    exports net.chesstango.pgn.core.search;

    requires net.chesstango.gardel;
    requires net.chesstango.search;
    requires net.chesstango.board;
    requires net.chesstango.reports;

    requires org.slf4j;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    requires static lombok;
    requires net.chesstango.evaluation;
    requires net.chesstango.engine;

    opens net.chesstango.pgn.core.report to com.fasterxml.jackson.databind;
    exports net.chesstango.pgn.core.main;
}