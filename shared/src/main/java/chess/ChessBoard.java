package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }


    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int rowNum = position.getRow() - 1;
        int columnNum = position.getColumn() - 1;
        squares[rowNum][columnNum] = piece;
    }



    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int rowNum = position.getRow() - 1;
        int columnNum = position.getColumn() - 1;
        return squares[rowNum][columnNum];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // add pawns
        int row = 7;
        for (int col = 1; col < 9; col++) {
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            addPiece(position, piece);
        }
        row = 2;
        for (int col = 1; col < 9; col++) {
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            addPiece(position, piece);
        }
        // black rook
        int time = 0;
        row = 8;
        int col = 1;
        while (time < 2) {
            time++;
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
            addPiece(position, piece);
            col = 8;
        }
        // black knights
        time = 0;
        col = 2;
        while (time < 2) {
            time++;
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
            addPiece(position, piece);
            col = 7;
        }
        // black bishops
        time = 0;
        col = 3;
        while (time < 2) {
            time++;
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
            addPiece(position, piece);
            col = 6;
        }
        // black queen
        ChessPosition position = new ChessPosition(row, 4);
        ChessPiece piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        addPiece(position, piece);
        // black king
        position = new ChessPosition(row, 5);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        addPiece(position, piece);


        // white rook
        time = 0;
        row = 1;
        col = 1;
        while (time < 2) {
            time++;
            position = new ChessPosition(row, col);
            piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
            addPiece(position, piece);
            col = 8;
        }
        // white knights
        time = 0;
        col = 2;
        while (time < 2) {
            time++;
            position = new ChessPosition(row, col);
            piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
            addPiece(position, piece);
            col = 7;
        }
        // white bishops
        time = 0;
        col = 3;
        while (time < 2) {
            time++;
            position = new ChessPosition(row, col);
            piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
            addPiece(position, piece);
            col = 6;
        }
        // white queen
        position = new ChessPosition(row, 4);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        addPiece(position, piece);
        // white king
        position = new ChessPosition(row, 5);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        addPiece(position, piece);


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChessBoard that)) {
            return false;
        }
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                '}';
    }
}
