package chess.PieceMoves;
import chess.*;

import java.awt.*;
import java.util.ArrayList;

public class KnightMoves {

    // okay! already know the piece (knight of course!)
    // but she's gonna need to know also the location of other pieces on the board
    // i think theres a method or something for this in ChessBoard
    // like i can check the spot on the chess board and see if its there?

    // ok yes its ChessBoard.getPiece and you pass in ChessPosition chessPosition (it's row then col oh and its one
    // based!


    // mm we're also gonna need to know the color of this piece
    // OH I KNOW ChessPiece.pieceColor  !!!!!

    public ArrayList<ChessMove> returnKnightMoves(ChessBoard board, ChessPosition startingPosition, ChessGame.TeamColor myColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ArrayList<ChessMove> availableMoves = new ArrayList<>();

        int startingRow = startingPosition.getRow();
        int startingCol = startingPosition.getColumn();

        int rowNum = startingRow;
        int colNum = startingCol;

        // going up? (and to the right!)
        while (rowNum <= 6 && colNum <= 7) {

            rowNum = rowNum + 2;
            colNum = colNum + 1;

            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
            ChessPiece pieceAtNewPosition = board.getPiece(currPosition);
            ChessGame.TeamColor otherPieceColor = null;
            if (pieceAtNewPosition != null) {
                otherPieceColor = pieceAtNewPosition.getTeamColor();
            }
            if (otherPieceColor != myColor) {
                ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                availableMoves.add(currMove);
            }
        }

        rowNum = startingRow;
        colNum = startingCol;

        // hands up like the celieing cant hold us but left this time
        while (rowNum <= 6 && colNum >= 2) {

            rowNum = rowNum + 2;
            colNum = colNum - 1;

            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
            ChessPiece pieceAtNewPosition = board.getPiece(currPosition);
            ChessGame.TeamColor otherPieceColor = null;
            if (pieceAtNewPosition != null) {
                otherPieceColor = pieceAtNewPosition.getTeamColor();
            }
            if (otherPieceColor != myColor) {
                ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                availableMoves.add(currMove);
            }
        }

        rowNum = startingRow;
        colNum = startingCol;


        // get low low low low (right)
        while (rowNum >= 3 && colNum <= 7) {
            rowNum = rowNum - 2;
            colNum = colNum + 1;

            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
            ChessPiece pieceAtNewPosition = board.getPiece(currPosition);
            ChessGame.TeamColor otherPieceColor = null;
            if (pieceAtNewPosition != null) {
                otherPieceColor = pieceAtNewPosition.getTeamColor();
            }
            if (otherPieceColor != myColor) {
                ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                availableMoves.add(currMove);
            }
        }

        // to the left to the left (and still down)
        while (rowNum >= 3 && colNum >= 2) {
            rowNum = rowNum - 2;
            colNum = colNum - 1;

            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
            ChessPiece pieceAtNewPosition = board.getPiece(currPosition);
            ChessGame.TeamColor otherPieceColor = null;
            if (pieceAtNewPosition != null) {
                otherPieceColor = pieceAtNewPosition.getTeamColor();
            }
            if (otherPieceColor != myColor) {
                ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                availableMoves.add(currMove);
            }
        }
        return availableMoves;
    }
}
