package exceptions;

/**
 * @author Edwin W (570900) on Nov 2020
 * This Exceptions occur when a ship cannot handle them on its own.
 */
public class ShipException extends Throwable implements ExceptionMessages {
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
