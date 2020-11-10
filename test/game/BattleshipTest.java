package game;

import exceptions.BattleshipException;
import exceptions.PhaseException;
import exceptions.ShipException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ship.Ship;

import java.awt.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Edwin W (570900) on Nov 2020
 * <p>
 * Tests the Battleship for all Methods choosing the names for two player, setting up the ships in a field
 * and attacking field.
 * <p>
 * There is no field tested yet
 * <p>
 * <p>
 * Using @nested tests: <a href="https://jaxenter.de/highlights-junit-5-65986">jaxenter.de/highlights-junit-5-65986</a>
 */
class BattleshipTest {
    Battleship bs;

    public static final String PNAME1 = "Edwin";
    public static final String PNAME2 = "Pia";
    public static final String PNAME3 = "Kevin";


    Ship ship1, ship2, ship3, ship4, ship5, ship6, ship7, ship8, ship9, ship10;
    Point p_1_2;
    Point p_2_2;
    Point p_1_9;
    Point p_6_6;
    Point p_7_6;

    @BeforeEach
    void setUp() throws ShipException {
        bs = new BattleshipImpl();

        //good Ships: inside field and right amount of each type, not collidating
        ship1 = new Ship(Shipmodel.BATTLESHIP, 1, 1);
        ship2 = new Ship(Shipmodel.CRUISERS, 3, 1);
        ship3 = new Ship(Shipmodel.CRUISERS, 5, 1);
        ship4 = new Ship(Shipmodel.DESTROYERS, 7, 1);
        ship5 = new Ship(Shipmodel.DESTROYERS, 9, 1);
        ship6 = new Ship(Shipmodel.DESTROYERS, 2, 6);
        ship7 = new Ship(Shipmodel.SUBMARINES, 4, 6);
        ship8 = new Ship(Shipmodel.SUBMARINES, 6, 6);
        ship9 = new Ship(Shipmodel.SUBMARINES, 8, 6);
        ship10 = new Ship(Shipmodel.SUBMARINES, 10, 6);

        p_1_2 = new Point(1, 2);
        p_2_2 = new Point(2, 2);
        p_1_9 = new Point(1, 9);
        p_6_6 = new Point(6, 6);
        p_7_6 = new Point(7, 6);

    }

    @Nested
    public class PhaseChoose {

        @Test
        void choosePlayerGood() throws BattleshipException, PhaseException {
            bs.choosePlayer(PNAME1);
            bs.choosePlayer(PNAME2);
        }

        @Test
        void choosePlayerSameName() throws BattleshipException, PhaseException {
            bs.choosePlayer(PNAME2);
            Throwable e = assertThrows(BattleshipException.class, () -> bs.choosePlayer(PNAME2));
            assertEquals("Name already taken!", e.getMessage());
        }

        @Test
        void choosePlayer3Player() throws BattleshipException, PhaseException {
            bs.choosePlayer(PNAME1);
            bs.choosePlayer(PNAME2);
            Throwable e = assertThrows(BattleshipException.class, () -> bs.choosePlayer(PNAME3));
            assertEquals("There are two Players already!", e.getMessage());
        }

        @Test
        void choosePlayer3PlayerSameName() throws BattleshipException, PhaseException {
            bs.choosePlayer(PNAME1);
            bs.choosePlayer(PNAME2);
            Throwable e = assertThrows(BattleshipException.class, () -> bs.choosePlayer(PNAME1));
            assertEquals("There are two Players already!", e.getMessage());
        }

        @Test
        void setShipWrongPhase1() throws BattleshipException, PhaseException {
            bs.choosePlayer(PNAME1);
            Throwable e = assertThrows(PhaseException.class, () -> bs.setShip(PNAME1, ship1));
            assertEquals("Wrong phase!", e.getMessage());
        }

        @Test
        void setShipWrongPhase2() throws BattleshipException, PhaseException {
            Throwable e = assertThrows(PhaseException.class, () -> bs.setShip(PNAME1, ship1));
            assertEquals("Wrong phase!", e.getMessage());
        }

        @Test
        void attackWrongPhase1() throws BattleshipException, PhaseException {
            bs.choosePlayer(PNAME1);
            Throwable e = assertThrows(PhaseException.class, () -> bs.attack(PNAME1, p_1_2));
            assertEquals("Wrong phase!", e.getMessage());
        }

        @Test
        void attackWrongPhase2() throws BattleshipException, PhaseException {
            Throwable e = assertThrows(PhaseException.class, () -> bs.attack(PNAME1, p_1_2));
            assertEquals("Wrong phase!", e.getMessage());
        }
    }

    @Nested
    public class PhaseSetShips {
        @BeforeEach
        public void setUp() throws Exception {
            choosePlayerDefault();
        }

        @Test
        void setShipGood() throws BattleshipException, PhaseException {
            for (Ship ship : Arrays.asList(ship1, ship2, ship3, ship4, ship5, ship6, ship7, ship8, ship9, ship10)) {
                assertEquals(true, bs.setShip(PNAME1, ship));
            }
            assertEquals(false, bs.setShip(PNAME1, ship10));
        }

