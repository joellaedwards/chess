package ui;
import chess.*;
import exception.ResponseException;
import websocket.NotificationHandler;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.*;
import static ui.State.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;

public class Repl implements NotificationHandler {
    private final ChessClient client;
    private String result = "";
    private ChessGame currGame = null;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl, this);
    }

    public void run() {
        System.out.println("Welcome to chess game! Enter 'help' for options.");
        System.out.println(client.help());

        Scanner scanner = new Scanner(System.in);
        String line;
        while (!result.equals("quit")) {
            if (!result.equals("move") && !result.contains("join") && !result.equals("piece moved")) {
                printPrompt();
            }
            line = scanner.nextLine();

            try {
//                System.out.println("Line: " + line);
                result = (String) client.eval(line);
//                System.out.println(result);

            } catch (ResponseException e) {
                var msg = e.toString();
                System.out.print(msg);
                throw new RuntimeException(e);
            }

        }
        System.out.println();
    }

    // based on the pet shop idk how itll come into play yet
    public void notify(ServerMessage serverMessage) {
        if (serverMessage.getChessGame() != null) {
            currGame = serverMessage.getChessGame();
        }
        if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            try {
                result = (String) client.eval("redraw");
            } catch (ResponseException e) {
                throw new RuntimeException(e);
            }
            printPrompt();
        }


        if (serverMessage.getServerMessage() != null) {
//            System.out.println("printing server message: ");
            if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
                System.out.print(SET_TEXT_COLOR_RED + serverMessage.getServerMessage());
                System.out.print("\n" + RESET_BG_COLOR + SET_TEXT_COLOR_WHITE + "[IN GAME] >>> ");

            }
