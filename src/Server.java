import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {

    private static final int SERVER_PORT = 12345;
    private static final List<String> members = Collections.synchronizedList(new ArrayList<>());
    private static final Queue<String[]> waitingList = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started on port " + SERVER_PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private final Socket clientSocket;
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
                    System.out.println("Received from " + clientSocket.getInetAddress().getHostAddress() + ": " + clientRequest);

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
                    System.out.println("Client disconnected: " + clientSocket.getInetAddress().getHostAddress());
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleSearch(String searchQuery) {
            List<String> searchResults = new ArrayList<>();
            System.out.println("Search query: " + searchQuery);  // Debug log
        
            synchronized (members) {
                for (String member : members) {
                    if (member.toLowerCase().contains(searchQuery.toLowerCase())) {
                        searchResults.add(member);
                    }
                }
            }
        
            out.println("search-results:" + searchResults.size());
            for (String result : searchResults) {
                out.println(result);
            }
            System.out.println("Search results sent: " + searchResults);  // Debug log
        }
        
        private void handleAdd(String memberData) {
            try {
                // Split name and address using the delimiter
                String[] parts = memberData.split("\\|", 2);
                System.out.println("Received data: " + memberData); // Debug log
        
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    String address = parts[1].trim();
                    System.out.println("Parsed name: " + name + ", Parsed address: " + address); // Debug log
        
                    synchronized (members) {
                        if (members.size() < 5) {
                            members.add(name + ", " + address);
                            out.println("add-success");
                            return;
                        }
                    }
        
                    waitingList.add(new String[]{name, address});
                    out.println("add-waiting-list");
                } else {
                    System.out.println("Invalid data format."); // Debug log
                    out.println("Invalid data");
                }
            } catch (Exception e) {
                System.out.println("Error processing data: " + e.getMessage()); // Debug log
                out.println("Error processing data");
                e.printStackTrace();
            }
        }
             
    }
}
