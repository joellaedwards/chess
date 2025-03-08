package chess.piecemoves;

import chess.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PawnMoves {
    public ArrayList<ChessMove> returnPawnMoves(ChessBoard board, ChessPosition startingPosition, ChessGame.TeamColor
            myColor) {
        ArrayList<ChessMove> availableMoves = new ArrayList<>();
        int startingRow = startingPosition.getRow();
        int startingColumn = startingPosition.getColumn();
        ChessGame.TeamColor otherColor;


        // if its the first time the pawn is moved it can be moved forward 2 squares

        if (myColor == ChessGame.TeamColor.WHITE) {
            otherColor = ChessGame.TeamColor.BLACK;
            if (startingRow == 2) {
                ChessPosition oneUp = new ChessPosition(3, startingColumn);
                ChessPosition currPosition = new ChessPosition(4, startingColumn);
                canMoveTwo(board, currPosition, oneUp, startingPosition, myColor, availableMoves);
            }


            // take diagonally
            ChessPosition diagonalPosition = new ChessPosition(startingRow + 1, startingColumn + 1);
            evalDiagonalMove(board, startingPosition, myColor, availableMoves, diagonalPosition, otherColor);

            ChessPosition otherDiagonal = new ChessPosition(startingRow + 1, startingColumn - 1);
            evalDiagonalMove(board, startingPosition, myColor, availableMoves, otherDiagonal, otherColor);

            // just go up one
            ChessPosition currPosition = new ChessPosition(startingRow + 1, startingColumn);
            evalMove(board, startingPosition, availableMoves, currPosition, myColor);

        }


        if (myColor == ChessGame.TeamColor.BLACK) {
            otherColor = ChessGame.TeamColor.WHITE;
            if (startingRow == 7) {
                ChessPosition downJustOne = new ChessPosition(6, startingColumn);
                ChessPosition currPosition = new ChessPosition(5, startingColumn);
                canMoveTwo(board, currPosition, downJustOne, startingPosition, myColor, availableMoves);
            }


            // take diagonally
            ChessPosition diagonalPosition = new ChessPosition(startingRow - 1, startingColumn + 1);
            evalDiagonalMove(board, startingPosition, myColor, availableMoves, diagonalPosition, otherColor);

            ChessPosition otherDiagonal = new ChessPosition(startingRow - 1, startingColumn - 1);
            evalDiagonalMove(board, startingPosition, myColor, availableMoves, otherDiagonal, otherColor);

            // just go down one
            ChessPosition currPosition = new ChessPosition(startingRow - 1, startingColumn);
            evalMove(board, startingPosition, availableMoves, currPosition, myColor);

        }
            return availableMoves;
        }

    private void evalDiagonalMove(ChessBoard board, ChessPosition startingPosition, ChessGame.TeamColor myColor,
                          ArrayList<ChessMove> availableMoves, ChessPosition diagonalPosition, ChessGame.TeamColor otherColor) {
        if (isAcceptableMove(diagonalPosition, startingPosition)) {
            ChessPiece pieceAtDiagonal = board.getPiece(diagonalPosition);
            if (pieceAtDiagonal != null) {
                if (pieceAtDiagonal.getTeamColor() == otherColor) {
                    if (canBePromoted(diagonalPosition, myColor)) {
                        promotePiece(startingPosition, diagonalPosition, availableMoves);
                    } else {
                        ChessMove currMove = new ChessMove(startingPosition, diagonalPosition, null);
                        availableMoves.add(currMove);
                    }
                }
            }
        }
    }

    private void evalMove(ChessBoard board, ChessPosition startingPosition,
                          ArrayList<ChessMove> availableMoves, ChessPosition position, ChessGame.TeamColor myColor) {
        if (isAcceptableMove(position, startingPosition)) {
            ChessPiece pieceHere = board.getPiece(position);
            if (pieceHere == null) {
                    if (canBePromoted(position, myColor)) {
                        promotePiece(startingPosition, position, availableMoves);
                    } else {
                        ChessMove currMove = new ChessMove(startingPosition, position, null);
                        availableMoves.add(currMove);
                    }
            }
        }
    }

    public boolean isAcceptableMove(ChessPosition position, ChessPosition startingPosition) {
        if (position.getRow() < 1 || position.getRow() > 8 || position.getColumn() < 1 || position.getColumn() > 8) {
            return false;
        }
        return position != startingPosition;
    }

    public boolean isOccupied(ChessBoard board, ChessPosition position) {
        return board.getPiece(position) != null;
    }

    public boolean canBePromoted(ChessPosition position, ChessGame.TeamColor myColor) {
        return (myColor == ChessGame.TeamColor.BLACK && position.getRow() == 1) ||
                (myColor == ChessGame.TeamColor.WHITE && position.getRow() == 8);
    }

    public void canMoveTwo(ChessBoard board, ChessPosition position, ChessPosition one, ChessPosition start,
                              ChessGame.TeamColor color, ArrayList<ChessMove> available) {
        if (isAcceptableMove(position, start) && isAcceptableMove(one,
                start) && !isOccupied(board, position) && !isOccupied(board, one)) {
            if (canBePromoted(position, color)) {
                promotePiece(start, position, available);
            } else {
                ChessMove currMove = new ChessMove(start, position, null);
                available.add(currMove);
            }
        }
    }

    public void promotePiece(ChessPosition startingPosition, ChessPosition currPosition,
                             ArrayList<ChessMove> availableMoves) {
        ChessMove currMove = new ChessMove(startingPosition, currPosition, ChessPiece.PieceType.ROOK);
        availableMoves.add(currMove);
        currMove = new ChessMove(startingPosition, currPosition, ChessPiece.PieceType.KNIGHT);
        availableMoves.add(currMove);
        currMove = new ChessMove(startingPosition, currPosition, ChessPiece.PieceType.BISHOP);
        availableMoves.add(currMove);
        currMove = new ChessMove(startingPosition, currPosition, ChessPiece.PieceType.QUEEN);
        availableMoves.add(currMove);
    }


}