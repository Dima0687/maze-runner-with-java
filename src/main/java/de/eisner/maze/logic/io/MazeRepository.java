package de.eisner.maze.logic.io;

import de.eisner.maze.model.Maze;

import java.io.FileNotFoundException;
import java.util.Optional;

/**
 * Interface for persistence operations regarding maze data.
 * Provides methods to load and save maze structures from/to the file system.
 */
public interface MazeRepository {
    /**
     * Loads a maze from a specified file path.
     * @param filePath The path to the file containing the maze data.
     * @return An Optional containing the Maze if successful, or empty if the format is invalid.
     * @throws FileNotFoundException If the file at the given path does not exist.
     */
    Optional<Maze> load(String filePath) throws FileNotFoundException;

    /**
     * Saves the given maze to a specified file path.
     * @param filePath The destination path.
     * @param maze The Maze object to persist.
     * @return true if the operation was successful, false otherwise.
     */
    boolean save(String filePath, Maze maze);
}
