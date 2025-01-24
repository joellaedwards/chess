package chess.PieceMoves;

import chess.*;
import java.util.ArrayList;



public class BishopMoves {
    public ArrayList<ChessMove> returnBishopMoves(ChessBoard board, ChessPosition startingPosition, ChessGame.TeamColor myColor) {
        ArrayList<ChessMove> availableMoves = new ArrayList<>();
        int startingRow = startingPosition.getRow();
        int startingCol = startingPosition.getColumn();
        int rowNum = startingRow;
        int colNum = startingCol;


        // up and right

        while (rowNum < 8 && colNum < 8) {
            rowNum++;
            colNum++;
            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
            if (pieceAtCurrPosition != null) {
                if (pieceAtCurrPosition.getTeamColor() == myColor) {
                    if (rowNum - 1 >= 1 && (rowNum - 1) != startingRow && colNum - 1 >= 1 && (colNum - 1) != startingCol) {
                        ChessPosition backUpPosition = new ChessPosition(rowNum - 1, colNum - 1);
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

        // up and left
        rowNum = startingRow;
        colNum = startingCol;
        while (rowNum < 8 && colNum > 1) {
            rowNum++;
            colNum--;
            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
            if (pieceAtCurrPosition != null) {
                if (pieceAtCurrPosition.getTeamColor() == myColor) {
                    if (rowNum - 1 >= 1 && (rowNum - 1) != startingRow && colNum + 1 <= 8 && (colNum + 1) != startingCol) {
                        ChessPosition backUpPosition = new ChessPosition(rowNum - 1, colNum + 1);
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

        // down and right
        rowNum = startingRow;
        colNum = startingCol;
        while (rowNum > 1 && colNum < 8) {
            rowNum--;
            colNum++;
            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
            if (pieceAtCurrPosition != null) {
                if (pieceAtCurrPosition.getTeamColor() == myColor) {
                    if (rowNum + 1 <= 8 && (rowNum + 1) != startingRow && colNum - 1 >= 1 && (colNum - 1) != startingCol) {
                        ChessPosition backUpPosition = new ChessPosition(rowNum + 1, colNum - 1);
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

        // down and left
        rowNum = startingRow;
        colNum = startingCol;
        while (rowNum > 1 && colNum > 1) {
            rowNum--;
            colNum--;
            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
            if (pieceAtCurrPosition != null) {
                if (pieceAtCurrPosition.getTeamColor() == myColor) {
                    if (rowNum + 1 <= 8 && (rowNum + 1) != startingRow && colNum + 1 <= 8 && (colNum + 1) != startingCol) {
                        ChessPosition backUpPosition = new ChessPosition(rowNum + 1, colNum + 1);
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

        return availableMoves;
    }



}



