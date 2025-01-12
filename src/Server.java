import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {

    private static final int SERVER_PORT = 12345;
    private static final String DATA_FILE = "Membership3.data";
    private static final Vector<String> members = new Vector<>();
    private static final Vector<String[]> waitingList = new Vector<>();

    public static void main(String[] args) {
        loadData();

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

    private static void loadData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    members.add(line);
                }
                System.out.println("Data loaded from " + DATA_FILE);
            } catch (IOException e) {
                System.err.println("Error reading data file: " + e.getMessage());
            }
        }
    }

    private static void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            synchronized (members) {
                for (String member : members) {
                    writer.write(member);
                    writer.newLine();
                }
            }
            System.out.println("Data saved to " + DATA_FILE);
        } catch (IOException e) {
            System.err.println("Error saving data file: " + e.getMessage());
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

                    if (clientRequest.equals("LOAD")) {
                        handleLoad();
                    } else if (clientRequest.startsWith("SEARCH:")) {
                        handleSearch(clientRequest.substring(7));
                    } else if (clientRequest.startsWith("ADD:")) {
                        handleAdd(clientRequest.substring(4));
                    } else if (clientRequest.startsWith("DELETE:")) {  // Handle delete
                        handleDelete(clientRequest.substring(7));
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

        private void handleLoad() {
            synchronized (members) {
                out.println("load-results:" + members.size());
                for (String member : members) {
                    out.println(member);
                }
            }
        }

        private void handleSearch(String searchQuery) {
            List<String> searchResults = new ArrayList<>();
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
        }

        private void handleAdd(String memberData) {
            try {
                String[] parts = memberData.split("\\|", 2);
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    String address = parts[1].trim();

                    synchronized (members) {
                        if (members.size() < 5) {
                            members.add(name + ", " + address);
                            saveData();
                            out.println("add-success");
                            return;
                        }
                    }

                    // Add to waiting list using Vector
                    waitingList.add(new String[]{name, address});
                    out.println("add-waiting-list");
                } else {
                    out.println("Invalid data");
                }
            } catch (Exception e) {
                e.printStackTrace();
                out.println("Error processing data");
            }
        }

        private void handleDelete(String memberName) {
            synchronized (members) {
                boolean memberRemoved = members.removeIf(member -> member.equalsIgnoreCase(memberName));
                
                if (memberRemoved) {
                    saveData();
                    out.println("delete-success");
                } else {
                    out.println("delete-fail");
                }
            }
        }
    }
}
