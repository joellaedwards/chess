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
            printPrompt();
            line = scanner.nextLine();

            try {
//                System.out.println("Line: " + line);
                result = (String) client.eval(line);
//                printPrompt();
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
//        System.out.println(RED + serverMessage.getServerMessage());
        if (serverMessage.getChessGame() != null) {
            currGame = serverMessage.getChessGame();
        }
        if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            System.out.println("you joined " + serverMessage.getGameName());
        }
        System.out.println(serverMessage.getServerMessage());
        printPrompt();
    }



    private void printPrompt() {

//        System.out.println("this is what it returned: " + result);
        String textColor = SET_TEXT_COLOR_RED;


        if (Objects.equals(result, "redraw white")) {
            ChessBoard currBoard = currGame.getBoard();
            String currColor = SET_BG_COLOR_BLACK;
            boolean leaveColor = false;

            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "     " + "  a  "
                    + "  b  " + "  c  " + "  d  " + "  e  " + "  f  " + "  g  " + "  h  " + "     "
                    + RESET_BG_COLOR);

            for (int i = 1; i <= 8; ++i) {
                leaveColor = true;
                System.out.print(RESET_BG_COLOR + "\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  " + (9 - i) + "  ");
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
                    } else if (currPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        printPieces(currColor, currPiece, SET_TEXT_COLOR_RED);
                    } else if (currPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        printPieces(currColor, currPiece, SET_TEXT_COLOR_BLUE);
                    }

                }
                System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  " + (9 - i) + "  ");
            }
            System.out.print(RESET_BG_COLOR);
            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "     " + "  a  "
                    + "  b  " + "  c  " + "  d  " + "  e  " + "  f  " + "  g  " + "  h  " + "     "
                    + RESET_BG_COLOR);
        }



        if (Objects.equals(result, "redraw black")) {
            System.out.println("redrwaing black now");
            ChessBoard currBoard = currGame.getBoard();
            String currColor = SET_BG_COLOR_BLACK;
            boolean leaveColor = false;

            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "     " + "  h  "
                    + "  g  " + "  f  " + "  e  " + "  d  " + "  c  " + "  b  " + "  a  " + "     "
                    + RESET_BG_COLOR);

            for (int i = 1; i <= 8; ++i) {
                leaveColor = true;
                System.out.print(RESET_BG_COLOR + "\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  " + (i) + "  ");
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
                    } else if (currPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        printPieces(currColor, currPiece, SET_TEXT_COLOR_RED);
                    } else if (currPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        printPieces(currColor, currPiece, SET_TEXT_COLOR_BLUE);
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
            textColor = SET_TEXT_COLOR_RED;
            System.out.print("\n" + RESET_BG_COLOR + textColor + result);
        }
        else if (result.contains("!")) {
            textColor = SET_TEXT_COLOR_GREEN;
            System.out.print("\n" + RESET_BG_COLOR + textColor + result);
        }
        else {
//            System.out.println("printing else...");
            textColor = SET_TEXT_COLOR_WHITE;
            System.out.print("\n" + RESET_BG_COLOR + textColor + result);
        }


        if (ChessClient.state == SIGNEDOUT) {
            System.out.print("\n" + RESET_BG_COLOR + SET_TEXT_COLOR_WHITE + "[LOGGED OUT] >>> ");
        }
        else {
            System.out.print("\n" + RESET_BG_COLOR + SET_TEXT_COLOR_WHITE + "[LOGGED IN] >>> ");
        }
    }

    private void printPieces(String currColor, ChessPiece currPiece, String setTextColorBlue) {
        String textColor1;
        textColor1 = setTextColorBlue;
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
