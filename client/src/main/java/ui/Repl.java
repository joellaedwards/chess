package ui;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import websocket.NotificationHandler;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.*;
import static ui.State.SIGNEDOUT;

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
        System.out.println("notifying now");
        if (serverMessage.getChessGame() != null) {
            System.out.println("currgame: " + serverMessage.getChessGame());
            currGame = serverMessage.getChessGame();
            // TODO switch currGame to update here?
        }
//        if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
////            if (result.contains("join"))
//            System.out.println("load message type");
//            currGame = serverMessage.getChessGame();
////          printGame();
//        }
        System.out.println("printing server message: ");
        System.out.println(serverMessage.getServerMessage());
        printPrompt();
    }


    private void printPrompt() {

//        System.out.println("this is what it returned: " + result);
        // TODO might have to do something fancy when you get the piece at each place like it might look weird
        // from dif perspectices idk

        if (Objects.equals(result, "piece moved")) {
            System.out.println("currGame: " + currGame);
            System.out.print("piece moved successfully.");
        }

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



        if (Objects.equals(result, "redraw black") || Objects.equals(result, "join black")) {
            ChessGame.TeamColor myColor = ChessGame.TeamColor.BLACK;
            System.out.println("drawing black now");
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


        if (result.contains("Games: \n")) {
            System.out.println(result);
        }



        else if (result.contains("invalid") || result.contains("Please") ||
                result.contains("valid") || result.contains("already") || result.contains("authorized")) {
            System.out.print("\n" + RESET_BG_COLOR + SET_TEXT_COLOR_RED + result);
        }
        else if (result.contains("!")) {
            System.out.print("\n" + RESET_BG_COLOR + SET_TEXT_COLOR_GREEN + result);
        }
        else {
//            System.out.println("printing else...");
            System.out.print("\n" + RESET_BG_COLOR + SET_TEXT_COLOR_WHITE + result);
        }


        if (ChessClient.state == SIGNEDOUT) {
            System.out.print("\n" + RESET_BG_COLOR + SET_TEXT_COLOR_WHITE + "[LOGGED OUT] >>> ");
        }
        else {
            System.out.print("\n" + RESET_BG_COLOR + SET_TEXT_COLOR_WHITE + "[LOGGED IN] >>> ");
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
