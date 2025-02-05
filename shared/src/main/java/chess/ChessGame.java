package chess;

import java.util.ArrayList;
import java.util.Collection;



//This class serves as the top-level management of the Chess Game.
// It is responsible for executing moves as well as reporting the game status.
//
//By default, a new ChessGame represents an immediately playable board with the
// pieces in their default locations and the starting player set to WHITE.
//
//ChessGame functionality will now implement the rules of Chess not handled by the
// ChessPiece class. This will involve removing moves returned from ChessPiece.validMoves()
// that violate game rules.

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor team;
    private ChessBoard board;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */

//    Takes as input a position on the chessboard and returns all moves the piece
//    there can legally make. If there is no piece at that location, this method returns null.

//    A move is valid if it is a "piece move" for the piece at the input location and making
//    that move would not leave the team’s king in danger of check


    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        if (board.getPiece(startPosition) == null) {
            return null;
        }

        Collection<ChessMove> openMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(startPosition);
        openMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();

        // deep copy of board

        //  A move is valid if it is a "piece move" for the piece at the input location and making
        //  that move would not leave the team’s king in danger of check


        for (ChessMove move : openMoves) {
            try {
                makeMove(move);
            } catch (InvalidMoveException) {
                return null;

            }
        }

//        for (ChessMove move : openMoves) {
//
//            // i think make a copy of the board here with this piece in its new position
//            // then check if the team is in check and if its not add to valid moves
//            int row = 4;
//
//          //  if (ChessGame.isInCheck)
//        }

        return validMoves;

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */

//    Receives a given move and executes it, provided it is a legal move.
//    If the move is illegal, it throws an InvalidMoveException. A move is illegal if
//    it is not a "valid" move for the piece at the starting location,
//    (A move is valid if it is a "piece move" for the piece at the input location and making
//     that move would not leave the team’s king in danger of check)
//
//    or if it’s not the corresponding team's turn.
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece pieceToMove;
        if (move.getPromotionPiece() == null) {
            pieceToMove = board.getPiece(move.getStartPosition());
        }
        else {
            pieceToMove = new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece());
        }


        ChessBoard copyBoard = new ChessBoard();
        for (int i = 1; i < 9; ++i) {
            for (int k = 1; k < 9; ++k) {

                ChessPosition currPosition = new ChessPosition(i, k);

                if (board.getPiece(currPosition) != null) {
                    // get the piece that's at this position on the real board
                    // maybe do an if here
                    ChessPiece currPiece = board.getPiece(currPosition);
                    ChessPiece copiedPiece = new ChessPiece(currPiece);
                    copyBoard.addPiece(currPosition, copiedPiece);

                }
            }
        }
        // remove piece from starting position
        copyBoard.addPiece(move.getStartPosition(), null);
        // add to where it ends up
        copyBoard.addPiece(move.getEndPosition(), pieceToMove);

        // execute the move here on the copy board

        if (copyBoard.getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("not your turn.");
        }
        else if (isInCheck(board.getPiece(move.getStartPosition()).getTeamColor())) {
            throw new InvalidMoveException("puts your king in check");
        }
        else {
            board.addPiece(move.getStartPosition(), null);
            board.addPiece(move.getEndPosition(), pieceToMove);
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
