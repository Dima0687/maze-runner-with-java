package de.eisner.maze.ui;

/**
 * Defines the visual representation of different maze elements in the console.
 */
public enum TileType {
    /** Represented as empty space for walkable areas. */
    PASS("  "),
    /** Represented as slashes for the solution path. */
    PATH("//"),
    /** Represented as full blocks for walls. */
    WALL("\u2588\u2588"),
    /** Represented as a colored "O/" symbol for the player character. */
    RUNNER("\033[1;31mO/\033[0m");

    private final String symbol;

    TileType(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
