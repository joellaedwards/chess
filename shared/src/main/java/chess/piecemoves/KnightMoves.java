package chess.piecemoves;
import chess.*;
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
        ArrayList<ChessMove> availableMoves = new ArrayList<>();
        int startingRow = startingPosition.getRow();
        int startingCol = startingPosition.getColumn();
        int rowNum;
        int colNum;

        rowNum = startingRow + 2;
        colNum = startingCol + 1;
        ChessPosition currPosition = new ChessPosition(rowNum, colNum);
        if (isAcceptableMove(board, currPosition, myColor)) {
            ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
            availableMoves.add(currMove);
        }

        rowNum = startingRow + 1;
        colNum = startingCol + 2;
        currPosition = new ChessPosition(rowNum, colNum);
        if (isAcceptableMove(board, currPosition, myColor)) {
            ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
            availableMoves.add(currMove);
        }

        rowNum = startingRow - 1;
        colNum = startingCol + 2;
        currPosition = new ChessPosition(rowNum, colNum);
        if (isAcceptableMove(board, currPosition, myColor)) {
            ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
            availableMoves.add(currMove);
        }

        rowNum = startingRow - 2;
        colNum = startingCol + 1;
        currPosition = new ChessPosition(rowNum, colNum);
        if (isAcceptableMove(board, currPosition, myColor)) {
            ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
            availableMoves.add(currMove);
        }

        rowNum = startingRow - 2;
        colNum = startingCol - 1;
        currPosition = new ChessPosition(rowNum, colNum);
        if (isAcceptableMove(board, currPosition, myColor)) {
            ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
            availableMoves.add(currMove);
        }

        rowNum = startingRow - 1;
        colNum = startingCol - 2;
        currPosition = new ChessPosition(rowNum, colNum);
        if (isAcceptableMove(board, currPosition, myColor)) {
            ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
            availableMoves.add(currMove);
        }

        rowNum = startingRow + 1;
        colNum = startingCol - 2;
        currPosition = new ChessPosition(rowNum, colNum);
        if (isAcceptableMove(board, currPosition, myColor)) {
            ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
            availableMoves.add(currMove);
        }

        rowNum = startingRow + 2;
        colNum = startingCol - 1;
        currPosition = new ChessPosition(rowNum, colNum);
        if (isAcceptableMove(board, currPosition, myColor)) {
            ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
            availableMoves.add(currMove);
        }

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
}
