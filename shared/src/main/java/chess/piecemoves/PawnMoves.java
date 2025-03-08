package chess.piecemoves;

import chess.*;

import java.util.ArrayList;

public class PawnMoves {
    public ArrayList<ChessMove> returnPawnMoves(ChessBoard board, ChessPosition startingPosition, ChessGame.TeamColor
            myColor) {
        ArrayList<ChessMove> availableMoves = new ArrayList<>();
        int startingRow = startingPosition.getRow();
        int startingColumn = startingPosition.getColumn();


        // if its the first time the pawn is moved it can be moved forward 2 squares

        if (myColor == ChessGame.TeamColor.WHITE) {
            if (startingRow == 2) {
                ChessPosition oneUp = new ChessPosition(3, startingColumn);
                ChessPosition currPosition = new ChessPosition(4, startingColumn);
                if (isAcceptableMove(currPosition, startingPosition) && isAcceptableMove(oneUp,
                        startingPosition) && !isOccupied(board, currPosition) && !isOccupied(board, oneUp)) {
                    ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                    if (canBePromoted(currPosition, myColor)) {
                        currMove = new ChessMove(startingPosition, currPosition, ChessPiece.PieceType.ROOK);
                        availableMoves.add(currMove);
                        currMove = new ChessMove(startingPosition, currPosition, ChessPiece.PieceType.KNIGHT);
                        availableMoves.add(currMove);
                        currMove = new ChessMove(startingPosition, currPosition, ChessPiece.PieceType.BISHOP);
                        availableMoves.add(currMove);
                        currMove = new ChessMove(startingPosition, currPosition, ChessPiece.PieceType.QUEEN);
                        availableMoves.add(currMove);
                    } else {
                        availableMoves.add(currMove);
                    }
                }
            }


            // take diagonally
            ChessPosition diagonalPosition = new ChessPosition(startingRow + 1, startingColumn + 1);
            if (isAcceptableMove(diagonalPosition, startingPosition)) {
                ChessPiece pieceAtDiagonal = board.getPiece(diagonalPosition);
                if (pieceAtDiagonal != null) {
                    if (pieceAtDiagonal.getTeamColor() == ChessGame.TeamColor.BLACK) {
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
                }
            }
            ChessPosition otherDiagonal = new ChessPosition(startingRow + 1, startingColumn - 1);
            if (isAcceptableMove(otherDiagonal, startingPosition)) {
                ChessPiece otherPiece = board.getPiece(otherDiagonal);
                if (otherPiece != null) {
                    if (otherPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        ChessMove currMove = new ChessMove(startingPosition, otherDiagonal, null);
                        if (canBePromoted(otherDiagonal, myColor)) {
                            currMove = new ChessMove(startingPosition, otherDiagonal, ChessPiece.PieceType.ROOK);
                            availableMoves.add(currMove);
                            currMove = new ChessMove(startingPosition, otherDiagonal, ChessPiece.PieceType.KNIGHT);
                            availableMoves.add(currMove);
                            currMove = new ChessMove(startingPosition, otherDiagonal, ChessPiece.PieceType.BISHOP);
                            availableMoves.add(currMove);
                            currMove = new ChessMove(startingPosition, otherDiagonal, ChessPiece.PieceType.QUEEN);
                            availableMoves.add(currMove);
                        } else {
                            availableMoves.add(currMove);
                        }
                    }
                }
            }

            // just go up one
            ChessPosition currPosition = new ChessPosition(startingRow + 1, startingColumn);
            if (isAcceptableMove(currPosition, startingPosition) && !isOccupied(board, currPosition)) {
                ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                if (canBePromoted(currPosition, myColor)) {
                    promotePiece(currMove, startingPosition, currPosition, availableMoves);

                } else {
                    availableMoves.add(currMove);
                }
            }

        }


        if (myColor == ChessGame.TeamColor.BLACK) {
            if (startingRow == 7) {
                ChessPosition downJustOne = new ChessPosition(6, startingColumn);
                ChessPosition currPosition = new ChessPosition(5, startingColumn);
                if (isAcceptableMove(currPosition, startingPosition) && isAcceptableMove(
                        downJustOne, startingPosition) && !isOccupied(board, downJustOne) &&
                        !isOccupied(board, currPosition)) {
                    ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                    if (canBePromoted(currPosition, myColor)) {
                        currMove = new ChessMove(startingPosition, currPosition, ChessPiece.PieceType.ROOK);
                        availableMoves.add(currMove);
                        currMove = new ChessMove(startingPosition, currPosition, ChessPiece.PieceType.KNIGHT);
                        availableMoves.add(currMove);
                        currMove = new ChessMove(startingPosition, currPosition, ChessPiece.PieceType.BISHOP);
                        availableMoves.add(currMove);
                        currMove = new ChessMove(startingPosition, currPosition, ChessPiece.PieceType.QUEEN);
                        availableMoves.add(currMove);
                    } else {
                        availableMoves.add(currMove);
                    }
                }
            }


            // take diagonally
            ChessPosition diagonalPosition = new ChessPosition(startingRow - 1, startingColumn + 1);
            if (isAcceptableMove(diagonalPosition, startingPosition)) {
                ChessPiece pieceAtDiagonal = board.getPiece(diagonalPosition);
                if (pieceAtDiagonal != null) {
                    if (pieceAtDiagonal.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        ChessMove currMove = new ChessMove(startingPosition, diagonalPosition, null);
                        if (canBePromoted(diagonalPosition, myColor)) {
                            promotePiece(currMove, startingPosition, diagonalPosition, availableMoves);
                        } else {
                            availableMoves.add(currMove);
                        }
                    }
                }
            }

            ChessPosition otherDiagonal = new ChessPosition(startingRow - 1, startingColumn - 1);
            if (isAcceptableMove(otherDiagonal, startingPosition)) {
                ChessPiece otherPiece = board.getPiece(otherDiagonal);
                if (otherPiece != null) {
                    if (otherPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        ChessMove currMove = new ChessMove(startingPosition, otherDiagonal, null);
                        if (canBePromoted(otherDiagonal, myColor)) {
                            promotePiece(currMove, startingPosition, otherDiagonal, availableMoves);
                        } else {
                            availableMoves.add(currMove);
                        }
                    }
                }
            }

            // just go down one
            ChessPosition currPosition = new ChessPosition(startingRow - 1, startingColumn);
            if (isAcceptableMove(currPosition, startingPosition) && !isOccupied(board, currPosition)) {
                ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                if (canBePromoted(currPosition, myColor)) {
                    promotePiece(currMove, startingPosition, currPosition, availableMoves);
                } else {
                    availableMoves.add(currMove);
                }
            }


            // take diagonally

        }
            return availableMoves;
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
        if (isAcceptableMove(one, start) && !isOccupied(board, one) &&
                isAcceptableMove(position, start) && !isOccupied(board, one)) {
                ChessMove currMove = new ChessMove(start, position, null);
                if (canBePromoted(position, color)) {
                    currMove = new ChessMove(start, position, ChessPiece.PieceType.ROOK);
                    available.add(currMove);
                    currMove = new ChessMove(start, position, ChessPiece.PieceType.KNIGHT);
                    available.add(currMove);
                    currMove = new ChessMove(start, position, ChessPiece.PieceType.BISHOP);
                    available.add(currMove);
                    currMove = new ChessMove(start, position, ChessPiece.PieceType.QUEEN);
                    available.add(currMove);
                }
                else {
                    available.add(currMove);
                }
            }
    }

    public void promotePiece(ChessMove currMove, ChessPosition startingPosition, ChessPosition currPosition,
                             ArrayList<ChessMove> availableMoves) {
        currMove = new ChessMove(startingPosition, currPosition, ChessPiece.PieceType.ROOK);
        availableMoves.add(currMove);
        currMove = new ChessMove(startingPosition, currPosition, ChessPiece.PieceType.KNIGHT);
        availableMoves.add(currMove);
        currMove = new ChessMove(startingPosition, currPosition, ChessPiece.PieceType.BISHOP);
        availableMoves.add(currMove);
        currMove = new ChessMove(startingPosition, currPosition, ChessPiece.PieceType.QUEEN);
        availableMoves.add(currMove);
    }


}