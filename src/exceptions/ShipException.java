package exceptions;

/**
 * @author Edwin W (HTW) on Nov 2020
 * This Exceptions occurs when a ship cannot handle them on its own.
 */
public class ShipException extends Throwable implements ExceptionMsg {
    public ShipException() {
        super();
    }

    public ShipException(String msg) {
        super(msg);
    }

    public ShipException(String msg, Throwable t) {
        super(msg, t);
    }
}
