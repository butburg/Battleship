package game;

import exceptions.*;
import field.Coordinate;
import field.Ocean;
import field.OceanImpl;
import ship.Ship;
import ship.ShipImpl;
import ship.Shipmodel;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Edwin W (HTW) on Nov 2020
 * This is an implementation for the battleship game.
 */
public class BattleshipImpl implements Battleship {

    private Phase phase = Phase.CHOOSE;
    final private int PLAYERCOUNT = 2;
    private int playerNumber = 0; //is there a more generic way (needed?)
    private final String[] players = new String[PLAYERCOUNT];
    private Ocean ocean;

    private Ocean ocean1 = new OceanImpl(11);
    private Ocean ocean2 = new OceanImpl(11);

    private ArrayList<Ship> ships;
    private ArrayList<Ship> ships1 = new ArrayList<>();
    private ArrayList<Ship> ships2 = new ArrayList<>();

    public BattleshipImpl() {
        createAllShips();
    }

    /**
     * This Method creates the ships that can be set in the game. Will create the ships for both player.
     * Here you can change the number of every shipmodel!
     * (Default: 1 Battleship, 2 Cruiser, 3 Destroyers, 4 Submarines)
     */
    private void createAllShips() {
        createShip(Shipmodel.BATTLESHIP, 1);
        createShip(Shipmodel.CRUISERS, 2);
        createShip(Shipmodel.DESTROYERS, 3);
        createShip(Shipmodel.SUBMARINES, 4);
    }

    private void createShip(Shipmodel ship, int count) {
        for (; count > 0; count--) {
            ships1.add(new ShipImpl(ship));
            ships2.add(new ShipImpl(ship));
        }
    }

    public String[] getPlayers() {
        return players;
    }

    @Override
    public void choosePlayerName(String playerName) throws BattleshipException, PhaseException {
        if (phase != Phase.CHOOSE) throw new PhaseException(ExceptionMsg.ph_wrongPhase);
        if (nameTaken(playerName)) throw new BattleshipException(ExceptionMsg.bs_playerNameTaken);

        //check for actual player names in the array and add the name if not full
        if (playerNumber == 0) {
            this.players[playerNumber] = playerName;
            playerNumber++;
        } else if (playerNumber == 1) {
            this.players[playerNumber] = playerName;
            playerNumber++;
            setNextPhase();
        }
    }

    private boolean nameTaken(String playerName) {
        return Arrays.asList(players).contains(playerName);
    }

    private void setPhase(Phase newPhase) {
        this.phase = newPhase;
    }


    @Override
    public boolean setShip(String player, Shipmodel shipmodel, Coordinate xy, boolean vertical) throws BattleshipException, PhaseException, OceanException, ShipException {
        if (phase != Phase.SETSHIPS) throw new PhaseException(ExceptionMsg.ph_wrongPhase);
        //get the ships and ocean from the actual player
        chooseOceanAndShips(player); //also have to restore the values back with actual list/map
        if (ships.isEmpty()) throw new BattleshipException(ExceptionMsg.bs_shipAllSet);

        Ship shipToSet = null;
        for (Ship ship : ships) {
            if (ship.getModel() == shipmodel && shipToSet == null) {
                shipToSet = ship;
            }
        }
        ships.remove(shipToSet);
        if (shipToSet == null) throw new BattleshipException(ExceptionMsg.bs_shipTypeAllSet);
        ocean.placeShip(shipToSet, xy.x, xy.y, vertical);
        //update the list and the ocean for the actual player
        updateOceanAndShips(ships, ocean, player);
        if (ships.isEmpty()) {
            if (ships1.isEmpty() && ships2.isEmpty()) {
                setNextPhase();
            }
            //if there are more ships to set for the actual player
            return false;
        } else
            //if it was the last placed ship for the actual player
            return true;
    }


    @Override
    public boolean setShip(String player, Shipmodel ship, Coordinate xy) throws BattleshipException, PhaseException, OceanException, ShipException {
        return setShip(player, ship, xy, false);
    }


    private void setNextPhase() {
        switch (this.phase) {
            case CHOOSE -> setPhase(Phase.SETSHIPS);
            case SETSHIPS, WAITFORPLAY -> setPhase(Phase.PLAY);
            case PLAY -> setPhase(Phase.WAITFORPLAY);
        }
    }

    private void updateOceanAndShips(ArrayList<Ship> ships, Ocean ocean, String player) throws BattleshipException {
        if (player.equals(players[0])) {
            this.ocean1 = ocean;
            this.ships1 = ships;
        } else if (player.equals(players[1])) {
            this.ocean2 = ocean;
            this.ships2 = ships;
        } else throw new BattleshipException(ExceptionMsg.bs_wrongPlayer);
    }

    private void chooseOceanAndShips(String player) throws BattleshipException {
        if (player.equals(players[0])) {
            this.ocean = ocean1;
            this.ships = ships1;
        } else if (player.equals(players[1])) {
            this.ocean = ocean2;
            this.ships = ships2;
        } else throw new BattleshipException(ExceptionMsg.bs_wrongPlayer);
    }

    @Override
    public Result attack(String player, Coordinate position) throws PhaseException, BattleshipException, ShipException, OceanException {
        if (phase != Phase.PLAY && phase != Phase.WAITFORPLAY) throw new PhaseException(ExceptionMsg.ph_wrongPhase);
        if (!nameTaken(player)) throw new BattleshipException(ExceptionMsg.bs_wrongPlayer);
        if (phase == Phase.WAITFORPLAY && players[0].equals(player))
            throw new BattleshipException(ExceptionMsg.bs_wrongTurn1);
        if (phase == Phase.PLAY && players[1].equals(player)) throw new BattleshipException(ExceptionMsg.bs_wrongTurn2);

        Result hitResult = ocean.bombAt(position);

        setNextPhase();
        return hitResult;
    }

    @Override
    public Phase getPhase() {
        return this.phase;
    }
}
