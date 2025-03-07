package chess.piecemoves;

import chess.*;
import java.util.ArrayList;



public class BishopMoves {
    public ArrayList<ChessMove> returnBishopMoves(ChessBoard board, ChessPosition startingPosition, ChessGame.TeamColor myColor) {
        ArrayList<ChessMove> availableMoves = new ArrayList<>();

        getMovesInDirection(board, startingPosition, 1, 1, myColor, availableMoves);
        getMovesInDirection(board, startingPosition, 1, -1, myColor, availableMoves);
        getMovesInDirection(board, startingPosition, -1, 1, myColor, availableMoves);
        getMovesInDirection(board, startingPosition, -1, -1, myColor, availableMoves);


//
//        // up and right
//
//        while (rowNum < 8 && colNum < 8) {
//            rowNum++;
//            colNum++;
//            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
//            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
//            if (pieceAtCurrPosition != null) {
//                if (pieceAtCurrPosition.getTeamColor() == myColor) {
//                    if ((rowNum - 1) != startingRow && (colNum - 1) != startingCol) {
//                        ChessPosition backUpPosition = new ChessPosition(rowNum - 1, colNum - 1);
//                        tryAddMove(startingPosition, backUpPosition, availableMoves);
//                    }
//                    else {
//                        break;
//                    }
//                }
//                else {
//                    tryAddMove(startingPosition, currPosition, availableMoves);
//                }
//                break;
//            }
//            tryAddMove(startingPosition, currPosition, availableMoves);
//        }
//
//        // up and left
//        rowNum = startingRow;
//        colNum = startingCol;
//        while (rowNum < 8 && colNum > 1) {
//            rowNum++;
//            colNum--;
//            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
//            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
//            if (pieceAtCurrPosition != null) {
//                if (pieceAtCurrPosition.getTeamColor() == myColor) {
//                    if (rowNum - 1 >= 1 && (rowNum - 1) != startingRow && colNum + 1 <= 8 && (colNum + 1) != startingCol) {
//                        ChessPosition backUpPosition = new ChessPosition(rowNum - 1, colNum + 1);
//                        ChessMove currMove = new ChessMove(startingPosition, backUpPosition, null);
//                        availableMoves.add(currMove);
//                    }
//                    else {
//                        break;
//                    }
//                }
//                else {
//                    ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
//                    availableMoves.add(currMove);
//                }
//                break;
//            }
//            ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
//            availableMoves.add(currMove);
//        }
//
//        // down and right
//        rowNum = startingRow;
//        colNum = startingCol;
//        while (rowNum > 1 && colNum < 8) {
//            rowNum--;
//            colNum++;
//            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
//            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
//            if (pieceAtCurrPosition != null) {
//                if (pieceAtCurrPosition.getTeamColor() == myColor) {
//                    if (rowNum + 1 <= 8 && (rowNum + 1) != startingRow && colNum - 1 >= 1 && (colNum - 1) != startingCol) {
//                        ChessPosition backUpPosition = new ChessPosition(rowNum + 1, colNum - 1);
//                        ChessMove currMove = new ChessMove(startingPosition, backUpPosition, null);
//                        availableMoves.add(currMove);
//                    }
//                    else {
//                        break;
//                    }
//                }
//                else {
//                    ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
//                    availableMoves.add(currMove);
//                }
//                break;
//            }
//            ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
//            availableMoves.add(currMove);
//        }
//
//        // down and left
//        rowNum = startingRow;
//        colNum = startingCol;
//        while (rowNum > 1 && colNum > 1) {
//            rowNum--;
//            colNum--;
//            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
//            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
//            if (pieceAtCurrPosition != null) {
//                if (pieceAtCurrPosition.getTeamColor() == myColor) {
//                    if (rowNum + 1 <= 8 && (rowNum + 1) != startingRow && colNum + 1 <= 8 && (colNum + 1) != startingCol) {
//                        ChessPosition backUpPosition = new ChessPosition(rowNum + 1, colNum + 1);
//                        ChessMove currMove = new ChessMove(startingPosition, backUpPosition, null);
//                        availableMoves.add(currMove);
//                    }
//                    else {
//                        break;
//                    }
//                }
//                else {
//                    ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
//                    availableMoves.add(currMove);
//                }
//                break;
//            }
//            tryAddMove(startingPosition, currPosition, null);
//        }

        return availableMoves;
    }



    public void getMovesInDirection(ChessBoard board, ChessPosition startingPosition, int rowInc, int colInc, ChessGame.TeamColor myColor, ArrayList<ChessMove> availableMoves) {
        int rowNum = startingPosition.getRow();
        int colNum = startingPosition.getColumn();

        // always add increment

        while (rowNum + rowInc <= 8 && rowNum + rowInc >= 1 && colNum + colInc <= 8 && colNum + colInc >= 1) {
            rowNum = rowNum + rowInc;
            colNum = colNum + colInc;
            ChessPosition currPosition = new ChessPosition(rowNum, colNum);
            ChessPiece pieceAtCurrPosition = board.getPiece(currPosition);
            if (pieceAtCurrPosition != null) {
                if (pieceAtCurrPosition.getTeamColor() == myColor) {
                    if ((rowNum - rowInc) != startingPosition.getRow() && (colNum - colInc) != startingPosition.getColumn()) {
                        ChessPosition backUpPosition = new ChessPosition(rowNum - rowInc, colNum - rowInc);
                        ChessMove currMove = new ChessMove(startingPosition, backUpPosition, null);
                        if (!availableMoves.contains(currMove)) {
                            availableMoves.add(currMove);
                        }
                    }
                    else {
                        break;
                    }
                }
                else {
                    ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
                    if (!availableMoves.contains(currMove)) {
                        availableMoves.add(currMove);
                    }
                }
                break;

            }
            ChessMove currMove = new ChessMove(startingPosition, currPosition, null);
            if (!availableMoves.contains(currMove)) {
                availableMoves.add(currMove);
            }
        }


    }

}



