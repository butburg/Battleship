package ship;

/**
 * @author Edwin W (570900) on Nov 2020
 * The different types of ship, that exists in the game and their specific size!
 */
public enum Shipmodel {
    /**
     * usually 4 SUBMARINES
     */
    SUBMARINES(2),  //4x
    /**
     * usually 3 DESTROYERS
     */
    DESTROYERS(3),  //3x
    /**
     * usually 2 CRUISERS
     */
    CRUISERS(4),    //2x
    /**
     * usually 1 BATTLESHIP
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

