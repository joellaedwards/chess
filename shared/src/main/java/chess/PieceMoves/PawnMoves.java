package chess.PieceMoves;

import chess.*;
import java.util.ArrayList;

public class PawnMoves {
    public ArrayList<ChessMove> returnPawnMoves(ChessBoard board, ChessPosition startingPosition, ChessGame.TeamColor myColor) {
        ArrayList<ChessMove> availableMoves = new ArrayList<>();
        int startingRow = startingPosition.getRow();
        int startingColumn = startingPosition.getColumn();
        int rowNum = startingRow;
        int colNum = startingColumn;


        // if its the first time the pawn is moved it can be moved forward 2 sqares

        if (myColor == ChessGame.TeamColor.WHITE) {
            if (startingRow == 2) {
                ChessPosition currPosition = new ChessPosition(4, colNum);
                if (isAcceptableMove(board, currPosition, myColor)) {
                    ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                    if (canBePromoted(board, currPosition, myColor)) {
                        // TODO should be able to pick promotion type
                        currMove = new ChessMove(startingPosition, currPosition, ChessPiece.PieceType.BISHOP);
                    }
                    availableMoves.add(currMove);
                }
            }


            // take diagonally
            ChessPosition diagonalPosition = new ChessPosition(startingRow + 1, colNum + 1);
            ChessPiece pieceAtDiagonal = board.getPiece(diagonalPosition);
            if (pieceAtDiagonal != null) {
                if (pieceAtDiagonal.getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove currMove = new ChessMove(startingPosition, diagonalPosition, null);
                    if (canBePromoted(board, diagonalPosition, myColor)) {
                        currMove = new ChessMove(startingPosition, diagonalPosition, ChessPiece.PieceType.BISHOP);
                    }
                    availableMoves.add(currMove);
                }
            }
            ChessPosition otherDiagonal = new ChessPosition(startingRow + 1, colNum - 1);
            ChessPiece otherPiece = board.getPiece(otherDiagonal);
            if (otherPiece != null) {
                if (otherPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                    ChessMove currMove = new ChessMove(startingPosition, otherDiagonal, null);
                    if (canBePromoted(board, otherDiagonal, myColor)) {
                        currMove = new ChessMove(startingPosition, otherDiagonal, ChessPiece.PieceType.BISHOP);
                    }
                    availableMoves.add(currMove);
                }
            }

            // just go up one
            ChessPosition currPosition = new ChessPosition(startingRow + 1, colNum);
            if (isAcceptableMove(board, currPosition, myColor)) {
                ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                if (canBePromoted(board, currPosition, myColor)) {
                    currMove = new ChessMove(startingPosition, currPosition, ChessPiece.PieceType.BISHOP);
                    // TODO should be able to pick promotion type
                }
                availableMoves.add(currMove);
            }

        }



        if (myColor == ChessGame.TeamColor.BLACK) {
            if (startingRow == 7) {
                ChessPosition currPosition = new ChessPosition(5, colNum);
                if (isAcceptableMove(board, currPosition, myColor)) {
                    ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                    if (canBePromoted(board, currPosition, myColor)) {
                        // TODO should be able to pick promotion type
                        currMove = new ChessMove(startingPosition, currPosition, ChessPiece.PieceType.BISHOP);
                    }
                    availableMoves.add(currMove);
                }
            }


            // take diagonally
            ChessPosition diagonalPosition = new ChessPosition(startingRow - 1, colNum + 1);
            ChessPiece pieceAtDiagonal = board.getPiece(diagonalPosition);
            if (pieceAtDiagonal != null) {
                if (pieceAtDiagonal.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove currMove = new ChessMove(startingPosition, diagonalPosition, null);
                    if (canBePromoted(board, diagonalPosition, myColor)) {
                        currMove = new ChessMove(startingPosition, diagonalPosition, ChessPiece.PieceType.BISHOP);
                    }
                    availableMoves.add(currMove);
                }
            }
            ChessPosition otherDiagonal = new ChessPosition(startingRow - 1, colNum - 1);
            ChessPiece otherPiece = board.getPiece(otherDiagonal);
            if (otherPiece != null) {
                if (otherPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    ChessMove currMove = new ChessMove(startingPosition, otherDiagonal, null);
                    if (canBePromoted(board, otherDiagonal, myColor)) {
                        currMove = new ChessMove(startingPosition, otherDiagonal, ChessPiece.PieceType.BISHOP);
                    }
                    availableMoves.add(currMove);
                }
            }

            // just go down one
            ChessPosition currPosition = new ChessPosition(startingRow - 1, colNum);
            if (isAcceptableMove(board, currPosition, myColor)) {
                ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                if (canBePromoted(board, currPosition, myColor)) {
                    currMove = new ChessMove(startingPosition, currPosition, ChessPiece.PieceType.BISHOP);
                    // TODO should be able to pick promotion type
                }
                availableMoves.add(currMove);
            }

        }




        // take diagonally




//        Pawns normally may move forward one square if that square is unoccupied, though if it is the first
//        time that pawn is being moved, it may be moved forward 2 squares (provided both squares are unoccupied).
//        Pawns cannot capture forward, but instead capture forward diagonally (1 square forward and 1 square sideways).

//        They may only move diagonally like this if capturing an enemy piece. When a pawn moves to the end of the
//        board (row 8 for white and row 1 for black), they get promoted and are replaced with the
//        player's choice of Rook, Knight, Bishop, or Queen (they cannot stay a Pawn or become King).

        return availableMoves;
    }


    public boolean isAcceptableMove(ChessBoard board, ChessPosition position, ChessGame.TeamColor myColor) {
        if (position.getRow() < 1 || position.getRow() > 8 || position.getColumn() < 1 || position.getColumn() > 8) {
            return false;
        }
        ChessPiece pieceAtNewPosition = board.getPiece(position);
        return pieceAtNewPosition == null;
    }

    public boolean canBePromoted(ChessBoard board, ChessPosition position, ChessGame.TeamColor myColor) {
        return (myColor == ChessGame.TeamColor.BLACK && position.getRow() == 1) || (myColor == ChessGame.TeamColor.WHITE && position.getRow() == 8);
    }
}
