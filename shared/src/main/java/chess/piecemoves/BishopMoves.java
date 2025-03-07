package chess.piecemoves;

import chess.*;
import java.util.ArrayList;



public class BishopMoves {
    public ArrayList<ChessMove> returnBishopMoves(ChessBoard board, ChessPosition startingPosition, ChessGame.TeamColor myColor) {
        ArrayList<ChessMove> availableMoves = new ArrayList<>();

        getMovesInDirection(board, startingPosition, 1, 1, myColor, availableMoves);
        getMovesInDirection(board, startingPosition, 1, -1, myColor, availableMoves);
        getMovesInDirection(board, startingPosition, -1, 1, myColor, availableMoves);
        getMovesInDirection(board, startingPosition, -1, -1, myColor, availableMoves);

        return availableMoves;
    }



    public void getMovesInDirection(ChessBoard board, ChessPosition startingPosition, int rowInc, int colInc, ChessGame.TeamColor myColor, ArrayList<ChessMove> availableMoves) {
        int rowNum = startingPosition.getRow();
        int colNum = startingPosition.getColumn();
        // always add increment

        while (rowNum < 8 && rowNum > 1 && colNum < 8 && colNum > 1) {
            rowNum = rowNum + rowInc;
            colNum = colNum + colInc;
            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
            if (pieceAtCurrPosition != null) {
                if (pieceAtCurrPosition.getTeamColor() == myColor) {
                    if ((rowNum - rowInc) != startingPosition.getRow() && (colNum + colInc) != startingPosition.getColumn()) {
                        ChessPosition backUpPosition = new ChessPosition(rowNum - rowInc, colNum + rowInc);
                        ChessMove currMove = new ChessMove(startingPosition, backUpPosition, null);
                        if (!availableMoves.contains(currMove)) {
                            availableMoves.add(currMove);
                        }
                    }
                    else {
                        break;
                    }
                }
                else {
                    ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                    if (!availableMoves.contains(currMove)) {
                        availableMoves.add(currMove);
                    }
                }
                break;
            }
            ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
            if (!availableMoves.contains(currMove)) {
                availableMoves.add(currMove);
            }
        }
    }

}



