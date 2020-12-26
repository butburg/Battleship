package view;

import field.Ocean;
import ship.Ship;
import ship.ShipImpl;

import java.io.IOException;
import java.io.PrintStream;

/**
 * @author Edwin W (HTW) on Dez 2020
 */
public class BattleshipPrintStreamView implements PrintStreamView {
    private final Ocean ocean;

    public BattleshipPrintStreamView(Ocean ocean) {
        this.ocean = ocean;
    }


    @Override
    public void print(PrintStream ps) {
        for (int v = 0; v < ocean.getSize(); v++) {
            ps.print(v + " ");
            for (int h = 0; h < ocean.getSize(); h++) {
                Ship[][] field = this.ocean.getField();
                if (field[h][v] == null) {
                    System.out.print(" - ");
                } else {
                    ShipImpl s = (ShipImpl) field[h][v];
                    if (s.getHurt()[0] == true) { System.out.print(" X "); } else {
                        ps.print(" O ");
                    }
                }
            }
            ps.print("\n");
        }
    }
}

