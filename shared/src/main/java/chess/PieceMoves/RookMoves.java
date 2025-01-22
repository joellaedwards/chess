package chess.PieceMoves;

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
            ChessGame.TeamColor otherPieceColor = null;
            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
            if (pieceAtCurrPosition != null) {
                if (pieceAtCurrPosition.getTeamColor() == myColor) {
                    ChessPosition backUpPosition = new ChessPosition(rowNum - 1, colNum);
                    ChessMove currMove = new ChessMove(startingPosition, backUpPosition, null);
                    System.out.println("adding " + currMove.toString());
                    availableMoves.add(currMove);
                }
                else {
                    ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                    System.out.println("adding " + currMove.toString());
                    availableMoves.add(currMove);
                }
                break;
            }
            ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
            System.out.println("adding " + currMove.toString());
            availableMoves.add(currMove);
        }

        rowNum = startingRow;
        while (rowNum > 1) {
            rowNum--;
            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
            ChessGame.TeamColor otherPieceColor = null;
            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
            if (pieceAtCurrPosition != null) {
                if (pieceAtCurrPosition.getTeamColor() == myColor) {
                    ChessPosition backUpPosition = new ChessPosition(rowNum + 1, colNum);
                    ChessMove currMove = new ChessMove(startingPosition, backUpPosition, null);
                    if (!availableMoves.contains(currMove)) {
                        System.out.println("adding " + currMove.toString());
                        availableMoves.add(currMove);
                    }
                }
                else {
                    ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                    System.out.println("adding " + currMove.toString());
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
            ChessGame.TeamColor otherPieceColor = null;
            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
            if (pieceAtCurrPosition != null) {
                if (pieceAtCurrPosition.getTeamColor() == myColor) {
                    ChessPosition backUpPosition = new ChessPosition(rowNum, colNum - 1);
                    ChessMove currMove = new ChessMove(startingPosition, backUpPosition, null);
                    if (!availableMoves.contains(currMove)) {
                        System.out.println("adding " + currMove.toString());
                        availableMoves.add(currMove);
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
            ChessGame.TeamColor otherPieceColor = null;
            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
            if (pieceAtCurrPosition != null) {
                if (pieceAtCurrPosition.getTeamColor() == myColor) {
                    ChessPosition backUpPosition = new ChessPosition(rowNum, colNum + 1);
                    ChessMove currMove = new ChessMove(startingPosition, backUpPosition, null);
                    if (!availableMoves.contains(currMove)) {
                        System.out.println("adding " + currMove.toString());
                        availableMoves.add(currMove);
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
        // TODO make that above a function and call it for every for loop. up down left right


        return availableMoves;
    }

}
