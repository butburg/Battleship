package network;

import exceptions.BattleshipException;
import exceptions.OceanException;
import exceptions.PhaseException;
import exceptions.ShipException;
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
    public boolean setShip(String player, Shipmodel ship, Coordinate xy, boolean vertical) throws BattleshipException {
        DataOutputStream dos = new DataOutputStream(this.os);

        try {
            dos.writeInt(1);

            dos.writeUTF(player);
            dos.writeUTF(String.valueOf(ship));
            dos.writeInt(xy.x);
            dos.writeInt(xy.y);
            dos.writeBoolean(vertical);
        } catch (IOException e) {
            throw new BattleshipException("Serialize error!");
        }
        return false;
    }

    private void deserializeSetShipV() throws ShipException, PhaseException, BattleshipException, OceanException {
        DataInputStream dis = new DataInputStream(this.is);

        try {
            Coordinate xy = new Coordinate(-1, -1);
            String player = dis.readUTF();
            Shipmodel ship = Shipmodel.valueOf(dis.readUTF());
            xy.x = dis.readInt();
            xy.y = dis.readInt();
            boolean vertical = dis.readBoolean();
            this.gameEngine.setShip(player, ship, xy, vertical);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean setShip(String player, Shipmodel ship, Coordinate xy) throws BattleshipException {
        DataOutputStream dos = new DataOutputStream(this.os);

        try {
            dos.writeInt(2);

            dos.writeUTF(player);
            dos.writeUTF(String.valueOf(ship));
            dos.writeInt(xy.x);
            dos.writeInt(xy.y);
        } catch (IOException e) {
            throw new BattleshipException("Serialize error!");
        }

        return false;
    }

    private void deserializeSetShip() throws ShipException, PhaseException, BattleshipException, OceanException {
        DataInputStream dis = new DataInputStream(this.is);

        try {
            Coordinate xy = new Coordinate(-1, -1);
            String player = dis.readUTF();
            Shipmodel ship = Shipmodel.valueOf(dis.readUTF());
            xy.x = dis.readInt();
            xy.y = dis.readInt();
            this.gameEngine.setShip(player, ship, xy);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Result attack(String player, Coordinate position) throws BattleshipException {
        DataOutputStream dos = new DataOutputStream(this.os);

        try {
            dos.writeInt(3);
            dos.writeUTF(player);
            dos.writeInt(position.x);
            dos.writeInt(position.y);
        } catch (IOException e) {
            throw new BattleshipException("Serialize error!");
        }

        return null;
    }

    private void deserializeAttack() throws ShipException, PhaseException, BattleshipException, OceanException {
        DataInputStream dis = new DataInputStream(this.is);

        try {
            Coordinate xy = new Coordinate(-1,-1);
            String player = dis.readUTF();
            xy.x = dis.readInt();
            xy.y = dis.readInt();
            this.gameEngine.attack(player, xy);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Phase getPhase() {
        return null;
    }

    @Override
    public String[] getPlayers() {
        return new String[0];
    }

    public void read() throws BattleshipException, PhaseException, ShipException, OceanException {
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


}
