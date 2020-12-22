package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author T. Schwotzer (modified by Edwin W (HTW) on Nov 2020)
 */
public interface ProtocolEngine {
    /**
     * Setup protocol engine. Provide streams to read from and write to.
     *
     * @param is the stream the class reads from
     * @param os the stream the class write into
     * @throws IOException if there is an input / output incorrect
     */
    void handleConnectionStream(InputStream is, OutputStream os) throws IOException;

    /**
     * Stop engine - close streams and release all resources
     *
     * @throws IOException if there is an input / output incorrect, cant be closed
     */
    void close() throws IOException;

    //void subscribeGameSessionEstablishedListener(GameSessionEstablishedListener ocListener);

    //void unsubscribeGameSessionEstablishedListener(GameSessionEstablishedListener ocListener);
}
