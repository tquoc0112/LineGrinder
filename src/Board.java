import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Board {

    private BoardGUI gui;
    private int[][] boardMatrix;

    public Board(int sideLength, int boardSize) {
        gui = new BoardGUI(sideLength, boardSize);
        boardMatrix = new int[boardSize][boardSize];
    }

    public Board(Board board) {
        int[][] matrixToCopy = board.getBoardMatrix();
        boardMatrix = new int[matrixToCopy.length][matrixToCopy.length];
        for (int i = 0; i < matrixToCopy.length; i++) {
            for (int j = 0; j < matrixToCopy.length; j++) {
                boardMatrix[i][j] = matrixToCopy[i][j];
            }
        }
    }

    public int getBoardSize() {
        return boardMatrix.length;
    }

    public void removeStoneNoGUI(int posX, int posY) {
        boardMatrix[posY][posX] = 0;
    }

    public void addStoneNoGUI(int posX, int posY, boolean black) {
        boardMatrix[posY][posX] = black ? 2 : 1;
    }

    public boolean addStone(int posX, int posY, boolean black) {
        if (boardMatrix[posY][posX] != 0) return false;

        gui.drawStone(posX, posY, black);
        boardMatrix[posY][posX] = black ? 2 : 1;
        return true;
    }

    public ArrayList<int[]> generateMoves() {
        ArrayList<int[]> moveList = new ArrayList<>();

        int boardSize = boardMatrix.length;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {

                if (boardMatrix[i][j] > 0) continue;

                if (hasAdjacentStone(i, j)) {
                    moveList.add(new int[]{i, j});
                }
            }
        }

        return moveList;
    }

    private boolean hasAdjacentStone(int i, int j) {
        int boardSize = boardMatrix.length;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int ni = i + dx;
                int nj = j + dy;

                if (ni >= 0 && ni < boardSize && nj >= 0 && nj < boardSize && boardMatrix[ni][nj] > 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

    public void startListening(MouseListener listener) {
        gui.attachListener(listener);
    }

    public BoardGUI getGUI() {
        return gui;
    }

    public int getRelativePos(int x) {
        return gui.getRelativePos(x);
    }

    public void printWinner(int winner) {
        gui.printWinner(winner);
    }

    public void thinkingStarted(int depth) {
        gui.updateAIStatus(depth, 0, 0);
    }

    public void updateAIStatus(int depth, int casesCalculated, long calculationTime) {
        gui.updateAIStatus(depth, casesCalculated, calculationTime);
    }

    public void thinkingFinished() {
        gui.updateAIStatus(0, 0, 0);
    }
}
