package network;

import exceptions.BattleshipException;
import exceptions.PhaseException;
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
    public void simpleChooseTest() throws BattleshipException, PhaseException {
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
        assertEquals("Alice", bsRXtester.lastPlayerName);
        assertFalse(bsRXtester.lastCallsetShip);
        assertFalse(bsRXtester.lastCallAttack);
    }

    private class BattleshipRXTester implements Battleship {
        private boolean lastCallsetShip = false;
        private boolean lastCallchoosePlayerName = false;
        private boolean lastCallAttack = false;
        private String lastPlayerName = null;

        @Override
        public void choosePlayerName(String playerName) {
            lastPlayerName = playerName;
            this.lastCallchoosePlayerName = true;
            this.lastCallsetShip = false;
            this.lastCallAttack = false;
        }

        @Override
        public boolean setShip(String player, Shipmodel ship, Coordinate xy, boolean vertical) {
            this.lastCallchoosePlayerName = false;
            this.lastCallsetShip = true;
            this.lastCallAttack = false;
            return false;
        }

        @Override
        public boolean setShip(String player, Shipmodel ship, Coordinate xy) {
            this.lastCallchoosePlayerName = false;
            this.lastCallsetShip = true;
            this.lastCallAttack = false;
            return false;
        }

        @Override
        public Result attack(String player, Coordinate position) {
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