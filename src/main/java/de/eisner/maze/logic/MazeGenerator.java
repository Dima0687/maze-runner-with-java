package de.eisner.maze.logic;

import de.eisner.maze.model.Tile;
import de.eisner.maze.model.Maze;
import de.eisner.maze.model.Point;

import java.util.*;

/**
 * Core logic for creating randomized mazes.
 * Uses a modified Prim's algorithm based on a grid of cells and walls.
 * Ensures the maze has distinct entry and exit points with a minimum distance.
 */
public class MazeGenerator {
    private final Random random = new Random();

    /**
     * Generates a randomized maze.
     * @param rows Height of the grid.
     * @param cols Width of the grid.
     * @return A newly constructed Maze object.
     */
    public Maze generate(int rows, int cols) {
        int[][] newGrid = initGrid(rows, cols);
        initMaze(newGrid);
        stretchMazeIfEven(newGrid);
        addEntryAndExitPoints(newGrid);

        return new Maze(newGrid);
    }

    private int[][] initGrid(int rows, int cols) {
        int[][] m = new int[rows][cols];
        Arrays.setAll(m, i -> {
            int[] row = new int[cols];
            Arrays.fill(row, 1);
            return row;
        });
        return m;
    }

    private void initMaze(int[][] grid) {
        List<Tile> neighbors = new ArrayList<>();
        // initial cell
        int row = 1 + 2 * random.nextInt((grid.length - 1) / 2);
        int col = 1 + 2 * random.nextInt((grid[0].length - 1) / 2);
        grid[row][col] = 0;
        addNeighbors(row, col, grid, neighbors);

        // further cells
        while (!neighbors.isEmpty()) {
            int rIndex = random.nextInt(neighbors.size());
            Tile cell = neighbors.remove(rIndex);

            if (grid[cell.next().row()][cell.next().col()] == 1) {
                grid[cell.path().row()][cell.path().col()] = 0;
                grid[cell.next().row()][cell.next().col()] = 0;
                addNeighbors(cell.next().row(), cell.next().col(), grid, neighbors);
            }
        }
    }

    private void addNeighbors(int row, int col, int[][] grid, List<Tile> neighbors) {
        // up, down, left, right
        int[][] dirs = {{-2, 0}, {2, 0}, {0, -2}, {0, 2}};
        int height = grid.length - 1;
        int width = grid[0].length - 1;


        for (int[] d : dirs) {
            int nr = row + d[0];
            int nc = col + d[1];

            if (nr > 0 && nr < height && nc > 0 && nc < width) {
                if (grid[nr][nc] == 1) {
                    Point path = new Point(row + d[0] / 2, col + d[1] / 2);
                    Point next = new Point(nr, nc);
                    neighbors.add(new Tile(path, next));
                }
            }
        }
    }

    private void addEntryAndExitPoints(int[][] grid) {
        List<Point> candidates = new ArrayList<>();
        int height = grid.length - 1;
        int width = grid[0].length - 1;

        for (int i = 1; i < height; i++) {
            // Left side
            if (grid[i][1] == 0) candidates.add(new Point(i, 0));
            // Right side
            if (grid[i][width - 1] == 0) candidates.add(new Point(i, width));
        }

        for (int i = 1; i < width; i++) {
            // Upper side
            if (grid[1][i] == 0) candidates.add(new Point(0, i));
            // Bottom side
            if (grid[height - 1][i] == 0) candidates.add(new Point(height, i));
        }

        if (candidates.size() < 2) return;

        Point p1 = candidates.remove(random.nextInt(candidates.size()));
        double minDistance = Math.sqrt(height * height + width * width) / 3;

        List<Point> farCandidates = new ArrayList<>();
        for (Point c : candidates) {
            if (calculateDistance(p1, c) >= minDistance) {
                farCandidates.add(c);
            }
        }

        Point p2;
        if (!farCandidates.isEmpty()) {
            p2 = farCandidates.get(random.nextInt(farCandidates.size()));
        } else {
            p2 = candidates.stream()
                    .max(Comparator.comparingDouble(c -> calculateDistance(p1, c)))
                    .orElse(candidates.getFirst());
        }

        grid[p1.row()][p1.col()] = 0;
        grid[p2.row()][p2.col()] = 0;
    }

    private double calculateDistance(Point a, Point b) {
        return Math.sqrt(Math.pow(a.row() - b.row(), 2) + Math.pow(a.col() - b.col(), 2));
    }

    private boolean canOpen(int r, int c, int[][] grid) {
        int openNeighbors = 0;
        int height = grid.length;
        int width = grid[0].length;

        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] d : dirs) {
            int nr = r + d[0];
            int nc = c + d[1];

            if (nr >= 0 && nr < height &&
                nc >= 0 && nc < width &&
                grid[nr][nc] == 0) {
                openNeighbors++;
            }
        }

        return openNeighbors == 1;
    }

    private void stretchMazeIfEven(int[][] grid) {
        int height = grid.length;
        int width = grid[0].length;

        if (height % 2 == 0) {
            for (int c = 1; c < width - 1; c++) {
                if (canOpen(height - 2, c, grid)) {
                    grid[height - 2][c] = 0;
                }
            }
        }

        if (width % 2 == 0) {
            for (int r = 1; r < height - 1; r++) {
                if (canOpen(r, width - 2, grid)) {
                    grid[r][width - 2] = 0;
                }
            }
        }
    }
}
