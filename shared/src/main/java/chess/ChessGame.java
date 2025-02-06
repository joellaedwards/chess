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
//    private ChessBoard copyBoard;


    public ChessGame() {}

//    public ChessBoard copyChessBoard(ChessBoard board) {
//
//        ChessBoard copyBoard = new ChessBoard();
//        for (int i = 1; i < 9; ++i) {
//            for (int k = 1; k < 9; ++k) {
//
//                ChessPosition currPosition = new ChessPosition(i, k);
//
//                if (board.getPiece(currPosition) != null) {
//                    // get the piece that's at this position on the real board
//                    // maybe do an if here
//                    ChessPiece currPiece = board.getPiece(currPosition);
//                    ChessPiece copiedPiece = new ChessPiece(currPiece);
//                    copyBoard.addPiece(currPosition, copiedPiece);
//                }
//            }
//        }
//        return copyBoard;
//    }


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

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        ChessPiece originalPiece = board.getPiece(startPosition);

        if (board.getPiece(startPosition) == null) {
            return null;
        }

        Collection<ChessMove> openMoves;
        ChessPiece piece = board.getPiece(startPosition);
        openMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();

        // deep copy of board

        //  A move is valid if it is a "piece move" for the piece at the input location and making
        //  that move would not leave the team’s king in danger of check

        for (ChessMove move : openMoves) {
            ChessPiece movePiece = originalPiece;
            if (move.getPromotionPiece() != null) {
                movePiece = new ChessPiece(originalPiece.getTeamColor(), move.getPromotionPiece());
            }

            ChessPiece undoPiece = board.getPiece(move.getEndPosition());
            board.addPiece(move.getStartPosition(), null);
            board.addPiece(move.getEndPosition(), movePiece);

            // if it's not in check add it
            if (!isInCheck(originalPiece.getTeamColor())) {
                validMoves.add(move);
            }

            // undo the move
            board.addPiece(move.getEndPosition(), undoPiece);
            board.addPiece(move.getStartPosition(), originalPiece);

        }



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
        ChessPiece originalPiece = board.getPiece(move.getStartPosition());
        if (originalPiece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("not your turn.");
        }

        if (move.getPromotionPiece() == null) {
            pieceToMove = originalPiece;
        }
        else {
            pieceToMove = new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece());
        }

        ChessPiece undoPiece = board.getPiece(move.getEndPosition());
        // remove piece from starting position
        board.addPiece(move.getStartPosition(), null);
        // add to where it ends up
        board.addPiece(move.getEndPosition(), pieceToMove);


        if (isInCheck(originalPiece.getTeamColor())) {
            // undo the move
            board.addPiece(move.getEndPosition(), undoPiece);
            board.addPiece(move.getStartPosition(), originalPiece);
            throw new InvalidMoveException("puts your king in check");
        }

        if (getTeamTurn() == TeamColor.WHITE) {
            setTeamTurn(TeamColor.BLACK);
        }
        else {
            setTeamTurn(TeamColor.WHITE);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // this includes like if the queen was protecting the king you cant move it.
        ChessPosition kingPosition = null;
        ArrayList<ChessPosition> nextTurnMoves = new ArrayList<>();

        // find the king
        for (int i = 1; i <= 8; ++i) {
            for (int k = 1; k <= 8; ++k) {
                ChessPosition currPosition = new ChessPosition(i, k);
                ChessPiece currPiece = board.getPiece(currPosition);
                if (currPiece != null) {

                    // if the opp color, add to nextTurnMoves
                    if (board.getPiece(currPosition).getTeamColor() != teamColor) {
                        for (ChessMove move : board.getPiece(currPosition).pieceMoves(board, currPosition)) {
                            nextTurnMoves.add(move.getEndPosition());
                        }
                    }
                    // if my king, save position
                    else if (board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(currPosition).getTeamColor() == teamColor) {
                        kingPosition = new ChessPosition(currPosition);
                    }
                }
            }
        }

        return (nextTurnMoves.contains(kingPosition));
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
