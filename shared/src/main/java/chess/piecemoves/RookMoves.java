package chess.piecemoves;

import chess.*;

import java.util.ArrayList;

public class RookMoves {

//    Rooks may move in straight lines as far as there is open space. If there is an
//    enemy piece at the end of the line, rooks may move to that position and capture the enemy piece.


    public ArrayList<ChessMove> returnRookMoves(ChessBoard board, ChessPosition startingPosition, ChessGame.TeamColor myColor) {
        ArrayList<ChessMove> availableMoves = new ArrayList<>();

        getMoves(board, startingPosition, 1, 0, myColor, availableMoves);
        getMoves(board, startingPosition, -1, 0, myColor, availableMoves);
        getMoves(board, startingPosition, 0, 1, myColor, availableMoves);
        getMoves(board, startingPosition, 0, -1, myColor, availableMoves);

        return availableMoves;
    }

    public void getMoves(ChessBoard board, ChessPosition start, int rowInc, int colInc, ChessGame.TeamColor color, ArrayList<ChessMove> available) {
        int rowNum = start.getRow();
        int colNum = start.getColumn();

        while(rowNum + rowInc <= 8 && rowNum + rowInc >= 1 && colNum + colInc <= 8 && colNum + colInc >=1) {
            rowNum = rowNum + rowInc;
            colNum = colNum + colInc;
            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
            if (pieceAtCurrPosition != null) {
                if (pieceAtCurrPosition.getTeamColor() == color) {
                    if ((rowNum - rowInc) != start.getRow() && (colNum - colInc) != start.getColumn()) {
                        ChessPosition backUpPosition = new ChessPosition(rowNum - 1, colNum);
                        ChessMove currMove = new ChessMove(start, backUpPosition, null);
                        available.add(currMove);
                    }
                    else {
                        break;
                    }
                }
                else {
                    ChessMove currMove = new ChessMove(start, currPosition, null);
                    if (!available.contains(currMove)) {
                        available.add(currMove);
                    }
                }
                break;
            }
            ChessMove currMove = new ChessMove(start, currPosition, null);
            if (!available.contains(currMove)) {
                available.add(currMove);
            }
        }



    }

}
