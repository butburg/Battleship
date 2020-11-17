package game;

/**
 * @author Edwin W (570900) on Nov 2020
 * The game can be in more than one state. These states are here defined.
 */
public enum Phase {
    /**
     * The players choose their names.
     */
    CHOOSE,
    /**
     * the players place their own ships on their own field
     */
    SETSHIPS,
    /**
     * It is the current player turn and he can attack the opponent.
     */
    PLAY,
    /**
     * It is the opponent's turn and the current player must wait.
     */
    WAITFORPLAY;
}
