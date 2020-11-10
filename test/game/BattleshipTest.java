package game;

import exceptions.BattleshipException;
import exceptions.ShipException;
import exceptions.StatusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ship.Ship;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Edwin W (570900) on Nov 2020
 */
class BattleshipTest {
    Battleship bs;

    Ship s1;
    Ship s2;
    Ship s3;
    Ship s4;
    Ship s5;
    Ship s6;
    Ship s7;
    Ship s8;
    Ship s9;
    Ship s10;

    @BeforeEach
    void setUp() throws ShipException {
        bs = new BattleshipImpl();
        s1 = new Ship(Shipmodel.BATTLESHIP, 1, 1);
        s2 = new Ship(Shipmodel.CRUISERS, 3, 1);
        s3 = new Ship(Shipmodel.CRUISERS, 5, 1);
        s4 = new Ship(Shipmodel.DESTROYERS, 7, 1);
        s5 = new Ship(Shipmodel.DESTROYERS, 9, 1);
        s6 = new Ship(Shipmodel.DESTROYERS, 2, 6);
        s7 = new Ship(Shipmodel.SUBMARINES, 4, 6);
        s8 = new Ship(Shipmodel.SUBMARINES, 6, 6);
        s9 = new Ship(Shipmodel.SUBMARINES, 8, 6);
        s10 = new Ship(Shipmodel.SUBMARINES, 10, 6);


    }


    @Test
    void choosePlayer() throws BattleshipException, StatusException {
        bs.choosePlayer("Edwin");
        bs.choosePlayer("Pia");
    }

    @Test
    void choosePlayer3Player() throws BattleshipException, StatusException {
        bs.choosePlayer("Pia");
        Throwable e = assertThrows(BattleshipException.class, () -> bs.choosePlayer("Pia"));
        assertEquals("Name already taken!", e.getMessage());
    }

    @Test
    void choosePlayerSameName() throws BattleshipException, StatusException {
        bs.choosePlayer("Edwin");
        bs.choosePlayer("Pia");
        Throwable e = assertThrows(BattleshipException.class, () -> bs.choosePlayer("Kevin"));
        assertEquals("There are two Players already!", e.getMessage());
    }


    @Test
    void setShip() throws BattleshipException, StatusException {
        assertEquals(true, bs.setShip("Edwin", s1));
        assertEquals(true, bs.setShip("Edwin", s2));
        assertEquals(true, bs.setShip("Edwin", s3));
        assertEquals(true, bs.setShip("Edwin", s4));
        assertEquals(true, bs.setShip("Edwin", s5));
        assertEquals(true, bs.setShip("Edwin", s6));
        assertEquals(true, bs.setShip("Edwin", s7));
        assertEquals(true, bs.setShip("Edwin", s8));
        assertEquals(true, bs.setShip("Edwin", s9));
        assertEquals(true, bs.setShip("Edwin", s10));
        assertEquals(false, bs.setShip("Edwin", s10));
    }

    @Test
    void attack() throws BattleshipException, StatusException {
        assertEquals(Result.HIT, bs.attack("Edwin", new Point(1, 2)));
        assertEquals(Result.MISSED, bs.attack("Edwin", new Point(1, 9)));
        assertEquals(Result.HIT, bs.attack("Edwin", new Point(6, 6)));
        assertEquals(Result.SINK, bs.attack("Edwin", new Point(7, 6)));
    }
}