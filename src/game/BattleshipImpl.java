package game;

import exceptions.BattleshipException;
import exceptions.ExceptionMsg;
import exceptions.PhaseException;
import field.Coordinate;
import field.Ocean;
import field.OceanImpl;
import ship.Ship;
import ship.ShipImpl;
import ship.Shipmodel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Edwin W (570900) on Nov 2020
 * This is an implementation for the battleship game.
 */
public class BattleshipImpl implements Battleship {

    private Phase phase = Phase.CHOOSE;
    final private int PLAYERCOUNT = 2;
    private int playerNumber = 0; //is there a more generic way (needed?)
    private String[] players = new String[PLAYERCOUNT];
    private Ocean ocean;

    private Ocean ocean1 = new OceanImpl(11);
    private Ocean ocean2 = new OceanImpl(11);

    private ArrayList<Ship> ships;
    private ArrayList<Ship> ships1 = new ArrayList<>();
    private ArrayList<Ship> ships2 = new ArrayList<>();

    public BattleshipImpl() {
        //TODO add an function for that task
        //TODO set a Map with Shipmodel as Key and Count as Value Map(Shipmodel.CRUISERS->2,...)
        for (Shipmodel shipmodel1 : Arrays.asList(Shipmodel.BATTLESHIP, Shipmodel.CRUISERS, Shipmodel.CRUISERS, Shipmodel.DESTROYERS, Shipmodel.DESTROYERS, Shipmodel.DESTROYERS, Shipmodel.SUBMARINES, Shipmodel.SUBMARINES, Shipmodel.SUBMARINES, Shipmodel.SUBMARINES)) {
            ships1.add(new ShipImpl(shipmodel1));
        }
        for (Shipmodel shipmodel : Arrays.asList(Shipmodel.BATTLESHIP, Shipmodel.CRUISERS, Shipmodel.CRUISERS, Shipmodel.DESTROYERS, Shipmodel.DESTROYERS, Shipmodel.DESTROYERS, Shipmodel.SUBMARINES, Shipmodel.SUBMARINES, Shipmodel.SUBMARINES, Shipmodel.SUBMARINES)) {
            ships2.add(new ShipImpl(shipmodel));
        }

    }

    public String[] getPlayers() {
        return players;
    }

    @Override
    public void choosePlayerName(String playerName) throws BattleshipException, PhaseException {
        if (phase != Phase.CHOOSE) throw new PhaseException(ExceptionMsg.wrongPhase);
        if (nameTaken(playerName)) throw new BattleshipException(ExceptionMsg.playerNameTaken);
        //if (playerNumber > 1) throw new BattleshipException(ExceptionMsg.tooManyPlayers);
        //is this check needed when I set the Phase also here?

        //check for actual player names in the array and add the name if not full
        if (playerNumber == 0) {
            this.players[playerNumber] = playerName;
            playerNumber++;
        } else if (playerNumber == 1) {
            this.players[playerNumber] = playerName;
            playerNumber++;
            setPhase(Phase.SETSHIPS); //maybe wrong place?
        }
    }


    private boolean nameTaken(String playerName) {
        return Arrays.asList(players).contains(playerName);
    }

    private void setPhase(Phase newPhase) {
        //TODO build a Pattern for Status with Classes
        this.phase = newPhase;
    }


    @Override
    public boolean setShip(String player, Ship ship) throws BattleshipException, PhaseException {
        return setShip(player, ship.getModel(), new Coordinate(3, 4), false);
    }


    @Override
    public boolean setShip(String player, Shipmodel shipmodel, Coordinate xy, boolean vertical) throws BattleshipException, PhaseException {
        if (phase != Phase.SETSHIPS) throw new PhaseException(ExceptionMsg.wrongPhase);
        chooseOceanAnShips(player); //also have to restore the values back with actual list/map
        if (ships.isEmpty()) throw new BattleshipException(ExceptionMsg.shipAllSet);

        Ship shipToSet = null;
        for (Ship ship : ships) {
            if (ship.getModel() == shipmodel && shipToSet == null) {
                shipToSet = ship;
            }
        }
        ships.remove(shipToSet);
        if (shipToSet == null) throw new BattleshipException(ExceptionMsg.shipTypeAllSet);
        ocean.placeShipPart(shipToSet, xy.x, xy.y, vertical);
        //TODO set the ship variable back into ship1 or 2 to get the change/remove stored properly
        if (ships.isEmpty()) {
            if (ships1.isEmpty() && ships2.isEmpty()) {
                setPhase(Phase.PLAY);
            }
            return false;
        } else
            return true;
    }

    private void chooseOceanAnShips(String player) throws BattleshipException {
        if (player.equals(players[0])) {
            this.ocean = ocean1;
            this.ships = ships1;
        } else if (player.equals(players[1])) {
            this.ocean = ocean2;
            this.ships = ships2;
        } else throw new BattleshipException(ExceptionMsg.wrongPlayer);
    }

    @Override
    public Result attack(String player, Point position) throws PhaseException {
        if (phase != Phase.PLAY && phase != Phase.WAITFORPLAY) throw new PhaseException(ExceptionMsg.wrongPhase);
        return null;
    }

    @Override
    public Phase getPhase() {
        return this.phase;
    }
}
