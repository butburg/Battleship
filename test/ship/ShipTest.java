package ship;

import exceptions.ExceptionMsg;
import exceptions.OceanException;
import exceptions.ShipException;
import field.Ocean;
import field.OceanImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.*;

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
    void setUp() throws OceanException, ShipException {
        s1 = new ShipImpl(Shipmodel.BATTLESHIP);
        s2 = new ShipImpl(Shipmodel.CRUISERS);
        s3 = new ShipImpl(Shipmodel.DESTROYERS);
        s4 = new ShipImpl(Shipmodel.SUBMARINES);

        ocean = new OceanImpl(11);

    }

    @Nested
    public class PlaceShip {
        @BeforeEach
        void setUp() throws OceanException, ShipException {
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
            }, s2.getPosition());

            assertArrayEquals(new Point[]{
                    new Point(4, 7),
                    new Point(5, 7),
                    new Point(6, 7),
            }, s3.getPosition());

            assertArrayEquals(new Point[]{
                    new Point(2, 6),
                    new Point(2, 7),
            }, s4.getPosition());
        }
    }

    @Nested
    public class ShipsPlaced {
        @BeforeEach
        void setUp() throws OceanException, ShipException {
            ocean.placeShipPart(s1, 0, 0, true);
            ocean.placeShipPart(s2, 2, 4, false);
            ocean.placeShipPart(s3, 4, 7, false);
            ocean.placeShipPart(s4, 2, 6, true);
        }

        @Test
        void getSize() {
            assertEquals(5, s1.getSize());
            assertEquals(4, s2.getSize());
            assertEquals(3, s3.getSize());
            assertEquals(2, s4.getSize());
        }

        @Test
        void getModel() {
            assertEquals(Shipmodel.BATTLESHIP, s1.getModel());
            assertEquals(Shipmodel.CRUISERS, s2.getModel());
            assertEquals(Shipmodel.DESTROYERS, s3.getModel());
            assertEquals(Shipmodel.SUBMARINES, s4.getModel());
        }


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
    void locate() throws ShipException {
        s1.locate(0, 0, 0);
        s1.locate(1, 1, 0);
        s1.locate(2, 2, 0);
        s1.locate(3, 3, 0);
        s1.locate(4, 4, 0);

        s2.locate(0, 3, 3);
        s2.locate(1, 3, 4);
        s2.locate(2, 3, 5);
        s2.locate(3, 3, 6);

        s3.locate(0, 10, 8);
        s3.locate(1, 10, 9);
        s3.locate(2, 10, 10);

        s4.locate(0, 0, 10);
        s4.locate(1, 1, 10);
    }
    @Test
    void locateBad1() throws ShipException {
        s2.locate(0, 3, 3);
        s2.locate(1, 3, 4);
Throwable e = assertThrows(ShipException.class, ()-> s2.locate(1, 3, 5));
assertEquals(ExceptionMsg.sh_wrongLocate,e.getMessage());
    }

    @Test
    void locateBad2() throws ShipException {
        s2.locate(0, 3, 3);
        s2.locate(1, 3, 4);
        Throwable e = assertThrows(ShipException.class, ()-> s2.locate(0, 3, 5));
        assertEquals(ExceptionMsg.sh_wrongLocate,e.getMessage());
    }
    @Test
    void locateBad3() throws ShipException {
        s2.locate(0, 3, 3);
        s2.locate(1, 3, 4);
        Throwable e = assertThrows(ShipException.class, ()-> s2.locate(2, 4, 5));
        assertEquals(ExceptionMsg.sh_wrongLocate,e.getMessage());
    }
    @Test
    void locateBad4() throws ShipException {
        s2.locate(0, 3, 3);
        s2.locate(1, 3, 4);
        Throwable e = assertThrows(ShipException.class, ()-> s2.locate(2, 3, 7));
        assertEquals(ExceptionMsg.sh_wrongLocate,e.getMessage());
    }
    @Test
    void locateBad5() throws ShipException {
        s2.locate(0, 3, 3);
        s2.locate(1, 3, 4);
        Throwable e = assertThrows(ShipException.class, ()-> s2.locate(2, 0, 5));
        assertEquals(ExceptionMsg.sh_wrongLocate,e.getMessage());
    }

    @Test
    void getAncher() throws OceanException, ShipException {
        ocean.placeShipPart(s2, 3, 6, false);
        assertEquals(new Point(3, 6), s2.getAnchor());
    }

    @Test
    void getPosition() throws OceanException, ShipException {

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

}