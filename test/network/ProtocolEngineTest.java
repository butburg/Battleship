package network;

import exceptions.BattleshipException;
import exceptions.OceanException;
import exceptions.PhaseException;
import exceptions.ShipException;
import field.Coordinate;
import game.Battleship;
import game.BattleshipImpl;
import game.Phase;
import game.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ship.Shipmodel;
import tcp_helper.TCPStream;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Edwin W (HTW) on Nov 2020
 */
class ProtocolEngineTest {

    private final String PNAME1 = "Alice";
    private final String PNAME2 = "Jake";
    public static final long TEST_THREAD_SLEEP_DURATION = 1000;
    //Network
    public static final int PORTNUMBER = 5555;
    private static int port = 0;

    @BeforeEach
    void setUp() {
    }


    //////////////////////////////////////////////
    // with tcp, mainly by author thsc42
    //////////////////////////////////////////////

    private int getPortNumber() {
        if (ProtocolEngineTest.port == 0) {
            ProtocolEngineTest.port = PORTNUMBER;
        } else {
            ProtocolEngineTest.port++;
        }

        System.out.println("use portnumber " + ProtocolEngineTest.port);
        return ProtocolEngineTest.port;
    }


    @Test
    public void integrationTest1() throws IOException, InterruptedException {
        //two players in this test: Alice and Jake

        //create Alice's game engine
        BattleshipImpl aliceBsImpl = new BattleshipImpl(PNAME1);
        //create protocol engine for Alice
        BattleshipProtocolEngine aliceBsProtocol = new BattleshipProtocolEngine(aliceBsImpl, PNAME1);

        aliceBsImpl.setProtocolEngine(aliceBsProtocol);

        //create Jake's game engine
        BattleshipImpl jakeBsImpl = new BattleshipImpl(PNAME2);
        //create protocol engine for Jake
        BattleshipProtocolEngine jakeBsProtocol = new BattleshipProtocolEngine(jakeBsImpl, PNAME2);

        jakeBsImpl.setProtocolEngine(jakeBsProtocol);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                           setup tcp                                                    //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        int port = this.getPortNumber();
        // this stream plays TCP server role during connection establishment
        TCPStream aliceSide = new TCPStream(port, true, "aliceSide");
        // this stream plays TCP client role during connection establishment
        TCPStream jakeSide = new TCPStream(port, false, "jakeSide");
        // start both stream
        aliceSide.start();
        jakeSide.start();
        // wait until TCP connection is established
        aliceSide.waitForConnection();
        jakeSide.waitForConnection();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                       launch protocol engine                                           //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // give protocol engines streams and launch (read / write)
        aliceBsProtocol.handleConnection(aliceSide.getInputStream(), aliceSide.getOutputStream());
        jakeBsProtocol.handleConnection(jakeSide.getInputStream(), jakeSide.getOutputStream());
        // give it a moment - important stop this test thread - to threads must be launched
        System.out.println("give threads a moment to be launched");
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                             test results                                               //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // pieces must not be same
        assertEquals(aliceBsImpl.getPhase(), jakeBsImpl.getPhase());

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                             tidy up                                                    //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // stop test thread to allow operating system to close sockets
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);

