package de.eisner.maze.model;

/**
 * Represents the player character in the maze game.
 * Tracks the current position and handles movement logic against the maze walls.
 */
public class Runner {
    private int row;
    private int col;

    public Runner(Point start) {
        this.row = start.row();
        this.col = start.col();
    }

    /**
     * Moves the runner to a new position if the target tile is not a wall.
     * @param dRow The change in the row index (delta).
     * @param dCol The change in the column index (delta).
     * @param maze The maze object used for collision detection.
     */
    public void move(int dRow, int dCol, Maze maze) {
        int nr = row + dRow;
        int nc = col + dCol;

        if (nr >= 0 && nr < maze.grid().length &&
            nc >= 0 && nc < maze.grid()[0].length &&
            maze.grid()[nr][nc] != 1) {
            this.row = nr;
            this.col = nc;
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

}
