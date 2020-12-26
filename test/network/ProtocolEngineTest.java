package network;

import exceptions.BattleshipException;
import exceptions.OceanException;
import exceptions.PhaseException;
import exceptions.ShipException;
import field.Coordinate;
import game.BattleshipImpl;
import game.BattleshipProtocolEngine;
import game.Phase;
import game.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ship.Shipmodel;
import tcp_helper.TCPStream;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


/**
 * @author Edwin W (HTW) on Nov 2020
 */
class ProtocolEngineTest {

    private final String PNAME1 = "Alice";
    private final String PNAME2 = "Jake";
    public static final long TEST_THREAD_SLEEP_DURATION = 500;
    //Network
    public static final int PORTNUMBER = 5555;
    private static int port = 0;

    BattleshipImpl aliceBsImpl;
    BattleshipProtocolEngine aliceBsProtocol;
    BattleshipImpl jakeBsImpl;
    BattleshipProtocolEngine jakeBsProtocol;

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
    public void integrationTestBasic() throws IOException, InterruptedException {
        //two players in this test: Alice and Jake

        //create Alice's game engine
        aliceBsImpl = new BattleshipImpl(PNAME1);
        //create protocol engine for Alice
        aliceBsProtocol = new BattleshipProtocolEngine(aliceBsImpl, PNAME1);
        aliceBsImpl.setProtocolEngine(aliceBsProtocol);

        //create Jake's game engine
        jakeBsImpl = new BattleshipImpl(PNAME2);
        //create protocol engine for Jake
        jakeBsProtocol = new BattleshipProtocolEngine(jakeBsImpl, PNAME2);
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
        aliceBsProtocol.handleConnectionStream(aliceSide.getInputStream(), aliceSide.getOutputStream());
        jakeBsProtocol.handleConnectionStream(jakeSide.getInputStream(), jakeSide.getOutputStream());
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
        aliceBsProtocol.close();
        jakeBsProtocol.close();

        // stop test thread to allow operating system to close sockets
        Thread.sleep(TEST_THREAD_SLEEP_DURATION);
    }


    @Nested
    class Scenarios {
        @BeforeEach
        void setUp() throws IOException, InterruptedException {
            //two players in this test: Alice and Jake

            //create Alice's game engine
            aliceBsImpl = new BattleshipImpl(PNAME1);
            //create protocol engine for Alice
            aliceBsProtocol = new BattleshipProtocolEngine(aliceBsImpl, PNAME1);

            aliceBsImpl.setProtocolEngine(aliceBsProtocol);

            //create Jake's game engine
            jakeBsImpl = new BattleshipImpl(PNAME2);
            //create protocol engine for Jake
            jakeBsProtocol = new BattleshipProtocolEngine(jakeBsImpl, PNAME2);

            jakeBsImpl.setProtocolEngine(jakeBsProtocol);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //                                           setup tcp                                                    //
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            int port = getPortNumber();
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
            aliceBsProtocol.handleConnectionStream(aliceSide.getInputStream(), aliceSide.getOutputStream());
            jakeBsProtocol.handleConnectionStream(jakeSide.getInputStream(), jakeSide.getOutputStream());
            // give it a moment - important stop this test thread - to threads must be launched
            System.out.println("give threads a moment to be launched");
            Thread.sleep(TEST_THREAD_SLEEP_DURATION);
        }


        @Test
        public void integrationTestSetShips() throws IOException, InterruptedException, BattleshipException, PhaseException, ShipException, OceanException {
            // connection is established here
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //                                             run scenario                                               //
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////

            aliceBsImpl.choosePlayerName(PNAME1);
            jakeBsImpl.choosePlayerName(PNAME2);

            aliceBsImpl.choosePlayerName(PNAME2);
            jakeBsImpl.choosePlayerName(PNAME1);
            //give some time to transfer after call
            Thread.sleep(10);

            // pieces must not be same
            assertEquals(aliceBsImpl.getPhase(), jakeBsImpl.getPhase());

            setShipsHelper();
            Thread.sleep(10);

            assertNotEquals(aliceBsImpl.getPhase(), jakeBsImpl.getPhase());

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //                                             tidy up                                                    //
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            aliceBsProtocol.close();
            jakeBsProtocol.close();

            // stop test thread to allow operating system to close sockets
            Thread.sleep(TEST_THREAD_SLEEP_DURATION);
        }


