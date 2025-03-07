package chess.piecemoves;

import chess.*;
import java.util.ArrayList;

public class PawnMoves {
    public ArrayList<ChessMove> returnPawnMoves(ChessBoard board, ChessPosition startingPosition, ChessGame.TeamColor myColor) {
        ArrayList<ChessMove> availableMoves = new ArrayList<>();
        int startingRow = startingPosition.getRow();
        int startingColumn = startingPosition.getColumn();


        // if its the first time the pawn is moved it can be moved forward 2 squares

        if (myColor == ChessGame.TeamColor.WHITE) {
            if (startingRow == 2) {
                ChessPosition oneUp = new ChessPosition(3, startingColumn);
                ChessPosition currPosition = new ChessPosition(4, startingColumn);
                tryPromotion(startingPosition, myColor, availableMoves, oneUp, currPosition, isOccupied(board, currPosition), isOccupied(board, oneUp));
            }



            // take diagonally
            ChessPosition diagonalPosition = new ChessPosition(startingRow + 1, startingColumn + 1);
            if (isOnBoard(diagonalPosition,startingPosition)) {
                ChessPiece pieceAtDiagonal = board.getPiece(diagonalPosition);
                if (pieceAtDiagonal != null) {
                    if (pieceAtDiagonal.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        addPromotion(startingPosition, myColor, availableMoves, diagonalPosition);
                    }
                }
            }
            ChessPosition otherDiagonal = new ChessPosition(startingRow + 1, startingColumn - 1);
            if (isOnBoard(otherDiagonal, startingPosition)) {
                ChessPiece otherPiece = board.getPiece(otherDiagonal);
                if (otherPiece != null) {
                    if (otherPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        addPromotion(startingPosition, myColor, availableMoves, otherDiagonal);
                    }
                }
            }

            // just go up one
            ChessPosition currPosition = new ChessPosition(startingRow + 1, startingColumn);
            moveOneSpot(board, startingPosition, myColor, availableMoves, currPosition);

        }



        if (myColor == ChessGame.TeamColor.BLACK) {
            if (startingRow == 7) {
                ChessPosition downJustOne = new ChessPosition(6, startingColumn);
                ChessPosition currPosition = new ChessPosition(5, startingColumn);
                tryPromotion(startingPosition, myColor, availableMoves, downJustOne, currPosition, isOccupied(board, downJustOne), isOccupied(board, currPosition));
            }


            // take diagonally
            ChessPosition diagonalPosition = new ChessPosition(startingRow - 1, startingColumn + 1);
            takeDiagonally(board, startingPosition, myColor, availableMoves, diagonalPosition);

            ChessPosition otherDiagonal = new ChessPosition(startingRow - 1, startingColumn - 1);
            takeDiagonally(board, startingPosition, myColor, availableMoves, otherDiagonal);

            // just go down one
            ChessPosition currPosition = new ChessPosition(startingRow - 1, startingColumn);
            moveOneSpot(board, startingPosition, myColor, availableMoves, currPosition);

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

    private void takeDiagonally(ChessBoard board, ChessPosition startingPosition, ChessGame.TeamColor myColor, ArrayList<ChessMove> availableMoves, ChessPosition diagonalPosition) {
        if (isOnBoard(diagonalPosition, startingPosition)) {
            ChessPiece pieceAtDiagonal = board.getPiece(diagonalPosition);
            if (pieceAtDiagonal != null) {
                if (pieceAtDiagonal.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    addPromotion(startingPosition, myColor, availableMoves, diagonalPosition);
                }
            }
        }
    }

    private void addPromotion(ChessPosition startingPosition, ChessGame.TeamColor myColor, ArrayList<ChessMove> availableMoves, ChessPosition diagonalPosition) {
        ChessMove currMove = new ChessMove(startingPosition, diagonalPosition, null);
        if (canBePromoted(diagonalPosition, myColor)) {
            currMove = new ChessMove(startingPosition, diagonalPosition, ChessPiece.PieceType.ROOK);
            availableMoves.add(currMove);
            currMove = new ChessMove(startingPosition, diagonalPosition, ChessPiece.PieceType.KNIGHT);
            availableMoves.add(currMove);
            currMove = new ChessMove(startingPosition, diagonalPosition, ChessPiece.PieceType.BISHOP);
            availableMoves.add(currMove);
            currMove = new ChessMove(startingPosition, diagonalPosition, ChessPiece.PieceType.QUEEN);
            availableMoves.add(currMove);
        } else {
            availableMoves.add(currMove);
        }
    }

    private void tryPromotion(ChessPosition startingPosition, ChessGame.TeamColor myColor, ArrayList<ChessMove> availableMoves, ChessPosition downJustOne, ChessPosition currPosition, boolean occupied, boolean occupied2) {
        if (isOnBoard(currPosition, startingPosition) && isOnBoard(downJustOne, startingPosition) && !occupied && !occupied2) {
            addPromotion(startingPosition, myColor, availableMoves, currPosition);
        }
    }

    private void moveOneSpot(ChessBoard board, ChessPosition startingPosition, ChessGame.TeamColor myColor, ArrayList<ChessMove> availableMoves, ChessPosition currPosition) {
        if (isOnBoard(currPosition, startingPosition) && !isOccupied(board, currPosition)) {
            addPromotion(startingPosition, myColor, availableMoves, currPosition);
        }
    }


    public boolean isOnBoard(ChessPosition position, ChessPosition startingPosition) {
        if (position.getRow() < 1 || position.getRow() > 8 || position.getColumn() < 1 || position.getColumn() > 8) {
            return false;
        }
        return position != startingPosition;
    }

    public boolean isOccupied(ChessBoard board, ChessPosition position) {
        return board.getPiece(position) != null;
    }

    public boolean canBePromoted(ChessPosition position, ChessGame.TeamColor myColor) {
        return (myColor == ChessGame.TeamColor.BLACK && position.getRow() == 1) || (myColor == ChessGame.TeamColor.WHITE && position.getRow() == 8);
    }
}
