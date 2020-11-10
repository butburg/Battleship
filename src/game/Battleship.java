package game;

import exceptions.BattleshipException;
import exceptions.StatusException;
import ship.Ship;

import java.awt.*;

/**
 * @author Edwin W (570900) on Nov 2020
 */
public interface Battleship {

    /**
     * Two players are allowed to choose a name(must be different)
     * Phase 1
     *
     * @param playerName the new Name for a Player
     * @throws BattleshipException
     * @throws StatusException
     */
    void choosePlayer(String playerName) throws BattleshipException, StatusException;

    /**
     * Set one ship at players own field, validates the position of the ship and checks, if all ships are set
     * Phase 2
     *
     * @param player the player who is setting the ship
     * @param ship   the specific ship, that should be set into the field
     * @return if ships are left to set true else false
     * @throws BattleshipException wrong player, wrong ships, wrong positions, out of bounds, wrong state
     * @throws StatusException     not allowed to set a Ship, in the wrong phase
     */
    boolean setShip(String player, Ship ship) throws BattleshipException, StatusException;


    /**
     * hit ? fail ? win ? destroy (Enum Result)
     * Phase 3
     *
     * @param player   player who is attacking
     * @param position position to be attacked
     * @return hit ? fail ? win ? destroy
     * @throws BattleshipException wrong player, outOfBounds, wrong state, already attacked
     * @throws StatusException     not allowed to attack, in the wrong phase, not your turn
     */
    Result attack(String player, Point position) throws BattleshipException, StatusException;
}