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
    private static final String SETSHIP = "set";
    private static final String ATTACK = "attack";


    public BattleshipUI(String playerName, PrintStream os, InputStream in) {
        this.playerName = playerName;
        this.outStream = os;
        this.inBufferedReader = new BufferedReader(new InputStreamReader(in));

        this.gameEngine = new BattleshipImpl(playerName);
        this.localGame = this.gameEngine;
        this.localGame.addLocalBSChangedSubscriber(this);
    }

    public void printUsage() {
        StringBuilder b = new StringBuilder();
        b.append("\n");
        b.append("Valid commands:");
        b.append("\n");
        b.append(CONNECT2PORT + " hostname(optional)");
        b.append(".. connect as tcp client to another open port");
        b.append("\n");
        b.append(OPENPORT);
        b.append(".. open port and become tcp server");
        b.append("\n");
        b.append(PRINTOCEAN);
        b.append(".. print the ocean");
        b.append("\n");
        b.append(SETSHIP + " shiptype x y v(optional)");
        b.append(".. set a ship:");
        b.append("\n");
        for (int i = 0; i < Shipmodel.values().length; i++) {
            b.append(Shipmodel.values()[i].toString().toLowerCase());
            b.append(" ");
        }
        b.append(")\n");
        b.append("(Horizontal is default)");
        b.append("\n");
        b.append(ATTACK + " x y");
        b.append(".. attack a field");
        b.append("\n");
        b.append(EXIT);
        b.append(".. exit the game");
        b.append("\n\n");
        b.append("Example:");
        b.append("\n");
        b.append("set battleship 2 4 v");
        b.append("\n");
        b.append("attack 2 5");
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
                    case PRINTOCEAN -> this.doPrintOcean(false);
                    case CONNECT2PORT -> this.doConnect2Port(parameterPart);
                    case OPENPORT -> this.doOpenPort();
                    case SETSHIP -> {
                        this.doSetShip(parameterPart);
                        this.doPrintOcean(true);
                    }
                    case ATTACK -> {
                        this.doAttack(parameterPart);
                        this.doPrintOcean(true);
                    }
                    case "q", EXIT -> {
                        // end loop
                        again = false;
                        this.doExit();
                    }
                    default -> {
                        this.outStream.println("Unknown command: " + userInput);
                        this.printUsage();
                    }
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
            System.out.println("Please set the next ship!");
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
            this.doPrintOcean(true);
        } catch (IOException | BattleshipException e) {
            System.err.println("very very unexpected: " + e.getLocalizedMessage());
        }
    }

    private void doPrintOcean(boolean small) throws IOException, BattleshipException {
        if (small) gameEngine.getPrintStreamView().printSmall(System.out);
        else gameEngine.getPrintStreamView().printLarge(System.out);
        System.out.println("Your phase is " + localGame.getPhase());
        System.out.println("Your name is " + localGame.getLocalPlayerName());
    }

    @Override
    public void sessionEstablished(boolean oracle, String remoteName) {
        System.out.println("Game session created");
        this.remoteName = remoteName;
        if (oracle) {
            System.out.println("You will attack first!");
        } else {
            System.out.println("You will attack second!");
        }
        System.out.println("Please set your ships now!");
    }

    @Override
    public void streamCreated(TCPStream stream) {
        // connection established - setup protocol engine
        System.out.println("Stream created - setup engine now - we can play quite soon...");
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
