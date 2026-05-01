module net.chesstango.pgn.worker {
    exports net.chesstango.pgn.worker;

    requires net.chesstango.board;
    requires net.chesstango.gardel;
    requires net.chesstango.search;
    requires net.chesstango.evaluation;
    requires net.chesstango.pgn.core;

    requires org.slf4j;
    requires com.rabbitmq.client;
    requires static lombok;
}