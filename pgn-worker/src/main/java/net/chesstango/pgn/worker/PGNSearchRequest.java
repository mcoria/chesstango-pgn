package net.chesstango.pgn.worker;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.gardel.epd.EPD;

import java.io.*;
import java.util.List;

/**
 * @author Mauricio Coria
 */
@Accessors(chain = true)
@Getter
@Setter
public class PGNSearchRequest implements Serializable {
    public final static String PGN_REQUESTS_QUEUE_NAME = "pgn_requests";

    @Serial
    private static final long serialVersionUID = 1L;

    private String sessionId;
    private String searchId;
    private int depth;
    private int timeOut;

    private List<EPD> epdList;

    public static PGNSearchRequest decodeRequest(byte[] request) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(request);
             ObjectInputStream ois = new ObjectInputStream(bis);) {
            return (PGNSearchRequest) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] encodeRequest() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos);) {
            oos.writeObject(this);
            oos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
