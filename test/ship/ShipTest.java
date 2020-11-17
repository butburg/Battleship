package ship;

import exceptions.ExceptionMessages;
import exceptions.ShipException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {
    Ship s1;
    Ship s2;
    Ship s3;
    Ship s4;

    @BeforeEach
    void setUp() throws ShipException {
        s1 = new ShipImpl(Shipmodel.BATTLESHIP);
        s2 = new ShipImpl(Shipmodel.CRUISERS);
        s3 = new ShipImpl(Shipmodel.DESTROYERS);
        s4 = new ShipImpl(Shipmodel.SUBMARINES);

    }

    @Test
    void createPosition() throws ShipException {
        assertArrayEquals(new Point[]{
                new Point(0, 0),
                new Point(0, 1),
                new Point(0, 2),
                new Point(0, 3),
                new Point(0, 4),
        }, s1.createPosition(0, 0, true));

        assertArrayEquals(new Point[]{
                new Point(2, 4),
                new Point(3, 4),
                new Point(4, 4),
                new Point(5, 4),
        }, s2.createPosition(2, 4, false));

        assertArrayEquals(new Point[]{
                new Point(4, 7),
                new Point(5, 7),
                new Point(6, 7),
        }, s3.createPosition(4, 7));

        assertArrayEquals(new Point[]{
                new Point(2, 6),
                new Point(2, 7),
        }, s4.createPosition(2, 6, true));
    }

    @Test
    void createPositionNegative() {
        Throwable e1 = assertThrows(ShipException.class, () -> s1.createPosition(-1, 0, true));
        Throwable e2 = assertThrows(ShipException.class, () -> s2.createPosition(1, -10, true));
        Throwable e3 = assertThrows(ShipException.class, () -> s3.createPosition(-1, -20));
        Throwable e4 = assertThrows(ShipException.class, () -> s4.createPosition(-3, 0, false));
        for (Throwable e : Arrays.asList(e1, e2, e3, e4))
            assertEquals(ExceptionMessages.shipInvalidPosition, e.getMessage());
    }

    @Test
    void hit() throws ShipException {
        s1.createPosition(0, 0, true);
        s2.createPosition(6, 6);
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
    void sinking() throws ShipException {
        s1.createPosition(0, 0, true);
        assertFalse(s1.sunk());

        assertFalse(s1.hit(0));
        assertFalse(s1.hit(1));
        assertFalse(s1.hit(2));
        assertFalse(s1.hit(3));

        assertFalse(s1.sunk());

        assertTrue(s1.hit(4));
        assertTrue(s1.sunk());


        s2.createPosition(6, 6);
        assertFalse(s2.sunk());

        assertFalse(s2.hit(0));
        assertFalse(s2.hit(1));
        assertFalse(s2.hit(2));

        assertFalse(s2.sunk());

        assertTrue(s2.hit(3));
        assertTrue(s2.sunk());
    }

    @Test
    void getAncher() throws ShipException {
        s1.createPosition(3, 6);
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
    void getPosition() throws ShipException {
        s1.createPosition(0, 0, true);
        s3.createPosition(4, 7);
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