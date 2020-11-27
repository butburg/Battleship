package field;

import exceptions.ExceptionMsg;
import exceptions.OceanException;
import exceptions.ShipException;
import game.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ship.Ship;
import ship.ShipImpl;
import ship.Shipmodel;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Edwin W (HTW) on Nov 2020
 */
class OceanTest {

    private Ship s1;
    private Ship s2;
    private Ship s3;
    private Ship s4;
    private Ocean ocean;
    private int oceanSize;

    @BeforeEach
    void setUp() throws OceanException, ShipException {
        ocean = new OceanImpl(11);
        oceanSize = ocean.getSize();

        s1 = new ShipImpl(Shipmodel.BATTLESHIP);
        s2 = new ShipImpl(Shipmodel.CRUISERS);
        s3 = new ShipImpl(Shipmodel.DESTROYERS);
        s4 = new ShipImpl(Shipmodel.SUBMARINES);
    }


    @Test
    void getSize() {
        assertEquals(11, ocean.getSize());
    }

    @Test
    void placeShip() throws ShipException, OceanException {
        ocean.placeShip(s1, 0, 0, true);
        ocean.placeShip(s2, 2, 4, false);
        ocean.placeShip(s3, 4, 7, false);
        ocean.placeShip(s4, 2, 6, true);
    }


    @Test
    void placeShipEachShipCorner() throws ShipException, OceanException {
        ocean.placeShip(s1, 5, 5, false);
        ocean.placeShip(s2, 4, 6, true);
        ocean.placeShip(s3, 1, 5, false);
        ocean.placeShip(s4, 4, 3, true);
    }

    @Test
    void placeShipColliding() throws ShipException, OceanException {
        ocean.placeShip(s1, 4, 0, true);
        Throwable e = assertThrows(OceanException.class, () -> ocean.placeShip(s2, 1, 4, false));
        assertEquals(ExceptionMsg.oc_shipCollidingPosition, e.getMessage());
        ocean.placeShip(s3, 4, 7, false);
        e = assertThrows(OceanException.class, () -> ocean.placeShip(s4, 4, 7, true));
        assertEquals(ExceptionMsg.oc_shipCollidingPosition, e.getMessage());
    }

    @Test
    void placeShipOutside1() throws ShipException, OceanException {
        ocean.placeShip(s1, 0, 0, true);

        Throwable e = assertThrows(OceanException.class, () -> ocean.placeShip(s2, -2, 4, false));
        assertEquals(ExceptionMsg.oc_shipOutside, e.getMessage());

        e = assertThrows(OceanException.class, () -> ocean.placeShip(s3, 4, -7, false));
        assertEquals(ExceptionMsg.oc_shipOutside, e.getMessage());

        e = assertThrows(OceanException.class, () -> ocean.placeShip(s4, oceanSize, 7, true));
        assertEquals(ExceptionMsg.oc_shipOutside, e.getMessage());

        e = assertThrows(OceanException.class, () -> ocean.placeShip(s1, 7, oceanSize, false));
        assertEquals(ExceptionMsg.oc_shipOutside, e.getMessage());
    }

    @Test
    void placeShipOutside2() {
        Throwable e1 = assertThrows(OceanException.class, () -> ocean.placeShip(s1, -1, 0, true));
        Throwable e2 = assertThrows(OceanException.class, () -> ocean.placeShip(s2, 1, -10, true));
        Throwable e3 = assertThrows(OceanException.class, () -> ocean.placeShip(s3, -1, -20, false));
        Throwable e4 = assertThrows(OceanException.class, () -> ocean.placeShip(s4, -3, 0, false));
        for (Throwable e : Arrays.asList(e1, e2, e3, e4))
            assertEquals(ExceptionMsg.oc_shipOutside, e.getMessage());
    }

    @Test
    void placeShipTouching() throws ShipException, OceanException {
        ocean.placeShip(s1, 0, 0, true);

        Throwable e = assertThrows(OceanException.class, () -> ocean.placeShip(s2, 1, 0, false));
        assertEquals(ExceptionMsg.oc_shipTouching, e.getMessage());
    }

