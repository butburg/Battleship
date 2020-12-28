package game;

import exceptions.*;
import field.Coordinate;
import field.Ocean;
import field.OceanImpl;
import network.SessionEstablishedSubscriber;
import ship.Ship;
import ship.ShipImpl;
import ship.Shipmodel;
import view.BattleshipPrintStreamView;
import view.PrintStreamView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Edwin W (HTW) on Nov 2020
 * This is an implementation for the battleship game.
 */
public class BattleshipImpl implements Battleship, LocalBattleship, SessionEstablishedSubscriber {

    private Phase phase = Phase.SETSHIPS;
    private String playerLocal;
    private String playerRemote;

    private BattleshipProtocolEngine protocolEngine;
    private boolean startsFirst = false;

    private Ocean ocean;
    private Ocean oceanLocal = new OceanImpl(11);
    private Ocean oceanRemote = new OceanImpl(11);

    private ArrayList<Ship> ships;
    private ArrayList<Ship> shipsLocal = new ArrayList<>();
    private ArrayList<Ship> shipsRemote = new ArrayList<>();
    private int[] shipsDestroyed = new int[2];
    private int numberOfShips;

    private final List<LocalBSChangedSubscriber> localBSChangedSubscribers = new ArrayList<>();


    public BattleshipImpl(String localPLayer) {
        createAllShips();
        this.playerLocal = localPLayer;

        //FOR TESTS DIRTY
        this.playerRemote = "Pia";
    }

    /**
     * This Method creates the ships that can be set in the game. Will create the ships for both player.
     * Here you can change the number of every shipmodel!
     * (Default: 1 Battleship, 2 Cruiser, 3 Destroyers, 4 Submarines)
     */
    private void createAllShips() {
        createShip(Shipmodel.BATTLESHIP, 1);
        createShip(Shipmodel.CRUISER, 2);
        createShip(Shipmodel.DESTROYER, 0);
        createShip(Shipmodel.SUBMARINE, 0);
    }

    private void createShip(Shipmodel ship, int count) {
        for (; count > 0; count--) {
            shipsLocal.add(new ShipImpl(ship));
            shipsRemote.add(new ShipImpl(ship));
            numberOfShips++;
        }
    }

    /**
     * returns an Array with the names of the player
     *
     * @return the names of the player
     */
    public String[] getPlayers() {
        return new String[]{playerLocal, playerRemote};
    }

    private boolean nameTaken(String playerName) {
        return (playerName.equals(playerLocal) || playerName.equals(playerRemote));
    }

    public void setPhase(Phase newPhase) {
        this.phase = newPhase;
    }


    @Override
    public boolean setShip(String player, Shipmodel shipmodel, Coordinate xy, boolean vertical) throws BattleshipException, PhaseException, OceanException, ShipException {
        if (phase != Phase.SETSHIPS) throw new PhaseException(ExceptionMsg.ph_wrongPhase);

        //get the ships and ocean from the actual player
        chooseOceanAndShips(player);

        if (ships.isEmpty()) throw new BattleshipException(ExceptionMsg.bs_shipAllSet);

        Ship shipToSet = null;
        for (Ship ship : ships) {
            if (ship.getModel() == shipmodel) {
                shipToSet = ship;
                break;
            }
        }
        if (shipToSet == null) throw new BattleshipException(ExceptionMsg.bs_shipTypeAllSet);

        ocean.placeShip(shipToSet, xy.x, xy.y, vertical);

        ships.remove(shipToSet);

        //update the list and the ocean for the actual player
        updateOceanAndShips(player);

        // tell other side - if local call (test of null if for some unit tests)
        if (isLocalCall(player) && this.protocolEngine != null) {
            this.protocolEngine.setShip(player, shipmodel, xy, vertical);
        } else System.out.println(player + " has set his ship.");


        if (ships.isEmpty()) {
            if (shipsLocal.isEmpty() && shipsRemote.isEmpty()) {
                setNextPhase();
            }
            //if there are more ships to set for the actual player
            return false;
        } else
            //if it was the last placed ship for the actual player
            return true;
    }

    private boolean isLocalCall(String calledPlayerName) {
        return playerLocal.equals(calledPlayerName);
    }

    @Override
    public boolean setShip(String player, Shipmodel ship, Coordinate xy) throws BattleshipException, PhaseException, OceanException, ShipException {
        return setShip(player, ship, xy, false);
    }

    private void chooseOceanAndShips(String player) throws BattleshipException {
        chooseOcean(player);
        if (player.equals(playerLocal)) {
            this.ships = shipsLocal;
        } else if (player.equals(playerRemote)) {
            this.ships = shipsRemote;
        } else throw new BattleshipException(ExceptionMsg.bs_wrongPlayer);
    }

    private void chooseOcean(String player) throws BattleshipException {
        if (player.equals(playerLocal)) {
            this.ocean = oceanLocal;
        } else if (player.equals(playerRemote)) {
            this.ocean = oceanRemote;
        } else throw new BattleshipException(ExceptionMsg.bs_wrongPlayer);
    }

