package ui;
import exception.ResponseException;

import static ui.EscapeSequences.*;
import java.util.Scanner;

public class Repl {
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.println("wassup welcome to chess game");
        System.out.println(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.next();

            try {
                result = client.eval(line);

            } catch (ResponseException e) {
                throw new RuntimeException(e);
            }
        }

    }


    private void printPrompt() {
        System.out.print("\n" + RESET_BG_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

}
