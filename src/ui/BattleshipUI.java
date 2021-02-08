package ui;

import exceptions.BattleshipException;
import exceptions.OceanException;
import exceptions.PhaseException;
import exceptions.ShipException;
import field.Coordinate;
import game.BattleshipImpl;
import game.BattleshipProtocolEngine;
import game.LocalBSChangedSubscriber;
import game.LocalBattleship;
import network.SessionEstablishedSubscriber;
import ship.Shipmodel;
import tcp_helper.TCPStream;
import tcp_helper.TCPStreamCreatedListener;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * @author Edwin W (HTW) on Dez 2020
 */


public class BattleshipUI implements LocalBSChangedSubscriber, SessionEstablishedSubscriber, TCPStreamCreatedListener {
    private static final int PORT = 55555;
    private final String playerName;
    private String remoteName;

    private final PrintStream outStream;
    private final BufferedReader inBufferedReader;

    private final BattleshipImpl gameEngine;
    private final LocalBattleship localGame;

    private TCPStream tcpStream;
    private BattleshipProtocolEngine protocolEngine;

    private static final String PRINTOCEAN = "print";
    private static final String EXIT = "exit";
    private static final String CONNECT2PORT = "connect";
    private static final String OPENPORT = "open";
    private static final String SETSHIP = "place";
    private static final String ATTACK = "att";


    public BattleshipUI(String playerName, PrintStream os, InputStream in) {
        this.playerName = playerName;
        this.outStream = os;
        this.inBufferedReader = new BufferedReader(new InputStreamReader(in));

        this.gameEngine = new BattleshipImpl(playerName);
        this.localGame = this.gameEngine;
        this.localGame.addLocalBSChangedSubscriber(this);
    }

    public void printShips() {
        for (Shipmodel sm : Shipmodel.values()) {
            System.out.println(
                    (gameEngine.countShipsLocal().get(sm) == null ? "0" : gameEngine.countShipsLocal().get(sm))
                            + "x " + sm.toString()
                            + ", Size: "
                            + sm.returnSize());
        }
    }

    public void printUsage() {
        StringBuilder b = new StringBuilder();
        b.append("\n");
        b.append("Valid commands you can type:");
        b.append("\n");
        b.append(CONNECT2PORT + " [HOSTNAME]");
        b.append(" => connect as tcp client to another open port");
        b.append("\n");
        b.append(OPENPORT);
        b.append(" => open port and become tcp server");
        b.append("\n");
        b.append(PRINTOCEAN);
        b.append(" => print the ocean / game field");
        b.append("\n");
        b.append(SETSHIP + " [SHIPMODEL] x y v(optional)");
        b.append("\n");
        b.append(" => place a type of ship at position x y, add v to place it vertical");
        b.append("\n");
        b.append("(without v it will be placed horizontal)");
        b.append("\n");
        b.append(ATTACK + " x y");
        b.append(" => attack the enemy at the position x y");
        b.append("\n");
        b.append(EXIT);
        b.append(" => exit the game");
        b.append("\n\n");
        b.append("Examples:");
        b.append("\n");
        b.append(CONNECT2PORT + " 192.168.1.1");
        b.append("\n");
        b.append(SETSHIP + " battleship 2 4 v");
        b.append("\n");
        b.append(ATTACK + " 2 5");
        b.append("\n");

        this.outStream.println(b.toString());
    }

    public void runLoop() {
        boolean again = true;

        while (again) {
            String userInput;

            try {
                // read user input
                userInput = inBufferedReader.readLine();

                // finish that loop if less than nothing came in
                if (userInput == null) break;

                // trim whitespaces on both sides and make to lower case
                userInput = userInput.trim().toLowerCase();

                // extract command
                int spaceIndex = userInput.indexOf(' ');
                spaceIndex = spaceIndex != -1 ? spaceIndex : userInput.length();

                // got command part from user input
                String commandPart = userInput.substring(0, spaceIndex);

                // extract parameters part - can be empty
                String parameterPart = userInput.substring(spaceIndex);
                parameterPart = parameterPart.trim();

                // start command loop
                // redraw
                switch (commandPart) {
                    case PRINTOCEAN:
                        this.doPrint(false);
                        this.doPrint(true);
                        break;
                    case CONNECT2PORT:
                        this.doConnect2Port(parameterPart);
                        break;
                    case OPENPORT:
                        this.doOpenPort();
                        break;
                    case SETSHIP:
                        this.doSetShip(parameterPart);
                        break;
                    case ATTACK:
                        this.doAttack(parameterPart);
                        this.doPrint(true);
                        break;
                    case "default":
                        this.doSetShip("submarine 0 0");
                        this.doSetShip("submarine 0 2");
                        this.doSetShip("destroyer 0 4");
                        this.doSetShip("destroyer 0 6");
                        this.doSetShip("cruiser 0 8");
                        this.doSetShip("battleship 3 0");
                        this.doSetShip("carrier 3 2");
                        break;
                    case "q":
                    case EXIT:
                        again = false;
                        this.doExit();
                        break;
                    default:
                        this.outStream.println("Unknown command: " + userInput);
                        this.printUsage();
                        break;
                }
            } catch (IOException ex) {
                this.outStream.println("Cannot read from input stream - fatal. Exit.");
                try {
                    this.doExit();
                } catch (IOException e) {
                    // ignore
                }
            } catch (BattleshipException ex) {
                this.outStream.println("Game exception: " + ex.getLocalizedMessage());
            } catch (PhaseException ex) {
                this.outStream.println("Wrong phase: " + ex.getLocalizedMessage());
            } catch (OceanException ex) {
                this.outStream.println("Ocean problems: " + ex.getLocalizedMessage());
            } catch (ShipException ex) {
                this.outStream.println("Ship problems: " + ex.getLocalizedMessage());
            } catch (RuntimeException ex) {
                this.outStream.println("Runtime problems: " + ex.getLocalizedMessage());
            }
        }
    }

