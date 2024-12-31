import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private JTextField searchField;
    private JButton searchButton, clearButton, addButton;
    private JList<String> resultList;
    private DefaultListModel<String> resultListModel;
    private JTextField nameField, addressField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Client().createAndShowGUI();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Unable to connect to the server. Please try again later.");
                e.printStackTrace();
            }
        });
    }

    public Client() throws IOException {
        // Initialize socket and I/O streams
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Membership Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Search Section
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        clearButton = new JButton("Clear");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);
        panel.add(searchPanel);

        // Results List
        resultListModel = new DefaultListModel<>();
        resultList = new JList<>(resultListModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane resultScrollPane = new JScrollPane(resultList);
        panel.add(resultScrollPane);

        // Add Member Section
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new GridLayout(3, 2));
        nameField = new JTextField();
        addressField = new JTextField();
        addButton = new JButton("Add Member");
        addPanel.add(new JLabel("Name:"));
        addPanel.add(nameField);
        addPanel.add(new JLabel("Address:"));
        addPanel.add(addressField);
        addPanel.add(addButton);
        panel.add(addPanel);

        frame.add(panel, BorderLayout.CENTER);

        // Action Listeners
        searchButton.addActionListener(e -> new Thread(this::searchMembers).start());
        clearButton.addActionListener(e -> clearSearchResult());
        addButton.addActionListener(e -> new Thread(this::addMember).start());

        frame.setVisible(true);

        // Load members upon startup
        new Thread(this::loadMembers).start();
    }

    private void clearSearchResult() {
        searchField.setText("");
        resultListModel.clear();
    }

    private void loadMembers() {
        out.println("LOAD");
        try {
            String response = in.readLine();
            if (response.startsWith("load-results:")) {
                int count = Integer.parseInt(response.substring(13));
                resultListModel.clear();  // Clear existing results
                for (int i = 0; i < count; i++) {
                    String member = in.readLine();
                    resultListModel.addElement(member);  // Add to the results area
                }
            } else {
                SwingUtilities.invokeLater(() -> resultListModel.addElement("Failed to load members."));
            }
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> resultListModel.addElement("Error loading members."));
            e.printStackTrace();
        }
    }

    private void searchMembers() {
        String searchText = searchField.getText().trim();
        if (!searchText.isEmpty()) {
            out.println("SEARCH:" + searchText);
            try {
                String response = in.readLine();
                resultListModel.clear();  // Clear previous results

                if (response.startsWith("search-results:")) {
                    int count = Integer.parseInt(response.substring(15));
                    if (count > 0) {
                        for (int i = 0; i < count; i++) {
                            String memberData = in.readLine();
                            resultListModel.addElement(memberData);
                        }
                    } else {
                        resultListModel.addElement("No results found.");
                    }
                } else {
                    resultListModel.addElement("Error retrieving results.");
                }
            } catch (IOException e) {
                resultListModel.addElement("Error communicating with server.");
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Search field cannot be empty.");
        }
    }

    private void addMember() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();

        if (!name.isEmpty() && !address.isEmpty()) {
            out.println("ADD:" + name + "|" + address);
            try {
                String response = in.readLine();
                if ("add-success".equals(response)) {
                    SwingUtilities.invokeLater(() -> {
                        nameField.setText("");
                        addressField.setText("");
                        JOptionPane.showMessageDialog(null, "Member added successfully!");
                    });
                } else if ("add-waiting-list".equals(response)) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Membership full. Added to waiting list."));
                } else {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Failed to add member: " + response));
                }
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error communicating with server."));
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Both name and address are required.");
        }
    }
}
