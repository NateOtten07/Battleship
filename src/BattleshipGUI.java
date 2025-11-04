import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * BattleshipGUI is the main JFrame for the Battleship game.
 * It manages the overall layout, initializes the game board,
 * and coordinates between the Board, Game, Displays, and ButtonGrid classes.
 */
public class BattleshipGUI extends JFrame {

    // Top-level GUI components
    private JLabel titleLbl;
    private JPanel boardPnl;
    private JPanel statusPnl;
    private JPanel controlPnl;

    private JButton playAgainBtn;
    private JButton quitBtn;

    // Status labels
    private JLabel missLbl;
    private JLabel strikeLbl;
    private JLabel totalMissLbl;
    private JLabel totalHitLbl;

    // Core collaborators (inner classes below)
    private Board board;
    private Displays displays;
    private Game game;
    private ButtonGrid buttonGrid;

    /**
     * Constructs the BattleshipGUI window, sets up layout and panels,
     * and starts a new game.
     */
    public BattleshipGUI() {
        super("Battleship Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        createTitle();
        createStatus();
        createControls();
        createBoard();

        startNewGame();

        setVisible(true);
    }

    /**
     * Creates and adds the title label to the top of the frame.
     */
    private void createTitle() {
        titleLbl = new JLabel("Battleship Game", SwingConstants.CENTER);
        titleLbl.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLbl, BorderLayout.NORTH);
    }

    /**
     * Creates the status panel with labels for misses, strikes,
     * total misses, and total hits.
     */
    private void createStatus() {
        statusPnl = new JPanel(new GridLayout(2, 2, 10, 10));
        missLbl = new JLabel("Misses (streak): 0");
        strikeLbl = new JLabel("Strikes: 0");
        totalMissLbl = new JLabel("Total Misses: 0");
        totalHitLbl = new JLabel("Total Hits: 0");

        statusPnl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        statusPnl.add(missLbl);
        statusPnl.add(strikeLbl);
        statusPnl.add(totalMissLbl);
        statusPnl.add(totalHitLbl);
        add(statusPnl, BorderLayout.SOUTH);
    }

