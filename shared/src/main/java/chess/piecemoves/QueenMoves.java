package chess.piecemoves;

import chess.*;
import java.util.ArrayList;


public class QueenMoves {
    public ArrayList<ChessMove> returnQueenMoves(ChessBoard board, ChessPosition startingPosition, ChessGame.TeamColor myColor) {
        ArrayList<ChessMove> availableMoves = new RookMoves().returnRookMoves(board, startingPosition, myColor);
        ArrayList<ChessMove> bishopMoves = new BishopMoves().returnBishopMoves(board, startingPosition, myColor);

        for (ChessMove move : bishopMoves) {
            if (!availableMoves.contains(move)) {
                availableMoves.add(move);
            }
        }
        return availableMoves;
    }
}