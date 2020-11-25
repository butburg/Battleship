package exceptions;

/**
 * @author Edwin W (HTW) on Nov 2020
 * These eceptions are thrown when a method call is made
 * but are not allowed in this status/phase of the game.
 * Always is it a matter of timing!!
 */
public class PhaseException extends java.lang.Exception implements ExceptionMsg {
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