    /**
     * Creates the control panel with "Play Again" and "Quit" buttons.
     * Adds confirmation dialogs for both actions.
     */
    private void createControls() {
        controlPnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        playAgainBtn = new JButton("Play Again");
        quitBtn = new JButton("Quit");

        playAgainBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to start a new game?",
                    "Play Again",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                startNewGame();
            }
        });

        quitBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Quit the game?",
                    "Quit",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        controlPnl.add(playAgainBtn);
        controlPnl.add(quitBtn);
        add(controlPnl, BorderLayout.WEST);
    }

    /**
     * Creates the board panel where the grid of buttons will be displayed.
     */
    private void createBoard() {
        boardPnl = new JPanel();
        add(boardPnl, BorderLayout.CENTER);
    }

    /**
     * Starts a new game by resetting the board, displays, and button grid.
     */
    private void startNewGame() {
        board = new Board();
        displays = new Displays(missLbl, strikeLbl, totalMissLbl, totalHitLbl);
        game = new Game(board, displays);
        boardPnl.removeAll();
        buttonGrid = new ButtonGrid(boardPnl, game, board);
        boardPnl.revalidate();
        boardPnl.repaint();
        displays.update(0, 0, 0, 0);
    }

    /**
     * Inner class representing the game board.
     * Maintains a 10x10 grid and a list of ships.
     */
    private class Board {
        private final int SIZE = 10;
        private final char[][] grid = new char[SIZE][SIZE];
        private final List<Ship> ships = new ArrayList<>();

        /**
         * Constructs a Board, initializes all cells to water,
         * and places ships randomly using ShipPlacement.
         */
        Board() {
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
        int size() {
            return SIZE;
        }

        /**
         * Gets the character stored at a given cell.
         * @param r row index
         * @param c column index
         * @return the character at that cell
         */
        char getCell(int r, int c) {
            return grid[r][c];
        }

        /**
         * Sets a character value at a given cell.
         * @param r row index
         * @param c column index
         * @param val character to store
         */
        void setCell(int r, int c, char val) {
            grid[r][c] = val;
        }

        /**
         * Checks if any ships remain unsunk.
         * @return true if at least one ship is not sunk
         */
        boolean hasShipsRemaining() {
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
        String registerHit(int r, int c) {
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

    /**
     * Represents a single ship with a name, size, and occupied cells.
     */
    private class Ship {
        private final String name;
        private final int size;
        private final Set<Point> cells = new HashSet<>();
        private final Set<Point> hits = new HashSet<>();

        /**
         * Constructs a Ship with a given name and size.
         * @param name ship name
         * @param size ship length
         */
        Ship(String name, int size) {
            this.name = name;
            this.size = size;
        }

        /**
         * Adds a cell coordinate to this ship.
         * @param r row index
         * @param c column index
         */
        void addCell(int r, int c) {
            cells.add(new Point(r, c));
        }

        /**
         * Checks if this ship occupies the given cell.
         * @param r row index
         * @param c column index
         * @return true if the ship occupies the cell
         */
        boolean occupies(int r, int c) {
            return cells.contains(new Point(r, c));
        }

        /**
         * Registers a hit at the given cell.
         * @param r row index
         * @param c column index
         */
        void registerHit(int r, int c) {
            hits.add(new Point(r, c));
        }

        /**
         * Checks if the ship is sunk.
         * @return true if all cells have been hit
         */
        boolean isSunk() {
            return hits.size() == size;
        }

        /**
         * Gets the ship's name.
         * @return ship name
         */
        String getName() {
            return name;
        }
    }

    /**
     * Handles random placement of ships on the board.
     */
    private class ShipPlacement {
        private final int[] SIZES = {5, 4, 3, 3, 2};
        private final String[] NAMES = {"Carrier(5)", "Battleship(4)", "Cruiser(3)", "Submarine(3)", "Destroyer(2)"};
        private final Random rand = new Random();

        /**
         * Places ships randomly on the grid.
         * @param grid the board grid
         * @param ships the list to populate with Ship objects
         */
        void placeShips(char[][] grid, List<Ship> ships) {
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

    /**
     * Handles the main game logic: firing, tracking hits/misses,
     * and determining win/lose conditions.
     */
    private class Game {
        private final Board board;
        private final Displays displays;
        private int missCount = 0;   // streak miss counter [0-5]
        private int strikeCount = 0; // [0-3]
        private int totalMiss = 0;   // [0-83]
        private int totalHit = 0;    // [0-17]
        private boolean gameOver = false;

        /**
         * Constructs a Game with a board and displays.
         * @param board the game board
         * @param displays the status display manager
         */
        Game(Board board, Displays displays) {
            this.board = board;
            this.displays = displays;
        }

        /**
         * Fires at a given cell, updating the board, displays,
         * and checking for win/lose conditions.
         * @param r row index
         * @param c column index
         * @param btn the JButton representing the cell
         */
        void fire(int r, int c, JButton btn) {
            if (gameOver) return;

            char cell = board.getCell(r, c);
            if (cell == 'S') {
                board.setCell(r, c, 'X');
                ImageIcon hitIcon = loadIcon("src/hit.png", 60, 60);
                btn.setIcon(hitIcon);
                btn.setDisabledIcon(hitIcon);
                btn.setForeground(Color.RED);
                totalHit++;
                missCount = 0;

                String sunkName = board.registerHit(r, c);
                if (sunkName != null) {
                    JOptionPane.showMessageDialog(BattleshipGUI.this, "You sunk: " + sunkName + "!");
                }

                // Win check
                if (!board.hasShipsRemaining()) {
                    gameOver = true;
                    displays.update(totalHit, totalMiss, missCount, strikeCount);
                    int choice = JOptionPane.showConfirmDialog(
                            BattleshipGUI.this,
                            "You sunk all ships! You win!\nPlay again?",
                            "Victory",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (choice == JOptionPane.YES_OPTION) {
                        startNewGame();
                    }
                }
            } else if (cell == '~') {
                board.setCell(r, c, 'M');
                ImageIcon missIcon = loadIcon("src/miss.png", 60, 60);
                btn.setIcon(missIcon);
                btn.setDisabledIcon(missIcon);
                btn.setForeground(new Color(215, 180, 0)); // yellow-ish
                missCount++;
                totalMiss++;

                if (missCount == 5) {
                    strikeCount++;
                    missCount = 0;
                    if (strikeCount == 3) {
                        gameOver = true;
                        displays.update(totalHit, totalMiss, missCount, strikeCount);
                        int choice = JOptionPane.showConfirmDialog(
                                BattleshipGUI.this,
                                "Three strikes—game over.\nPlay again?",
                                "Defeat",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (choice == JOptionPane.YES_OPTION) {
                            startNewGame();
                        }
                    }
                }
            }
            // For already clicked cells ('M' or 'X'), do nothing (we disable the button).

            displays.update(totalHit, totalMiss, missCount, strikeCount);
            btn.setEnabled(false);
        }
    }

    /**
     * Handles the status displays for the game.
     * Updates the labels that track misses, strikes, total misses, and total hits.
     */
    private class Displays {
        private final JLabel missLbl;
        private final JLabel strikeLbl;
        private final JLabel totalMissLbl;
        private final JLabel totalHitLbl;

        /**
         * Constructs a Displays manager with references to the status labels.
         *
         * @param missLbl       label for current miss streak
         * @param strikeLbl     label for strike count
         * @param totalMissLbl  label for total misses
         * @param totalHitLbl   label for total hits
         */
        Displays(JLabel missLbl, JLabel strikeLbl, JLabel totalMissLbl, JLabel totalHitLbl) {
            this.missLbl = missLbl;
            this.strikeLbl = strikeLbl;
            this.totalMissLbl = totalMissLbl;
            this.totalHitLbl = totalHitLbl;
        }

        /**
         * Updates all status labels with the latest game statistics.
         *
         * @param totalHit    total number of hits
         * @param totalMiss   total number of misses
         * @param missCount   current miss streak
         * @param strikeCount current strike count
         */
        void update(int totalHit, int totalMiss, int missCount, int strikeCount) {
            missLbl.setText("Misses (streak): " + missCount);
            strikeLbl.setText("Strikes: " + strikeCount);
            totalMissLbl.setText("Total Misses: " + totalMiss);
            totalHitLbl.setText("Total Hits: " + totalHit);
        }
    }

    /**
     * Utility method to load and resize an image from disk.
     *
     * @param path   the file path to the image
     * @param width  desired width in pixels
     * @param height desired height in pixels
     * @return a scaled ImageIcon
     */
    private ImageIcon loadIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    /**
     * Builds and manages the 10x10 grid of buttons that represent the game board.
     * Each button is initialized with a water icon and wired to fire at its coordinates.
     */
    private class ButtonGrid {
        private final JButton[][] buttons = new JButton[10][10];

        /**
         * Constructs the button grid and adds it to the given panel.
         *
         * @param panel the JPanel to contain the grid
         * @param game  the Game logic handler
         * @param board the Board model
         */
        ButtonGrid(JPanel panel, Game game, Board board) {
            panel.setLayout(new GridLayout(board.size(), board.size(), 2, 2));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            for (int r = 0; r < board.size(); r++) {
                for (int c = 0; c < board.size(); c++) {
                    ImageIcon waveIcon = loadIcon("src/wave.png", 60, 60);
                    JButton btn = new JButton(waveIcon);
                    btn.setDisabledIcon(waveIcon);
                    btn.setBackground(new Color(180, 210, 255));
                    btn.setFocusPainted(false);
                    btn.setFont(new Font("Consolas", Font.BOLD, 16));
                    final int row = r;
                    final int col = c;
                    ActionListener listener = e -> game.fire(row, col, btn);
                    btn.addActionListener(listener);
                    buttons[r][c] = btn;
                    panel.add(btn);
                }
            }
        }

        /**
         * Enables or disables all buttons in the grid.
         *
         * @param enable true to enable, false to disable
         */
        void enableAll(boolean enable) {
            for (int r = 0; r < buttons.length; r++) {
                for (int c = 0; c < buttons[r].length; c++) {
                    buttons[r][c].setEnabled(enable);
                }
            }
        }
    }

    /**
     * Entry point for the Battleship game.
     * Launches the GUI on the Swing event dispatch thread.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BattleshipGUI::new);
    }
}