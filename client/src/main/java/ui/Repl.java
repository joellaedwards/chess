package ui;

public class Repl {
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.println("wassup welcome to chess game");
        System.out.println(client.help());
    }

}
