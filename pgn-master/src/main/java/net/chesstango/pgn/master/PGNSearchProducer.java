package net.chesstango.pgn.master;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import net.chesstango.pgn.worker.PGNSearchRequest;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * @author Mauricio Coria
 */
@Slf4j
public class PGNSearchProducer implements AutoCloseable {
    private final Connection connection;
    private final Channel channel;

    public PGNSearchProducer(ConnectionFactory factory) throws IOException, TimeoutException {
        this.connection = factory.newConnection();
        this.channel = connection.createChannel();
        channel.queueDeclare(PGNSearchRequest.PGN_REQUESTS_QUEUE_NAME, false, false, false, null);
        channel.basicQos(1);
    }

    @Override
    public void close() throws Exception {
        channel.close();
        connection.close();
    }

    public void publish(PGNSearchRequest epdSearchRequest) {
        try {
            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .build();
            byte[] message = epdSearchRequest.encodeRequest();
            channel.basicPublish("", PGNSearchRequest.PGN_REQUESTS_QUEUE_NAME, props, message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
