package ship;

import exceptions.ExceptionMsg;
import exceptions.OceanException;
import exceptions.ShipException;
import field.Ocean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Edwin W (HTW) on Nov 2020
 */
class ShipTest {
    Ship s1;
    Ship s2;
    Ship s3;
    Ship s4;
    Ocean ocean;

    @BeforeEach
    void setUp() throws OceanException {
        s1 = new ShipImpl(Shipmodel.BATTLESHIP);
        s2 = new ShipImpl(Shipmodel.CRUISERS);
        s3 = new ShipImpl(Shipmodel.DESTROYERS);
        s4 = new ShipImpl(Shipmodel.SUBMARINES);
        ocean.placeShipPart(s1, 0, 0, true);
        ocean.placeShipPart(s2, 2, 4, false);
        ocean.placeShipPart(s3, 4, 7, false);
        ocean.placeShipPart(s4, 2, 6, true);

    }

    @Nested
    public class PlaceShip {
        @BeforeEach
        void setUp() throws OceanException {
            ocean.placeShipPart(s1, 0, 0, true);
            ocean.placeShipPart(s2, 2, 4, false);
            ocean.placeShipPart(s3, 4, 7, false);
            ocean.placeShipPart(s4, 2, 6, true);
        }

        @Test
        void getPosition() {
            assertArrayEquals(new Point[]{
                    new Point(0, 0),
                    new Point(0, 1),
                    new Point(0, 2),
                    new Point(0, 3),
                    new Point(0, 4),
            }, s1.getPosition());

            assertArrayEquals(new Point[]{
                    new Point(2, 4),
                    new Point(3, 4),
                    new Point(4, 4),
                    new Point(5, 4),
            }, s1.getPosition());

            assertArrayEquals(new Point[]{
                    new Point(4, 7),
                    new Point(5, 7),
                    new Point(6, 7),
            }, s1.getPosition());

            assertArrayEquals(new Point[]{
                    new Point(2, 6),
                    new Point(2, 7),
            }, s1.getPosition());
        }
    }

    //TODO belongs to the ocean test
    @Test
    void setPositionNegative() {
        Throwable e1 = assertThrows(ShipException.class, () -> ocean.placeShipPart(s1, -1, 0, true));
        Throwable e2 = assertThrows(ShipException.class, () -> ocean.placeShipPart(s2, 1, -10, true));
        Throwable e3 = assertThrows(ShipException.class, () -> ocean.placeShipPart(s3, -1, -20, false));
        Throwable e4 = assertThrows(ShipException.class, () -> ocean.placeShipPart(s4, -3, 0, false));
        for (Throwable e : Arrays.asList(e1, e2, e3, e4))
            assertEquals(ExceptionMsg.shipOutside, e.getMessage());
    }

    @Test
    void hit() throws ShipException, OceanException {
        ocean.placeShipPart(s1, 0, 0, true);
        ocean.placeShipPart(s2, 6, 6, false);
        assertFalse(s1.hit(0));

        assertFalse(s2.hit(0));
        assertFalse(s2.hit(1));

        assertFalse(s1.hit(1));
        assertFalse(s1.hit(2));

        assertFalse(s1.hit(3));
        assertTrue(s1.hit(4));

        assertFalse(s2.hit(2));
        assertTrue(s2.hit(3));
    }

    @Test
    void sinking() throws ShipException, OceanException {
        ocean.placeShipPart(s1, 0, 0, true);
        assertFalse(s1.sunk());

        assertFalse(s1.hit(0));
        assertFalse(s1.hit(1));
        assertFalse(s1.hit(2));
        assertFalse(s1.hit(3));

        assertFalse(s1.sunk());

        assertTrue(s1.hit(4));
        assertTrue(s1.sunk());


        ocean.placeShipPart(s2, 6, 6, false);
        assertFalse(s2.sunk());

        assertFalse(s2.hit(0));
        assertFalse(s2.hit(1));
        assertFalse(s2.hit(2));

        assertFalse(s2.sunk());

        assertTrue(s2.hit(3));
        assertTrue(s2.sunk());
    }

    @Test
    void getAncher() throws OceanException {

        ocean.placeShipPart(s2, 3, 6, false);

        assertEquals(new Point(3, 6), s1.getAnchor());
    }

    @Test
    void getSize() {
        assertEquals(5, s1.getSize());
        assertEquals(4, s2.getSize());
        assertEquals(3, s3.getSize());
        assertEquals(2, s4.getSize());
    }

    @Test
    void getPosition() throws OceanException {

        ocean.placeShipPart(s1, 0, 0, true);
        ocean.placeShipPart(s3, 4, 7, false);

        assertArrayEquals(new Point[]{
                new Point(0, 0),
                new Point(0, 1),
                new Point(0, 2),
                new Point(0, 3),
                new Point(0, 4),
        }, s1.getPosition());
        assertArrayEquals(new Point[]{
                new Point(4, 7),
                new Point(5, 7),
                new Point(6, 7),
        }, s3.getPosition());
    }

    @Test
    void getModel() {
        assertEquals(Shipmodel.BATTLESHIP, s1.getModel());
        assertEquals(Shipmodel.CRUISERS, s2.getModel());
        assertEquals(Shipmodel.DESTROYERS, s3.getModel());
        assertEquals(Shipmodel.SUBMARINES, s4.getModel());
    }
}