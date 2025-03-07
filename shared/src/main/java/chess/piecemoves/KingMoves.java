package chess.piecemoves;

import chess.*;
import java.util.ArrayList;

import static chess.piecemoves.KnightMoves.onBoard;

//Kings may move 1 square in any direction (including diagonal) to either a position occupied by an enemy piece (capturing the enemy piece), or to an unoccupied position. A player is not allowed to make any move that would allow the opponent to capture their King. If your King is in danger of being captured on your turn, you must
//make a move that removes your King from immediate danger.

public class KingMoves {
    public ArrayList<ChessMove> returnKingMoves(ChessBoard board, ChessPosition startingPosition, ChessGame.TeamColor myColor) {

        ArrayList<ChessMove> availableMoves = new ArrayList<>();


        // up one
        ChessPosition newPosition = new ChessPosition(startingPosition.getRow() + 1, startingPosition.getColumn());
        if (isAcceptableMove(board, newPosition, myColor)) {
            ChessMove newMove = new ChessMove(startingPosition, newPosition, null);
            availableMoves.add(newMove);
        }
        // and to the right diagonal
        newPosition = new ChessPosition(startingPosition.getRow() + 1, startingPosition.getColumn() + 1);
        if (isAcceptableMove(board, newPosition, myColor)) {
            ChessMove newMove = new ChessMove(startingPosition, newPosition, null);
            availableMoves.add(newMove);
        }


        // to the right
        newPosition = new ChessPosition(startingPosition.getRow(), startingPosition.getColumn() + 1);
        if (isAcceptableMove(board, newPosition, myColor)) {
            ChessMove newMove = new ChessMove(startingPosition, newPosition, null);
            availableMoves.add(newMove);
        }


        //down and to the right diagonal
        newPosition = new ChessPosition(startingPosition.getRow() - 1, startingPosition.getColumn() + 1);
        if (isAcceptableMove(board, newPosition, myColor)) {
            ChessMove newMove = new ChessMove(startingPosition, newPosition, null);
            availableMoves.add(newMove);
        }
        // down one
        newPosition = new ChessPosition(startingPosition.getRow() - 1, startingPosition.getColumn());
        if (isAcceptableMove(board, newPosition, myColor)) {
            ChessMove newMove = new ChessMove(startingPosition, newPosition, null);
            availableMoves.add(newMove);
        }


        // down left diagonal
        newPosition = new ChessPosition(startingPosition.getRow() - 1, startingPosition.getColumn() - 1);
        if (isAcceptableMove(board, newPosition, myColor)) {
            ChessMove newMove = new ChessMove(startingPosition, newPosition, null);
            availableMoves.add(newMove);
        }

        // left
        newPosition = new ChessPosition(startingPosition.getRow(), startingPosition.getColumn() - 1);
        if (isAcceptableMove(board, newPosition, myColor)) {
            ChessMove newMove = new ChessMove(startingPosition, newPosition, null);
            availableMoves.add(newMove);
        }

        // left up diagonal
        newPosition = new ChessPosition(startingPosition.getRow() + 1, startingPosition.getColumn() - 1);
        if (isAcceptableMove(board, newPosition, myColor)) {
            ChessMove newMove = new ChessMove(startingPosition, newPosition, null);
            availableMoves.add(newMove);
        }

        return availableMoves;
    }


    public boolean isAcceptableMove(ChessBoard board, ChessPosition position, ChessGame.TeamColor myColor) {
        return onBoard(board, position, myColor);
    }


}
