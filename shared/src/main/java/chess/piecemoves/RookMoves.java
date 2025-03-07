package chess.piecemoves;

import chess.*;

import java.util.ArrayList;

public class RookMoves {

//    Rooks may move in straight lines as far as there is open space. If there is an
//    enemy piece at the end of the line, rooks may move to that position and capture the enemy piece.


    public ArrayList<ChessMove> returnRookMoves(ChessBoard board, ChessPosition startingPosition, ChessGame.TeamColor myColor) {
        ArrayList<ChessMove> availableMoves = new ArrayList<>();

        getMovesInDirections(board, startingPosition, 1, 0, myColor, availableMoves);
        getMovesInDirections(board, startingPosition, -1, 0, myColor, availableMoves);
        getMovesInDirections(board, startingPosition, 0, 1, myColor, availableMoves);
        getMovesInDirections(board, startingPosition, 0, -1, myColor, availableMoves);

        return availableMoves;
    }

    public void getMovesInDirections(ChessBoard board, ChessPosition startingPosition, int rowInc, int colInc, ChessGame.TeamColor myColor, ArrayList<ChessMove> availableMoves) {
        int rowNum = startingPosition.getRow();
        int colNum = startingPosition.getColumn();

        while(rowNum + rowInc <= 8 && rowNum + rowInc >= 1 && colNum + colInc <= 8 && colNum + colInc >=1) {
            rowNum = rowNum + rowInc;
            colNum = colNum + colInc;
            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
            if (pieceAtCurrPosition != null) {
                if (pieceAtCurrPosition.getTeamColor() == myColor) {
                    if ((rowNum - rowInc) != startingPosition.getRow() && (colNum - colInc) != startingPosition.getColumn()) {
                        ChessPosition backUpPosition = new ChessPosition(rowNum - 1, colNum);
                        ChessMove currMove = new ChessMove(startingPosition, backUpPosition, null);
                        availableMoves.add(currMove);
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
