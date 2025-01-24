package chess.PieceMoves;

import chess.*;
import java.util.ArrayList;

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

//        ArrayList<ChessMove> bishopMoves = new BishopMoves().returnBishopMoves(board, startingPosition, otherColor);
//        for (ChessMove move : bishopMoves) {
//            availableMoves.remove(move);
//        }
//        ArrayList<ChessMove> knightMoves = new KnightMoves().returnKnightMoves(board, startingPosition, otherColor);
//        for (ChessMove move : knightMoves) {
//            availableMoves.remove(move);
//        }
////        ArrayList<ChessMove> pawnMoves = new PawnMoves().returnPawnMoves(board, startingPosition, otherColor);
////        for (ChessMove move : pawnMoves) {
////            availableMoves.remove(move);
////        }
//        // TODO but it's only for the diagonal moves from the pawn.
//        // TODO it needs to not get queens moves based on this beginning spot weiss du
//        ArrayList<ChessMove> queenMoves = new QueenMoves().returnQueenMoves(board, startingPosition, otherColor);
//        for (ChessMove move : queenMoves) {
//            availableMoves.remove(move);
//        }
//        ArrayList<ChessMove> rookMoves = new QueenMoves().returnQueenMoves(board, startingPosition, otherColor);
//        for (ChessMove move : rookMoves) {
//            availableMoves.remove(move);
//        }

        return availableMoves;
    }


    public boolean isAcceptableMove(ChessBoard board, ChessPosition position, ChessGame.TeamColor myColor) {
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
// if you have to check and make sure the king doesnt move to a spot
    // where it could get taken, call all the other piece moves and make
    // sure the new position isnt in their possible positions





}
