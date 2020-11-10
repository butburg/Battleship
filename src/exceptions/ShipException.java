package exceptions;

/**
 * @author Edwin W (570900) on Nov 2020
 */
public class ShipException extends Throwable {
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
