import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a single ship with a name, size, and occupied cells.
 */
public class Ship {
    private final String name;
    private final int size;
    private final Set<Point> cells = new HashSet<>();
    private final Set<Point> hits = new HashSet<>();

    /**
     * Constructs a Ship with a given name and size.
     * @param name ship name
     * @param size ship length
     */
    public Ship(String name, int size) {
        this.name = name;
        this.size = size;
    }

    /**
     * Adds a cell coordinate to this ship.
     * @param r row index
     * @param c column index
     */
    public void addCell(int r, int c) {
        cells.add(new Point(r, c));
    }

    /**
     * Checks if this ship occupies the given cell.
     * @param r row index
     * @param c column index
     * @return true if the ship occupies the cell
     */
    public boolean occupies(int r, int c) {
        return cells.contains(new Point(r, c));
    }

    /**
     * Registers a hit at the given cell.
     * @param r row index
     * @param c column index
     */
    public void registerHit(int r, int c) {
        hits.add(new Point(r, c));
    }

    /**
     * Checks if the ship is sunk.
     * @return true if all cells have been hit
     */
    public boolean isSunk() {
        return hits.size() == size;
    }

    /**
     * Gets the ship's name.
     * @return ship name
     */
    public String getName() {
        return name;
    }
}