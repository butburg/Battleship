package game;

import exceptions.BattleshipException;
import exceptions.OceanException;
import exceptions.PhaseException;
import exceptions.ShipException;
import field.Coordinate;
import network.ProtocolEngine;
import network.SessionEstablishedSubscriber;
import ship.Shipmodel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Edwin W (HTW) on Nov 2020
 */
public class BattleshipProtocolEngine implements Battleship, Runnable, ProtocolEngine {

    /**
     * Name of the Protocol-Object
     */
    private final String name;
    /**
     * Name of the other Protocol-Object
     */
    private String partnerName;
    /**
     * True or false, will distinguish the both Protocol-Engines
     */
    private boolean oracle;

    /**
     * the Stream the Protocolengine receives from
     */
    private InputStream is;
    /**
     * the Stream the Protocolengine sends to
     */
    private OutputStream os;
    /**
     * the Gameengine the Protocol belongs to
     */
    private final Battleship gameEngine;

    /**
     * Methods to Int for simple streaming
     */
    private final int METHOD_SET = 1;
    private final int METHOD_ATTACK = 2;
    private static final int RESULT_SET = 3;
    private static final int RESULT_ATTACK = 4;

    /**
     * thread of the ProtocolEngine self for reading
     */
    private Thread protocolThread;

    /**
     * thread to wait until it gets interrupted when a setShips result is received
     */
    private Thread setWaitThread;
    /**
     * temp result from a received setShips result
     */
    private boolean storedSetResult;

    /**
     * thread to wait until it gets interrupted when a attack result is received
     */
    private Thread attackWaitThread;
    /**
     * temp result from a received attack result
     */
    private Result storedAttackResult;

    /**
     * flag for read loop, false when sockets gets closed
     */
    private boolean hasRead = true;

    /**
     * List containing all instances, that want to be notified by events of this ProtocolEngine
     */
    private final List<SessionEstablishedSubscriber> sessionCreatedSubscibers = new ArrayList<>();


    /**
     * The ProtocolEngine expects a GameEngine it belongs to and a name for better understanding, which ProtocolEngine
     * currently activev is
     *
     * @param gameEngine the actual GameEngine that belongs to the ProtocolEngine
     * @param name       the name of the current ProtocolEngine-Object
     */
    public BattleshipProtocolEngine(Battleship gameEngine, String name) {
        this.name = name;
        this.gameEngine = gameEngine;
    }


    @Override
    public boolean setShip(String player, Shipmodel ship, Coordinate xy, boolean vertical) throws BattleshipException {
        DataOutputStream dos = new DataOutputStream(this.os);

        try {
            //set the Method-Id for the receiver
            dos.writeInt(METHOD_SET);
            //write the params of the method call
            dos.writeUTF(player);
            dos.writeUTF(String.valueOf(ship));
            dos.writeInt(xy.x);
            dos.writeInt(xy.y);
            dos.writeBoolean(vertical);

            try {
                //store the current Thread
                this.setWaitThread = Thread.currentThread();
                //set it to sleep and wait for interruption
                Thread.sleep(Long.MAX_VALUE);

            } catch (InterruptedException e) {
                //interrupted
                //System.out.println("ProtocolEngine(" + name + "): Received return value of setShip");
            }
            setWaitThread = null;
            //after the interrupt the return value of the method was stored, return that stored value now
            return storedSetResult;

        } catch (IOException e) {
            throw new BattleshipException("ProtocolEngine(" + name + "): Serialize error!");
        }
    }

