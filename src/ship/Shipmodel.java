package ship;

/**
 * @author Edwin W (HTW) on Nov 2020
 * The different types of ship, that exists in the game and their specific size!
 */
public enum Shipmodel {
    /**
     * usually 4 Submarines(size: 2)
     */
    SUBMARINE(2),  //4x
    /**
     * usually 3 Destroyers(size: 3)
     */
    DESTROYER(3),  //3x
    /**
     * usually 2 Cruisers(size: 4)
     */
    CRUISER(4),    //2x
    /**
     * usually 1 Battleship(size: 5)
     */
    BATTLESHIP(5);  //1x

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

