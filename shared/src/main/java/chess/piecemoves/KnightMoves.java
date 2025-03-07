package chess.piecemoves;
import chess.*;

import java.util.ArrayList;

public class KnightMoves {

    public ArrayList<ChessMove> returnKnightMoves(ChessBoard board, ChessPosition startingPosition, ChessGame.TeamColor myColor) {
        ArrayList<ChessMove> availableMoves = new ArrayList<>();

        getMoves(board, startingPosition, 2, 1, myColor, availableMoves);
        getMoves(board, startingPosition, 1, 2, myColor, availableMoves);
        getMoves(board, startingPosition, -1, 2, myColor, availableMoves);
        getMoves(board, startingPosition, -2, 1, myColor, availableMoves);
        getMoves(board, startingPosition, -2, -1, myColor, availableMoves);
        getMoves(board, startingPosition, -1, -2, myColor, availableMoves);
        getMoves(board, startingPosition, 1, -2, myColor, availableMoves);
        getMoves(board, startingPosition, 2, -1, myColor, availableMoves);

        return availableMoves;
    }


    public void getMoves(ChessBoard board, ChessPosition start, int rowInc, int colInc, ChessGame.TeamColor color, ArrayList<ChessMove> available) {
        int rowNum = start.getRow() + rowInc;
        int colNum = start.getColumn() + colInc;
        ChessPosition currPosition = new ChessPosition(rowNum, colNum);
        if (onBoard(board, currPosition, color)) {
            ChessMove currMove = new ChessMove(start, currPosition, null);
            if (!available.contains(currMove)) {
                available.add(currMove);
            }
        }
    }


    static boolean onBoard(ChessBoard board, ChessPosition position, ChessGame.TeamColor myColor) {
        if (position.getRow() < 1 || position.getRow() > 8 || position.getColumn() < 1 || position.getColumn() > 8) {
            return false;
        }
        ChessGame.TeamColor otherPieceColor = null;
        ChessPiece pieceAtNewPosition = board.getPiece(position);
        if (pieceAtNewPosition != null) {
            otherPieceColor = pieceAtNewPosition.getTeamColor();
        }
        return otherPieceColor != myColor;
    }
}
