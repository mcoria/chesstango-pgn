module net.chesstango.pgn.master {

    exports net.chesstango.pgn.master.filters;
    exports net.chesstango.pgn.master;

    requires net.chesstango.board;

    requires net.chesstango.evaluation;
    requires net.chesstango.gardel;
    requires net.chesstango.piazzolla;
    requires net.chesstango.search;

    requires org.apache.commons.cli;
    requires com.fasterxml.jackson.databind;
    requires org.slf4j;

    requires static lombok;
    requires net.chesstango.engine;
    requires com.rabbitmq.client;
    requires net.chesstango.pgn.worker;
    requires net.chesstango.pgn.core;
}