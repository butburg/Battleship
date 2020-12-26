package game;

import exceptions.*;
import field.Coordinate;
import field.Ocean;
import field.OceanImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ship.Shipmodel;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Edwin W (HTW) on Nov 2020
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
    Ocean ocean;

    public static final String PNAME1 = "Edwin";
    public static final String PNAME2 = "Pia";
    public static final String PNAME3 = "Kevin";


    private Coordinate p_0_0;
    private Coordinate p_2_2;
    private Coordinate p_10_1;
    private Coordinate p_6_9;
    private Coordinate p_1_0;
    private Coordinate p_max_max;
    private int oceanSize;

    @BeforeEach
    void setUp() throws ShipException {
        bs = new BattleshipImpl("Edwin");
        ocean = new OceanImpl(11);
        oceanSize = ocean.getSize();

        p_0_0 = new Coordinate(0, 0);
        p_2_2 = new Coordinate(2, 2);
        p_10_1 = new Coordinate(10, 1);
        p_6_9 = new Coordinate(6, 9);
        p_1_0 = new Coordinate(1, 0);
        p_max_max = new Coordinate(oceanSize - 1, oceanSize - 1);

    }

    @Test
    void checkPhaseChanges() {
        assertEquals(Phase.SETSHIPS, ((BattleshipImpl) bs).getPhase());
    }

    @Test
    void getPlayers() throws BattleshipException, PhaseException {
        assertArrayEquals(new String[]{PNAME1, PNAME2}, ((BattleshipImpl) bs).getPlayers());
    }


    @Nested
    public class PhaseSetShips {
        @BeforeEach
        public void setUp() throws BattleshipException, PhaseException {

        }

        @Test
        void setShipBorderGood() throws BattleshipException, PhaseException, ShipException, OceanException {
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.BATTLESHIP, new Coordinate(6, 0)));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.CRUISER, new Coordinate(0, 5)));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.CRUISER, new Coordinate(0, 7), true));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(3, 0), true));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(8, 8)));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(8, 10)));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(0, 0)));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(9, 2)));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(3, 10)));
            assertEquals(false, bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(6, 9), true));


            assertEquals(true, bs.setShip(PNAME2, Shipmodel.BATTLESHIP, new Coordinate(6, 0)));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.CRUISER, new Coordinate(0, 5)));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.CRUISER, new Coordinate(0, 7), true));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.DESTROYER, new Coordinate(3, 0), true));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.DESTROYER, new Coordinate(8, 8)));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.DESTROYER, new Coordinate(8, 10)));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.SUBMARINE, new Coordinate(0, 0)));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.SUBMARINE, new Coordinate(9, 2)));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.SUBMARINE, new Coordinate(3, 10)));
            assertEquals(false, bs.setShip(PNAME2, Shipmodel.SUBMARINE, new Coordinate(6, 9), true));
        }

        @Test
        void setShipGood() throws BattleshipException, PhaseException, OceanException, ShipException {
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.BATTLESHIP, new Coordinate(6, 0)));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.CRUISER, new Coordinate(0, 5)));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.CRUISER, new Coordinate(0, 7), true));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(3, 0), true));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(8, 8)));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(8, 10)));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(0, 0)));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(9, 2)));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(3, 10)));
            assertEquals(false, bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(6, 9), true));


            assertEquals(true, bs.setShip(PNAME2, Shipmodel.BATTLESHIP, new Coordinate(6, 0)));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.CRUISER, new Coordinate(0, 5)));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.CRUISER, new Coordinate(0, 7), true));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.DESTROYER, new Coordinate(3, 0), true));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.DESTROYER, new Coordinate(8, 8)));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.DESTROYER, new Coordinate(8, 10)));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.SUBMARINE, new Coordinate(0, 0)));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.SUBMARINE, new Coordinate(9, 2)));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.SUBMARINE, new Coordinate(3, 10)));
            assertEquals(false, bs.setShip(PNAME2, Shipmodel.SUBMARINE, new Coordinate(6, 9), true));
        }

        @Test
        void setShipWithFailsBetween() throws BattleshipException, PhaseException, OceanException, ShipException {
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.BATTLESHIP, new Coordinate(6, 0)));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.CRUISER, new Coordinate(0, 5)));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.CRUISER, new Coordinate(0, 7), true));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(3, 0), true));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(8, 8)));
            assertThrows(OceanException.class, () -> bs.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(8, 8)));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(8, 10)));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(0, 0)));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(9, 2)));
            assertThrows(OceanException.class, () -> bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(9, 2)));
            assertEquals(true, bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(3, 10)));
            assertEquals(false, bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(6, 9), true));


            assertEquals(true, bs.setShip(PNAME2, Shipmodel.BATTLESHIP, new Coordinate(6, 0)));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.CRUISER, new Coordinate(0, 5)));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.CRUISER, new Coordinate(0, 7), true));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.DESTROYER, new Coordinate(3, 0), true));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.DESTROYER, new Coordinate(8, 8)));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.DESTROYER, new Coordinate(8, 10)));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.SUBMARINE, new Coordinate(0, 0)));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.SUBMARINE, new Coordinate(9, 2)));
            assertEquals(true, bs.setShip(PNAME2, Shipmodel.SUBMARINE, new Coordinate(3, 10)));
            assertEquals(false, bs.setShip(PNAME2, Shipmodel.SUBMARINE, new Coordinate(6, 9), true));
        }

        @Test
        void setShipWrongPlayer() throws BattleshipException, PhaseException {
            Throwable e1 = assertThrows(BattleshipException.class, () -> bs.setShip(PNAME3, Shipmodel.BATTLESHIP, new Coordinate(6, 5)));
            assertEquals(ExceptionMsg.bs_wrongPlayer, e1.getMessage());
        }


        @Test
        void setShipOutside() throws ShipException, BattleshipException, PhaseException {
            Throwable e1 = assertThrows(OceanException.class, () -> bs.setShip(PNAME1, Shipmodel.BATTLESHIP, new Coordinate(15, 1)));
            assertEquals(ExceptionMsg.oc_shipOutside, e1.getMessage());

            Throwable e2 = assertThrows(OceanException.class, () -> bs.setShip(PNAME1, Shipmodel.CRUISER, new Coordinate(1, 15)));
            assertEquals(ExceptionMsg.oc_shipOutside, e2.getMessage());

            Throwable e3 = assertThrows(OceanException.class, () -> bs.setShip(PNAME1, Shipmodel.CRUISER, new Coordinate(15, 16)));
            assertEquals(ExceptionMsg.oc_shipOutside, e3.getMessage());
        }

        @Test
        void setShipColliding() throws ShipException, BattleshipException, PhaseException, OceanException {
            bs.setShip(PNAME1, Shipmodel.BATTLESHIP, new Coordinate(1, 1), false);
            bs.setShip(PNAME1, Shipmodel.CRUISER, new Coordinate(1, 7), false);
            bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(8, 8), true);

            Throwable e = assertThrows(OceanException.class, () -> bs.setShip(PNAME1, Shipmodel.CRUISER, new Coordinate(5, 1), false));
            assertEquals(ExceptionMsg.oc_shipCollidingPosition, e.getMessage());
            e = assertThrows(OceanException.class, () -> bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(4, 6), true));
            assertEquals(ExceptionMsg.oc_shipCollidingPosition, e.getMessage());
            e = assertThrows(OceanException.class, () -> bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(0, 7), false));
            assertEquals(ExceptionMsg.oc_shipCollidingPosition, e.getMessage());
            e = assertThrows(OceanException.class, () -> bs.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(3, 0), true));
            assertEquals(ExceptionMsg.oc_shipCollidingPosition, e.getMessage());
            e = assertThrows(OceanException.class, () -> bs.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(7, 9), false));
            assertEquals(ExceptionMsg.oc_shipCollidingPosition, e.getMessage());
        }

        @Nested
        public class MoreShipsThanAllowed {
            @Test
            void setBattleshipToOften() throws ShipException, BattleshipException, PhaseException, OceanException {
                assertEquals(true, bs.setShip(PNAME1, Shipmodel.BATTLESHIP, new Coordinate(6, 0)));
                Throwable e1 = assertThrows(BattleshipException.class, () -> bs.setShip(PNAME1, Shipmodel.BATTLESHIP, new Coordinate(6, 4)));
                assertEquals(ExceptionMsg.bs_shipTypeAllSet, e1.getMessage());
            }

            @Test
            void setCruisersToOften() throws ShipException, BattleshipException, PhaseException, OceanException {
                assertEquals(true, bs.setShip(PNAME1, Shipmodel.CRUISER, new Coordinate(0, 5)));
                assertEquals(true, bs.setShip(PNAME1, Shipmodel.CRUISER, new Coordinate(0, 7), true));

                Throwable e1 = assertThrows(BattleshipException.class, () -> bs.setShip(PNAME1, Shipmodel.CRUISER, new Coordinate(6, 4)));
                assertEquals(ExceptionMsg.bs_shipTypeAllSet, e1.getMessage());
            }

            @Test
            void setDestroyersToOften() throws ShipException, BattleshipException, PhaseException, OceanException {
                assertEquals(true, bs.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(3, 0), true));
                assertEquals(true, bs.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(8, 8)));
                assertEquals(true, bs.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(8, 10)));
                Throwable e1 = assertThrows(BattleshipException.class, () -> bs.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(6, 4)));
                assertEquals(ExceptionMsg.bs_shipTypeAllSet, e1.getMessage());
            }

            @Test
            void setSubmarinesToOften() throws ShipException, BattleshipException, PhaseException, OceanException {
                assertEquals(true, bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(0, 0)));
                assertEquals(true, bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(9, 2)));
                assertEquals(true, bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(3, 10)));
                assertEquals(true, bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(6, 9), true));
                Throwable e1 = assertThrows(BattleshipException.class, () -> bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(6, 4)));
                assertEquals(ExceptionMsg.bs_shipTypeAllSet, e1.getMessage());
            }


            @Test
            void setShipsToOftenNotInRow() throws ShipException, BattleshipException, PhaseException, OceanException {
                assertEquals(true, bs.setShip(PNAME1, Shipmodel.BATTLESHIP, new Coordinate(6, 0)));
                assertEquals(true, bs.setShip(PNAME1, Shipmodel.CRUISER, new Coordinate(0, 5)));
                assertEquals(true, bs.setShip(PNAME1, Shipmodel.CRUISER, new Coordinate(0, 7), true));
                assertEquals(true, bs.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(3, 0), true));
                assertEquals(true, bs.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(8, 8)));
                assertEquals(true, bs.setShip(PNAME1, Shipmodel.DESTROYER, new Coordinate(8, 10)));
                assertEquals(true, bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(0, 0)));
                assertEquals(true, bs.setShip(PNAME1, Shipmodel.SUBMARINE, new Coordinate(9, 2)));
                Throwable e1 = assertThrows(BattleshipException.class, () -> bs.setShip(PNAME1, Shipmodel.CRUISER, new Coordinate(6, 4)));
                assertEquals(ExceptionMsg.bs_shipTypeAllSet, e1.getMessage());
            }

            @Test
            void setMoreShipsThanGameOwns() throws ShipException, BattleshipException, PhaseException, OceanException {
                setShipsDefaultOnePlayer(PNAME1);
                //set 11th ship
                Throwable e1 = assertThrows(BattleshipException.class, () -> bs.setShip(PNAME1, Shipmodel.CRUISER, new Coordinate(6, 4)));
                assertEquals(ExceptionMsg.bs_shipAllSet, e1.getMessage());
            }

        }

        @Nested
        public class WrongPhase {
            @Test
            void attackWrongPhase1() throws BattleshipException, PhaseException, OceanException, ShipException {
                bs.setShip(PNAME1, Shipmodel.BATTLESHIP, new Coordinate(6, 5));
                bs.setShip(PNAME1, Shipmodel.CRUISER, new Coordinate(0, 5));

                Throwable e = assertThrows(PhaseException.class, () -> bs.attack(PNAME1, p_0_0));
                assertEquals(ExceptionMsg.ph_wrongPhase, e.getMessage());
            }

            @Test
            void attackWrongPhase2() throws BattleshipException, PhaseException {
                Throwable e = assertThrows(PhaseException.class, () -> bs.attack(PNAME1, p_0_0));
                assertEquals(ExceptionMsg.ph_wrongPhase, e.getMessage());
            }
        }
    }

    @Nested
    public class PhaseAttack {
        @BeforeEach
        public void setUp() throws ShipException, OceanException, BattleshipException, PhaseException {

            setShipsDefault();
        }

        @Test
        void attackGood() throws BattleshipException, PhaseException, ShipException, OceanException {
            assertEquals(Result.HIT, bs.attack(PNAME2, p_0_0));
            assertEquals(Result.MISSED, bs.attack(PNAME1, p_10_1));
            assertEquals(Result.HIT, bs.attack(PNAME2, p_6_9));
            assertEquals(Result.HIT, bs.attack(PNAME1, p_1_0));
            assertEquals(Result.SINK, bs.attack(PNAME2, p_1_0));
        }

        @Test
        void attackWrongPlayer() throws BattleshipException, PhaseException {
            Throwable e2 = assertThrows(BattleshipException.class, () -> bs.attack(PNAME3, p_2_2));
            assertEquals(ExceptionMsg.bs_wrongPlayer, e2.getMessage());
        }

        @Test
        void attackWrongTurn1() throws BattleshipException, PhaseException, ShipException, OceanException {
            assertEquals(Result.HIT, bs.attack(PNAME2, p_0_0));
            assertEquals(Result.MISSED, bs.attack(PNAME1, p_10_1));
            Throwable e2 = assertThrows(BattleshipException.class, () -> bs.attack(PNAME1, p_6_9));
            assertEquals(ExceptionMsg.bs_wrongTurn1, e2.getMessage());
        }

        @Test
        void attackWrongTurn2() throws BattleshipException, PhaseException, ShipException, OceanException {
            assertEquals(Result.HIT, bs.attack(PNAME2, p_0_0));
            assertEquals(Result.MISSED, bs.attack(PNAME1, p_10_1));
            assertEquals(Result.SINK, bs.attack(PNAME2, p_1_0));
            Throwable e2 = assertThrows(BattleshipException.class, () -> bs.attack(PNAME2, p_1_0));
            assertEquals(ExceptionMsg.bs_wrongTurn2, e2.getMessage());
        }

        @Nested
        public class OutsideOcean {
            @Test
            void attackOutsideOcean1() throws BattleshipException, PhaseException {
                Throwable e = assertThrows(OceanException.class, () -> bs.attack(PNAME2, new Coordinate(-2, 3)));
                assertEquals(ExceptionMsg.oc_attackOutside, e.getMessage());
            }

            @Test
            void attackOutsideOcean2() throws BattleshipException, PhaseException, ShipException, OceanException {
                assertEquals(Result.HIT, bs.attack(PNAME2, p_0_0));
                assertEquals(Result.MISSED, bs.attack(PNAME1, p_10_1));
                Throwable e = assertThrows(OceanException.class, () -> bs.attack(PNAME2, new Coordinate(2, -3)));
                assertEquals(ExceptionMsg.oc_attackOutside, e.getMessage());
            }

            @Test
            void attackOutsideOcean3() throws BattleshipException, PhaseException, ShipException, OceanException {
                assertEquals(Result.HIT, bs.attack(PNAME2, p_0_0));
                assertEquals(Result.MISSED, bs.attack(PNAME1, p_10_1));
                Throwable e = assertThrows(OceanException.class, () -> bs.attack(PNAME2, new Coordinate(1, 15)));
                assertEquals(ExceptionMsg.oc_attackOutside, e.getMessage());
            }

            @Test
            void attackOutsideOcean4() throws BattleshipException, PhaseException {
                Throwable e = assertThrows(OceanException.class, () -> bs.attack(PNAME2, new Coordinate(22, 3)));
                assertEquals(ExceptionMsg.oc_attackOutside, e.getMessage());
            }

            @Test
            void attackOutsideOceanEdgeX() throws BattleshipException, PhaseException, ShipException, OceanException {
                assertEquals(Result.HIT, bs.attack(PNAME2, p_max_max));
                Throwable e = assertThrows(OceanException.class, () -> bs.attack(PNAME1, new Coordinate(oceanSize + 1, oceanSize)));
                assertEquals(ExceptionMsg.oc_attackOutside, e.getMessage());
            }

            @Test
            void attackOutsideOceanEdgeY() throws BattleshipException, PhaseException, ShipException, OceanException {
                assertEquals(Result.HIT, bs.attack(PNAME2, p_max_max));
                Throwable e = assertThrows(OceanException.class, () -> bs.attack(PNAME1, new Coordinate(oceanSize, oceanSize + 1)));
                assertEquals(ExceptionMsg.oc_attackOutside, e.getMessage());
            }
        }

        @Nested
        public class WrongPhase {
            @Test
            void setShipWrongPhase1() throws BattleshipException, PhaseException, ShipException, OceanException {
                bs.attack(PNAME2, p_0_0);

                Throwable e = assertThrows(PhaseException.class, () -> bs.setShip(PNAME1, Shipmodel.BATTLESHIP, new Coordinate(6, 5)));
                assertEquals(ExceptionMsg.ph_wrongPhase, e.getMessage());
            }

            @Test
            void setShipWrongPhase2() throws BattleshipException, PhaseException {
                Throwable e = assertThrows(PhaseException.class, () -> bs.setShip(PNAME2, Shipmodel.BATTLESHIP, new Coordinate(6, 5)));
                assertEquals(ExceptionMsg.ph_wrongPhase, e.getMessage());
            }
        }
    }

    @Nested
    public class WinScenarios {
        @BeforeEach
        public void setUp() throws ShipException, OceanException, BattleshipException, PhaseException {

            setShipsDefault();
        }

        @Test
        void winGood() throws ShipException, PhaseException, BattleshipException, OceanException {
            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(0, 0)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(0, 0)));
            assertEquals(Result.SINK, bs.attack(PNAME2, new Coordinate(1, 0)));
            assertEquals(Result.SINK, bs.attack(PNAME1, new Coordinate(1, 0)));

            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(3, 0)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(3, 0)));
            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(6, 0)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(6, 0)));
            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(7, 0)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(7, 0)));
            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(8, 0)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(8, 0)));
            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(9, 0)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(9, 0)));
            assertEquals(Result.SINK, bs.attack(PNAME2, new Coordinate(10, 0)));
            assertEquals(Result.SINK, bs.attack(PNAME1, new Coordinate(10, 0)));

            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(3, 1)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(3, 1)));
            assertEquals(Result.SINK, bs.attack(PNAME2, new Coordinate(3, 2)));
            assertEquals(Result.SINK, bs.attack(PNAME1, new Coordinate(3, 2)));

            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(9, 2)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(9, 2)));
            assertEquals(Result.SINK, bs.attack(PNAME2, new Coordinate(10, 2)));
            assertEquals(Result.SINK, bs.attack(PNAME1, new Coordinate(10, 2)));

            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(0, 5)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(0, 5)));
            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(1, 5)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(1, 5)));
            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(2, 5)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(2, 5)));
            assertEquals(Result.SINK, bs.attack(PNAME2, new Coordinate(3, 5)));
            assertEquals(Result.SINK, bs.attack(PNAME1, new Coordinate(3, 5)));

            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(0, 7)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(0, 7)));
            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(0, 8)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(0, 8)));
            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(0, 9)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(0, 9)));
            assertEquals(Result.SINK, bs.attack(PNAME2, new Coordinate(0, 10)));
            assertEquals(Result.SINK, bs.attack(PNAME1, new Coordinate(0, 10)));

            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(8, 8)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(8, 8)));
            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(9, 8)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(9, 8)));
            assertEquals(Result.SINK, bs.attack(PNAME2, new Coordinate(10, 8)));
            assertEquals(Result.SINK, bs.attack(PNAME1, new Coordinate(10, 8)));

            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(3, 10)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(3, 10)));
            assertEquals(Result.SINK, bs.attack(PNAME2, new Coordinate(4, 10)));
            assertEquals(Result.SINK, bs.attack(PNAME1, new Coordinate(4, 10)));

            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(8, 10)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(8, 10)));
            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(9, 10)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(9, 10)));
            assertEquals(Result.SINK, bs.attack(PNAME2, new Coordinate(10, 10)));
            assertEquals(Result.SINK, bs.attack(PNAME1, new Coordinate(10, 10)));

            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(6, 9)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(6, 9)));
            assertEquals(Result.WIN, bs.attack(PNAME2, new Coordinate(6, 10)));
        }

        @Test
        void winGoodP2() throws ShipException, PhaseException, BattleshipException, OceanException {
            oneShipLeftToSink();
            assertEquals(Result.MISSED, bs.attack(PNAME2, new Coordinate(6, 8)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(6, 9)));
            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(6, 9)));
            assertEquals(Result.WIN, bs.attack(PNAME1, new Coordinate(6, 10)));
        }

        @Test
        void lastAttack() throws ShipException, PhaseException, BattleshipException, OceanException {
            oneShipLeftToSink();
            assertEquals(Result.MISSED, bs.attack(PNAME2, new Coordinate(6, 8)));
            assertEquals(Result.HIT, bs.attack(PNAME1, new Coordinate(6, 9)));
            assertEquals(Result.HIT, bs.attack(PNAME2, new Coordinate(6, 9)));
            assertEquals(Result.WIN, bs.attack(PNAME1, new Coordinate(6, 10)));
            Throwable e = assertThrows(PhaseException.class, () -> bs.attack(PNAME2, new Coordinate(6, 10)));
            assertEquals(ExceptionMsg.ph_wrongPhase, e.getMessage());
        }

    }


    void setShipsDefault() throws ShipException, BattleshipException, PhaseException, OceanException {
        setShipsDefaultOnePlayer(PNAME1);
        setShipsDefaultOnePlayer(PNAME2);
    }


    private void setShipsDefaultOnePlayer(String player) throws BattleshipException, OceanException, PhaseException, ShipException {
        bs.setShip(player, Shipmodel.BATTLESHIP, new Coordinate(6, 0));
        bs.setShip(player, Shipmodel.CRUISER, new Coordinate(0, 5));
        bs.setShip(player, Shipmodel.CRUISER, new Coordinate(0, 7), true);
        bs.setShip(player, Shipmodel.DESTROYER, new Coordinate(3, 0), true);
        bs.setShip(player, Shipmodel.DESTROYER, new Coordinate(8, 8));
        bs.setShip(player, Shipmodel.DESTROYER, new Coordinate(8, 10));
        bs.setShip(player, Shipmodel.SUBMARINE, new Coordinate(0, 0));
        bs.setShip(player, Shipmodel.SUBMARINE, new Coordinate(9, 2));
        bs.setShip(player, Shipmodel.SUBMARINE, new Coordinate(3, 10));
        bs.setShip(player, Shipmodel.SUBMARINE, new Coordinate(6, 9), true);
    }

    private void oneShipLeftToSink() throws ShipException, PhaseException, BattleshipException, OceanException {
        bs.attack(PNAME2, new Coordinate(0, 0));
        bs.attack(PNAME1, new Coordinate(0, 0));
        bs.attack(PNAME2, new Coordinate(1, 0));
        bs.attack(PNAME1, new Coordinate(1, 0));

        bs.attack(PNAME2, new Coordinate(3, 0));
        bs.attack(PNAME1, new Coordinate(3, 0));
        bs.attack(PNAME2, new Coordinate(6, 0));
        bs.attack(PNAME1, new Coordinate(6, 0));
        bs.attack(PNAME2, new Coordinate(7, 0));
        bs.attack(PNAME1, new Coordinate(7, 0));
        bs.attack(PNAME2, new Coordinate(8, 0));
        bs.attack(PNAME1, new Coordinate(8, 0));
        bs.attack(PNAME2, new Coordinate(9, 0));
        bs.attack(PNAME1, new Coordinate(9, 0));
        bs.attack(PNAME2, new Coordinate(10, 0));
        bs.attack(PNAME1, new Coordinate(10, 0));

        bs.attack(PNAME2, new Coordinate(3, 1));
        bs.attack(PNAME1, new Coordinate(3, 1));
        bs.attack(PNAME2, new Coordinate(3, 2));
        bs.attack(PNAME1, new Coordinate(3, 2));

        bs.attack(PNAME2, new Coordinate(9, 2));
        bs.attack(PNAME1, new Coordinate(9, 2));
        bs.attack(PNAME2, new Coordinate(10, 2));
        bs.attack(PNAME1, new Coordinate(10, 2));

        bs.attack(PNAME2, new Coordinate(0, 5));
        bs.attack(PNAME1, new Coordinate(0, 5));
        bs.attack(PNAME2, new Coordinate(1, 5));
        bs.attack(PNAME1, new Coordinate(1, 5));
        bs.attack(PNAME2, new Coordinate(2, 5));
        bs.attack(PNAME1, new Coordinate(2, 5));
        bs.attack(PNAME2, new Coordinate(3, 5));
        bs.attack(PNAME1, new Coordinate(3, 5));

        bs.attack(PNAME2, new Coordinate(0, 7));
        bs.attack(PNAME1, new Coordinate(0, 7));
        bs.attack(PNAME2, new Coordinate(0, 8));
        bs.attack(PNAME1, new Coordinate(0, 8));
        bs.attack(PNAME2, new Coordinate(0, 9));
        bs.attack(PNAME1, new Coordinate(0, 9));
        bs.attack(PNAME2, new Coordinate(0, 10));
        bs.attack(PNAME1, new Coordinate(0, 10));

        bs.attack(PNAME2, new Coordinate(8, 8));
        bs.attack(PNAME1, new Coordinate(8, 8));
        bs.attack(PNAME2, new Coordinate(9, 8));
        bs.attack(PNAME1, new Coordinate(9, 8));
        bs.attack(PNAME2, new Coordinate(10, 8));
        bs.attack(PNAME1, new Coordinate(10, 8));

        bs.attack(PNAME2, new Coordinate(3, 10));
        bs.attack(PNAME1, new Coordinate(3, 10));
        bs.attack(PNAME2, new Coordinate(4, 10));
        bs.attack(PNAME1, new Coordinate(4, 10));

        bs.attack(PNAME2, new Coordinate(8, 10));
        bs.attack(PNAME1, new Coordinate(8, 10));
        bs.attack(PNAME2, new Coordinate(9, 10));
        bs.attack(PNAME1, new Coordinate(9, 10));
        bs.attack(PNAME2, new Coordinate(10, 10));
        bs.attack(PNAME1, new Coordinate(10, 10));
    }

}