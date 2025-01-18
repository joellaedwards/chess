package chess.PieceMoves;
import chess.ChessBoard;
import chess.ChessPosition;

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

    public ArrayList<ChessPosition> returnKnightMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessPosition> moves = new ArrayList<>();
        ArrayList<ChessPosition> availableMoves = new ArrayList<>();

        int startingRow = myPosition.getRow();
        int startingCol = myPosition.getColumn();

        int rowNum = startingRow;
        int colNum = startingCol;

        // going up? (and to the right!)
        while (rowNum <= 8 && colNum <= 8) {

            rowNum = rowNum + 2;
            colNum = colNum + 1;

            // check here if one of our own pieces is there.
            // if not add to availableMoves arraylist

        }

        rowNum = startingRow;
        colNum = startingCol;

        // hands up like the celieing cant hold us but left this time
        while (rowNum <= 8 && colNum >= 1) {

            rowNum = rowNum + 2;
            colNum = colNum - 1;

            // TODO CHECK HERE
        }

        rowNum = startingRow;
        colNum = startingCol;


        // get low low low low (right
        while (rowNum >= 1 && colNum <= 8) {
            rowNum = rowNum - 2;
            colNum = colNum + 1;

            // check here!

        }

        // to the left to the left (and still down)
        while (rowNum >= 1 && colNum >= 1) {
            rowNum = rowNum - 2;
            colNum = colNum - 1;

            // a check! but not in the way u think for chess

        }



        // moves will be anywhere it can go and then we'll for loop for maybe and see ? we'll see what's easiest
        // maybe dont make two dif arrays actually maybe just do the addition/subtraction and then check there like right then

        // how do i get the color? huh

        // if (board.getPiece([thisposition]).pieceColor != myPiecesColor) {
        //    go ahead and add it
        // }


        return availableMoves;
    }



}
