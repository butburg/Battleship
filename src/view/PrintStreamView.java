package view;

import exceptions.ShipException;

import java.io.PrintStream;

/**
 * @author Edwin W (HTW) on Dez 2020
 */
public interface PrintStreamView {
    /**
     * @param out PrintStream that will wait for the Stream
     * @throws ShipException when the ship get bad addressed
     */
    void printOcean(PrintStream out) throws ShipException;

    /**
     * @param out PrintStream that will wait for the Stream
     */
    void printAttack(PrintStream out);
}
