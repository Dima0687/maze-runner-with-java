package de.eisner.maze.logic.service;

import de.eisner.maze.model.Maze;
import de.eisner.maze.model.Point;
import de.eisner.maze.model.Runner;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * Business logic interface providing high-level operations for maze management.
 * Acts as a Facade for generation, pathfinding, and persistence.
 */
public interface MazeService {
    /**
     * Generates a new maze with the specified dimensions.
     * @param rows Number of rows.
     * @param cols Number of columns.
     * @return An Optional containing the generated Maze.
     */
    Optional<Maze> generateMaze(int rows, int cols);

    /**
     * Loads a maze from a file.
     * @param filePath Path to the source file.
     * @return An Optional containing the Maze.
     * @throws FileNotFoundException If file is missing.
     */
    Optional<Maze> load(String filePath) throws FileNotFoundException;

    /**
     * Saves a maze to a file.
     * @param filePath Target path.
     * @param maze Maze to save.
     * @return true if success.
     */
    boolean save(String filePath, Maze maze);

    /**
     * Attempts to find a solution path between the entry and exit points.
     * @param maze The maze to solve.
     * @return An Optional containing the Maze with the path marked.
     */
    Optional<Maze> findPath(Maze maze);

    /**
     * Renders the maze to the console using predefined tile symbols.
     * @param maze The maze to display.
     */
    void displayMaze(Maze maze);

    /**
     * Renders the maze and overlays the runner's current position.
     * Clears the console before rendering to create an animation effect.
     * @param maze The maze to be displayed.
     * @param runner The runner object containing the current coordinates.
     */
    void displayMazeWithRunner(Maze maze, Runner runner);

    /**
     * Retrieves all entry and exit points located on the maze boundaries.
     * @param maze The maze to analyze.
     * @return A list of points representing openings in the outer walls.
     */
    List<Point> getExits(Maze maze);

    /**
     * Displays a small ASCII animation when the player reaches the exit.
     */
    void celebrateGoal();
}
