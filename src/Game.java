import javax.swing.*;
import java.awt.Color;
import java.awt.Image;

/**
 * Handles the main game logic: firing, tracking hits/misses,
 * and determining win/lose conditions.
 */
public class Game {
    private final Board board;
    private final Displays displays;
    private final BattleshipGUI gui;

    private int missCount = 0;
    private int strikeCount = 0;
    private int totalMiss = 0;
    private int totalHit = 0;
    private boolean gameOver = false;

    /**
     * Constructs a Game with a board, displays, and a GUI reference.
     * @param board    the game board
     * @param displays the status display manager
     * @param gui      the main GUI to call back to
     */
    public Game(Board board, Displays displays, BattleshipGUI gui) {
        this.board = board;
        this.displays = displays;
        this.gui = gui;
    }

    /**
     *
     * @param r   row index
     * @param c   column index
     * @param btn the JButton representing the cell
     */
    public void fire(int r, int c, JButton btn) {
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

            if (!board.hasShipsRemaining()) {
                gameOver = true;
                gui.showWinDialog();
            } else if (sunkName != null) {
                gui.showSunkMessage("You sunk: " + sunkName + "!");
            }

        } else if (cell == '~') {
            board.setCell(r, c, 'M');
            // MODIFIED: Call local loadIcon method
            ImageIcon missIcon = loadIcon("src/miss.png", 60, 60);
            btn.setIcon(missIcon);
            btn.setDisabledIcon(missIcon);
            btn.setForeground(new Color(215, 180, 0));
            missCount++;
            totalMiss++;

            if (missCount == 5) {
                strikeCount++;
                missCount = 0;
                if (strikeCount == 3) {
                    gameOver = true;
                    gui.showLoseDialog();
                }
            }
        }

        displays.update(totalHit, totalMiss, missCount, strikeCount);
        btn.setEnabled(false);
    }

    /**
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
}