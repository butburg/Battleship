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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Edwin W (HTW) on Nov 2020
 */
public class BattleshipProtocolEngine implements Battleship, Runnable, ProtocolEngine {

    private final String name;
    private String partnerName;
    private boolean oracle;

    private InputStream is;
    private OutputStream os;
    private final Battleship gameEngine;

    private final int METHOD_CHOOSE = 0;
    private final int METHOD_SET = 1;
    private final int METHOD_ATTACK = 2;
    private static final int RESULT_SET = 3;
    private static final int RESULT_ATTACK = 4;

    private Thread protocolThread;
    private boolean storedSetResult;
    private Thread setWaitThread;
    private Thread attackWaitThread;
    private Result storedAttackResult;


    public BattleshipProtocolEngine(Battleship gameEngine, String name) {
        this.name = name;
        this.gameEngine = gameEngine;
    }

    @Override
    public void choosePlayerName(String playerName) throws BattleshipException {
        DataOutputStream dos = new DataOutputStream(this.os);

        try {
            dos.writeInt(METHOD_CHOOSE);
            dos.writeUTF(playerName);

        } catch (IOException e) {
            throw new BattleshipException("ProtocolEngine(" + name + "): Serialize error!");
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
            dos.writeInt(METHOD_SET);
            dos.writeUTF(player);
            dos.writeUTF(String.valueOf(ship));
            dos.writeInt(xy.x);
            dos.writeInt(xy.y);
            dos.writeBoolean(vertical);

            try {
                this.setWaitThread = Thread.currentThread();
                Thread.sleep(Long.MAX_VALUE);

            } catch (InterruptedException e) {
                //interrupted
                System.out.println("ProtocolEngine(" + name + "): Received return value of setShip");
            }
            setWaitThread = null;
            return storedSetResult;

        } catch (IOException e) {
            throw new BattleshipException("ProtocolEngine(" + name + "): Serialize error!");
        }
    }

