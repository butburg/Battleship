package field;

import ship.Ship;

/**
 * @author Edwin W (570900) on Nov 2020
 * This is an implementation for the ocean. It will be 11x11 squares in size.
 */
public class OceanImpl implements Ocean {

    private final int[][] field;

    public OceanImpl(int size) {
        this.field = new int[size][size];
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void placeShipPart(Ship ship, int x, int y, boolean vertical) {

    }


}
