package ui;
import exception.ResponseException;

import static ui.EscapeSequences.*;
import static ui.State.SIGNEDIN;
import static ui.State.SIGNEDOUT;

import java.util.Scanner;

public class Repl {
    private final ChessClient client;
    private String result = "";

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to chess game!");
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


    private void printPrompt() {

        String textColor = SET_TEXT_COLOR_RED;

        if (result.contains("joined") || result.contains("observing")) {

            System.out.print("\n" + SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLUE + "  R  "
                    + SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + "  N  "
                    + SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLUE + "  B  "
                    + SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + "  Q  "
                    + SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLUE + "  K  "
                    + SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + "  B  "
                    + SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLUE + "  N  "
                    + SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + "  R  "
            );
            System.out.print("\n" + SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + "  P  "
                    + SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLUE + "  P  "
                    + SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + "  P  "
                    + SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLUE + "  P  "
                    + SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + "  P  "
                    + SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLUE + "  P  "
                    + SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + "  P  "
                    + SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLUE + "  P  "
            );


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
