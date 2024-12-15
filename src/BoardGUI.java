import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class BoardGUI extends JPanel {

    private static final long serialVersionUID = 1L;
    private Graphics2D g2D;
    private BufferedImage image;
    private boolean isAIThinking = false;
    private int currentDepth = 0;
    private int casesCalculated = 0;
    private long calculationTime = 0;

    private int sideLength;
    private int boardSize;
    private final int cellLength;
    public BoardGUI(int sideLength, int boardSize) {
        this.sideLength = sideLength;
        this.boardSize = boardSize;
        this.cellLength = sideLength / boardSize;

        image = new BufferedImage(sideLength, sideLength, BufferedImage.TYPE_INT_ARGB);
        g2D = (Graphics2D) image.getGraphics();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        initializeBoardGraphics();
    }

    private void initializeBoardGraphics() {
        g2D.setColor(new Color(0.925F, 0.67F, 0.34F));
        g2D.fillRect(0, 0, sideLength, sideLength);

        g2D.setColor(Color.BLACK);
        for (int i = 1; i <= boardSize; i++) {
            g2D.drawLine(i * cellLength, 0, i * cellLength, sideLength);
            g2D.drawLine(0, i * cellLength, sideLength, i * cellLength);
        }
    }

    public int getRelativePos(int x) {
        if (x >= sideLength) x = sideLength - 1;
        return (int) (x * boardSize / sideLength);
    }

    public Dimension getPreferredSize() {
        return new Dimension(sideLength, sideLength + 50); // Extra space for AI status box
    }

    public void printWinner(int winner) {
        String text = winner == 2 ? "NGƯỜI THẮNG" : (winner == 1 ? "MÁY TÍNH THẮNG" : "HÒA");
        FontMetrics metrics = g2D.getFontMetrics(new Font("Arial", Font.BOLD, 48));

        g2D.setColor(winner == 2 ? Color.GREEN : (winner == 1 ? Color.RED : Color.BLUE));
        g2D.setFont(new Font("Arial", Font.BOLD, 48));
        int x = (sideLength - metrics.stringWidth(text)) / 2;
        int y = sideLength / 2;

        g2D.drawString(text, x, y);
        repaint();
    }

    public void drawStone(int posX, int posY, boolean black) {
        if (posX >= boardSize || posY >= boardSize) return;

        g2D.setColor(black ? Color.BLACK : Color.WHITE);
        g2D.fillOval((int) (cellLength * (posX + 0.05)),
                     (int) (cellLength * (posY + 0.05)),
                     (int) (cellLength * 0.9),
                     (int) (cellLength * 0.9));
        g2D.setColor(Color.BLACK);
        g2D.setStroke(new BasicStroke(2));
        g2D.drawOval((int) (cellLength * (posX + 0.05)),
                     (int) (cellLength * (posY + 0.05)),
                     (int) (cellLength * 0.9),
                     (int) (cellLength * 0.9));

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.drawImage(image, 0, 0, sideLength, sideLength, null);
        drawAIStatus(g2);
        g2.setColor(Color.BLACK);
        g2.drawRect(0, 0, sideLength, sideLength);
    }

    private void drawAIStatus(Graphics2D g2) {
        int statusBoxHeight = 50;
        int yStart = sideLength;

        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, yStart, sideLength, statusBoxHeight);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        String statusText = isAIThinking
                ? String.format("Depth: %d Cases calculated: %d Calculation time: %d ms",
                                currentDepth, casesCalculated, calculationTime)
                : "AI is idle.";
        g2.drawString(statusText, 10, yStart + 30);
    }

    public void updateAIStatus(int depth, int casesCalculated, long calculationTime) {
        this.isAIThinking = depth > 0;
        this.currentDepth = depth;
        this.casesCalculated = casesCalculated;
        this.calculationTime = calculationTime;
        repaint();
    }

    public void attachListener(MouseListener listener) {
        addMouseListener(listener);
    }
}