    @Test
    void placeShipTouchingAllSides1() throws ShipException, OceanException {
        ocean.placeShip(s4, 5, 5, true);
        Throwable e = assertThrows(OceanException.class, () -> ocean.placeShip(s3, 4, 4, true));
        assertEquals(ExceptionMsg.oc_shipTouching, e.getMessage());
        e = assertThrows(OceanException.class, () -> ocean.placeShip(s3, 4, 3, true));
        assertEquals(ExceptionMsg.oc_shipTouching, e.getMessage());
        e = assertThrows(OceanException.class, () -> ocean.placeShip(s3, 4, 7, false));
        assertEquals(ExceptionMsg.oc_shipTouching, e.getMessage());
        e = assertThrows(OceanException.class, () -> ocean.placeShip(s3, 6, 6, true));
        assertEquals(ExceptionMsg.oc_shipTouching, e.getMessage());
        e = assertThrows(OceanException.class, () -> ocean.placeShip(s3, 6, 6, false));
        assertEquals(ExceptionMsg.oc_shipTouching, e.getMessage());
        e = assertThrows(OceanException.class, () -> ocean.placeShip(s3, 6, 3, true));
        assertEquals(ExceptionMsg.oc_shipTouching, e.getMessage());
    }

    @Test
    void placeShipTouchingAllSides2() throws ShipException, OceanException {
        ocean.placeShip(s2, 5, 5, false);
        Throwable e = assertThrows(OceanException.class, () -> ocean.placeShip(s3, 4, 4, true));
        assertEquals(ExceptionMsg.oc_shipTouching, e.getMessage());
        e = assertThrows(OceanException.class, () -> ocean.placeShip(s3, 4, 3, true));
        assertEquals(ExceptionMsg.oc_shipTouching, e.getMessage());
        e = assertThrows(OceanException.class, () -> ocean.placeShip(s3, 9, 4, true));
        assertEquals(ExceptionMsg.oc_shipTouching, e.getMessage());
        e = assertThrows(OceanException.class, () -> ocean.placeShip(s3, 6, 6, true));
        assertEquals(ExceptionMsg.oc_shipTouching, e.getMessage());
        e = assertThrows(OceanException.class, () -> ocean.placeShip(s3, 6, 6, false));
        assertEquals(ExceptionMsg.oc_shipTouching, e.getMessage());
        e = assertThrows(OceanException.class, () -> ocean.placeShip(s3, 6, 2, true));
        assertEquals(ExceptionMsg.oc_shipTouching, e.getMessage());
    }

    @Test
    void bombAt() throws OceanException, ShipException {
        placeShip();
        assertEquals(Result.HIT, ocean.bombAt(new Coordinate(2, 6)));
        assertEquals(Result.MISSED, ocean.bombAt(new Coordinate(2, 5)));
        assertEquals(Result.HIT, ocean.bombAt(new Coordinate(0, 1)));
        assertEquals(Result.HIT, ocean.bombAt(new Coordinate(0, 0)));
        assertEquals(Result.HIT, ocean.bombAt(new Coordinate(0, 2)));
        assertEquals(Result.HIT, ocean.bombAt(new Coordinate(0, 3)));
        assertEquals(Result.SINK, ocean.bombAt(new Coordinate(0, 4)));
        assertEquals(Result.MISSED, ocean.bombAt(new Coordinate(2, 8)));
        assertEquals(Result.SINK, ocean.bombAt(new Coordinate(2, 7)));
    }

    @Test
    void bombTwice() throws OceanException, ShipException {
        placeShip();
        assertEquals(Result.HIT, ocean.bombAt(new Coordinate(0, 0)));
        assertEquals(Result.HIT, ocean.bombAt(new Coordinate(0, 1)));
        Throwable e = assertThrows(OceanException.class, () -> ocean.bombAt(new Coordinate(0, 1)));
        assertEquals(ExceptionMsg.oc_attackedAlready, e.getMessage());
    }


    @Test
    void bombOutside() throws OceanException, ShipException {
        placeShip();
        assertEquals(Result.HIT, ocean.bombAt(new Coordinate(0, 0)));
        assertEquals(Result.HIT, ocean.bombAt(new Coordinate(0, 1)));
        Throwable e = assertThrows(OceanException.class,
                () -> ocean.bombAt(new Coordinate(oceanSize, 1)));
        assertEquals(ExceptionMsg.oc_attackOutside, e.getMessage());
        e = assertThrows(OceanException.class, () -> ocean.bombAt(new Coordinate(1, oceanSize)));
        assertEquals(ExceptionMsg.oc_attackOutside, e.getMessage());
        e = assertThrows(OceanException.class, () -> ocean.bombAt(new Coordinate(-1, 1)));
        assertEquals(ExceptionMsg.oc_attackOutside, e.getMessage());
        e = assertThrows(OceanException.class, () -> ocean.bombAt(new Coordinate(1, -1)));
        assertEquals(ExceptionMsg.oc_attackOutside, e.getMessage());
    }

}