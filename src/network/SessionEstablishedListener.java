package network;

/**
 * @author Edwin W (HTW) on Dez 2020
 */
public interface SessionEstablishedListener {
    /**
     * is called when oracle was created
     * @param oracle
     */
    void sessionEstablished(boolean oracle, String partnerName);
}
