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
        String line = "";
        while (!result.equals("quit")) {
            printPrompt();
            line = scanner.nextLine();


            try {
                System.out.println("Line: " + line);
                result = (String) client.eval(line);
                System.out.println("results: " + result);

            } catch (ResponseException e) {
                var msg = e.toString();
                System.out.print(msg);
//                throw new RuntimeException(e);
            }

        }
        System.out.println();
    }


    private void printPrompt() {
        System.out.print("\n" + RESET_BG_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

}
