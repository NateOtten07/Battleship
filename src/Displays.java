import javax.swing.JLabel;

/**
 * Handles the status displays for the game.
 * Updates the labels that track misses, strikes, total misses, and total hits.
 */
public class Displays {
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
    public Displays(JLabel missLbl, JLabel strikeLbl, JLabel totalMissLbl, JLabel totalHitLbl) {
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
    public void update(int totalHit, int totalMiss, int missCount, int strikeCount) {
        missLbl.setText("Misses (streak): " + missCount);
        strikeLbl.setText("Strikes: " + strikeCount);
        totalMissLbl.setText("Total Misses: " + totalMiss);
        totalHitLbl.setText("Total Hits: " + totalHit);
    }
}