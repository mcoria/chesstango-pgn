package net.chesstango.pgn.worker;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static net.chesstango.pgn.worker.PGNSearchRequest.PGN_REQUESTS_QUEUE_NAME;

/**
 * @author Mauricio Coria
 */
@Slf4j
class RequestConsumer {

    private final Channel channel;

    public RequestConsumer(Channel channel) {
        this.channel = channel;
    }


    public PGNSearchRequest readMessage() throws IOException {
        do {
            GetResponse response = channel.basicGet(PGN_REQUESTS_QUEUE_NAME, true);
            if (response != null) {
                return PGNSearchRequest.decodeRequest(response.getBody());
            } else {
                try {
                    log.info("Waiting for PGNSearchRequest");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("Interrupted while waiting for message", e);
                    throw new RuntimeException(e);
                }
            }
        } while (true);
    }
}
