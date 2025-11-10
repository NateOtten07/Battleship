import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.Image;

/**
 * Builds and manages the 10x10 grid of buttons that represent the game board.
 */
public class ButtonGrid {
    private final JButton[][] buttons = new JButton[10][10];

    /**
     * Constructs the button grid and adds it to the given panel.
     *
     * @param panel the JPanel to contain the grid
     * @param game  the Game logic handler
     * @param board the Board model
     */
    public ButtonGrid(JPanel panel, Game game, Board board) {
        panel.setLayout(new GridLayout(board.size(), board.size(), 2, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int r = 0; r < board.size(); r++) {
            for (int c = 0; c < board.size(); c++) {
                // MODIFIED: Call local loadIcon method
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
    public void enableAll(boolean enable) {
        for (int r = 0; r < buttons.length; r++) {
            for (int c = 0; c < buttons[r].length; c++) {
                buttons[r][c].setEnabled(enable);
            }
        }
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