        aliceBsProtocol.close();
        jakeBsProtocol.close();
    }

    @Test
    public void integrationTestFull() throws IOException, InterruptedException, BattleshipException, PhaseException, ShipException, OceanException {
        //two players in this test: Alice and Jake

        //create Alice's game engine
        BattleshipImpl aliceBsImpl = new BattleshipImpl(PNAME1);
        //create protocol engine for Alice
        BattleshipProtocolEngine aliceBsProtocol = new BattleshipProtocolEngine(aliceBsImpl, PNAME1);

        aliceBsImpl.setProtocolEngine(aliceBsProtocol);

        //create Jake's game engine
        BattleshipImpl jakeBsImpl = new BattleshipImpl(PNAME2);
        //create protocol engine for Jake
        BattleshipProtocolEngine jakeBsProtocol = new BattleshipProtocolEngine(jakeBsImpl, PNAME2);

        jakeBsImpl.setProtocolEngine(jakeBsProtocol);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                           setup tcp                                                    //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        int port = this.getPortNumber();
        // this stream plays TCP server role during connection establishment
        TCPStream aliceSide = new TCPStream(port, true, "aliceSide");
        // this stream plays TCP client role during connection establishment
        TCPStream jakeSide = new TCPStream(port, false, "jakeSide");
        // start both stream
        aliceSide.start();
        jakeSide.start();
        // wait until TCP connection is established
        aliceSide.waitForConnection();
        jakeSide.waitForConnection();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                       launch protocol engine                                           //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // give protocol engines streams and launch (read / write)
        aliceBsProtocol.handleConnection(aliceSide.getInputStream(), aliceSide.getOutputStream());
        jakeBsProtocol.handleConnection(jakeSide.getInputStream(), jakeSide.getOutputStream());
        // give it a moment - important stop this test thread - to threads must be launched
        System.out.println("give threads a moment to be launched");
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                             run scenario                                               //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // connection is established here - pick thread waits for results

        aliceBsImpl.choosePlayerName(PNAME1);
        jakeBsImpl.choosePlayerName(PNAME2);

        aliceBsImpl.choosePlayerName(PNAME2);
        jakeBsImpl.choosePlayerName(PNAME1);
        //give some time to transfer after call
        Thread.sleep(100);

        // pieces must not be same
        assertEquals(aliceBsImpl.getPhase(), jakeBsImpl.getPhase());

        assertTrue(jakeBsImpl.setShip(PNAME2, Shipmodel.BATTLESHIP, new Coordinate(6, 0)));
        assertEquals(aliceBsImpl.getPhase(), jakeBsImpl.getPhase());
        assertTrue(jakeBsImpl.setShip(PNAME2, Shipmodel.CRUISERS, new Coordinate(0, 5)));
        assertTrue(jakeBsImpl.setShip(PNAME2, Shipmodel.CRUISERS, new Coordinate(0, 7), true));
        assertTrue(jakeBsImpl.setShip(PNAME2, Shipmodel.DESTROYERS, new Coordinate(3, 0), true));
        assertTrue(jakeBsImpl.setShip(PNAME2, Shipmodel.DESTROYERS, new Coordinate(8, 8)));
        assertTrue(jakeBsImpl.setShip(PNAME2, Shipmodel.DESTROYERS, new Coordinate(8, 10)));
        assertTrue(jakeBsImpl.setShip(PNAME2, Shipmodel.SUBMARINES, new Coordinate(0, 0)));
        assertTrue(jakeBsImpl.setShip(PNAME2, Shipmodel.SUBMARINES, new Coordinate(9, 2)));
        assertTrue(jakeBsImpl.setShip(PNAME2, Shipmodel.SUBMARINES, new Coordinate(3, 10)));
        assertFalse(jakeBsImpl.setShip(PNAME2, Shipmodel.SUBMARINES, new Coordinate(6, 9), true));


        assertTrue(aliceBsImpl.setShip(PNAME1, Shipmodel.BATTLESHIP, new Coordinate(6, 0)));
        assertTrue(aliceBsImpl.setShip(PNAME1, Shipmodel.CRUISERS, new Coordinate(0, 5)));
        assertTrue(aliceBsImpl.setShip(PNAME1, Shipmodel.CRUISERS, new Coordinate(0, 7), true));
        assertTrue(aliceBsImpl.setShip(PNAME1, Shipmodel.DESTROYERS, new Coordinate(3, 0), true));
        assertTrue(aliceBsImpl.setShip(PNAME1, Shipmodel.DESTROYERS, new Coordinate(8, 8)));
        assertTrue(aliceBsImpl.setShip(PNAME1, Shipmodel.DESTROYERS, new Coordinate(8, 10)));
        assertTrue(aliceBsImpl.setShip(PNAME1, Shipmodel.SUBMARINES, new Coordinate(0, 0)));
        assertTrue(aliceBsImpl.setShip(PNAME1, Shipmodel.SUBMARINES, new Coordinate(9, 2)));
        assertTrue(aliceBsImpl.setShip(PNAME1, Shipmodel.SUBMARINES, new Coordinate(3, 10)));
        assertFalse(aliceBsImpl.setShip(PNAME1, Shipmodel.SUBMARINES, new Coordinate(6, 9), true));

        Thread.sleep(100);

        assertNotEquals(aliceBsImpl.getPhase(), jakeBsImpl.getPhase());


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                             tidy up                                                    //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////


        // stop test thread to allow operating system to close sockets
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);
        aliceBsProtocol.close();
        jakeBsProtocol.close();
    }


    /*@Test
    public void pickNetworkTest() throws InterruptedException, IOException, BattleshipException, PhaseException, ShipException, OceanException {
        // there are players in this test: Alice and Kevin
        // create Alice's game engine tester
        BattleshipRXTester aliceGameEngineTester = new BattleshipRXTester();

        // create real protocol engine on Alice's side
        BattleshipProtocolEngine aliceBSProtocolEngine = new BattleshipProtocolEngine(aliceGameEngineTester, "Alice");

        // make it clear - this is a protocol engine
        ProtocolEngine aliceProtocolEngine = aliceBSProtocolEngine;
        // make it clear - it also supports the game engine interface
        Battleship aliceGameEngineSide = aliceBSProtocolEngine;

        // create Kevin's game engine tester
        BattleshipRXTester kevinGameEngineTester = new BattleshipRXTester();
        // create real protocol engine on Kevin's side
        ProtocolEngine kevinProtocolEngine = new BattleshipProtocolEngine(kevinGameEngineTester, "Kevin");

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                           setup tcp                                                    //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        int port = this.getPortNumber();
        // this stream plays TCP server role during connection establishment
        TCPStream aliceSide = new TCPStream(port, true, "aliceSide");
        // this stream plays TCP client role during connection establishment
        TCPStream kevinSide = new TCPStream(port, false, "kevinSide");
        // start both stream
        aliceSide.start();
        kevinSide.start();
        // wait until TCP connection is established
        aliceSide.waitForConnection();
        kevinSide.waitForConnection();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                       launch protocol engine                                           //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // give protocol engines streams and launch (reda / write)
        aliceProtocolEngine.handleConnection(aliceSide.getInputStream(), aliceSide.getOutputStream());
        kevinProtocolEngine.handleConnection(kevinSide.getInputStream(), kevinSide.getOutputStream());
        // give it a moment - important stop this test thread - to threads must be launched
        System.out.println("give threads a moment to be launched");
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                             run scenario                                               //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // connection is established here - pick thread waits for results

        aliceGameEngineSide.choosePlayerName(PNAME1);
        //give some time to transfer after call
        Thread.sleep(100);
        //test results
        assertTrue(kevinGameEngineTester.lastCallchoosePlayerName);
        assertFalse(kevinGameEngineTester.lastCallsetShip);
        assertFalse(kevinGameEngineTester.lastCallAttack);
        assertEquals(kevinGameEngineTester.lastPlayerName, PNAME1);

        aliceGameEngineSide.setShip(PNAME1, Shipmodel.SUBMARINES, new Coordinate(6, 9), true);
        //give some time to transfer after call
        Thread.sleep(100);
        //test results
        assertTrue(kevinGameEngineTester.lastCallsetShip);
        assertFalse(kevinGameEngineTester.lastCallchoosePlayerName);
        assertFalse(kevinGameEngineTester.lastCallAttack);
        assertEquals(kevinGameEngineTester.lastCoordinate, new Coordinate(6, 9));
        assertTrue(kevinGameEngineTester.lastVertical);
        assertEquals(kevinGameEngineTester.lastShip, Shipmodel.SUBMARINES);
        assertEquals(kevinGameEngineTester.lastPlayerName, PNAME1);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                             tidy up                                                    //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        aliceProtocolEngine.close();
        kevinProtocolEngine.close();
        // stop test thread to allow operating system to close sockets
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);
    }
*/
    //////////////////////////////////////////////
    // old test
    //////////////////////////////////////////////
