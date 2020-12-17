package game;

import exceptions.*;
import field.Coordinate;
import field.Ocean;
import field.OceanImpl;
import network.BattleshipProtocolEngine;
import network.SessionEstablishedListener;
import ship.Ship;
import ship.ShipImpl;
import ship.Shipmodel;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Edwin W (HTW) on Nov 2020
 * This is an implementation for the battleship game.
 */
public class BattleshipImpl implements Battleship, SessionEstablishedListener {

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
    private int[] shipsDestroyed = new int[2];
    private int numberOfShips;
    private BattleshipProtocolEngine protocolEngine;
    private boolean startsFirst = false;

    public BattleshipImpl(String localPLayer) {
        createAllShips();
        players[0] = localPLayer;
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
            numberOfShips++;
        }
    }

    public String[] getPlayers() {
        return players;
    }

    @Override
    public void choosePlayerName(String playerName) throws BattleshipException, PhaseException {
    /*    if (phase != Phase.CHOOSE) throw new PhaseException(ExceptionMsg.ph_wrongPhase);
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

        // tell other side - if local call (test of null if for some unit tests)
        if (isLocalCall(playerName) && this.protocolEngine != null) {
            this.protocolEngine.choosePlayerName(playerName);
        } else throw new BattleshipException();
*/
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

        // tell other side - if local call (test of null if for some unit tests)
        if (isLocalCall(player) && this.protocolEngine != null) {
            this.protocolEngine.setShip(player, shipmodel, xy, vertical);
        } else System.out.println(player + " set his ship for " + players[0]);


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

    private boolean isLocalCall(String calledPlayerName) {
        return players[0].equals(calledPlayerName);
    }

    @Override
    public boolean setShip(String player, Shipmodel ship, Coordinate xy) throws BattleshipException, PhaseException, OceanException, ShipException {
        return setShip(player, ship, xy, false);
    }

    private void chooseOceanAndShips(String player) throws BattleshipException {
        chooseOcean(player);
        if (player.equals(players[0])) {
            this.ships = ships1;
        } else if (player.equals(players[1])) {
            this.ships = ships2;
        } else throw new BattleshipException(ExceptionMsg.bs_wrongPlayer);
    }

    private void chooseOcean(String player) throws BattleshipException {
        if (player.equals(players[0])) {
            this.ocean = ocean1;
        } else if (player.equals(players[1])) {
            this.ocean = ocean2;
        } else throw new BattleshipException(ExceptionMsg.bs_wrongPlayer);
    }

    private void updateOceanAndShips(ArrayList<Ship> ships, Ocean ocean, String player) throws BattleshipException {
        updateOcean(player);
        if (player.equals(players[0])) {
            this.ships1 = ships;
        } else if (player.equals(players[1])) {
            this.ships2 = ships;
        } else throw new BattleshipException(ExceptionMsg.bs_wrongPlayer);
    }

    private void updateOcean(String player) throws BattleshipException {
        if (player.equals(players[0])) {
            this.ocean1 = ocean;
        } else if (player.equals(players[1])) {
            this.ocean2 = ocean;
        } else throw new BattleshipException(ExceptionMsg.bs_wrongPlayer);
    }

    @Override
    public Result attack(String player, Coordinate position) throws PhaseException, BattleshipException, ShipException, OceanException {
        if (phase != Phase.PLAY && phase != Phase.WAITFORPLAY) throw new PhaseException(ExceptionMsg.ph_wrongPhase);
        if (!nameTaken(player)) throw new BattleshipException(ExceptionMsg.bs_wrongPlayer);
        if (phase == Phase.WAITFORPLAY && players[0].equals(player))
            throw new BattleshipException(ExceptionMsg.bs_wrongTurn1);
        if (phase == Phase.PLAY && players[1].equals(player)) throw new BattleshipException(ExceptionMsg.bs_wrongTurn2);

        chooseOcean(player);
        Result hitResult = checkWin(ocean.bombAt(position), player);
        updateOcean(player);

        if (hitResult == Result.WIN) setPhase(Phase.END);

        // tell other side - if local call (test of null if for some unit tests)
        if (isLocalCall(player) && this.protocolEngine != null) {
            this.protocolEngine.attack(player, position);
        } else System.out.println(players[0] + "got attacked by " + player);
        setNextPhase();
        return hitResult;
    }

    private Result checkWin(Result bomb, String p) {
        //TODO improve the player one player two logic!
        if (p.equals(players[0])) {
            if (bomb == Result.SINK) shipsDestroyed[0]++;
            if (shipsDestroyed[0] == numberOfShips) return Result.WIN;
            else
                return bomb;
        } else {
            if (bomb == Result.SINK) shipsDestroyed[1]++;
            if (shipsDestroyed[1] == numberOfShips) return Result.WIN;
            else
                return bomb;
        }
    }

    @Override
    public Phase getPhase() {
        return this.phase;
    }

    private void setNextPhase() {
        switch (this.phase) {
            case CHOOSE -> setPhase(Phase.SETSHIPS);
            case SETSHIPS -> setPhase(startsFirst ? Phase.PLAY : Phase.WAITFORPLAY);
            case WAITFORPLAY -> setPhase(Phase.PLAY);
            case PLAY -> setPhase(Phase.WAITFORPLAY);
            case END -> {
            }
        }
    }

    @Override
    public void sessionEstablished(boolean oracle, String partnerName) {
        players[1] = partnerName;
        startsFirst = oracle;

        System.out.println(players[0] + ": gameSessionEstablished with " + players[1] + " | " + startsFirst);

        setPhase(Phase.SETSHIPS);
    }

    public void setProtocolEngine(BattleshipProtocolEngine protocolEngine) {
        this.protocolEngine = protocolEngine;
        this.protocolEngine.subscribeGameSessionEstablishedListener(this);
    }
}
