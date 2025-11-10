import javax.swing.*;
import java.awt.*;

public class BattleshipGUI extends JFrame {


    private JLabel titleLbl;
    private JPanel boardPnl;
    private JPanel statusPnl;
    private JPanel controlPnl;
    private JButton playAgainBtn;
    private JButton quitBtn;
    private JLabel missLbl;
    private JLabel strikeLbl;
    private JLabel totalMissLbl;
    private JLabel totalHitLbl;
    private Board board;
    private Displays displays;
    private Game game;
    private ButtonGrid buttonGrid;

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
     * Starts a new game by resetting the board, displays, and button grid.
     */
    private void startNewGame() {
        board = new Board();
        displays = new Displays(missLbl, strikeLbl, totalMissLbl, totalHitLbl);

        game = new Game(board, displays, this);

        boardPnl.removeAll();


        buttonGrid = new ButtonGrid(boardPnl, game, board);

        boardPnl.revalidate();
        boardPnl.repaint();
        displays.update(0, 0, 0, 0);
    }


    /**
     * Shows a simple message dialog when a ship is sunk.
     * Called by the Game class.
     * @param message The message to display (e.g., "You sunk: Carrier!")
     */
    public void showSunkMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    /**
     * Shows the victory dialog with a "Play Again?" option.
     * Called by the Game class.
     */
    public void showWinDialog() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "You sunk all ships! You win!\nPlay again?",
                "Victory",
                JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION) {
            startNewGame();
        }
    }

    /**
     * Shows the defeat dialog with a "Play Again?" option.
     * Called by the Game class.
     */
    public void showLoseDialog() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Three strikes—game over.\nPlay again?",
                "Defeat",
                JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION) {
            startNewGame();
        }
    }


    /**
     * Entry point for the Battleship game.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BattleshipGUI::new);
    }

    // ... (other private create methods are unchanged) ...
    private void createTitle() {
        titleLbl = new JLabel("Battleship Game", SwingConstants.CENTER);
        titleLbl.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLbl, BorderLayout.NORTH);
    }
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
    private void createBoard() {
        boardPnl = new JPanel();
        add(boardPnl, BorderLayout.CENTER);
    }
}