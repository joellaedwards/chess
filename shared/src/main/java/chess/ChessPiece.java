package chess;

import chess.piecemoves.*;

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

    // deep copy constructor
    public ChessPiece(ChessPiece other) {
        this.pieceColor = other.pieceColor;
        this.type = other.type;
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


    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        // ok return pieceMoves ArrayList but get that even from a function doesnt matter where u put it tbh
        if (type == PieceType.KNIGHT) {
            return new KnightMoves().returnKnightMoves(board, myPosition, pieceColor);
        }
        else if (type == PieceType.ROOK) {
            return new RookMoves().returnRookMoves(board, myPosition, pieceColor);
        }
        else if (type == PieceType.BISHOP) {
            return new BishopMoves().returnBishopMoves(board, myPosition, pieceColor);
        }
        else if (type == PieceType.QUEEN) {
            return new QueenMoves().returnQueenMoves(board, myPosition, pieceColor);
        }
        else if (type == PieceType.KING) {
            return new KingMoves().returnKingMoves(board, myPosition, pieceColor);
        }
        else if (type == PieceType.PAWN) {
            return new PawnMoves().returnPawnMoves(board, myPosition, pieceColor);
        }
        else {
            return null;
        }

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
