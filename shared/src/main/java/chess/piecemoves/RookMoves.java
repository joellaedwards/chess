package chess.piecemoves;

import chess.*;

import java.util.ArrayList;

public class RookMoves {

//    Rooks may move in straight lines as far as there is open space. If there is an
//    enemy piece at the end of the line, rooks may move to that position and capture the enemy piece.


    public ArrayList<ChessMove> returnRookMoves(ChessBoard board, ChessPosition startingPosition, ChessGame.TeamColor myColor) {
        ArrayList<ChessMove> availableMoves = new ArrayList<>();
        int startingRow = startingPosition.getRow();
        int startingCol = startingPosition.getColumn();
        int rowNum = startingRow;
        int colNum = startingCol;

        // straight until you hit a piece
        // going up!

        while (rowNum < 8) {
            rowNum++;
            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
            if (pieceAtCurrPosition != null) {
                if (pieceAtCurrPosition.getTeamColor() == myColor) {
                    if (rowNum - 1 >= 1 && (rowNum - 1) != startingRow) {
                        ChessPosition backUpPosition = new ChessPosition(rowNum - 1, colNum);
                        ChessMove currMove = new ChessMove(startingPosition, backUpPosition, null);
                        availableMoves.add(currMove);
                    }
                    else {
                        break;
                    }
                }
                else {
                    ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                    availableMoves.add(currMove);
                }
                break;
            }
            ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
            availableMoves.add(currMove);
        }

        rowNum = startingRow;
        while (rowNum > 1) {
            rowNum--;
            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
            if (pieceAtCurrPosition != null) {
                if (pieceAtCurrPosition.getTeamColor() == myColor) {
                    if (rowNum + 1 <= 8 && (rowNum + 1) != startingRow) {
                        ChessPosition backUpPosition = new ChessPosition(rowNum + 1, colNum);
                        tryAddMove(startingPosition, backUpPosition, availableMoves);
                    }
                    else {
                        break;
                    }
                }
                else {
                    ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                    availableMoves.add(currMove);
                }
                break;
            }
            else {
                ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                availableMoves.add(currMove);
            }
        }

        rowNum = startingRow;
        while (colNum < 8) {
            colNum++;
            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
            if (pieceAtCurrPosition != null) {
                if (pieceAtCurrPosition.getTeamColor() == myColor) {
                    if (colNum - 1 >= 1 && (colNum - 1) != startingCol) {
                        ChessPosition backUpPosition = new ChessPosition(rowNum, colNum - 1);
                        tryAddMove(startingPosition, backUpPosition, availableMoves);
                    }
                    else {
                        break;
                    }
                }
                else {
                    ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                    availableMoves.add(currMove);
                }
                break;
            }
            ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
            availableMoves.add(currMove);
        }

        colNum = startingCol;
        while (colNum > 1) {
            colNum--;
            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
            if (pieceAtCurrPosition != null) {
                if (pieceAtCurrPosition.getTeamColor() == myColor) {
                    if (colNum + 1 <= 8 && (colNum + 1) != startingCol) {
                        ChessPosition backUpPosition = new ChessPosition(rowNum, colNum + 1);
                        tryAddMove(startingPosition, backUpPosition, availableMoves);
                    }
                    else {
                        break;
                    }
                }
                else {
                    ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                    availableMoves.add(currMove);
                }
                break;
            }
            ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
            availableMoves.add(currMove);
        }

        return availableMoves;
    }



    public void tryAddMove(ChessPosition startingPosition, ChessPosition backUpPosition, ArrayList<ChessMove> availableMoves) {
        ChessMove currMove = new ChessMove(startingPosition, backUpPosition, null);
        if (!availableMoves.contains(currMove)) {
            availableMoves.add(currMove);
        }
    }

}