    private void doExit() throws IOException {
        protocolEngine.close();
    }

    private void doSetShip(String userParameterPart) throws BattleshipException, PhaseException, OceanException, ShipException {
        isConnectedGuard();
        boolean testVertical;
        StringTokenizer st = new StringTokenizer(userParameterPart);
        if (st.countTokens() == 3) {
            testVertical = false;
        } else if (st.countTokens() == 4) {
            testVertical = true;
        } else throw new IllegalStateException("Need 3 or 4 Parameter but was: " + st.countTokens());

        String ship = st.nextToken().toUpperCase();
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        boolean vertical = false;
        if (testVertical) {
            String v = st.nextToken();
            if (v.equals("v")) vertical = true;
        }
        // not MVP:
        if (gameEngine.setShip(Shipmodel.valueOf(ship), new Coordinate(x, y), vertical)) {
            printShips();
            System.out.println("Please set the next ship!");
            this.doPrint(false);
        } else { System.out.println("This was your last ship!"); }


    }

    private void doAttack(String userParameterPart) throws BattleshipException, PhaseException, OceanException, ShipException {
        isConnectedGuard();

        StringTokenizer st = new StringTokenizer(userParameterPart);
        if (st.countTokens() != 2) {
            throw new IllegalStateException("Need 2 Parameter but was: " + st.countTokens());
        }

        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        // not MVP:
        System.out.println("Your attack result: " + gameEngine.attack(new Coordinate(x, y)));
    }

    private void isConnectedGuard() throws BattleshipException {
        if (this.protocolEngine == null)
            throw new BattleshipException("Not connected yet, connect to a Port or open a Port!");
    }

    private void doOpenPort() {
        if (notConnected()) {
            tcpStream = new TCPStream(PORT, true, playerName);
            tcpStream.setStreamCreationListener(this);
            tcpStream.start();
        }
    }

    private void doConnect2Port(String userParamterPart) {
        if (notConnected()) {
            String hostname;
            try {
                StringTokenizer st = new StringTokenizer(userParamterPart);
                hostname = st.nextToken();
            } catch (NoSuchElementException e) {
                System.out.println("No hostname provided. Will use localhost.");
                hostname = "localhost";
            }
            tcpStream = new TCPStream(PORT, false, playerName);
            tcpStream.setRemoteEngine(hostname);
            tcpStream.setStreamCreationListener(this);
            tcpStream.start();
        }
    }

    @Override
    public void changed() {
        try {
            this.doPrint(false);
            System.out.println("Phase: " + localGame.getPhase());
        } catch (BattleshipException | ShipException e) {
            System.err.println("very very unexpected: " + e.getLocalizedMessage());
        }
    }

    private void doPrint(boolean attack) throws BattleshipException, ShipException {

        //System.out.println(playerName + " play against " + remoteName);
        if (attack) {
            gameEngine.getPrintStreamView().printAttack(System.out);
        } else {
            gameEngine.getPrintStreamView().printOcean(System.out);
        }
    }


    @Override
    public void sessionEstablished(boolean oracle, String remoteName) {
        //System.out.println("Game session created");
        this.remoteName = remoteName;
        if (oracle) {
            System.out.println("You will attack first!");
        } else {
            System.out.println("You will attack second!");
        }
        printShips();
        System.out.println();
        System.out.println("Please set your ships now!");
    }

    @Override
    public void streamCreated(TCPStream stream) {
        // connection established - setup protocol engine
        //System.out.println("Stream created - setup engine now - we can play quite soon...");
        this.protocolEngine = new BattleshipProtocolEngine(this.gameEngine, this.playerName);
        this.gameEngine.setProtocolEngine(protocolEngine);

        this.protocolEngine.addGameSessionEstablishedSubscriber(this);

        try {
            protocolEngine.handleConnectionStream(stream.getInputStream(), stream.getOutputStream());
        } catch (IOException e) {
            System.err.println("Cannot get streams from tcpStream - fatal, will exit: " + e.getLocalizedMessage());
            System.exit(1);
        }
    }

    private boolean notConnected() {
        if (tcpStream != null) {
            System.err.println("Connection established already or connection attempt in progress!");
            return false;
        } else return true;
    }
}
