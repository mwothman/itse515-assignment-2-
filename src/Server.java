import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    private static final int SERVER_PORT = 12345;
    private static final String DATA_FILE = "Membership3.data";
    private static Membership3 membership;

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
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                membership = (Membership3) ois.readObject();
                System.out.println("Data loaded from " + DATA_FILE);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error reading data file: " + e.getMessage());
                membership = new Membership3(5); // Default size if loading fails
            }
        } else {
            membership = new Membership3(5); // Default size if file doesn't exist
        }
    }

    private static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(membership);
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
                    System.out.println("Received: " + clientRequest);

                    if (clientRequest.equals("LOAD")) {
                        handleLoad();
                    } else if (clientRequest.startsWith("SEARCH:")) {
                        handleSearch(clientRequest.substring(7));
                    } else if (clientRequest.startsWith("ADD:")) {
                        handleAdd(clientRequest.substring(4));
                    } else if (clientRequest.startsWith("DELETE:")) {
                        handleDelete(clientRequest.substring(7));
                    } else if (clientRequest.equals("EXIT")) {
                        break;
                    } else {
                        out.println("Unknown command");
                    }
                }
            } catch (IOException | MembershipFullException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleLoad() {
            Enumeration<ClubMember> members = membership.getAllMembers();
            List<String> memberDetails = new ArrayList<>();

            while (members.hasMoreElements()) {
                memberDetails.add(members.nextElement().toString());
            }

            out.println("load-results:" + memberDetails.size());
            for (String detail : memberDetails) {
                out.println(detail);
            }
        }

        private void handleSearch(String searchQuery) {
            Enumeration<ClubMember> members = membership.getAllMembers();
            List<String> searchResults = new ArrayList<>();

            while (members.hasMoreElements()) {
                ClubMember member = members.nextElement();
                if (member.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
                    searchResults.add(member.toString());
                }
            }

            out.println("search-results:" + searchResults.size());
            for (String result : searchResults) {
                out.println(result);
            }
        }

        private void handleAdd(String memberData) throws MembershipFullException {
            try {
                String[] parts = memberData.split("\\|", 2);
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    String address = parts[1].trim();

                    membership.addMember(name, address);
                    saveData();
                    out.println("add-success");
                } else {
                    out.println("Invalid data");
                }
            } catch (Exception e) {
                e.printStackTrace();
                out.println("Error adding member");
            }
        }

        private void handleDelete(String memberNumberStr) {
            try {
                int memberNumber = Integer.parseInt(memberNumberStr.trim());
                membership.removeMember(memberNumber);  // Call Membership3's remove method
                saveData();
                out.println("delete-success");
            } catch (NumberFormatException e) {
                out.println("Invalid member number");
            } catch (InvalidMemberNumberException e) {
                out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                out.println("Error deleting member");
            }
        }
        
    }
}