    private void deserializeSetShip() throws ShipException, PhaseException, BattleshipException, OceanException {
        DataInputStream dis = new DataInputStream(this.is);

        try {
            //receive the params for the method call
            String player = dis.readUTF();
            Shipmodel ship = Shipmodel.valueOf(dis.readUTF());
            int x = dis.readInt();
            int y = dis.readInt();
            boolean vertical = dis.readBoolean();

            //apply the method call with its params and save the return value
            boolean isLastShip = this.gameEngine.setShip(player, ship, new Coordinate(x, y), vertical);

            // write the return value and send it back
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
            // get the return value and store it
            storedSetResult = dis.readBoolean();
            // interrupt the Thread waiting for the return value, it can now read the returned value
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
                //System.out.println("ProtocolEngine(" + name + "): Received return value of attack");
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


    public boolean read() throws BattleshipException, PhaseException, ShipException, OceanException {
        //System.out.println("ProtocolEngine(" + name + "): Read from input stream...");
        DataInputStream dis = new DataInputStream(this.is);
        try {
            // get first int value from stream
            int commandID = dis.readInt();
            // call method identified by int value
            switch (commandID) {
                case METHOD_SET -> this.deserializeSetShip();
                case METHOD_ATTACK -> this.deserializeAttack();
                case RESULT_SET -> this.deserializeResultSet();
                case RESULT_ATTACK -> this.deserializeResultAttack();
                default -> throw new BattleshipException("Deserialize: unknown Method ID:" + commandID);
            }
            return true;
        } catch (IOException e) {
            System.err.println("IOException caught - most probably connection close - stop thread / stop engine");
            try {
                this.close();
            } catch (IOException ioException) {
                System.out.println("ignore?");
            }
            return false;
        }
    }


    @Override
    public void run() {
        //System.out.println("ProtocolEngine(" + name + "): started...flip coin!" + this);

        // get "random" value
        long seed = this.hashCode() * System.currentTimeMillis();
        Random random = new Random(seed);

        int localInt, remoteInt;
        try {
            DataOutputStream dos = new DataOutputStream(this.os);
            DataInputStream dis = new DataInputStream(this.is);
            // as long as the remote and the local random value are the same, change the random value
            do {
                localInt = random.nextInt();
                //System.out.println("ProtocolEngine(" + name + "): flip and take number " + localInt);
                dos.writeInt(localInt);
                remoteInt = dis.readInt();
            } while (localInt == remoteInt);

            // the one instance with the lower random value becomes the only and one oracle
            this.oracle = localInt < remoteInt;
            //System.out.println("ProtocolEngine(" + name + "): Flipped a coin and got an oracle == " + oracle);

            // finally - exchange names with other ProtocolEngine
            dos.writeUTF(this.name);
            this.partnerName = dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // let all listeners know, that the connection is established now
        notifySessionEstablished(oracle, partnerName);

        try {
            // everytime we can read st. from the stream, we run the read method
            while (hasRead) {
                //stop, when stream is closed
                hasRead = this.read();
            }
        } catch (BattleshipException | PhaseException | ShipException | OceanException e) {
            System.err.println("ProtocolEngine(" + name + "): Exception called in protocol engine thread");
            e.printStackTrace();
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                         oracle creation listener                                      //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////


    // add a Listener
    public void addGameSessionEstablishedSubscriber(SessionEstablishedSubscriber ocListener) {
        this.sessionCreatedSubscibers.add(ocListener);
    }

    public void removeGameSessionEstablishedSubscriber(SessionEstablishedSubscriber ocListener) {
        this.sessionCreatedSubscibers.remove(ocListener);
    }

    private void notifySessionEstablished(boolean oracle, String partnerName) {
        // call all listener
        if (!this.sessionCreatedSubscibers.isEmpty()) {
            for (SessionEstablishedSubscriber oclistener : this.sessionCreatedSubscibers) {
                //not clear why own Thread?
                new Thread(() -> {
                    try {
                        Thread.sleep(1); // block a moment to let read thread start - just in case
                    } catch (InterruptedException e) { e.getStackTrace(); }
                    oclistener.sessionEstablished(oracle, partnerName);
                }).start();
            }
        }
    }

    @Override
    public void handleConnectionStream(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;

        protocolThread = new Thread(this);
        protocolThread.start();
    }

    @Override
    public void close() throws IOException {
        if (os != null) os.close();
        if (is != null) is.close();
    }
}
