package exceptions;

/**
 * @author Edwin W (570900) on Nov 2020
 */
public class PhaseException extends Exception {
    public PhaseException() {
        super();
    }

    public PhaseException(String msg) {
        super(msg);
    }

    public PhaseException(String msg, Throwable t) {
        super(msg, t);
    }
}
