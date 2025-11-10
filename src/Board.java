import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Inner class representing the game board.
 * Maintains a 10x10 grid and a list of ships.
 */
public class Board {
    private final int SIZE = 10;
    private final char[][] grid = new char[SIZE][SIZE];
    private final List<Ship> ships = new ArrayList<>();

    /**
     * Constructs a Board, initializes all cells to water,
     * and places ships randomly using ShipPlacement.
     */
    public Board() {
        for (int r = 0; r < SIZE; r++) {
            Arrays.fill(grid[r], '~'); // blank water
        }
        ShipPlacement placer = new ShipPlacement();
        placer.placeShips(grid, ships); // fills grid and ships list
    }

    /**
     * Returns the board size (10).
     * @return the size of the board
     */
    public int size() {
        return SIZE;
    }

    /**
     * Gets the character stored at a given cell.
     * @param r row index
     * @param c column index
     * @return the character at that cell
     */
    public char getCell(int r, int c) {
        return grid[r][c];
    }

    /**
     * Sets a character value at a given cell.
     * @param r row index
     * @param c column index
     * @param val character to store
     */
    public void setCell(int r, int c, char val) {
        grid[r][c] = val;
    }

    /**
     * Checks if any ships remain unsunk.
     * @return true if at least one ship is not sunk
     */
    public boolean hasShipsRemaining() {
        for (Ship s : ships) {
            if (!s.isSunk()) return true;
        }
        return false;
    }

    /**
     * Registers a hit at the given coordinates.
     * Updates the ship if one occupies the cell.
     * @param r row index
     * @param c column index
     * @return the name of the ship if sunk, otherwise null
     */
    public String registerHit(int r, int c) {
        for (Ship s : ships) {
            if (s.occupies(r, c)) {
                s.registerHit(r, c);
                if (s.isSunk()) {
                    return s.getName();
                }
            }
        }
        return null;
    }
}