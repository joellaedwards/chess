package chess.piecemoves;

import chess.*;
import java.util.ArrayList;



public class BishopMoves {
    public ArrayList<ChessMove> returnBishopMoves(ChessBoard board, ChessPosition startingPosition, ChessGame.TeamColor myColor) {
        ArrayList<ChessMove> availableMoves = new ArrayList<>();

        getMoves(board, startingPosition, 1, 1, myColor, availableMoves);
        getMoves(board, startingPosition, 1, -1, myColor, availableMoves);
        getMoves(board, startingPosition, -1, 1, myColor, availableMoves);
        getMoves(board, startingPosition, -1, -1, myColor, availableMoves);

        return availableMoves;
    }



    public void getMoves(ChessBoard board, ChessPosition start, int rowInc, int colInc, ChessGame.TeamColor color, ArrayList<ChessMove> available) {
        int rowNum = start.getRow();
        int colNum = start.getColumn();

        // always add increment

        while (rowNum + rowInc <= 8 && rowNum + rowInc >= 1 && colNum + colInc <= 8 && colNum + colInc >= 1) {
            rowNum = rowNum + rowInc;
            colNum = colNum + colInc;
            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
            if (pieceAtCurrPosition != null) {
                if (pieceAtCurrPosition.getTeamColor() == color) {
                    if ((rowNum - rowInc) != start.getRow() && (colNum - colInc) != start.getColumn()) {
                        ChessPosition backUpPosition = new ChessPosition(rowNum - rowInc, colNum - rowInc);
                        ChessMove currMove = new ChessMove(start, backUpPosition, null);
                        tryAddMove(currMove, available);
                    }
                    else {
                        break;
                    }
                }
                else {
                    ChessMove currMove = new ChessMove(start, currPosition, null);
                    tryAddMove(currMove, available);
                }
                break;

            }
            ChessMove currMove = new ChessMove(start, currPosition, null);
            tryAddMove(currMove, available);
        }


    }

    public void tryAddMove(ChessMove move, ArrayList<ChessMove> available) {
        if (!available.contains(move)) {
            available.add(move);
        }
    }

}