        @Test
        public void integrationTestWin() throws IOException, InterruptedException, BattleshipException, PhaseException, ShipException, OceanException {
            // connection is established here
            setShipsHelper();
            Thread.sleep(10);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //                                             run scenario                                               //
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////

            assertNotEquals(aliceBsImpl.getPhase(), jakeBsImpl.getPhase());

            if (aliceBsImpl.getPhase() == Phase.WAITFORPLAY) {
                aliceBsImpl.setPhase(Phase.PLAY);
                jakeBsImpl.setPhase(Phase.WAITFORPLAY);
            }

            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(0, 0)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(0, 0)));
            assertEquals(Result.SINK, aliceBsImpl.attack(PNAME1, new Coordinate(1, 0)));
            assertEquals(Result.SINK, jakeBsImpl.attack(PNAME2, new Coordinate(1, 0)));

            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(3, 0)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(3, 0)));
            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(6, 0)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(6, 0)));
            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(7, 0)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(7, 0)));
            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(8, 0)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(8, 0)));
            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(9, 0)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(9, 0)));
            assertEquals(Result.SINK, aliceBsImpl.attack(PNAME1, new Coordinate(10, 0)));
            assertEquals(Result.SINK, jakeBsImpl.attack(PNAME2, new Coordinate(10, 0)));

            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(3, 1)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(3, 1)));
            assertEquals(Result.SINK, aliceBsImpl.attack(PNAME1, new Coordinate(3, 2)));
            assertEquals(Result.SINK, jakeBsImpl.attack(PNAME2, new Coordinate(3, 2)));

            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(9, 2)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(9, 2)));
            assertEquals(Result.SINK, aliceBsImpl.attack(PNAME1, new Coordinate(10, 2)));
            assertEquals(Result.SINK, jakeBsImpl.attack(PNAME2, new Coordinate(10, 2)));

            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(0, 5)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(0, 5)));
            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(1, 5)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(1, 5)));
            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(2, 5)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(2, 5)));
            assertEquals(Result.SINK, aliceBsImpl.attack(PNAME1, new Coordinate(3, 5)));
            assertEquals(Result.SINK, jakeBsImpl.attack(PNAME2, new Coordinate(3, 5)));

            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(0, 7)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(0, 7)));
            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(0, 8)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(0, 8)));
            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(0, 9)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(0, 9)));
            assertEquals(Result.SINK, aliceBsImpl.attack(PNAME1, new Coordinate(0, 10)));
            assertEquals(Result.SINK, jakeBsImpl.attack(PNAME2, new Coordinate(0, 10)));

            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(8, 8)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(8, 8)));
            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(9, 8)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(9, 8)));
            assertEquals(Result.SINK, aliceBsImpl.attack(PNAME1, new Coordinate(10, 8)));
            assertEquals(Result.SINK, jakeBsImpl.attack(PNAME2, new Coordinate(10, 8)));

            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(3, 10)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(3, 10)));
            assertEquals(Result.SINK, aliceBsImpl.attack(PNAME1, new Coordinate(4, 10)));
            assertEquals(Result.SINK, jakeBsImpl.attack(PNAME2, new Coordinate(4, 10)));

            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(8, 10)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(8, 10)));
            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(9, 10)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(9, 10)));
            assertEquals(Result.SINK, aliceBsImpl.attack(PNAME1, new Coordinate(10, 10)));
            assertEquals(Result.SINK, jakeBsImpl.attack(PNAME2, new Coordinate(10, 10)));

            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(6, 9)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(6, 9)));
            assertEquals(Result.WIN, aliceBsImpl.attack(PNAME1, new Coordinate(6, 10)));

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //                                             tidy up                                                    //
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            aliceBsProtocol.close();
            jakeBsProtocol.close();

            // stop test thread to allow operating system to close sockets
            Thread.sleep(TEST_THREAD_SLEEP_DURATION);
        }

        @Test
        public void integrationTestOtherWin() throws IOException, InterruptedException, BattleshipException, PhaseException, ShipException, OceanException {
            // connection is established here
            setShipsHelper();
            Thread.sleep(10);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //                                             run scenario                                               //
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////

            assertNotEquals(aliceBsImpl.getPhase(), jakeBsImpl.getPhase());

            if (aliceBsImpl.getPhase() == Phase.WAITFORPLAY) {
                aliceBsImpl.setPhase(Phase.PLAY);
                jakeBsImpl.setPhase(Phase.WAITFORPLAY);
            }

            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(0, 0)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(0, 0)));
            assertEquals(Result.SINK, aliceBsImpl.attack(PNAME1, new Coordinate(1, 0)));
            assertEquals(Result.SINK, jakeBsImpl.attack(PNAME2, new Coordinate(1, 0)));

            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(3, 0)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(3, 0)));
            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(6, 0)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(6, 0)));
            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(7, 0)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(7, 0)));
            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(8, 0)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(8, 0)));
            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(9, 0)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(9, 0)));
            assertEquals(Result.SINK, aliceBsImpl.attack(PNAME1, new Coordinate(10, 0)));
            assertEquals(Result.SINK, jakeBsImpl.attack(PNAME2, new Coordinate(10, 0)));

            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(3, 1)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(3, 1)));
            assertEquals(Result.SINK, aliceBsImpl.attack(PNAME1, new Coordinate(3, 2)));
            assertEquals(Result.SINK, jakeBsImpl.attack(PNAME2, new Coordinate(3, 2)));

            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(9, 2)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(9, 2)));
            assertEquals(Result.SINK, aliceBsImpl.attack(PNAME1, new Coordinate(10, 2)));
            assertEquals(Result.SINK, jakeBsImpl.attack(PNAME2, new Coordinate(10, 2)));

            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(0, 5)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(0, 5)));
            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(1, 5)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(1, 5)));
            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(2, 5)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(2, 5)));
            assertEquals(Result.SINK, aliceBsImpl.attack(PNAME1, new Coordinate(3, 5)));
            assertEquals(Result.SINK, jakeBsImpl.attack(PNAME2, new Coordinate(3, 5)));

            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(0, 7)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(0, 7)));
            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(0, 8)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(0, 8)));
            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(0, 9)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(0, 9)));
            assertEquals(Result.SINK, aliceBsImpl.attack(PNAME1, new Coordinate(0, 10)));
            assertEquals(Result.SINK, jakeBsImpl.attack(PNAME2, new Coordinate(0, 10)));

            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(8, 8)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(8, 8)));
            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(9, 8)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(9, 8)));
            assertEquals(Result.SINK, aliceBsImpl.attack(PNAME1, new Coordinate(10, 8)));
            assertEquals(Result.SINK, jakeBsImpl.attack(PNAME2, new Coordinate(10, 8)));

            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(3, 10)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(3, 10)));
            assertEquals(Result.SINK, aliceBsImpl.attack(PNAME1, new Coordinate(4, 10)));
            assertEquals(Result.SINK, jakeBsImpl.attack(PNAME2, new Coordinate(4, 10)));

            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(8, 10)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(8, 10)));
            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(9, 10)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(9, 10)));
            assertEquals(Result.SINK, aliceBsImpl.attack(PNAME1, new Coordinate(10, 10)));
            assertEquals(Result.SINK, jakeBsImpl.attack(PNAME2, new Coordinate(10, 10)));

            assertEquals(Result.HIT, aliceBsImpl.attack(PNAME1, new Coordinate(6, 9)));
            assertEquals(Result.HIT, jakeBsImpl.attack(PNAME2, new Coordinate(6, 9)));
            assertEquals(Result.MISSED, aliceBsImpl.attack(PNAME1, new Coordinate(6, 8)));
            assertEquals(Result.WIN, jakeBsImpl.attack(PNAME2, new Coordinate(6, 10)));

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //                                             tidy up                                                    //
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            aliceBsProtocol.close();
            jakeBsProtocol.close();

            // stop test thread to allow operating system to close sockets
            Thread.sleep(TEST_THREAD_SLEEP_DURATION);
        }
    }

    private void setShipsHelper() throws BattleshipException, PhaseException, OceanException, ShipException, InterruptedException {
        aliceBsImpl.choosePlayerName(PNAME1);
        jakeBsImpl.choosePlayerName(PNAME2);

        aliceBsImpl.choosePlayerName(PNAME2);
        jakeBsImpl.choosePlayerName(PNAME1);
        //give some time to transfer after call
        Thread.sleep(10);

        jakeBsImpl.setShip(PNAME2, Shipmodel.BATTLESHIP, new Coordinate(6, 0));
        jakeBsImpl.setShip(PNAME2, Shipmodel.CRUISER, new Coordinate(0, 5));
        jakeBsImpl.setShip(PNAME2, Shipmodel.CRUISER, new Coordinate(0, 7), true);
        jakeBsImpl.setShip(PNAME2, Shipmodel.DESTROYER, new Coordinate(3, 0), true);
        jakeBsImpl.setShip(PNAME2, Shipmodel.DESTROYER, new Coordinate(8, 8));
        jakeBsImpl.setShip(PNAME2, Shipmodel.DESTROYER, new Coordinate(8, 10));
        jakeBsImpl.setShip(PNAME2, Shipmodel.SUBMARINE, new Coordinate(0, 0));
        jakeBsImpl.setShip(PNAME2, Shipmodel.SUBMARINE, new Coordinate(9, 2));
        jakeBsImpl.setShip(PNAME2, Shipmodel.SUBMARINE, new Coordinate(3, 10));
        jakeBsImpl.setShip(PNAME2, Shipmodel.SUBMARINE, new Coordinate(6, 9), true);

        aliceBsImpl.setShip(PNAME1, Shipmodel.BATTLESHIP, new Coordinate(6, 0));
        aliceBsImpl.setShip(PNAME1, Shipmodel.CRUISER, new Coordinate(0, 5));
        aliceBsImpl.setShip(PNAME1, Shipmodel.CRUISER, new Coordinate(0, 7), true);
        aliceBsImpl.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(3, 0), true);
        aliceBsImpl.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(8, 8));
        aliceBsImpl.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(8, 10));
        aliceBsImpl.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(0, 0));
        aliceBsImpl.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(9, 2));
        aliceBsImpl.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(3, 10));
        aliceBsImpl.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(6, 9), true);
    }
}