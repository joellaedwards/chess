package ui;
import exception.ResponseException;
import websocket.messages.ServerMessage;

import static java.awt.Color.RED;
import static ui.EscapeSequences.*;
import static ui.State.SIGNEDOUT;

import java.util.Scanner;

public class Repl {
    private final ChessClient client;
    private String result = "";

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
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
        System.out.println(RED + serverMessage.getServerMessage());
        printPrompt();
    }


    private void printWhiteBoard() {
        System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "     " + "  a  "
                + "  b  " + "  c  " + "  d  " + "  e  " + "  f  " + "  g  " + "  h  " + "     "
                + RESET_BG_COLOR);
        System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  8  "
                + SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLUE + "  R  "  + SET_BG_COLOR_BLACK + "  N  "
                + SET_BG_COLOR_LIGHT_GREY + "  B  " + SET_BG_COLOR_BLACK + "  Q  "
                + SET_BG_COLOR_LIGHT_GREY + "  K  " + SET_BG_COLOR_BLACK + "  B  "
                + SET_BG_COLOR_LIGHT_GREY + "  N  " + SET_BG_COLOR_BLACK + "  R  " + SET_BG_COLOR_DARK_GREY +
                SET_TEXT_COLOR_BLACK + "  8  " + RESET_BG_COLOR);
        System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  7  "
                + SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + "  P  "
                + SET_BG_COLOR_LIGHT_GREY + "  P  " + SET_BG_COLOR_BLACK + "  P  "
                + SET_BG_COLOR_LIGHT_GREY + "  P  " + SET_BG_COLOR_BLACK + "  P  "
                + SET_BG_COLOR_LIGHT_GREY + "  P  " + SET_BG_COLOR_BLACK + "  P  "
                + SET_BG_COLOR_LIGHT_GREY + "  P  " + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  7  "
                + RESET_BG_COLOR);
        System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  6  "
                + SET_BG_COLOR_LIGHT_GREY + "     " + SET_BG_COLOR_BLACK + "     "
                + SET_BG_COLOR_LIGHT_GREY + "     " + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_LIGHT_GREY + "     "
                + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_LIGHT_GREY + "     "
                + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_DARK_GREY + "  6  " + RESET_BG_COLOR);
        System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  5  "
                + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_LIGHT_GREY + "     "
                + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_LIGHT_GREY + "     "
                + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_LIGHT_GREY + "     "
                + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_LIGHT_GREY + "     "
                + SET_BG_COLOR_DARK_GREY + "  5  " + RESET_BG_COLOR);
        System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  4  "
                + SET_BG_COLOR_LIGHT_GREY + "     " + SET_BG_COLOR_BLACK + "     "
                + SET_BG_COLOR_LIGHT_GREY + "     " + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_LIGHT_GREY + "     "
                + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_LIGHT_GREY + "     " + SET_BG_COLOR_BLACK + "     "
                + SET_BG_COLOR_DARK_GREY + "  4  " + RESET_BG_COLOR);
        System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  3  "
                + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_LIGHT_GREY + "     "
                + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_LIGHT_GREY + "     "
                + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_LIGHT_GREY + "     "
                + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_LIGHT_GREY + "     "
                + SET_BG_COLOR_DARK_GREY + "  3  " + RESET_BG_COLOR);
        System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  2  "
                + SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_RED + "  P  "
                + SET_BG_COLOR_BLACK + "  P  "
                + SET_BG_COLOR_LIGHT_GREY + "  P  "
                + SET_BG_COLOR_BLACK + "  P  "
                + SET_BG_COLOR_LIGHT_GREY + "  P  "
                + SET_BG_COLOR_BLACK + "  P  "
                + SET_BG_COLOR_LIGHT_GREY + "  P  "
                + SET_BG_COLOR_BLACK + "  P  "
                + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  2  "
                + RESET_BG_COLOR
        );
        System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  1  "
                + SET_BG_COLOR_BLACK + SET_TEXT_COLOR_RED + "  R  "
                + SET_BG_COLOR_LIGHT_GREY + "  N  "
                + SET_BG_COLOR_BLACK + "  B  "
                + SET_BG_COLOR_LIGHT_GREY + "  Q  "
                + SET_BG_COLOR_BLACK + "  K  "
                + SET_BG_COLOR_LIGHT_GREY + "  B  "
                + SET_BG_COLOR_BLACK + "  N  "
                + SET_BG_COLOR_LIGHT_GREY + "  R  "
                + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  1  "
                + RESET_BG_COLOR);

        System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "     " + "  a  "
                + "  b  " + "  c  " + "  d  " + "  e  " + "  f  " + "  g  " + "  h  " + "     "
                + RESET_BG_COLOR);

    }

    private void printPrompt() {

        String textColor = SET_TEXT_COLOR_RED;

        if (result.contains("white") || result.contains("Observing")) {
            printWhiteBoard();
        }

        else if (result.contains("joined")) {
            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "     " + "  h  "
                    + "  g  " + "  f  " + "  e  " + "  d  " + "  c  " + "  b  " + "  a  " + "     "
                    + RESET_BG_COLOR);
            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  1  "
                    + SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_RED + "  R  " + SET_BG_COLOR_BLACK + "  N  "
                    + SET_BG_COLOR_LIGHT_GREY + "  B  " + SET_BG_COLOR_BLACK + "  K  "
                    + SET_BG_COLOR_LIGHT_GREY + "  Q  " + SET_BG_COLOR_BLACK + "  B  "
                    + SET_BG_COLOR_LIGHT_GREY + "  N  " + SET_BG_COLOR_BLACK + "  R  "
                    + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  1  " + RESET_BG_COLOR);
            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  2  "
                    + SET_BG_COLOR_BLACK + SET_TEXT_COLOR_RED + "  P  " + SET_BG_COLOR_LIGHT_GREY + "  P  "
                    + SET_BG_COLOR_BLACK + "  P  " + SET_BG_COLOR_LIGHT_GREY + "  P  "
                    + SET_BG_COLOR_BLACK + "  P  " + SET_BG_COLOR_LIGHT_GREY + "  P  "
                    + SET_BG_COLOR_BLACK + "  P  " + SET_BG_COLOR_LIGHT_GREY + "  P  "
                    + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  2  " + RESET_BG_COLOR);
            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  3  "
                    + SET_BG_COLOR_LIGHT_GREY + "     " + SET_BG_COLOR_BLACK + "     "
                    + SET_BG_COLOR_LIGHT_GREY + "     " + SET_BG_COLOR_BLACK + "     "
                    + SET_BG_COLOR_LIGHT_GREY + "     " + SET_BG_COLOR_BLACK + "     "
                    + SET_BG_COLOR_LIGHT_GREY + "     " + SET_BG_COLOR_BLACK + "     "
                    + SET_BG_COLOR_DARK_GREY + "  3  " + RESET_BG_COLOR);
            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  4  "
                    + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_LIGHT_GREY + "     "
                    + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_LIGHT_GREY + "     "
                    + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_LIGHT_GREY + "     "
                    + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_LIGHT_GREY + "     "
                    + SET_BG_COLOR_DARK_GREY + "  4  " + RESET_BG_COLOR);
            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  5  "
                    + SET_BG_COLOR_LIGHT_GREY + "     " + SET_BG_COLOR_BLACK + "     "
                    + SET_BG_COLOR_LIGHT_GREY + "     " + SET_BG_COLOR_BLACK + "     "
                    + SET_BG_COLOR_LIGHT_GREY + "     " + SET_BG_COLOR_BLACK + "     "
                    + SET_BG_COLOR_LIGHT_GREY + "     " + SET_BG_COLOR_BLACK + "     "
                    + SET_BG_COLOR_DARK_GREY + "  5  " + RESET_BG_COLOR);
            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  6  "
                    + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_LIGHT_GREY + "     "
                    + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_LIGHT_GREY + "     "
                    + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_LIGHT_GREY + "     "
                    + SET_BG_COLOR_BLACK + "     " + SET_BG_COLOR_LIGHT_GREY + "     "
                    + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  6  " + RESET_BG_COLOR);
            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  7  "
                    + SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLUE + "  P  "
                    + SET_BG_COLOR_BLACK + "  P  " + SET_BG_COLOR_LIGHT_GREY + "  P  "
                    + SET_BG_COLOR_BLACK + "  P  "
                    + SET_BG_COLOR_LIGHT_GREY + "  P  " + SET_BG_COLOR_BLACK + "  P  "
                    + SET_BG_COLOR_LIGHT_GREY + "  P  " + SET_BG_COLOR_BLACK + "  P  "
                    + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  7  " + RESET_BG_COLOR);
            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  8  "
                    + SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + "  R  "
                    + SET_BG_COLOR_LIGHT_GREY + "  N  "
                    + SET_BG_COLOR_BLACK + "  B  "
                    + SET_BG_COLOR_LIGHT_GREY + "  K  "
                    + SET_BG_COLOR_BLACK + "  Q  "
                    + SET_BG_COLOR_LIGHT_GREY + "  B  "
                    + SET_BG_COLOR_BLACK + "  N  "
                    + SET_BG_COLOR_LIGHT_GREY + "  R  "
                    + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "  8  "
                    + RESET_BG_COLOR
            );
            System.out.print("\n" + SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLACK + "     " + "  h  "
                    + "  g  " + "  f  " + "  e  " + "  d  " + "  c  " + "  b  " + "  a  " + "     "
                    + RESET_BG_COLOR);
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
}
