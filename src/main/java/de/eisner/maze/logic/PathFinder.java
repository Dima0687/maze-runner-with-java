package de.eisner.maze.logic;

import de.eisner.maze.model.Maze;
import de.eisner.maze.model.Point;

import java.util.*;

/**
 * Logic class for finding the shortest path through a maze.
 * Utilizes a Breadth-First Search (BFS) algorithm to ensure the shortest path is found.
 */
public class PathFinder {

    /**
     * Solves the maze by marking the path from the first exit to the last exit found.
     * @param maze The maze to be solved.
     * @return An Optional containing the Maze with marked path (value 2), or empty if no path exists.
     */
    public Optional<Maze> findPath(Maze maze) {
        int[][] grid = maze.grid();
        List<Point> exits = getAllExits(grid);

        if (exits.size() < 2) {
            return Optional.empty();
        }
        Point start = exits.getFirst();
        Point end = exits.getLast();

        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> comeFrom = new HashMap<>();

        queue.add(start);
        comeFrom.put(start, null);

        int height = grid.length;
        int width = grid[0].length;
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

        boolean found = false;
        while (!queue.isEmpty()) {
            Point current = queue.poll();

            if (current.equals(end)) {
                found = true;
                break;
            }

            for (int[] d : dirs) {
                Point neighbor = new Point(current.row() + d[0], current.col() + d[1]);

                // not visited yet, within bound and is pass
                if (neighbor.row() >= 0 && neighbor.row() < height &&
                    neighbor.col() >= 0 && neighbor.col() < width &&
                    grid[neighbor.row()][neighbor.col()] == 0 &&
                    !comeFrom.containsKey(neighbor)) {

                    comeFrom.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        if (found) {
            int[][] gridCopy = Arrays.stream(grid).map(int[]::clone).toArray(int[][]::new);
            Point step = end;
            while (step != null) {
                gridCopy[step.row()][step.col()] = 2;
                step = comeFrom.get(step);
            }
            return Optional.of(new Maze(gridCopy));
        }

        return Optional.empty();
    }

    /**
     * Scans the maze boundaries for any traversable tiles (value 0).
     * @param m The grid to scan.
     * @return List of points on the border.
     */
    public List<Point> getAllExits(int[][] m) {
        List<Point> exits = new ArrayList<>();
        int h = m.length;
        int w = m[0].length;

        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                boolean isBorder = (r == 0 || r == h - 1 || c == 0 || c == w - 1);

                if (isBorder && m[r][c] == 0) {
                    exits.add(new Point(r, c));
                }
            }
        }
        return exits;
    }
}
