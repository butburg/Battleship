package network;

import exceptions.BattleshipException;
import exceptions.OceanException;
import exceptions.PhaseException;
import exceptions.ShipException;
import field.Coordinate;
import game.Battleship;
import game.Phase;
import game.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ship.Shipmodel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Edwin W (HTW) on Nov 2020
 */
class ProtocolEngineTest {

    private final String PNAME1 = "Alice";

    @BeforeEach
    void setUp() {
    }

    private Battleship getBsEngine(InputStream is, OutputStream os, Battleship engine) {
        return new BattleshipProtocolEngine(is, os, engine);
    }

    @Test
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


    @Test
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

    @Test
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

    @Test
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