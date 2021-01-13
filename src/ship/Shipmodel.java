package ship;

/**
 * @author Edwin W (HTW) on Nov 2020
 * The different types of ship, that exists in the game and their specific size!
 */
public enum Shipmodel {
    /**
     * usually 2 Submarines(size: 1)
     */
    SUBMARINE(1),  //2x
    /**
     * usually 2 Destroyers(size: 2)
     */
    DESTROYER(2),  //2x
    /**
     * usually 1 Cruisers(size: 3)
     */
    CRUISER(3),    //1x
    /**
     * usually 1 Battleship(size: 4)
     */
    BATTLESHIP(4),  //1x
    /**
     * usually 1 Aircraft carrier(size: 5)
     */
    CARRIER(5);  //1x

    /**
     * the size or number of fields for the type
     */
    private int size;

    Shipmodel(int size) {
        this.size = size;
    }

    /**
     * @return the size or number of fields for the type
     */
    public int returnSize() {
        return this.size;
    }
}

