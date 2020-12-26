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
        b.append("\n");
        b.append("valid commands:");
        b.append("\n");
        b.append(CONNECT2PORT);
        b.append(".. connect as tcp client to another open port");
        b.append("\n");
        b.append(OPENPORT);
        b.append(".. open port and become tcp server");
        b.append("\n");
        b.append(PRINTOCEAN);
        b.append(".. print the ocean");
        b.append("\n");
        b.append(SETSHIP);
        b.append(".. set a ship");
        b.append("\n");
        b.append(ATTACK);
        b.append(".. attack a field");
        b.append("\n");
        b.append(EXIT);
        b.append(".. exit the game");

        this.outStream.println(b.toString());
    }

    public void runLoop() {
        boolean again = true;

        while (again) {
            String userInput = null;

            try {
                // read user input
                userInput = inBufferedReader.readLine();

                // finish that loop if less than nothing came in
                if (userInput == null) break;

                // trim whitespaces on both sides
                userInput = userInput.trim();

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
                    case PRINTOCEAN -> this.doPrintOcean();
                    case CONNECT2PORT -> this.doConnect2Port(parameterPart);
                    case OPENPORT -> this.doOpenPort();
                    case SETSHIP -> {
                        this.doSetShip(parameterPart);
                        this.doPrintOcean();
                    }
                    case ATTACK -> {
                        this.doAttack(parameterPart);
                        this.doPrintOcean();
                    }
                    case "q", EXIT -> {
                        // end loop
                        again = false;
                        this.doExit();
                    }
                    default -> {
                        this.outStream.println("unknown command:" + userInput);
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
                this.outStream.println("game exception: " + ex.getLocalizedMessage());
            } catch (PhaseException ex) {
                this.outStream.println("wrong phase: " + ex.getLocalizedMessage());
            } catch (OceanException ex) {
                this.outStream.println("ocean problems: " + ex.getLocalizedMessage());
            } catch (ShipException ex) {
                this.outStream.println("ship problems: " + ex.getLocalizedMessage());
            } catch (RuntimeException ex) {
                this.outStream.println("runtime problems: " + ex.getLocalizedMessage());
            }
        }
    }

    private void doExit() throws IOException {
        protocolEngine.close();
    }

    private void doSetShip(String userParameterPart) throws BattleshipException, PhaseException, OceanException, ShipException {
        isConnectedGuard();

        StringTokenizer st = new StringTokenizer(userParameterPart);
        if (st.countTokens() < 3 || st.countTokens() > 4) {
            throw new IllegalStateException("Need 3 or 4 Parameter but was: " + st.countTokens());
        }

        String ship = st.nextToken();
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        if (st.countTokens() == 4) {
            String vertical = st.nextToken();
            gameEngine.setShip(Shipmodel.valueOf(ship), new Coordinate(x, y), Boolean.getBoolean(vertical));
        } else gameEngine.setShip(Shipmodel.valueOf(ship), new Coordinate(x, y));

    }

    private void doAttack(String userParameterPart) throws BattleshipException, PhaseException, OceanException, ShipException {
        isConnectedGuard();

        StringTokenizer st = new StringTokenizer(userParameterPart);
        if (st.countTokens() != 2) {
            throw new IllegalStateException("Need 3 or 4 Parameter but was: " + st.countTokens());
        }
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        gameEngine.attack(new Coordinate(x, y));

    }

    private void isConnectedGuard() throws BattleshipException {
        if (this.protocolEngine == null) throw new BattleshipException("not connected yet, connect to or open Port!");
    }

    private void doOpenPort() {
        if (!alreadyConnected()) {
            tcpStream = new TCPStream(PORT, true, playerName);
            tcpStream.setStreamCreationListener(this);
            tcpStream.start();
        }
    }

    private void doConnect2Port(String userParamterPart) {
        if (!alreadyConnected()) {
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
            this.doPrintOcean();
        } catch (IOException e) {
            System.err.println("very very unexpected: " + e.getLocalizedMessage());
        }
    }

    private void doPrintOcean() throws IOException {
        gameEngine.getPrintStreamView().print(System.out);
        System.out.println("your phase is " + localGame.getPhase());
        System.out.println("your name is " + localGame.getLocalPlayerName());
    }

    @Override
    public void sessionEstablished(boolean oracle, String remoteName) {
        System.out.println("game session created");
        this.remoteName = remoteName;

        if (oracle) {
            System.out.println("Your turn now!");
        } else {
            System.out.println("Wait for the game partner to start...");
        }
    }

    @Override
    public void streamCreated(TCPStream stream) {
        // connection established - setup protocol engine
        System.out.println("stream created - setup engine - we can play quite soon.");
        this.protocolEngine = new BattleshipProtocolEngine(this.gameEngine, this.playerName);
        this.gameEngine.setProtocolEngine(protocolEngine);

        this.protocolEngine.addGameSessionEstablishedSubscriber(this);

        try {
            protocolEngine.handleConnectionStream(stream.getInputStream(), stream.getOutputStream());
        } catch (IOException e) {
            System.err.println("cannot get streams from tcpStream - fatal, exit: " + e.getLocalizedMessage());
            System.exit(1);
        }
    }

    private boolean alreadyConnected() {
        if (tcpStream != null) {
            System.err.println("Connection established or connection attempt in progress");
            return true;
        } else return false;
    }
}