        @Test
        void setShipWrongPlayer() throws BattleshipException, PhaseException {
            Throwable e1 = assertThrows(BattleshipException.class, () -> bs.setShip(PNAME3, ship1));
            assertEquals("Wrong player!", e1.getMessage());
        }


        @Test
        void setShipOutside() throws ShipException, BattleshipException, PhaseException {
            choosePlayerDefault();

            Ship shipOutside1 = new Ship(Shipmodel.BATTLESHIP, 15, 1);
            Throwable e1 = assertThrows(Exception.class, () -> bs.setShip(PNAME1, shipOutside1));
            assertEquals("", e1.getMessage());

            Ship shipOutside2 = new Ship(Shipmodel.BATTLESHIP, 1, 15);
            Throwable e2 = assertThrows(Exception.class, () -> bs.setShip(PNAME1, shipOutside2));
            assertEquals("", e2.getMessage());

            Ship shipOutside3 = new Ship(Shipmodel.BATTLESHIP, 15, 16);
            Throwable e3 = assertThrows(Exception.class, () -> bs.setShip(PNAME1, shipOutside3));
            assertEquals("", e3.getMessage());
        }


        @Test
        void choosePlayerWrongPhase1() throws BattleshipException, PhaseException {
            bs.setShip(PNAME1, ship1);
            bs.setShip(PNAME1, ship2);

            Throwable e = assertThrows(PhaseException.class, () -> bs.choosePlayer(PNAME3));
            assertEquals("Wrong phase!", e.getMessage());
        }

        @Test
        void choosePlayerWrongPhase2() throws BattleshipException, PhaseException {
            Throwable e = assertThrows(PhaseException.class, () -> bs.choosePlayer(PNAME3));
            assertEquals("Wrong phase!", e.getMessage());
        }


        @Test
        void attackWrongPhase1() throws BattleshipException, PhaseException {
            bs.setShip(PNAME1, ship1);
            bs.setShip(PNAME1, ship2);

            Throwable e = assertThrows(PhaseException.class, () -> bs.attack(PNAME1, p_1_2));
            assertEquals("Wrong phase!", e.getMessage());
        }

        @Test
        void attackWrongPhase2() throws BattleshipException, PhaseException {
            Throwable e = assertThrows(PhaseException.class, () -> bs.attack(PNAME1, p_1_2));
            assertEquals("Wrong phase!", e.getMessage());
        }

    }

    @Nested
    public class PhaseAttack {
        @BeforeEach
        public void setUp() throws Exception {
            choosePlayerDefault();
            setShipsDefault();
        }

        @Test
        void attackGood() throws BattleshipException, PhaseException {
            assertEquals(Result.HIT, bs.attack(PNAME1, p_1_2));
            assertEquals(Result.MISSED, bs.attack(PNAME1, p_1_9));
            assertEquals(Result.HIT, bs.attack(PNAME1, p_6_6));
            assertEquals(Result.SINK, bs.attack(PNAME1, p_7_6));
        }


        @Test
        void attackWrongPlayer() throws BattleshipException, PhaseException {
            choosePlayerDefault();

            Throwable e2 = assertThrows(BattleshipException.class, () -> bs.attack(PNAME3, p_2_2));
            assertEquals("Wrong player!", e2.getMessage());
        }

        @Test
        void choosePlayerWrongPhase1() throws BattleshipException, PhaseException {
            bs.attack(PNAME1, p_1_2);

            Throwable e = assertThrows(PhaseException.class, () -> bs.choosePlayer(PNAME3));
            assertEquals("Wrong phase!", e.getMessage());
        }

        @Test
        void choosePlayerWrongPhase2() throws BattleshipException, PhaseException {
            Throwable e = assertThrows(PhaseException.class, () -> bs.choosePlayer(PNAME3));
            assertEquals("Wrong phase!", e.getMessage());
        }

        @Test
        void setShipWrongPhase1() throws BattleshipException, PhaseException {
            bs.attack(PNAME1, p_1_2);

            Throwable e = assertThrows(PhaseException.class, () -> bs.setShip(PNAME1, ship1));
            assertEquals("Wrong phase!", e.getMessage());
        }

        @Test
        void setShipWrongPhase2() throws BattleshipException, PhaseException {
            Throwable e = assertThrows(PhaseException.class, () -> bs.setShip(PNAME1, ship1));
            assertEquals("Wrong phase!", e.getMessage());
        }

    }

    /* helping methods */

    void choosePlayerDefault() throws BattleshipException, PhaseException {
        bs.choosePlayer(PNAME1);
        bs.choosePlayer(PNAME2);
    }

    void setShipsDefault() throws BattleshipException, PhaseException {
        for (Ship ship : Arrays.asList(ship1, ship2, ship3, ship4, ship5, ship6, ship7, ship8, ship9, ship10)) {
            bs.setShip(PNAME1, ship);
        }

        for (Ship ship : Arrays.asList(ship1, ship2, ship3, ship4, ship5, ship6, ship7, ship8, ship9, ship10)) {
            bs.setShip(PNAME2, ship);
        }
    }
}