//            System.out.println(serverMessage.getServerMessage());
            else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                System.out.println(serverMessage.getServerMessage());
                if (!serverMessage.getServerMessage().contains("Game over.")) {
                    System.out.print("\n" + RESET_BG_COLOR + SET_TEXT_COLOR_WHITE + "[IN GAME] >>> ");

                }
            }

        }
    }


    private void printPrompt() {

//        System.out.println("this is what it returned: " + result);
        // from dif perspectices idk

//        if (Objects.equals(result, "piece moved")) {
////            System.out.println("currGame: " + currGame);
////            System.out.print("piece moved successfully.");
//        }

        if (Objects.equals(result, "redraw white") || Objects.equals(result, "join white")) {
            ChessGame.TeamColor myColor = ChessGame.TeamColor.WHITE;
            ChessGame newGame = new ChessGame();
            ChessBoard currBoard = newGame.getBoard();
            if (currGame != null) {
                currBoard = currGame.getBoard();
            }

            String currColor = SET_BG_COLOR_BLACK;
            boolean leaveColor = false;

            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "     " + "  a  "
                    + "  b  " + "  c  " + "  d  " + "  e  " + "  f  " + "  g  " + "  h  " + "     "
                    + RESET_BG_COLOR);

            // rows, 8 down to 1
            for (int i = 8; i >= 1; --i) {
                leaveColor = true;
                System.out.print(RESET_BG_COLOR + "\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  " + (i) + "  ");
                // columns, a - h
                for (int k = 1; k <= 8; ++k) {
                    if (!leaveColor) {
                        if (Objects.equals(currColor, SET_BG_COLOR_BLACK)) {
                            currColor = SET_BG_COLOR_LIGHT_GREY;
                        } else {
                            currColor = SET_BG_COLOR_BLACK;
                        }
                    }
                    leaveColor = false;
                    ChessPosition currPosition = new ChessPosition(i,k);
                    ChessPiece currPiece = currBoard.getPiece(currPosition);
                    // if no pieces. add empty space.
                    if (currPiece == null) {
                        System.out.print(currColor + "     ");
                    } else if (currPiece.getTeamColor() == myColor) {
                        printPieces(currColor, currPiece, SET_TEXT_COLOR_RED);
                    } else if (currPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        printPieces(currColor, currPiece, SET_TEXT_COLOR_BLUE);
                    }

                }
                System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  " + (i) + "  ");
            }
            System.out.print(RESET_BG_COLOR);
            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "     " + "  a  "
                    + "  b  " + "  c  " + "  d  " + "  e  " + "  f  " + "  g  " + "  h  " + "     "
                    + RESET_BG_COLOR);
        }



        else if (Objects.equals(result, "redraw black") || Objects.equals(result, "join black")) {
            ChessGame.TeamColor myColor = ChessGame.TeamColor.BLACK;
            ChessBoard currBoard = currGame.getBoard();
            String currColor = SET_BG_COLOR_LIGHT_GREY;
            boolean leaveColor = false;

            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "     " + "  h  "
                    + "  g  " + "  f  " + "  e  " + "  d  " + "  c  " + "  b  " + "  a  " + "     "
                    + RESET_BG_COLOR);

            // rows 1 to 8
            for (int i = 1; i <= 8 ; ++i) {
                leaveColor = true;
                System.out.print(RESET_BG_COLOR + "\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  " + (i) + "  ");

                // col h to a
                for (int k = 8; k >= 1; --k) {
                    if (!leaveColor) {
                        if (Objects.equals(currColor, SET_BG_COLOR_BLACK)) {
                            currColor = SET_BG_COLOR_LIGHT_GREY;
                        } else {
                            currColor = SET_BG_COLOR_BLACK;
                        }
                    }
                    leaveColor = false;
                    ChessPosition currPosition = new ChessPosition(i,k);
                    ChessPiece currPiece = currBoard.getPiece(currPosition);
                    // if no pieces. add empty space.
                    if (currPiece == null) {
                        System.out.print(currColor + "     ");
                    } else if (currPiece.getTeamColor() == myColor) {
                        printPieces(currColor, currPiece, SET_TEXT_COLOR_BLUE);
                    } else {
                        printPieces(currColor, currPiece, SET_TEXT_COLOR_RED);
                    }

                }
                System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  " + (i) + "  ");
            }
            System.out.print(RESET_BG_COLOR);
            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "     " + "  h  "
                    + "  g  " + "  f  " + "  e  " + "  d  " + "  c  " + "  b  " + "  a  " + "     "
                    + RESET_BG_COLOR);
        }

        else if (result.contains("Highlight white ")) {
            String[] vals = result.split("\\s+");

            int row = Integer.parseInt(vals[2]);
            int col = Integer.parseInt(vals[3]);

            ChessPosition startPos = new ChessPosition(row, col);
            Collection<ChessMove> validMoves = currGame.validMoves(startPos);
            ArrayList<ChessPosition> highlight = new ArrayList<>();

            for (ChessMove move : validMoves) {
                highlight.add(move.getEndPosition());
            }


            // PRINT BOARD

            ChessGame.TeamColor myColor = ChessGame.TeamColor.WHITE;
            ChessGame newGame = new ChessGame();
            ChessBoard currBoard = newGame.getBoard();
            if (currGame != null) {
                currBoard = currGame.getBoard();
            }

            String highlightColor = SET_BG_COLOR_DARK_GREEN;
            String currColor = SET_BG_COLOR_BLACK;
            boolean leaveColor = false;

            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "     " + "  a  "
                    + "  b  " + "  c  " + "  d  " + "  e  " + "  f  " + "  g  " + "  h  " + "     "
                    + RESET_BG_COLOR);

            // rows, 8 down to 1
            for (int i = 8; i >= 1; --i) {
                leaveColor = true;
                System.out.print(RESET_BG_COLOR + "\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  " + (i) + "  ");
                // columns, a - h
                for (int k = 1; k <= 8; ++k) {
                    if (!leaveColor) {
                        if (Objects.equals(currColor, SET_BG_COLOR_BLACK)) {
                            currColor = SET_BG_COLOR_LIGHT_GREY;
                            highlightColor = SET_BG_COLOR_GREEN;
                        } else {
                            currColor = SET_BG_COLOR_BLACK;
                            highlightColor = SET_BG_COLOR_DARK_GREEN;
                        }
                    }
                    leaveColor = false;
                    ChessPosition currPosition = new ChessPosition(i,k);
                    ChessPiece currPiece = currBoard.getPiece(currPosition);
                    // if no pieces. add empty space.
                    if (currPiece == null) {
                        if (highlight.contains(currPosition)) {
                            System.out.print(highlightColor + "     ");

                        } else {
                            System.out.print(currColor + "     ");
                        }
                    } else if (currPiece.getTeamColor() == myColor) {
                        if (currPosition.getRow() == startPos.getRow() && currPosition.getColumn() == startPos.getColumn()) {
                            printPieces(SET_BG_COLOR_YELLOW, currPiece, SET_TEXT_COLOR_RED);
                        }
                        else {
                            printPieces(currColor, currPiece, SET_TEXT_COLOR_RED);
                        }
                    } else if (currPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        if (highlight.contains(currPosition)) {
                            printPieces(highlightColor, currPiece, SET_TEXT_COLOR_BLUE);
                            System.out.print(currColor);
                        } else {
                            printPieces(currColor, currPiece, SET_TEXT_COLOR_BLUE);
                        }
                    }

                }
                System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  " + (i) + "  ");
            }
            System.out.print(RESET_BG_COLOR);
            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "     " + "  a  "
                    + "  b  " + "  c  " + "  d  " + "  e  " + "  f  " + "  g  " + "  h  " + "     "
                    + RESET_BG_COLOR);
        }


        else if (result.contains("Highlight black ")) {
            String[] vals = result.split("\\s+");

            int row = Integer.parseInt(vals[2]);
            int col = Integer.parseInt(vals[3]);

            ChessPosition startPos = new ChessPosition(row, col);
            Collection<ChessMove> validMoves = currGame.validMoves(startPos);
            ArrayList<ChessPosition> highlight = new ArrayList<>();

            for (ChessMove move : validMoves) {
                highlight.add(move.getEndPosition());
            }


            // PRINT BOARD

            ChessGame.TeamColor myColor = ChessGame.TeamColor.BLACK;

            String highlightColor = SET_BG_COLOR_GREEN;
            ChessBoard currBoard = currGame.getBoard();
            String currColor = SET_BG_COLOR_LIGHT_GREY;
            boolean leaveColor = false;

            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "     " + "  h  "
                    + "  g  " + "  f  " + "  e  " + "  d  " + "  c  " + "  b  " + "  a  " + "     "
                    + RESET_BG_COLOR);

            // rows 1 to 8
            for (int i = 1; i <= 8 ; ++i) {
                leaveColor = true;
                System.out.print(RESET_BG_COLOR + "\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  " + (i) + "  ");

                // col h to a
                for (int k = 8; k >= 1; --k) {
                    if (!leaveColor) {
                        if (Objects.equals(currColor, SET_BG_COLOR_BLACK)) {
                            currColor = SET_BG_COLOR_LIGHT_GREY;
                            highlightColor = SET_BG_COLOR_GREEN;
                        } else {
                            currColor = SET_BG_COLOR_BLACK;
                            highlightColor = SET_BG_COLOR_DARK_GREEN;
                        }
                    }
                    leaveColor = false;
                    ChessPosition currPosition = new ChessPosition(i,k);
                    ChessPiece currPiece = currBoard.getPiece(currPosition);
                    // if no pieces. add empty space.
                    if (currPiece == null) {
                        if (highlight.contains(currPosition)) {
                            System.out.print(highlightColor + "     ");

                        } else {
                            System.out.print(currColor + "     ");
                        }
                    } else if (currPiece.getTeamColor() == myColor) {
                        if (currPosition.getRow() == startPos.getRow() && currPosition.getColumn() == startPos.getColumn()) {
                            printPieces(SET_BG_COLOR_YELLOW, currPiece, SET_TEXT_COLOR_BLUE);
                        }
                        else {
                            printPieces(currColor, currPiece, SET_TEXT_COLOR_BLUE);
                        }
                    } else {
                        if (highlight.contains(currPosition)) {
                            printPieces(highlightColor, currPiece, SET_TEXT_COLOR_RED);
                            System.out.print(currColor);
                        } else {
                            printPieces(currColor, currPiece, SET_TEXT_COLOR_RED);
                        }
                    }

                }
                System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  " + (i) + "  ");
            }
            System.out.print(RESET_BG_COLOR);
            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "     " + "  h  "
                    + "  g  " + "  f  " + "  e  " + "  d  " + "  c  " + "  b  " + "  a  " + "     "
                    + RESET_BG_COLOR);
        }


        else if (result.contains("Games: \n")) {
            System.out.println(result);
        }



        else if (result.contains("invalid") || result.contains("Please") ||
                result.contains("valid") || result.contains("already") || result.contains("authorized")) {
            System.out.print("\n" + RESET_BG_COLOR + SET_TEXT_COLOR_RED + result);
        }
        else if (result.contains("!")) {
            System.out.print("\n" + RESET_BG_COLOR + SET_TEXT_COLOR_GREEN + result);
        }
        else if (result.contains("Help")) {
            System.out.print("\n" + RESET_BG_COLOR + SET_TEXT_COLOR_WHITE + result);
        }
        else {
//            System.out.println("printing else...");
            System.out.print("\n" + RESET_BG_COLOR + SET_TEXT_COLOR_WHITE + result);
        }


        if (ChessClient.state == SIGNEDOUT) {
            System.out.print("\n" + RESET_BG_COLOR + SET_TEXT_COLOR_WHITE + "[LOGGED OUT] >>> ");
        }
        else if (ChessClient.state == SIGNEDIN){
            System.out.print("\n" + RESET_BG_COLOR + SET_TEXT_COLOR_WHITE + "[LOGGED IN] >>> ");
        }
        else if (ChessClient.state == INGAME) {
            System.out.print("\n" + RESET_BG_COLOR + SET_TEXT_COLOR_WHITE + "[IN GAME] >>> ");
        }
    }

    private void printPieces(String currColor, ChessPiece currPiece, String setTextColor) {
        String textColor1;
        textColor1 = setTextColor;
        if (currPiece.getPieceType().equals(ChessPiece.PieceType.KING)) {
            System.out.print(currColor + textColor1 + "  K  ");
        } else if (currPiece.getPieceType().equals(ChessPiece.PieceType.ROOK)) {
            System.out.print(currColor + textColor1 + "  R  ");
        } else if (currPiece.getPieceType().equals(ChessPiece.PieceType.KNIGHT)) {
            System.out.print(currColor + textColor1 + "  N  ");
        } else if (currPiece.getPieceType().equals(ChessPiece.PieceType.BISHOP)) {
            System.out.print(currColor + textColor1 + "  B  ");
        } else if (currPiece.getPieceType().equals(ChessPiece.PieceType.QUEEN)) {
            System.out.print(currColor + textColor1 + "  Q  ");
        } else if (currPiece.getPieceType().equals(ChessPiece.PieceType.PAWN)) {
            System.out.print(currColor + textColor1 + "  P  ");
        }
    }

}