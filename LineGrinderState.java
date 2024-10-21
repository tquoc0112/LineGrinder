import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

class LineGrinderState {
    public static final int NONE = 0;
    public static final int X = 1;
    public static final int O = 2;

    private int[][] board;
    private int currentPlayer;
    private int size;

    public LineGrinderState(int size) {
        this.size = size;
        board = new int[size][size];
        currentPlayer = X;
    }

    public int getPiece(int row, int col) {
        return board[row][col];
    }

    public void playPiece(int row, int col) {
        if (board[row][col] == NONE) {
            board[row][col] = currentPlayer;
            currentPlayer = (currentPlayer == X) ? O : X;
        }
    }

    public int getWinner() {
        // Logic for determining the winner (checking rows, columns, and diagonals)
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int piece = getPiece(row, col);
                if (piece != NONE) {
                    if (checkDirection(row, col, 1, 0, piece) || // Check horizontally
                            checkDirection(row, col, 0, 1, piece) || // Check vertically
                            checkDirection(row, col, 1, 1, piece) || // Check diagonal down-right
                            checkDirection(row, col, 1, -1, piece)) { // Check diagonal down-left
                        return piece;
                    }
                }
            }
        }
        return NONE;
    }

    private boolean checkDirection(int row, int col, int rowDir, int colDir, int piece) {
        int count = 0;
        for (int i = 0; i < 5; i++) {
            int newRow = row + i * rowDir;
            int newCol = col + i * colDir;
            if (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size
                    && getPiece(newRow, newCol) == piece) {
                count++;
            } else {
                break;
            }
        }
        return count == 5;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }
}