/*

    private Battleship getBsEngine(InputStream is, OutputStream os, Battleship engine) {
        return new BattleshipProtocolEngine(engine);
    }


    //@Test
    public void simpleChooseTest() throws BattleshipException, PhaseException, ShipException, OceanException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Battleship bsProtocolTX = this.getBsEngine(null, baos, null);

        bsProtocolTX.choosePlayerName(PNAME1);

        //our network simulation
        byte[] serializedBytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedBytes);

        BattleshipRXTester bsRXtester = new BattleshipRXTester();
        Battleship bsProtocolRX = this.getBsEngine(bais, null, bsRXtester);

        //helper: trigger the read action from outside
        BattleshipProtocolEngine bsEngine = (BattleshipProtocolEngine) bsProtocolRX;
        bsEngine.read();


        assertTrue(bsRXtester.lastCallchoosePlayerName);
        assertEquals(PNAME1, bsRXtester.lastPlayerName);
        assertFalse(bsRXtester.lastCallsetShip);
        assertFalse(bsRXtester.lastCallAttack);
    }


    //@Test
    public void simpleSetVerticalTest() throws BattleshipException, PhaseException, ShipException, OceanException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Battleship bsProtocolTX = this.getBsEngine(null, baos, null);

        bsProtocolTX.setShip("Hans", Shipmodel.CRUISERS, new Coordinate(7, 2), true);

        //our network simulation
        byte[] serializedBytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedBytes);

        BattleshipRXTester bsRXtester = new BattleshipRXTester();
        Battleship bsProtocolRX = this.getBsEngine(bais, null, bsRXtester);

        //helper: trigger the read action from outside
        BattleshipProtocolEngine bsEngine = (BattleshipProtocolEngine) bsProtocolRX;
        bsEngine.read();


        assertTrue(bsRXtester.lastCallsetShip);
        assertEquals("Hans", bsRXtester.lastPlayerName);
        assertEquals(Shipmodel.CRUISERS, bsRXtester.lastShip);
        assertEquals(new Coordinate(7, 2), bsRXtester.lastCoordinate);
        assertTrue(bsRXtester.lastVertical);
        assertFalse(bsRXtester.lastCallchoosePlayerName);
        assertFalse(bsRXtester.lastCallAttack);
    }

    //@Test
    public void simpleSetNoVerticalTest() throws BattleshipException, PhaseException, ShipException, OceanException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Battleship bsProtocolTX = this.getBsEngine(null, baos, null);

        bsProtocolTX.setShip("Max", Shipmodel.CRUISERS, new Coordinate(8, 2));

        //our network simulation
        byte[] serializedBytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedBytes);

        BattleshipRXTester bsRXtester = new BattleshipRXTester();
        Battleship bsProtocolRX = this.getBsEngine(bais, null, bsRXtester);

        //helper: trigger the read action from outside
        BattleshipProtocolEngine bsEngine = (BattleshipProtocolEngine) bsProtocolRX;
        bsEngine.read();


        assertTrue(bsRXtester.lastCallsetShip);
        assertEquals("Max", bsRXtester.lastPlayerName);
        assertEquals(Shipmodel.CRUISERS, bsRXtester.lastShip);
        assertEquals(new Coordinate(8, 2), bsRXtester.lastCoordinate);
        assertFalse(bsRXtester.lastCallchoosePlayerName);
        assertFalse(bsRXtester.lastCallAttack);
    }

    //@Test
    public void simpleAttackTest() throws BattleshipException, PhaseException, ShipException, OceanException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Battleship bsProtocolTX = this.getBsEngine(null, baos, null);

        bsProtocolTX.attack("Jorg", new Coordinate(8, 5));

        //our network simulation
        byte[] serializedBytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedBytes);

        BattleshipRXTester bsRXtester = new BattleshipRXTester();
        Battleship bsProtocolRX = this.getBsEngine(bais, null, bsRXtester);

        //helper: trigger the read action from outside
        BattleshipProtocolEngine bsEngine = (BattleshipProtocolEngine) bsProtocolRX;
        bsEngine.read();


        assertTrue(bsRXtester.lastCallAttack);
        assertEquals("Jorg", bsRXtester.lastPlayerName);
        assertEquals(new Coordinate(8, 5), bsRXtester.lastCoordinate);
          assertFalse(bsRXtester.lastCallchoosePlayerName);
        assertFalse(bsRXtester.lastCallsetShip);
    }
*/
    private class BattleshipRXTester implements Battleship {
        private boolean lastCallsetShip = false;
        private boolean lastCallchoosePlayerName = false;
        private boolean lastCallAttack = false;
        private String lastPlayerName = null;
        private Shipmodel lastShip = null;
        private Coordinate lastCoordinate = null;
        private boolean lastVertical = false;

        @Override
        public void choosePlayerName(String playerName) {
            lastPlayerName = playerName;
            this.lastCallchoosePlayerName = true;
            this.lastCallsetShip = false;
            this.lastCallAttack = false;
        }

        @Override
        public boolean setShip(String player, Shipmodel ship, Coordinate xy, boolean vertical) {
            this.lastPlayerName = player;
            this.lastShip = ship;
            this.lastCoordinate = xy;
            this.lastVertical = vertical;
            this.lastCallchoosePlayerName = false;
            this.lastCallsetShip = true;
            this.lastCallAttack = false;
            return false;
        }

        @Override
        public boolean setShip(String player, Shipmodel ship, Coordinate xy) {
            this.lastPlayerName = player;
            this.lastShip = ship;
            this.lastCoordinate = xy;
            this.lastCallchoosePlayerName = false;
            this.lastCallsetShip = true;
            this.lastCallAttack = false;
            return false;
        }

        @Override
        public Result attack(String player, Coordinate position) {
            this.lastPlayerName = player;
            this.lastCoordinate = position;
            this.lastCallchoosePlayerName = false;
            this.lastCallsetShip = false;
            this.lastCallAttack = true;
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
    }
}