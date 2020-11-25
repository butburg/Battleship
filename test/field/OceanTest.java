package field;

import exceptions.ExceptionMsg;
import exceptions.OceanException;
import exceptions.ShipException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ship.Ship;
import ship.ShipImpl;
import ship.Shipmodel;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Edwin W (HTW) on Nov 2020
 */
class OceanTest {

    private Ship s1;
    private Ship s2;
    private Ship s3;
    private Ship s4;
    private Ocean ocean;

    @BeforeEach
    void setUp() throws OceanException, ShipException {
        s1 = new ShipImpl(Shipmodel.BATTLESHIP);
        s2 = new ShipImpl(Shipmodel.CRUISERS);
        s3 = new ShipImpl(Shipmodel.DESTROYERS);
        s4 = new ShipImpl(Shipmodel.SUBMARINES);
        ocean.placeShipPart(s1, 0, 0, true);
        ocean.placeShipPart(s2, 2, 4, false);
        ocean.placeShipPart(s3, 4, 7, false);
        ocean.placeShipPart(s4, 2, 6, true);
    }

    
    @Test
    void getSize() {
    }

    @Test
    void placeShipPart() {
    }

    //TODO belongs to the ocean test
    @Test
    void placeShipPartNegative() {
        Throwable e1 = assertThrows(OceanException.class, () -> ocean.placeShipPart(s1, -1, 0, true));
        Throwable e2 = assertThrows(OceanException.class, () -> ocean.placeShipPart(s2, 1, -10, true));
        Throwable e3 = assertThrows(OceanException.class, () -> ocean.placeShipPart(s3, -1, -20, false));
        Throwable e4 = assertThrows(OceanException.class, () -> ocean.placeShipPart(s4, -3, 0, false));
        for (Throwable e : Arrays.asList(e1, e2, e3, e4))
            assertEquals(ExceptionMsg.oc_shipOutside, e.getMessage());
    }
    

    @Test
    void bombAt() {
    }
}