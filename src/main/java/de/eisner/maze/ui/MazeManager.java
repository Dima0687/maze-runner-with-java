package de.eisner.maze.ui;

import de.eisner.maze.logic.service.MazeService;
import de.eisner.maze.logic.service.MazeServiceImpl;
import de.eisner.maze.model.Maze;
import de.eisner.maze.model.Point;
import de.eisner.maze.model.Runner;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Controller class for the User Interface.
 * Handles the menu loop, user input, and coordinates calls to the MazeService.
 */
public class MazeManager {
    private final List<String> menuOptions = new ArrayList<>(
            List.of(
                    "Generate a new maze",
                    "Load a maze",
                    "Save the maze",
                    "Display the maze",
                    "Find the escape",
                    "Run the maze"
            )
    );
    private final int MENU_SIZE_IF_MAZE_NULL = 2;
    private final Scanner scanner = new Scanner(System.in);
    private final MazeService service;
    private Maze maze;

    public MazeManager() {
        this.service = new MazeServiceImpl();
    }

    private void generateMaze() throws NumberFormatException {
        System.out.println("Enter the size of a new maze");
        String[] input = scanner.nextLine().trim().split(" ");

        int row;
        int col;
        if (input.length > 1) {
            row = Integer.parseInt(input[0]);
            col = Integer.parseInt(input[1]);
        } else {
            row = Integer.parseInt(input[0]);
            col = row;
        }

        row = row <= 0 ? 10 : row;
        col = col <= 0 ? 10 : col;

        var newMaze = service.generateMaze(row, col);
        newMaze.ifPresentOrElse(
                m -> {
                    maze = m;
                    service.displayMaze(m);
                },
                () -> System.out.println("Couldn't generate a new maze!")
        );
    }

    private void loadMaze() throws FileNotFoundException {
        System.out.println("Enter the file path: ");
        String filePath = scanner.nextLine().trim();
        var loadedMaze = service.load(filePath);
        loadedMaze.ifPresent(m -> maze = m);
    }

    private void saveMaze() {
        System.out.println("Enter the file path where to save the maze: ");
        String filePath = scanner.nextLine().trim();
        boolean success = service.save(filePath, maze);

        if (success) {
            System.out.println("Successfully saved the maze at " + filePath);
        }
    }

    private void displayMaze() {
        service.displayMaze(maze);
    }

    private void findTheEscape() {
        var mazeWithPath = service.findPath(maze);
        mazeWithPath.ifPresentOrElse(
                service::displayMaze,
                () -> System.out.println("Couldn't find the escape!")
        );
    }

    private void displayMenu() {
        System.out.println("=== Menu ===");
        for (int i = 0; i < menuOptions.size(); i++) {
            if (maze == null && i >= MENU_SIZE_IF_MAZE_NULL) {
                break;
            }
            System.out.printf("%d. %s%n", (i + 1), menuOptions.get(i));
        }
        System.out.println("0. Exit");
    }

    /**
     * Starts the interactive 'Maze Runner' mode.
     * Allows the user to move through the maze using WASD keys.
     * Provides options to reveal the path or return to the menu.
     */
    private void runTheMaze() {
        if (maze == null) return;

        List<Point> exits = service.getExits(maze);
        if (exits.size() < 2) return;

        Runner runner = new Runner(exits.getFirst());
        Point target = exits.getLast();

        while (true) {
            service.displayMazeWithRunner(maze, runner);

            if (runner.getRow() == target.row() && runner.getCol() == target.col()) {
                service.celebrateGoal();
                System.out.println("Press Enter to return to menu...");
                scanner.nextLine();
                break;
            }

            System.out.println("W/A/S/D - Move | 1 - Show Path | 0 - Back to Menu");
            String input = scanner.nextLine().trim().toLowerCase();

            if ("0".equals(input)) break;

            if ("1".equals(input)) {
                var solved = service.findPath(maze);
                solved.ifPresent(service::displayMaze);
                System.out.println("Path revealed. Game over. Press Enter...");
                scanner.nextLine();
                break;
            }

            switch (input) {
                case "w" -> runner.move(-1, 0, maze);
                case "s" -> runner.move(1, 0, maze);
                case "a" -> runner.move(0, -1, maze);
                case "d" -> runner.move(0, 1, maze);
            }
        }
    }

    /**
     * Starts the main menu loop and waits for user interaction.
     */
    public void run() {
        while (true) {
            displayMenu();
            int menuSize = maze == null ? MENU_SIZE_IF_MAZE_NULL : menuOptions.size();

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 0) {
                    System.out.println("Bye!");
                    break;
                }

                if (choice > menuSize || choice < 0) {
                    System.out.println("Incorrect option. Please try again");
                    continue;
                }

                switch (choice) {
                    case 1 -> generateMaze();
                    case 2 -> loadMaze();
                    case 3 -> saveMaze();
                    case 4 -> displayMaze();
                    case 5 -> findTheEscape();
                    case 6 -> runTheMaze();
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Wrong input! Please enter only integer!");
            } catch (FileNotFoundException ffe) {
                System.out.println(ffe.getMessage());
            }
        }
    }
}
