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
    private ChessBoard board = new ChessBoard();


    public ChessGame() {
        board.resetBoard();
        setTeamTurn(TeamColor.WHITE);
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

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        ChessPiece originalPiece = board.getPiece(startPosition);
        if (board.getPiece(startPosition) == null) {
            return null;
        }

        Collection<ChessMove> openMoves;
        ChessPiece piece = board.getPiece(startPosition);
        openMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();


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

    public void makeMove(ChessMove move) throws InvalidMoveException {

        if (board.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("no piece at starting position");
        }
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());

        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("not a valid move");
        }

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

        // make move
        // remove piece from starting position
        board.addPiece(move.getStartPosition(), null);
        // add to where it ends up
        board.addPiece(move.getEndPosition(), pieceToMove);


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
                    else if (board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.KING) {
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

//    Returns true if the given team has no way to protect their king from being captured.
    public boolean isInCheckmate(TeamColor teamColor) {
        // see if its in check
        if (isInCheck(teamColor)) {
            return getOutOfCheck(teamColor);
        }
        return false;
    }

    private boolean getOutOfCheck(TeamColor teamColor) {
        for (int i = 1; i <= 8; ++i) {
            for (int k = 1; k <= 8; ++k) {
                ChessPosition currPosition = new ChessPosition(i, k);
                ChessPiece currPiece = board.getPiece(currPosition);
                if (currPiece != null && currPiece.getTeamColor() == teamColor) {
                        Collection<ChessMove> availableMoves = validMoves(currPosition);
                        if (!availableMoves.isEmpty()) {
                            return false;
                        }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return getOutOfCheck(teamColor);
        }
        return false;
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
