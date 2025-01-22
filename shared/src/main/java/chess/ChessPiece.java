package chess;

import chess.PieceMoves.KnightMoves;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

    // PROMOTION ONLY HAPPENS WHEN A PAWN MOVES <3


    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        // ok return pieceMoves ArrayList but get that even from a function doesnt matter where u put it tbh
        if (type == PieceType.KNIGHT) {
            System.out.println("type = knight! calling knight function");
            return new KnightMoves().returnKnightMoves(board, myPosition, pieceColor);
        }

        else {
            return null;
        }

        // lets do knight first!!!

        // ok so like just make a for loop that adds all the ways it can move and then
        // within the for loop check somehow if someone else is there - might have to
        // go through all the pieces?
        // and just check like is their position there rn?

        // if bishop then call bishop moves given original ChessPosition
        // else if pawn call pawn etc

//        RETURN LIKE THIS
//        var pieceMoves = new ArrayList<>(testPiece.pieceMoves(board, startPosition));



//        This method is similar to ChessGame.validMoves, except it does not honor whose
//        turn it is or check if the king is being attacked. This method DOES!!! account
//        for enemy and friendly pieces blocking movement paths.

//        not implementing ChessGame.validMoves in this part



        // The pieceMoves method will need to take into account the type of piece, and the location of other pieces on the board.


        // you'll retrun all the options of where the bishop can go based on where it is now.
        // return new ArrayList<>()
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChessPiece that)) {
            return false;
        }
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

}
