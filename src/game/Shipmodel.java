package game;

/**
 * @author Edwin W (570900) on Nov 2020
 */
public enum Shipmodel {
    SUBMARINES(2),
    DESTROYERS(3),
    CRUISERS(4),
    BATTLESHIP(5);

    private int size;

    Shipmodel(int size) {
        this.size = size;
    }

    public int returnSize() {
        return this.size;
    }
}

