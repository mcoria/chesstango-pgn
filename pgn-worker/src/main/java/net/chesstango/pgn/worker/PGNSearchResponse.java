package net.chesstango.pgn.worker;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.pgn.core.search.PGNSearchResult;

import java.io.*;
import java.util.List;

/**
 * @author Mauricio Coria
 */
@Accessors(chain = true)
@Getter
@Setter
public class PGNSearchResponse implements Serializable {
    public final static String PGN_RESPONSES_QUEUE_NAME = "pgn_responses";

    @Serial
    private static final long serialVersionUID = 1L;

    private String sessionId;
    private String searchId;

    private List<PGNSearchResult> PGNSearchResults;

    public static PGNSearchResponse decodeResponse(byte[] request) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(request);
             ObjectInputStream ois = new ObjectInputStream(bis);) {
            return (PGNSearchResponse) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] encodeResponse() {
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
