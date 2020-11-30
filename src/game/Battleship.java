package game;

import exceptions.BattleshipException;
import exceptions.OceanException;
import exceptions.PhaseException;
import exceptions.ShipException;
import field.Coordinate;
import ship.Shipmodel;

/**
 * @author Edwin W (HTW) on Nov 2020
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
     * @throws BattleshipException wrong player or no ships to set
     * @throws PhaseException      not allowed to set a Ship, in the wrong phase
     * @throws OceanException      when the ship cant be set into the ocean / field
     * @throws ShipException       when the ship cant be located
     */
    boolean setShip(String player, Shipmodel ship, Coordinate xy, boolean vertical) throws BattleshipException, PhaseException, OceanException, ShipException;


    /**
     * Overwriting setShip without the parameter vertical, will be set to false.
     *
     * @param player the player who is setting the ship
     * @param ship   the specific ship, that should be set into the field
     * @param xy     the coordinate
     * @return if ships are left to set true else false
     * @throws BattleshipException wrong player, wrong ships, wrong positions, out of bounds, wrong state
     * @throws PhaseException      not allowed to set a Ship, in the wrong phase
     * @throws OceanException      when the ship cant be set into the ocean / field
     * @throws ShipException       when the ship cant be located
     */
    boolean setShip(String player, Shipmodel ship, Coordinate xy) throws BattleshipException, PhaseException, OceanException, ShipException;

    /**
     * The attack can hit an opponents ship or fail. If its a ships last hit, it will sink.
     * If the last ship of all sinks, the game ends and you win.
     * Phase 3
     *
     * @param player   player who is attacking
     * @param position position to be attacked at the ocean(field)
     * @return can be hit, missed, sink or win
     * @throws BattleshipException wrong player, outOfBounds, wrong state, already attacked
     * @throws PhaseException      not allowed to attack, in the wrong phase, not your turn
     */
    Result attack(String player, Coordinate position) throws BattleshipException, PhaseException, ShipException, OceanException;

    /**
     * returns the phase of the game
     *
     * @return the Enum Phase: CHOOSE,SETSHIPS, PLAY, WAITFORPLAY or END
     */
    Phase getPhase();

    /**
     * returns an Array with the names of the player
     *
     * @return the names of the player
     */
    String[] getPlayers();
}