package de.eisner.maze.logic.io;

import de.eisner.maze.model.Maze;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

/**
 * Implementation of MazeRepository using standard Java NIO for file operations.
 * Maze data is expected to be stored as a text file where each digit represents a tile.
 */
public class MazeRepositoryImpl implements MazeRepository {
    @Override
    public Optional<Maze> load(String filePath) throws FileNotFoundException {
        Path path = Path.of(filePath);

        if (Files.notExists(path)) {
            throw new FileNotFoundException("The file " + filePath + " does not exist");
        }

        try {
            var allLines = Files.readAllLines(path, StandardCharsets.UTF_8);

            if (allLines.isEmpty()) {
                return Optional.empty();
            }

            boolean invalidFormat = allLines.stream()
                    .anyMatch(s -> !s.matches("[012]*"));

            if (invalidFormat) {
                System.out.println("Cannot load the maze. It has an invalid format");
                return Optional.empty();
            }

            int[][] maze = allLines.stream()
                    .map(s -> Arrays.stream(s.split(""))
                            .mapToInt(Integer::parseInt).toArray())
                    .toArray(int[][]::new);
            return Optional.of(new Maze(maze));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean save(String filePath, Maze maze) {
        Path path = Path.of(filePath);
        StringBuilder sb = new StringBuilder();

        for (int[] row : maze.grid()) {
            for (int c : row) {
                sb.append(c);
            }
            sb.append(System.lineSeparator());
        }

        try {
            Files.writeString(path, sb.toString(), StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Could not save file: " + e.getMessage());
        }
    }
}
