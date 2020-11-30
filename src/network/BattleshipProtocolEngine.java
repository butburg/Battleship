package network;

import exceptions.BattleshipException;
import exceptions.PhaseException;
import field.Coordinate;
import game.Battleship;
import game.Phase;
import game.Result;
import ship.Shipmodel;

import java.io.*;

/**
 * @author Edwin W (HTW) on Nov 2020
 */
public class BattleshipProtocolEngine implements Battleship {

    private final InputStream is;
    private final OutputStream os;
    private final Battleship gameEngine;
    private final int METHOD_CHOOSE = 0;
    private final int METHOD_SET_V = 1;
    private final int METHOD_SET = 2;
    private final int METHOD_ATTACK = 3;


    public BattleshipProtocolEngine(InputStream is, OutputStream os, Battleship gameEngine) {
        this.is = is;
        this.os = os;
        this.gameEngine = gameEngine;
    }

    @Override
    public void choosePlayerName(String playerName) throws BattleshipException {
        DataOutputStream dos = new DataOutputStream(this.os);

        try {
            dos.writeInt(0);
            dos.writeUTF(playerName);
        } catch (IOException e) {
            throw new BattleshipException("Serialize error!");
        }

    }

    private void deserializeChoosePlayerName() throws BattleshipException, PhaseException {
        DataInputStream dis = new DataInputStream(this.is);
        try {
            String playerName = dis.readUTF();
            this.gameEngine.choosePlayerName(playerName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean setShip(String player, Shipmodel ship, Coordinate xy, boolean vertical)  {
        return false;
    }

    @Override
    public boolean setShip(String player, Shipmodel ship, Coordinate xy)  {
        return false;
    }

    @Override
    public Result attack(String player, Coordinate position){
        return null;
    }

    @Override
    public Phase getPhase() {
        return null;
    }

    @Override
    public String[] getPlayers() {
        return new String[0];
    }

    public void read() throws BattleshipException, PhaseException {
        DataInputStream dis = new DataInputStream(this.is);
        try {

            int commandID = dis.readInt();
            switch (commandID) {
                case METHOD_CHOOSE -> this.deserializeChoosePlayerName();
                case METHOD_SET -> this.deserializeSetShip();
                case METHOD_SET_V -> this.deserializeSetShipV();
                case METHOD_ATTACK -> this.deserializeAttack();
                default -> throw new BattleshipException("Deserialize: unknown Method ID:" + commandID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deserializeAttack() {

    }

    private void deserializeSetShipV() {

    }

    private void deserializeSetShip() {
    }
}
