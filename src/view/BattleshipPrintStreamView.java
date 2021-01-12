package view;

import exceptions.BattleshipException;
import exceptions.ShipException;
import ship.Ship;
import ship.ShipImpl;

import java.io.PrintStream;

/**
 * @author Edwin W (HTW) on Dez 2020
 */
public class BattleshipPrintStreamView implements PrintStreamView {
    private int size;
    private Ship[][] shipFields;
    private boolean[][] attackFields;

    public BattleshipPrintStreamView(Ship[][] shipFields, boolean[][] attackFields) throws BattleshipException {
        this.shipFields = shipFields;
        this.attackFields = attackFields;
        if (shipFields.length != attackFields.length) throw new BattleshipException("The ship and ocean field should be the same size!");
        this.size = shipFields.length;
    }


    @Override
    public void printLarge(PrintStream ps) throws ShipException {
        //print your ocean with ships
        scala(ps, "  ", " | ");
        ps.println();
        shipField(ps, " X |", "   |", " O |", false);
        lineHorizont(ps);

        //print the attacked field
        scala(ps, "  ", " | ");
        ps.println();
        attackField(ps, " X |", "   |", false);
        lineHorizont(ps);
        scala(ps, "  ", " | ");
        ps.println();

    }


    @Override
    public void printSmall(PrintStream ps) throws ShipException {
        //print your ocean with ships
        scala(ps, " ", "  ");
        ps.println();
        shipField(ps, " X ", " ~ ", " O ", true);
        scala(ps, " ", "  ");
        ps.println();

        //print the attacked field
        attackField(ps, " X ", " ~ ", true);
        scala(ps, " ", "  ");
        ps.println();
    }


    private void shipField(PrintStream ps, String hitfield, String emptyfield, String okfield, boolean small) throws ShipException {
        for (int y = 0; y < size; y++) {
            if (!small) {
                lineHorizont(ps);
                ps.printf("%2d |", y);
            } else {
                ps.printf("%2d", y);
            }
            for (int h = 0; h < size; h++) {

                if (shipFields[h][y] == null) {
                    ps.print(emptyfield);
                } else {
                    ShipImpl s = (ShipImpl) shipFields[h][y];
                    if (s.getHurtAt(h,y)) { ps.print(hitfield); } else {
                        ps.print(okfield);
                    }
                }
            }
            ps.printf("%2d", y);
            ps.print("\n");
        }
    }

    private void attackField(PrintStream ps, String hitfield, String emptyfield, boolean small) {
        for (int y = 0; y < size; y++) {
            if (!small) {
                lineHorizont(ps);
                ps.printf("%2d |", y);
            } else {
                ps.printf("%2d", y);
            }
            for (int h = 0; h < size; h++) {

                ps.print(attackFields[h][y] ? hitfield : emptyfield);
            }
            ps.printf("%2d", y);
            ps.print("\n");
        }
    }

    private void lineHorizont(PrintStream ps) {
        ps.print("---");
        for (int i = 0; i <= size; i++) {
            ps.print("|---");
        }
        ps.println();
    }

    private void scala(PrintStream ps, String s2, String s3) {
        ps.print(s2);
        for (int x = 0; x < size; x++) {
            ps.print(s3 + x);
        }
    }

}


