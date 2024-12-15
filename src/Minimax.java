import java.util.ArrayList;

public class Minimax {
    public static int evaluationCount = 0;
    private Board board;
    private static final int WIN_SCORE = 100_000_000;
    public Minimax(Board board) {
        this.board = board;
    }

    public static int getWinScore() {
        return WIN_SCORE;
    }

    public static double evaluateBoardForWhite(Board board, boolean blacksTurn) {
        evaluationCount++;

        double blackScore = getScore(board, true, blacksTurn);
        double whiteScore = getScore(board, false, blacksTurn);

        if (blackScore == 0) blackScore = 1.0;

        return whiteScore / blackScore;
    }

    public static int getScore(Board board, boolean forBlack, boolean blacksTurn) {

        int[][] boardMatrix = board.getBoardMatrix();
        return evaluateHorizontal(boardMatrix, forBlack, blacksTurn) +
                evaluateVertical(boardMatrix, forBlack, blacksTurn) +
                evaluateDiagonal(boardMatrix, forBlack, blacksTurn);
    }

    public int[] calculateNextMove(int depth) {
        board.thinkingStarted(depth);

        int[] move = new int[2];

        long startTime = System.currentTimeMillis();

        Object[] bestMove = searchWinningMove(board);

        if (bestMove != null) {
            move[0] = (Integer) (bestMove[1]);
            move[1] = (Integer) (bestMove[2]);

        } else {
            bestMove = minimaxSearchAB(depth, new Board(board), true, -1.0, getWinScore());
            if (bestMove[1] == null) {
                move = null;
            } else {
                move[0] = (Integer) (bestMove[1]);
                move[1] = (Integer) (bestMove[2]);
            }
        }

        long calculationTime = System.currentTimeMillis() - startTime;
        board.updateAIStatus(depth, evaluationCount, calculationTime);

        evaluationCount = 0;

        return move;
    }

    private static Object[] minimaxSearchAB(int depth, Board dummyBoard, boolean max, double alpha, double beta) {

        if (depth == 0) {
            Object[] x = {evaluateBoardForWhite(dummyBoard, !max), null, null};
            return x;
        }
        ArrayList<int[]> allPossibleMoves = dummyBoard.generateMoves();

        if (allPossibleMoves.size() == 0) {
            Object[] x = {evaluateBoardForWhite(dummyBoard, !max), null, null};
            return x;
        }

        Object[] bestMove = new Object[3];

        if (max) {
            bestMove[0] = -1.0;
            for (int[] move : allPossibleMoves) {

                dummyBoard.addStoneNoGUI(move[1], move[0], false);
                Object[] tempMove = minimaxSearchAB(depth - 1, dummyBoard, false, alpha, beta);
                dummyBoard.removeStoneNoGUI(move[1], move[0]);

                if ((Double) (tempMove[0]) > alpha) {
                    alpha = (Double) (tempMove[0]);
                }

                if ((Double) (tempMove[0]) >= beta) {
                    return tempMove;
                }
                if ((Double) tempMove[0] > (Double) bestMove[0]) {
                    bestMove = tempMove;
                    bestMove[1] = move[0];
                    bestMove[2] = move[1];
                }
            }
        } else {
            bestMove[0] = 100_000_000.0;
            bestMove[1] = allPossibleMoves.get(0)[0];
            bestMove[2] = allPossibleMoves.get(0)[1];

            for (int[] move : allPossibleMoves) {
                dummyBoard.addStoneNoGUI(move[1], move[0], true);

                Object[] tempMove = minimaxSearchAB(depth - 1, dummyBoard, true, alpha, beta);

                dummyBoard.removeStoneNoGUI(move[1], move[0]);

                if (((Double) tempMove[0]) < beta) {
                    beta = (Double) (tempMove[0]);
                }

                if ((Double) (tempMove[0]) <= alpha) {
                    return tempMove;
                }

                if ((Double) tempMove[0] < (Double) bestMove[0]) {
                    bestMove = tempMove;
                    bestMove[1] = move[0];
                    bestMove[2] = move[1];
                }
            }
        }

        return bestMove;
    }

    private static Object[] searchWinningMove(Board board) {
        ArrayList<int[]> allPossibleMoves = board.generateMoves();
        Object[] winningMove = new Object[3];

        for (int[] move : allPossibleMoves) {
            evaluationCount++;
            Board dummyBoard = new Board(board);
            dummyBoard.addStoneNoGUI(move[1], move[0], false);

            if (getScore(dummyBoard, false, false) >= WIN_SCORE) {
                winningMove[1] = move[0];
                winningMove[2] = move[1];
                return winningMove;
            }
        }
        return null;
    }

