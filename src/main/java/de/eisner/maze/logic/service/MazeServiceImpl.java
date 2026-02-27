package de.eisner.maze.logic.service;

import de.eisner.maze.logic.MazeGenerator;
import de.eisner.maze.logic.PathFinder;
import de.eisner.maze.logic.io.MazeRepository;
import de.eisner.maze.logic.io.MazeRepositoryImpl;
import de.eisner.maze.model.Maze;
import de.eisner.maze.model.Point;
import de.eisner.maze.model.Runner;
import de.eisner.maze.ui.TileType;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

public class MazeServiceImpl implements MazeService {

    private final MazeRepository repository;
    private final MazeGenerator generator;
    private final PathFinder pathFinder;

    public MazeServiceImpl() {
        this.generator = new MazeGenerator();
        this.repository = new MazeRepositoryImpl();
        this.pathFinder = new PathFinder();
    }

    @Override
    public Optional<Maze> generateMaze(int rows, int cols) {
        Maze maze = generator.generate(rows, cols);
        if (maze == null) {
            return Optional.empty();
        }
        return Optional.of(maze);
    }

    @Override
    public Optional<Maze> load(String filePath) throws FileNotFoundException {
        return repository.load(filePath);
    }

    @Override
    public boolean save(String filePath, Maze maze) {
        return repository.save(filePath, maze);
    }

    @Override
    public Optional<Maze> findPath(Maze maze) {
        return pathFinder.findPath(maze);
    }

    @Override
    public void displayMaze(Maze maze) {
        for (int[] row : maze.grid()) {
            for (int c : row) {
                System.out.print(c == 0
                        ? TileType.PASS
                        : c == 1
                        ? TileType.WALL
                        : TileType.PATH);
            }
            System.out.println();
        }
    }

    @Override
    public void displayMazeWithRunner(Maze maze, Runner runner) {
        // Clear console (ANSI escape codes)
        System.out.print("\033[H\033[2J");
        System.out.flush();

        int[][] grid = maze.grid();
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (r == runner.getRow() && c == runner.getCol()) {
                    System.out.print(TileType.RUNNER);
                } else {
                    int val = grid[r][c];
                    System.out.print(val == 0
                            ? TileType.PASS
                            : val == 1
                            ? TileType.WALL
                            : TileType.PATH);
                }
            }
            System.out.println();
        }
    }

    @Override
    public List<Point> getExits(Maze maze) {
        return pathFinder.getAllExits(maze.grid());
    }

    @Override
    public void celebrateGoal() {
        String[] frames = {
                "\033[31m   * \n  *** \n   * \033[0m",
                "\033[32m  \\|/  \n --*-- \n  /|\\  \033[0m",
                "\033[34m ' . ' \n.  * .\n ' . ' \033[0m"
        };

        for (String frame : frames) {
            // Clear console
            System.out.print("\033[2J\033[2;1H");
            System.out.flush();
            System.out.println("\n\n      YOU DID IT!");
            System.out.println(frame);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
