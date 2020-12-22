package network;

/**
 * @author Edwin W (HTW) on Dez 2020
 */
public interface SessionEstablishedSubscriber {
    /**
     * is called when oracle was created
     * @param oracle if the instance is the one and only oracle true, or the other left one false
     */
    void sessionEstablished(boolean oracle, String partnerName);
}