    private void updateOceanAndShips(String player) throws BattleshipException {
        updateOcean(player);
        if (player.equals(playerLocal)) {
            this.shipsLocal = ships;
        } else if (player.equals(playerRemote)) {
            this.shipsRemote = ships;
        } else throw new BattleshipException(ExceptionMsg.bs_wrongPlayer);
        ships = null;
    }

    private void updateOcean(String player) throws BattleshipException {
        if (player.equals(playerLocal)) {
            this.oceanLocal = ocean;
        } else if (player.equals(playerRemote)) {
            this.oceanRemote = ocean;
        } else throw new BattleshipException(ExceptionMsg.bs_wrongPlayer);
        ocean = null;
    }

    @Override
    public Result attack(String player, Coordinate position) throws PhaseException, BattleshipException, ShipException, OceanException {
        if (phase != Phase.PLAY && phase != Phase.WAITFORPLAY) throw new PhaseException(ExceptionMsg.ph_wrongPhase);
        if (!nameTaken(player)) throw new BattleshipException(ExceptionMsg.bs_wrongPlayer);
        if (phase == Phase.WAITFORPLAY && playerLocal.equals(player))
            throw new BattleshipException(ExceptionMsg.bs_wrongTurn1);
        if (phase == Phase.PLAY && playerRemote.equals(player))
            throw new BattleshipException(ExceptionMsg.bs_wrongTurn2);

        chooseOcean(player);
        Result hitResult = checkWin(ocean.bombAt(position), player);
        updateOcean(player);

        if (hitResult == Result.WIN) setPhase(Phase.END);

        // tell other side - if local call (test of null if for some unit tests)
        if (isLocalCall(player) && this.protocolEngine != null) {
            this.protocolEngine.attack(player, position);
        } else {
            System.out.println("You got attacked by " + player + "!");
            this.notifyLocalBSChanged();
        }
        setNextPhase();
        return hitResult;
    }

    private Result checkWin(Result r, String player) {
        //TODO improve the player one player two logic!
        if (player.equals(playerLocal)) {
            if (r == Result.SINK) shipsDestroyed[0]++;
            if (shipsDestroyed[0] == numberOfShips) return Result.WIN;
            else
                return r;
        } else {
            if (r == Result.SINK) shipsDestroyed[1]++;
            if (shipsDestroyed[1] == numberOfShips) return Result.WIN;
            else
                return r;
        }
    }

    @Override
    public boolean setShip(Shipmodel ship, Coordinate xy, boolean vertical) throws BattleshipException, PhaseException, OceanException, ShipException {
        return setShip(playerLocal, ship, xy, vertical);
    }

    @Override
    public boolean setShip(Shipmodel ship, Coordinate xy) throws BattleshipException, PhaseException, OceanException, ShipException {
        return setShip(playerLocal, ship, xy);
    }

    @Override
    public Result attack(Coordinate position) throws BattleshipException, PhaseException, ShipException, OceanException {
        return attack(playerLocal, position);
    }

    @Override
    public Phase getPhase() {
        return this.phase;
    }

    @Override
    public String getLocalPlayerName() {
        return playerLocal;
    }

    private void setNextPhase() {
        switch (this.phase) {
            case SETSHIPS -> setPhase(startsFirst ? Phase.PLAY : Phase.WAITFORPLAY);
            case WAITFORPLAY -> setPhase(Phase.PLAY);
            case PLAY -> setPhase(Phase.WAITFORPLAY);
            case END -> {
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                          battleship changed listener                                  //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    // add a Listener
    @Override
    public void addLocalBSChangedSubscriber(LocalBSChangedSubscriber changeListener) {
        this.localBSChangedSubscribers.add(changeListener);
    }

    @Override
    public void removeLocalBSChangedSubscriber(LocalBSChangedSubscriber changeListener) {
        this.localBSChangedSubscribers.remove(changeListener);
    }

    private void notifyLocalBSChanged() {
        // call all listener
        if (!this.localBSChangedSubscribers.isEmpty()) {
            //not clear why own Thread?
            new Thread(() -> {
                for (LocalBSChangedSubscriber listener : this.localBSChangedSubscribers) {
                    listener.changed();
                }
            }).start();
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                            listener                                                 //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void sessionEstablished(boolean oracle, String partnerName) {
        playerRemote = partnerName;
        startsFirst = oracle;

        System.out.println(playerLocal + ": gameSessionEstablished with " + playerRemote + " | " + startsFirst);

        setPhase(Phase.SETSHIPS);
    }

    public void setProtocolEngine(BattleshipProtocolEngine protocolEngine) {
        this.protocolEngine = protocolEngine;
        this.protocolEngine.addGameSessionEstablishedSubscriber(this);
    }

    public PrintStreamView getPrintStreamView() {
        return new BattleshipPrintStreamView(oceanLocal);
    }
}
