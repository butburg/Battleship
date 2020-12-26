package view;

import field.Ocean;

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
    public void print(PrintStream ps) throws IOException {
        System.out.println("SEE THE FIELD HERE FROM YOUR SHIPS AND FALLEN BOMBS OF ENEMY");
        System.out.println("SEE THE FIELD HERE FROM YOUR SHIPS AND FALLEN BOMBS OF ENEMY");
        System.out.println("SEE THE FIELD HERE FROM YOUR SHIPS AND FALLEN BOMBS OF ENEMY");
        if (false) throw new IOException();
    }
}
