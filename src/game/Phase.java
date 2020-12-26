package game;

/**
 * @author Edwin W (HTW) on Nov 2020
 * The game can be in more than one state. These states are here defined.
 */
public enum Phase {
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
    WAITFORPLAY,

    /**
     * When the game is over!
     */
    END
}