	public static int evaluateHorizontal(int[][] boardMatrix, boolean forBlack, boolean playersTurn ) {

		int[] evaluations = {0, 2, 0};
		for(int i=0; i<boardMatrix.length; i++) {
			for(int j=0; j<boardMatrix[0].length; j++) {
				evaluateDirections(boardMatrix,i,j,forBlack,playersTurn,evaluations);
			}
			evaluateDirectionsAfterOnePass(evaluations, forBlack, playersTurn);
		}

		return evaluations[2];
	}
	
	public static  int evaluateVertical(int[][] boardMatrix, boolean forBlack, boolean playersTurn ) {

		int[] evaluations = {0, 2, 0};
		
		for(int j=0; j<boardMatrix[0].length; j++) {
			for(int i=0; i<boardMatrix.length; i++) {
				evaluateDirections(boardMatrix,i,j,forBlack,playersTurn,evaluations);
			}
			evaluateDirectionsAfterOnePass(evaluations,forBlack,playersTurn);
			
		}
		return evaluations[2];
	}

	public static  int evaluateDiagonal(int[][] boardMatrix, boolean forBlack, boolean playersTurn ) {

		int[] evaluations = {0, 2, 0}; 
		for (int k = 0; k <= 2 * (boardMatrix.length - 1); k++) {
		    int iStart = Math.max(0, k - boardMatrix.length + 1);
		    int iEnd = Math.min(boardMatrix.length - 1, k);
		    for (int i = iStart; i <= iEnd; ++i) {
		        evaluateDirections(boardMatrix,i,k-i,forBlack,playersTurn,evaluations);
		    }
		    evaluateDirectionsAfterOnePass(evaluations,forBlack,playersTurn);
		}
		for (int k = 1-boardMatrix.length; k < boardMatrix.length; k++) {
		    int iStart = Math.max(0, k);
		    int iEnd = Math.min(boardMatrix.length + k - 1, boardMatrix.length-1);
		    for (int i = iStart; i <= iEnd; ++i) {
				evaluateDirections(boardMatrix,i,i-k,forBlack,playersTurn,evaluations);
		    }
			evaluateDirectionsAfterOnePass(evaluations,forBlack,playersTurn);
		}
		return evaluations[2];
	}
	public static void evaluateDirections(int[][] boardMatrix, int i, int j, boolean isBot, boolean botsTurn, int[] eval) {
		if (boardMatrix[i][j] == (isBot ? 2 : 1)) {
			eval[0]++;
		}
		else if (boardMatrix[i][j] == 0) {
			if (eval[0] > 0) {
				eval[1]--;
				eval[2] += getConsecutiveSetScore(eval[0], eval[1], isBot == botsTurn);
				eval[0] = 0;
			}
			eval[1] = 1;
		}
		else if (eval[0] > 0) {
			eval[2] += getConsecutiveSetScore(eval[0], eval[1], isBot == botsTurn);
			eval[0] = 0;
			eval[1] = 2;
		} else {
			eval[1] = 2;
		}
	}
	private static void evaluateDirectionsAfterOnePass(int[] eval, boolean isBot, boolean playersTurn) {
		if (eval[0] > 0) {
			eval[2] += getConsecutiveSetScore(eval[0], eval[1], isBot == playersTurn);
		}
		eval[0] = 0;
		eval[1] = 2;
	}

	public static  int getConsecutiveSetScore(int count, int blocks, boolean currentTurn) {
		final int winGuarantee = 1000000;
		if(blocks == 2 && count < 5) return 0;

		switch(count) {
		case 5: {
			return WIN_SCORE;
		}
		case 4: {
			if(currentTurn) return winGuarantee;
			else {
				if(blocks == 0) return winGuarantee/4;
				else return 200;
			}
		}
		case 3: {
			if(blocks == 0) {
				if(currentTurn) return 50_000;
				else return 200;
			}
			else {

				if(currentTurn) return 10;
				else return 5;
			}
		}
		case 2: {
			if(blocks == 0) {
				if(currentTurn) return 7;
				else return 5;
			}
			else {
				return 3;
			}
		}
		case 1: {
			return 1;
		}
		}

		return WIN_SCORE*2;
	}
}