    private void deserializeSetShip() throws ShipException, PhaseException, BattleshipException, OceanException {
        DataInputStream dis = new DataInputStream(this.is);

        try {
            String player = dis.readUTF();
            Shipmodel ship = Shipmodel.valueOf(dis.readUTF());
            int x = dis.readInt();
            int y = dis.readInt();
            boolean vertical = dis.readBoolean();

            boolean isLastShip = this.gameEngine.setShip(player, ship, new Coordinate(x, y), vertical);

            // write Result
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeInt(RESULT_SET);
            dos.writeBoolean(isLastShip);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean setShip(String player, Shipmodel ship, Coordinate xy) throws BattleshipException {
        return setShip(player, ship, xy, false);
    }

    private void deserializeResultSet() {
        DataInputStream dis = new DataInputStream(this.is);

        try {
            storedSetResult = dis.readBoolean();
            setWaitThread.interrupt();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Result attack(String player, Coordinate position) throws BattleshipException {
        DataOutputStream dos = new DataOutputStream(this.os);

        try {
            dos.writeInt(METHOD_ATTACK);
            dos.writeUTF(player);
            dos.writeInt(position.x);
            dos.writeInt(position.y);

            try {
                this.attackWaitThread = Thread.currentThread();
                Thread.sleep(Long.MAX_VALUE);

            } catch (InterruptedException e) {
                //interrupted
                System.out.println("ProtocolEngine(" + name + "): Received return value of attack");
            }
            attackWaitThread = null;
            return storedAttackResult;

        } catch (IOException e) {
            throw new BattleshipException("ProtocolEngine(" + name + "): Serialize error!");
        }
    }

    private void deserializeAttack() throws ShipException, PhaseException, BattleshipException, OceanException {
        DataInputStream dis = new DataInputStream(this.is);

        try {
            String player = dis.readUTF();
            int x = dis.readInt();
            int y = dis.readInt();

            Result result = this.gameEngine.attack(player, new Coordinate(x, y));

            // write Result
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeInt(RESULT_ATTACK);
            dos.writeUTF(String.valueOf(result));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void deserializeResultAttack() {
        DataInputStream dis = new DataInputStream(this.is);

        try {
            storedAttackResult = Result.valueOf(dis.readUTF());
            attackWaitThread.interrupt();

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
        System.out.println("ProtocolEngine(" + name + "): Read from input stream...");
        DataInputStream dis = new DataInputStream(this.is);
        try {

            int commandID = dis.readInt();
            switch (commandID) {
                case METHOD_CHOOSE -> {
                    System.out.println("ProtocolEngine(" + name + "): read METHOD_CHOOSE");
                    this.deserializeChoosePlayerName();
                }
                case METHOD_SET -> {
                    System.out.println("ProtocolEngine(" + name + "): read METHOD_SET");
                    this.deserializeSetShip();
                }
                case METHOD_ATTACK -> {
                    System.out.println("ProtocolEngine(" + name + "): read METHOD_ATTACK");
                    this.deserializeAttack();
                }
                case RESULT_SET -> {
                    System.out.println("ProtocolEngine(" + name + "): read RESULT_SET");
                    this.deserializeResultSet();
                }
                case RESULT_ATTACK -> {
                    System.out.println("ProtocolEngine(" + name + "): read RESULT_ATTACK");
                    this.deserializeResultAttack();
                }
                default -> throw new BattleshipException("Deserialize: unknown Method ID:" + commandID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        System.out.println("ProtocolEngine(" + name + "): started...flip coin!" + this);

        long seed = this.hashCode() * System.currentTimeMillis();
        Random random = new Random(seed);

        int localInt, remoteInt;
        try {
            DataOutputStream dos = new DataOutputStream(this.os);
            DataInputStream dis = new DataInputStream(this.is);
            do {
                localInt = random.nextInt();
                System.out.println("ProtocolEngine(" + name + "): flip and take number " + localInt);
                dos.writeInt(localInt);
                remoteInt = dis.readInt();
            } while (localInt == remoteInt);

            this.oracle = localInt < remoteInt;
            System.out.println("ProtocolEngine(" + name + "): Flipped a coin and got an oracle == " + oracle);
            //this.oracleSet = true;

            // finally - exchange names
            dos.writeUTF(this.name);
            this.partnerName = dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        notifySessionEstablished(oracle, partnerName);


        try {
            while (true) {
                this.read();
            }
        } catch (BattleshipException | PhaseException | ShipException | OceanException e) {
            System.err.println("ProtocolEngine(" + name + "): Exception called in protocol engine thread");
            e.printStackTrace();
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                         oracle creation listener                                      //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    private List<SessionEstablishedListener> sessionCreatedListenerList = new ArrayList<>();

    public void subscribeGameSessionEstablishedListener(SessionEstablishedListener ocListener) {
        this.sessionCreatedListenerList.add(ocListener);
    }

    public void unsubscribeGameSessionEstablishedListener(SessionEstablishedListener ocListener) {
        this.sessionCreatedListenerList.remove(ocListener);
    }

    private void notifySessionEstablished(boolean oracle, String partnerName) {
        // call listener
        if (this.sessionCreatedListenerList != null && !this.sessionCreatedListenerList.isEmpty()) {
            for (SessionEstablishedListener oclistener : this.sessionCreatedListenerList) {
                new Thread(() -> {
                    try {
                        Thread.sleep(1); // block a moment to let read thread start - just in case
                    } catch (InterruptedException e) {
                        // will not happen
                    }
                    oclistener.sessionEstablished(oracle, partnerName);
                }).start();
            }
        }
    }


    @Override
    public void handleConnection(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;

        this.protocolThread = new Thread(this);
        this.protocolThread.start();
    }

    @Override
    public void close() throws IOException {
        //if (os != null) os.close();
        //if (is != null) is.close();
    }
}
