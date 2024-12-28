import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {

    private static final int SERVER_PORT = 12345;
    private static final List<String> members = new ArrayList<>();
    private static final Queue<String[]> waitingList = new LinkedList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started on port " + SERVER_PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                String clientRequest;
                while ((clientRequest = in.readLine()) != null) {
                    System.out.println("Received: " + clientRequest);

                    if (clientRequest.startsWith("SEARCH:")) {
                        handleSearch(clientRequest.substring(7));
                    } else if (clientRequest.startsWith("ADD:")) {
                        handleAdd(clientRequest.substring(4));
                    } else if (clientRequest.equals("EXIT")) {
                        break;
                    } else {
                        out.println("Unknown command");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleSearch(String searchQuery) {
            List<String> searchResults = new ArrayList<>();
            for (String member : members) {
                if (member.toLowerCase().contains(searchQuery.toLowerCase())) {
                    searchResults.add(member);
                }
            }

            out.println("search-results:" + searchResults.size());
            for (String result : searchResults) {
                out.println(result);
            }
        }

        private void handleAdd(String memberData) {
            String[] parts = memberData.split(",");
            if (parts.length == 2) {
                String name = parts[0].trim();
                String address = parts[1].trim();
                if (members.size() < 5) {
                    members.add(name + ", " + address);
                    out.println("add-success");
                } else {
                    waitingList.add(parts);
                    out.println("add-waiting-list");
                }
            } else {
                out.println("Invalid data");
            }
        }
    }
}
