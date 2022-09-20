package minesweeper.view;

import minesweeper.model.Location;
import minesweeper.model.Minesweeper;
import minesweeper.model.MinesweeperException;
import minesweeper.model.MinesweeperSolver;

import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import backtracker.Backtracker;
import backtracker.Configuration;

public class Terminal {
    public static void main(String[] args) throws NumberFormatException, MinesweeperException {
        try(Scanner scanner = new Scanner(System.in)) {
//Grab user input for the minesweeper constructor.
            System.out.println("Please enter Row: ");
            int row = scanner.nextInt();
            System.out.println("Please enter Col: ");
            int col = scanner.nextInt();
            System.out.println("Please enter mine count: ");
            int mineCount = scanner.nextInt();
//Make board.
            Minesweeper board = new Minesweeper(row, col, mineCount);
            boolean value = true;
            String commandString = scanner.nextLine();
// Create loop that is going to loop user into making moves onto the game.
            while(value) {
                System.out.println(board);
                System.out.println("Please enter in your command: ");
                commandString = scanner.nextLine();
                String[] command = commandString.split(" ");
                switch(command[0]) {
                    case "quit":
                        value = !quit(scanner);
                        break;
                    case "reset":
                        board = reset(board, row, col, mineCount);
                        System.out.println("Game has been reset.");
                        break;
                    case "hint":
                        Location hint = hint(board);
                        System.out.println(hint.getRow() + "," + hint.getCol());
                        break;
                    case "help":
                        help();
                        break;
                    case "move":
                        move(board, Integer.parseInt(command[1]),Integer.parseInt(command[2]));
                        break;
                    case "solve":
                        Configuration initial = new MinesweeperSolver(board);
                        Backtracker back = new Backtracker(false);
                        MinesweeperSolver solution = (MinesweeperSolver)back.solve(initial);
                        if (back!=null){
                            System.out.println(solution.toString());
                        }
                        break;
                    default:
                        System.out.println("Please enter a valid command. Enter hint for a list of commands");
                        break;
                }
            }      
            }
        }
    /**
     * Makes a move on the board using the Minesweeper makeSelection method
     * @param board
     * @param row
     * @param col
     * @throws MinesweeperException
     */
    private static void move(Minesweeper board, int row, int col) throws MinesweeperException{
        Location move = new Location(row, col, 0);
        try{
            board.makeSelection(move);
        }catch (MinesweeperException e){
            System.out.println("Invalid move");
        }catch(NumberFormatException nfe){
            System.out.println("Enter valid integers please.");
        }
    }
    /**
     * 
     * Makes a new game board using the users input
     * @param board
     * @param row
     * @param col
     * @param mineCount
     * @return
     */
    private static Minesweeper reset(Minesweeper board, int row, int col, int mineCount) {
        board = new Minesweeper(row, col, mineCount);
        return board;
    }
    /**
     * Returns a row and col that is a safe space on the board
     * @param board
     * @return
     */
    private static Location hint(Minesweeper board) {
        Collection<Location> location = board.getPossibleSelections();
        Stream<Location> locations = location.stream();
        List<Location> finalLocation = locations.toList();
        return finalLocation.get(0);

    }

    /**
     * This helper function will exit the loop for the CLI.
     * @param scanner The scanner used to read the user response.
     * @return True if the game should quit, false if it should not.
     */
    private static boolean quit(Scanner scanner) {
        System.out.print("Are you sure? (y/n): ");
        String response = scanner.nextLine();
        return response.equalsIgnoreCase("y");
    }

    /**
     * Prints a help message with the available commands.
     */
    private static void help() {
        System.out.println("Available commands: ");
        System.out.println("  help - displays this message");
        System.out.println("  move R C - makes a move for the current player");
        System.out.println("  hint - returns a row and col that is not a mine");
        System.out.println("  quit - quits the game");
        System.out.println("  solve - solves the game");
        System.out.println();
    }
}