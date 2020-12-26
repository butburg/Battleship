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
    void print(PrintStream ps) throws IOException;
}
