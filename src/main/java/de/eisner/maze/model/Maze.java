package de.eisner.maze.model;

/**
 * Data container representing the maze grid.
 * @param grid A 2D integer array where 0=Pass, 1=Wall, 2=Path.
 */
public record Maze(int[][] grid) {
}
