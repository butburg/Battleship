package exceptions;

/**
 * @author Edwin W (HTW) on Nov 2020
 * These eceptions are thrown by the main game.
 */
public class BattleshipException extends java.lang.Exception implements ExceptionMsg {
    public BattleshipException() {
        super();
    }

    public BattleshipException(String msg) {
        super(msg);
    }

    public BattleshipException(String msg, Throwable t) {
        super(msg, t);
    }
}
