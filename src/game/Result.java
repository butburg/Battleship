package game;

/**
 * @author Edwin W (HTW) on Nov 2020
 * The attack can result in different ways.
 */
public enum Result {
    /**
     * The attack hit a ship.
     */
    HIT,
    /**
     * Missed the ships. Splash. You hit the ocean.
     */
    MISSED,
    /**
     * You win! The attack hit the last remaining healthy part of the last ship!
     */
    WIN,
    /**
     * The attack hit the last remaining healthy part of a ship!
     */
    SINK;

    Result() {

    }
}
