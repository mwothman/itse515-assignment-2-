import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

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
        SwingUtilities.invokeLater(() -> new Client().createAndShowGUI());
    }

    public Client() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        searchButton.addActionListener(e -> searchMembers());
        clearButton.addActionListener(e -> searchField.setText(""));
        addButton.addActionListener(e -> addMember());

        frame.setVisible(true);
    }

    private void searchMembers() {
        String searchText = searchField.getText().trim();
        if (!searchText.isEmpty()) {
            out.println("SEARCH:" + searchText);  // Send the search request to the server
            try {
                String response = in.readLine();
                if (response.startsWith("search-results:")) {
                    int count = Integer.parseInt(response.substring(15));
                    resultListModel.clear();
                    for (int i = 0; i < count; i++) {
                        String memberData = in.readLine();
                        resultListModel.addElement(memberData);
                    }
                } else {
                    resultListModel.addElement("No results found.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addMember() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        if (!name.isEmpty() && !address.isEmpty()) {
            out.println("ADD:" + name + "," + address);  // Send the add request to the server
            try {
                String response = in.readLine();
                if ("add-success".equals(response)) {
                    nameField.setText("");
                    addressField.setText("");
                    JOptionPane.showMessageDialog(null, "Member added successfully!");
                    searchMembers();  // Refresh the list after adding
                } else if ("add-waiting-list".equals(response)) {
                    JOptionPane.showMessageDialog(null, "Membership full. Added to waiting list.");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add member: " + response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
