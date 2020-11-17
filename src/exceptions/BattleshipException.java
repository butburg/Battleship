package exceptions;

/**
 * @author Edwin W (570900) on Nov 2020
 * These eceptions are thrown by the main game.
 */
public class BattleshipException extends Exception implements ExceptionMessages {
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
