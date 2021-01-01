package view;

import java.io.IOException;
import java.io.PrintStream;

/**
 * @author Edwin W (HTW) on Dez 2020
 */
public interface PrintStreamView {
    /**
     *
     * @param ps
     * @throws IOException
     */
    void printLarge(PrintStream ps) throws IOException;
    /**
     *
     * @param ps
     * @throws IOException
     */
    void printSmall(PrintStream ps) throws IOException;
}
