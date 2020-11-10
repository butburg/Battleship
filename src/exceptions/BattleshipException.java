package exceptions;

/**
 * @author Edwin W (570900) on Nov 2020
 */
public class BattleshipException extends Exception {
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
