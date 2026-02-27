package de.eisner.maze.model;

/**
 * Helper record used during maze generation to link a wall (path) to its destination cell.
 * @param path The coordinate of the wall to be broken.
 * @param next The coordinate of the destination cell.
 */
public record Tile(Point path, Point next) {
}
