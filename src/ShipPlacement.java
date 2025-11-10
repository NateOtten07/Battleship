import java.util.List;
import java.util.Random;

/**
 * Handles random placement of ships on the board.
 */
public class ShipPlacement {
    private final int[] SIZES = {5, 4, 3, 3, 2};
    private final String[] NAMES = {"Carrier(5)", "Battleship(4)", "Cruiser(3)", "Submarine(3)", "Destroyer(2)"};
    private final Random rand = new Random();

    /**
     * Places ships randomly on the grid.
     * @param grid the board grid
     * @param ships the list to populate with Ship objects
     */
    public void placeShips(char[][] grid, List<Ship> ships) {
        int n = grid.length;
        for (int idx = 0; idx < SIZES.length; idx++) {
            int size = SIZES[idx];
            String name = NAMES[idx];
            boolean placed = false;
            while (!placed) {
                boolean horizontal = rand.nextBoolean(); // random orientation
                int row = rand.nextInt(n);
                int col = rand.nextInt(n);

                if (canPlace(grid, row, col, size, horizontal)) {
                    Ship ship = new Ship(name, size);
                    for (int i = 0; i < size; i++) {
                        int rr = horizontal ? row : row + i;
                        int cc = horizontal ? col + i : col;
                        grid[rr][cc] = 'S';
                        ship.addCell(rr, cc);
                    }
                    ships.add(ship);
                    placed = true;
                }
            }
        }
    }

    /**
     * Checks if a ship can be placed at the given location.
     * @param grid the board grid
     * @param row starting row
     * @param col starting column
     * @param size ship length
     * @param horizontal true if horizontal placement
     * @return true if placement is valid
     */
    private boolean canPlace(char[][] grid, int row, int col, int size, boolean horizontal) {
        int n = grid.length;
        if (horizontal) {
            if (col + size > n) return false;
            for (int i = 0; i < size; i++) {
                if (grid[row][col + i] != '~') return false;
            }
        } else {
            if (row + size > n) return false;
            for (int i = 0; i < size; i++) {
                if (grid[row + i][col] != '~') return false;
            }
        }
        return true;
    }
}