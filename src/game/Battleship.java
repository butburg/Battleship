package game;

import exceptions.BattleshipException;
import exceptions.OceanException;
import exceptions.PhaseException;
import field.Coordinate;
import ship.Ship;
import ship.Shipmodel;

import java.awt.*;

/**
 * @author Edwin W (570900) on Nov 2020
 * The interface for the battleship game. There will be two players and oceans. Players can set their ships
 * and after that attack each other in rounds until each ship has been hit and sunk by one player.
 */
public interface Battleship {

    /**
     * The start of the game. Two players must choose a name (must be different).
     * <p>
     * Phase 1
     *
     * @param playerName the new Name for a Player
     * @throws BattleshipException when the names are equal or too many players call method
     * @throws PhaseException      when the phase of the game is not anymore choosing the names, e.i. attacking phase
     */
    void choosePlayerName(String playerName) throws BattleshipException, PhaseException;

    /**
     * Set one ship at the players field, validate the position of the ship and check that all ships are set.
     * Usually there are 10 ships of different types to be placed.
     * <p>
     * Phase 2
     *
     * @param player the player who is setting the ship
     * @param ship   the specific ship, that should be set into the field
     * @param xy     the coordinate
     * @return if ships are left to set true else false
     * @throws BattleshipException wrong player, wrong ships, wrong positions, out of bounds, wrong state
     * @throws PhaseException      not allowed to set a Ship, in the wrong phase
     */
    boolean setShip(String player, Shipmodel ship, Coordinate xy, boolean vertical) throws BattleshipException, PhaseException, OceanException;

    boolean setShip(String player, Ship ship) throws BattleshipException, PhaseException, OceanException;

    /**
     * The attack can hit an opponents ship or fail. If its a ships last hit, it will sink.
     * If the last ship of all sinks, the game ends and you win.
     * <p>
     * Phase 3
     *
     * @param player   player who is attacking
     * @param position position to be attacked at the ocean(field)
     * @return can be hit, miss, sink or win
     * @throws BattleshipException wrong player, outOfBounds, wrong state, already attacked
     * @throws PhaseException      not allowed to attack, in the wrong phase, not your turn
     */
    Result attack(String player, Point position) throws BattleshipException, PhaseException;

    /**
     * returns the phase of the game
     *
     * @return the Enum Phase: CHOOSE,SETSHIPS, PLAY, WAITFORPLAY
     */
    Phase getPhase();

    String[] getPlayers();
}