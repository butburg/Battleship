package game;

import exceptions.BattleshipException;
import exceptions.ExceptionMessages;
import exceptions.PhaseException;
import exceptions.ShipException;
import field.Ocean;
import field.OceanImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ship.*;

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
    Ocean ocean;

    public static final String PNAME1 = "Edwin";
    public static final String PNAME2 = "Pia";
    public static final String PNAME3 = "Kevin";


    private Ship ship1, ship2, ship3, ship4, ship5, ship6, ship7, ship8, ship9, ship10;
    private Point p_0_0;
    private Point p_2_2;
    private Point p_10_1;
    private Point p_6_6;
    private Point p_1_0;
    private Point p_max_max;
    private int oceanSize;

    @BeforeEach
    void setUp() throws ShipException {
        bs = new BattleshipImpl();
        ocean = new OceanImpl();
        oceanSize = ocean.getSize();

        //good Ships: inside ocean and right amount of each type, not collidating
        ship1 = new ShipImpl(Shipmodel.BATTLESHIP, 6, 0);
        ship2 = new ShipImpl(Shipmodel.CRUISERS, 0, 5);
        ship3 = new ShipImpl(Shipmodel.CRUISERS, 0, 7, true);
        ship4 = new ShipImpl(Shipmodel.DESTROYERS, 3, 0, true);
        ship5 = new ShipImpl(Shipmodel.DESTROYERS, 8, 8);
        ship6 = new ShipImpl(Shipmodel.DESTROYERS, 8, 10);
        ship7 = new ShipImpl(Shipmodel.SUBMARINES, 0, 0);
        ship8 = new ShipImpl(Shipmodel.SUBMARINES, 9, 2);
        ship9 = new ShipImpl(Shipmodel.SUBMARINES, 3, 10);
        ship10 = new ShipImpl(Shipmodel.SUBMARINES, 6, 9, true);

        p_0_0 = new Point(0, 0);
        p_2_2 = new Point(2, 2);
        p_10_1 = new Point(10, 1);
        p_6_6 = new Point(6, 6);
        p_1_0 = new Point(1, 0);
        p_max_max = new Point(oceanSize, oceanSize);

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
            assertEquals(ExceptionMessages.playerNameTaken, e.getMessage());
        }

        @Test
        void choosePlayer3Player() throws BattleshipException, PhaseException {
            bs.choosePlayer(PNAME1);
            bs.choosePlayer(PNAME2);
            Throwable e = assertThrows(BattleshipException.class, () -> bs.choosePlayer(PNAME3));
            assertEquals(ExceptionMessages.tooManyPlayers, e.getMessage());
        }

        @Test
        void choosePlayer3PlayerSameName() throws BattleshipException, PhaseException {
            bs.choosePlayer(PNAME1);
            bs.choosePlayer(PNAME2);
            Throwable e = assertThrows(BattleshipException.class, () -> bs.choosePlayer(PNAME1));
            assertEquals(ExceptionMessages.tooManyPlayers, e.getMessage());
        }

        @Nested
        public class WrongPhase {
            @Test
            void setShipWrongPhase1() throws BattleshipException, PhaseException {
                bs.choosePlayer(PNAME1);
                Throwable e = assertThrows(PhaseException.class, () -> bs.setShip(PNAME1, ship1));
                assertEquals(ExceptionMessages.wrongPhase, e.getMessage());
            }

            @Test
            void setShipWrongPhase2() throws BattleshipException, PhaseException {
                Throwable e = assertThrows(PhaseException.class, () -> bs.setShip(PNAME1, ship1));
                assertEquals(ExceptionMessages.wrongPhase, e.getMessage());
            }

            @Test
            void attackWrongPhase1() throws BattleshipException, PhaseException {
                bs.choosePlayer(PNAME1);
                Throwable e = assertThrows(PhaseException.class, () -> bs.attack(PNAME1, p_0_0));
                assertEquals(ExceptionMessages.wrongPhase, e.getMessage());
            }

            @Test
            void attackWrongPhase2() throws BattleshipException, PhaseException {
                Throwable e = assertThrows(PhaseException.class, () -> bs.attack(PNAME1, p_0_0));
                assertEquals(ExceptionMessages.wrongPhase, e.getMessage());
            }
        }
    }

    @Nested
    public class PhaseSetShips {
        @BeforeEach
        public void setUp() throws Exception {
            choosePlayerDefault();
        }

        @Test
        void setShipBorderGood() throws BattleshipException, PhaseException, ShipException {
            assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.BATTLESHIP, 6, 0)));
            assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.CRUISERS, 0, 5)));
            assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.CRUISERS, 0, 7, true)));
            assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.DESTROYERS, 3, 0, true)));
            assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.DESTROYERS, 8, 8)));
            assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.DESTROYERS, 8, 10)));
            assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.SUBMARINES, 0, 0)));
            assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.SUBMARINES, 9, 2)));
            assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.SUBMARINES, 3, 10)));
            assertEquals(false, bs.setShip(PNAME1, new ShipImpl(Shipmodel.SUBMARINES, 6, 9, true)));


            assertEquals(true, bs.setShip(PNAME2, new ShipImpl(Shipmodel.BATTLESHIP, 6, 0)));
            assertEquals(true, bs.setShip(PNAME2, new ShipImpl(Shipmodel.CRUISERS, 0, 5)));
            assertEquals(true, bs.setShip(PNAME2, new ShipImpl(Shipmodel.CRUISERS, 0, 7, true)));
            assertEquals(true, bs.setShip(PNAME2, new ShipImpl(Shipmodel.DESTROYERS, 3, 0, true)));
            assertEquals(true, bs.setShip(PNAME2, new ShipImpl(Shipmodel.DESTROYERS, 8, 8)));
            assertEquals(true, bs.setShip(PNAME2, new ShipImpl(Shipmodel.DESTROYERS, 8, 10)));
            assertEquals(true, bs.setShip(PNAME2, new ShipImpl(Shipmodel.SUBMARINES, 0, 0)));
            assertEquals(true, bs.setShip(PNAME2, new ShipImpl(Shipmodel.SUBMARINES, 9, 2)));
            assertEquals(true, bs.setShip(PNAME2, new ShipImpl(Shipmodel.SUBMARINES, 3, 10)));
            assertEquals(false, bs.setShip(PNAME2, new ShipImpl(Shipmodel.SUBMARINES, 6, 9, true)));
        }

        @Test
        void setShipGood() throws BattleshipException, PhaseException {
            for (Ship ship : Arrays.asList(ship1, ship2, ship3, ship4, ship5, ship6, ship7, ship8, ship9)) {
                assertEquals(true, bs.setShip(PNAME1, ship));
            }
            assertEquals(false, bs.setShip(PNAME1, ship10));

            for (Ship ship : Arrays.asList(ship1, ship2, ship3, ship4, ship5, ship6, ship7, ship8, ship9)) {
                assertEquals(true, bs.setShip(PNAME2, ship));
            }
            assertEquals(false, bs.setShip(PNAME2, ship10));
        }

        @Test
        void setShipWrongPlayer() throws BattleshipException, PhaseException {
            Throwable e1 = assertThrows(BattleshipException.class, () -> bs.setShip(PNAME3, ship1));
            assertEquals(ExceptionMessages.wrongPlayer, e1.getMessage());
        }


        @Test
        void setShipOutside() throws ShipException, BattleshipException, PhaseException {
            Ship shipOutside1 = new ShipImpl(Shipmodel.BATTLESHIP, 15, 1);
            Throwable e1 = assertThrows(Exception.class, () -> bs.setShip(PNAME1, shipOutside1));
            assertEquals(ExceptionMessages.shipOutside, e1.getMessage());

            Ship shipOutside2 = new ShipImpl(Shipmodel.BATTLESHIP, 1, 15);
            Throwable e2 = assertThrows(Exception.class, () -> bs.setShip(PNAME1, shipOutside2));
            assertEquals(ExceptionMessages.shipOutside, e2.getMessage());

            Ship shipOutside3 = new ShipImpl(Shipmodel.BATTLESHIP, 15, 16);
            Throwable e3 = assertThrows(Exception.class, () -> bs.setShip(PNAME1, shipOutside3));
            assertEquals(ExceptionMessages.shipOutside, e3.getMessage());
        }

        @Nested
        public class MoreShipsThanAllowed {
            @Test
            void setBattleshipToOften() throws ShipException, BattleshipException, PhaseException {
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.BATTLESHIP, 6, 0)));
                Throwable e1 = assertThrows(Exception.class, () -> bs.setShip(PNAME1, new ShipImpl(Shipmodel.BATTLESHIP, 6, 4)));
                assertEquals(ExceptionMessages.shipAllSet, e1.getMessage());
            }

            @Test
            void setCruisersToOften() throws ShipException, BattleshipException, PhaseException {
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.CRUISERS, 0, 5)));
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.CRUISERS, 0, 7, true)));

                Throwable e1 = assertThrows(Exception.class, () -> bs.setShip(PNAME1, new ShipImpl(Shipmodel.CRUISERS, 6, 4)));
                assertEquals(ExceptionMessages.shipAllSet, e1.getMessage());
            }

            @Test
            void setDestroyersToOften() throws ShipException, BattleshipException, PhaseException {
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.DESTROYERS, 3, 0, true)));
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.DESTROYERS, 8, 8)));
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.DESTROYERS, 8, 10)));
                Throwable e1 = assertThrows(Exception.class, () -> bs.setShip(PNAME1, new ShipImpl(Shipmodel.DESTROYERS, 6, 4)));
                assertEquals(ExceptionMessages.shipAllSet, e1.getMessage());
            }

            @Test
            void setSubmarinesToOften() throws ShipException, BattleshipException, PhaseException {
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.SUBMARINES, 0, 0)));
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.SUBMARINES, 9, 2)));
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.SUBMARINES, 3, 10)));
                assertEquals(false, bs.setShip(PNAME1, new ShipImpl(Shipmodel.SUBMARINES, 6, 9, true)));
                Throwable e1 = assertThrows(Exception.class, () -> bs.setShip(PNAME1, new ShipImpl(Shipmodel.SUBMARINES, 6, 4)));
                assertEquals(ExceptionMessages.shipAllSet, e1.getMessage());
            }


            @Test
            void setShipsToOftenNotInRow() throws ShipException, BattleshipException, PhaseException {
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.BATTLESHIP, 6, 0)));
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.CRUISERS, 0, 5)));
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.CRUISERS, 0, 7, true)));
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.DESTROYERS, 3, 0, true)));
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.DESTROYERS, 8, 8)));
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.DESTROYERS, 8, 10)));
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.SUBMARINES, 0, 0)));
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.SUBMARINES, 9, 2)));
                Throwable e1 = assertThrows(Exception.class, () -> bs.setShip(PNAME1, new ShipImpl(Shipmodel.CRUISERS, 6, 4)));
                assertEquals(ExceptionMessages.shipAllSet, e1.getMessage());
            }

            @Test
            void setMoreShipsThanGameOwns() throws ShipException, BattleshipException, PhaseException {
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.BATTLESHIP, 6, 0)));
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.CRUISERS, 0, 5)));
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.CRUISERS, 0, 7, true)));
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.DESTROYERS, 3, 0, true)));
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.DESTROYERS, 8, 8)));
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.DESTROYERS, 8, 10)));
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.SUBMARINES, 0, 0)));
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.SUBMARINES, 9, 2)));
                assertEquals(true, bs.setShip(PNAME1, new ShipImpl(Shipmodel.SUBMARINES, 3, 10)));
                assertEquals(false, bs.setShip(PNAME1, new ShipImpl(Shipmodel.SUBMARINES, 6, 9, true)));
                //set 11th ship
                Throwable e1 = assertThrows(Exception.class, () -> bs.setShip(PNAME1, new ShipImpl(Shipmodel.CRUISERS, 6, 4)));
                assertEquals(ExceptionMessages.shipAllSet, e1.getMessage());
            }

        }

        @Nested
        public class WrongPhase {
            @Test
            void choosePlayerWrongPhase1() throws BattleshipException, PhaseException {
                bs.setShip(PNAME1, ship1);
                bs.setShip(PNAME1, ship2);

                Throwable e = assertThrows(PhaseException.class, () -> bs.choosePlayer(PNAME3));
                assertEquals(ExceptionMessages.wrongPhase, e.getMessage());
            }

            @Test
            void choosePlayerWrongPhase2() throws BattleshipException, PhaseException {
                Throwable e = assertThrows(PhaseException.class, () -> bs.choosePlayer(PNAME3));
                assertEquals(ExceptionMessages.wrongPhase, e.getMessage());
            }


            @Test
            void attackWrongPhase1() throws BattleshipException, PhaseException {
                bs.setShip(PNAME1, ship1);
                bs.setShip(PNAME1, ship2);

                Throwable e = assertThrows(PhaseException.class, () -> bs.attack(PNAME1, p_0_0));
                assertEquals(ExceptionMessages.wrongPhase, e.getMessage());
            }

            @Test
            void attackWrongPhase2() throws BattleshipException, PhaseException {
                Throwable e = assertThrows(PhaseException.class, () -> bs.attack(PNAME1, p_0_0));
                assertEquals(ExceptionMessages.wrongPhase, e.getMessage());
            }
        }
    }

    @Nested
    public class PhaseAttack {
        @BeforeEach
        public void setUp() throws Exception, ShipException {
            choosePlayerDefault();
            setShipDefaultGood();
        }

        @Test
        void attackGood() throws BattleshipException, PhaseException {
            assertEquals(Result.HIT, bs.attack(PNAME1, p_0_0));
            assertEquals(Result.MISSED, bs.attack(PNAME2, p_10_1));
            assertEquals(Result.HIT, bs.attack(PNAME1, p_6_6));
            assertEquals(Result.SINK, bs.attack(PNAME2, p_1_0));
        }

        @Test
        void attackWrongPlayer() throws BattleshipException, PhaseException {
            Throwable e2 = assertThrows(BattleshipException.class, () -> bs.attack(PNAME3, p_2_2));
            assertEquals(ExceptionMessages.wrongPlayer, e2.getMessage());
        }

        @Test
        void attackWrongTurn1() throws BattleshipException, PhaseException {
            assertEquals(Result.HIT, bs.attack(PNAME1, p_0_0));
            assertEquals(Result.MISSED, bs.attack(PNAME2, p_10_1));
            Throwable e2 = assertThrows(BattleshipException.class, () -> bs.attack(PNAME2, p_6_6));
            assertEquals(ExceptionMessages.wrongTurn, e2.getMessage());
        }

        @Test
        void attackWrongTurn2() throws BattleshipException, PhaseException {
            assertEquals(Result.HIT, bs.attack(PNAME1, p_0_0));
            assertEquals(Result.MISSED, bs.attack(PNAME2, p_10_1));
            assertEquals(Result.HIT, bs.attack(PNAME1, p_6_6));
            Throwable e2 = assertThrows(BattleshipException.class, () -> bs.attack(PNAME1, p_1_0));
            assertEquals(ExceptionMessages.wrongTurn, e2.getMessage());
        }

        @Nested
        public class OutsideOcean {
            @Test
            void attackOutsideOcean1() throws BattleshipException, PhaseException {
                Throwable e = assertThrows(PhaseException.class, () -> bs.attack(PNAME1, new Point(-2, 3)));
                assertEquals(ExceptionMessages.attackOutside, e.getMessage());
            }

            @Test
            void attackOutsideOcean2() throws BattleshipException, PhaseException {
                assertEquals(Result.HIT, bs.attack(PNAME1, p_0_0));
                assertEquals(Result.MISSED, bs.attack(PNAME2, p_10_1));
                Throwable e = assertThrows(PhaseException.class, () -> bs.attack(PNAME1, new Point(2, -3)));
                assertEquals(ExceptionMessages.attackOutside, e.getMessage());
            }

            @Test
            void attackOutsideOcean3() throws BattleshipException, PhaseException {
                assertEquals(Result.HIT, bs.attack(PNAME1, p_0_0));
                assertEquals(Result.MISSED, bs.attack(PNAME2, p_10_1));
                Throwable e = assertThrows(PhaseException.class, () -> bs.attack(PNAME1, new Point(1, 15)));
                assertEquals(ExceptionMessages.attackOutside, e.getMessage());
            }

            @Test
            void attackOutsideOcean4() throws BattleshipException, PhaseException {
                Throwable e = assertThrows(PhaseException.class, () -> bs.attack(PNAME1, new Point(22, 3)));
                assertEquals(ExceptionMessages.attackOutside, e.getMessage());
            }

            @Test
            void attackOutsideOceanEdgeX() throws BattleshipException, PhaseException {
                assertEquals(Result.HIT, bs.attack(PNAME1, p_max_max));
                Throwable e = assertThrows(PhaseException.class, () -> bs.attack(PNAME2, new Point(oceanSize + 1, oceanSize)));
                assertEquals(ExceptionMessages.attackOutside, e.getMessage());
            }

            @Test
            void attackOutsideOceanEdgeY() throws BattleshipException, PhaseException {
                assertEquals(Result.HIT, bs.attack(PNAME1, p_max_max));
                Throwable e = assertThrows(PhaseException.class, () -> bs.attack(PNAME2, new Point(oceanSize, oceanSize + 1)));
                assertEquals(ExceptionMessages.attackOutside, e.getMessage());
            }
        }

        @Nested
        public class WrongPhase {
            @Test
            void choosePlayerWrongPhase1() throws BattleshipException, PhaseException {
                bs.attack(PNAME1, p_0_0);

                Throwable e = assertThrows(PhaseException.class, () -> bs.choosePlayer(PNAME3));
                assertEquals(ExceptionMessages.wrongPhase, e.getMessage());
            }

            @Test
            void choosePlayerWrongPhase2() throws BattleshipException, PhaseException {
                Throwable e = assertThrows(PhaseException.class, () -> bs.choosePlayer(PNAME2));
                assertEquals(ExceptionMessages.wrongPhase, e.getMessage());
            }

            @Test
            void setShipWrongPhase1() throws BattleshipException, PhaseException {
                bs.attack(PNAME1, p_0_0);

                Throwable e = assertThrows(PhaseException.class, () -> bs.setShip(PNAME1, ship1));
                assertEquals(ExceptionMessages.wrongPhase, e.getMessage());
            }

            @Test
            void setShipWrongPhase2() throws BattleshipException, PhaseException {
                Throwable e = assertThrows(PhaseException.class, () -> bs.setShip(PNAME1, ship1));
                assertEquals(ExceptionMessages.wrongPhase, e.getMessage());
            }
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

    void setShipDefaultGood() throws ShipException, BattleshipException, PhaseException {
        bs.setShip(PNAME1, new ShipImpl(Shipmodel.BATTLESHIP, 6, 0));
        bs.setShip(PNAME1, new ShipImpl(Shipmodel.CRUISERS, 0, 5));
        bs.setShip(PNAME1, new ShipImpl(Shipmodel.CRUISERS, 0, 7, true));
        bs.setShip(PNAME1, new ShipImpl(Shipmodel.DESTROYERS, 3, 0, true));
        bs.setShip(PNAME1, new ShipImpl(Shipmodel.DESTROYERS, 8, 8));
        bs.setShip(PNAME1, new ShipImpl(Shipmodel.DESTROYERS, 8, 10));
        bs.setShip(PNAME1, new ShipImpl(Shipmodel.SUBMARINES, 0, 0));
        bs.setShip(PNAME1, new ShipImpl(Shipmodel.SUBMARINES, 9, 2));
        bs.setShip(PNAME1, new ShipImpl(Shipmodel.SUBMARINES, 3, 10));
        bs.setShip(PNAME1, new ShipImpl(Shipmodel.SUBMARINES, 6, 9, true));


        bs.setShip(PNAME2, new ShipImpl(Shipmodel.BATTLESHIP, 6, 0));
        bs.setShip(PNAME2, new ShipImpl(Shipmodel.CRUISERS, 0, 5));
        bs.setShip(PNAME2, new ShipImpl(Shipmodel.CRUISERS, 0, 7, true));
        bs.setShip(PNAME2, new ShipImpl(Shipmodel.DESTROYERS, 3, 0, true));
        bs.setShip(PNAME2, new ShipImpl(Shipmodel.DESTROYERS, 8, 8));
        bs.setShip(PNAME2, new ShipImpl(Shipmodel.DESTROYERS, 8, 10));
        bs.setShip(PNAME2, new ShipImpl(Shipmodel.SUBMARINES, 0, 0));
        bs.setShip(PNAME2, new ShipImpl(Shipmodel.SUBMARINES, 9, 2));
        bs.setShip(PNAME2, new ShipImpl(Shipmodel.SUBMARINES, 3, 10));
        bs.setShip(PNAME2, new ShipImpl(Shipmodel.SUBMARINES, 6, 9, true));

    }
}