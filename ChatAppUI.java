import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import java.util.List;

public class ChatAppUI {
    private JFrame frame;
    private JPanel leftPanel;
    private JTextArea chatArea;
    private JTextField messageField;
    private ChatService chatService;
    private String username;
    private String selectedUser; // Track the selected user for messaging

    public ChatAppUI() {
        try {
            // Connect to the RMI server
            chatService = (ChatService) Naming.lookup("//localhost/ChatService");

            // Get the username from the user
            username = JOptionPane.showInputDialog("Enter your username:");
            if (username == null || username.trim().isEmpty()) {
                System.exit(0); // Exit if no username is provided
            }

            // Register the user with the server
            chatService.registerClient(username);

            // Initialize the UI
            initializeUI();

            // Start a thread to periodically update the user list and chat messages
            new Thread(this::updatePeriodically).start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error connecting to the chat server.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void initializeUI() {
        frame = new JFrame("Chat App - " + username);
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Left panel with user list
        leftPanel = new JPanel();
        leftPanel.setBackground(new Color(250, 250, 250));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane leftScrollPane = new JScrollPane(leftPanel);
        leftScrollPane.setBorder(BorderFactory.createEmptyBorder());
        frame.add(leftScrollPane, BorderLayout.WEST);

        // Center panel with chat messages
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chatArea.setBackground(Color.WHITE);
        chatArea.setForeground(new Color(50, 50, 50));
        chatArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane centerScrollPane = new JScrollPane(chatArea);
        centerScrollPane.setBorder(BorderFactory.createEmptyBorder());
        frame.add(centerScrollPane, BorderLayout.CENTER);

        // Bottom panel with message input
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 240));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        messageField = new JTextField();
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageField.setBackground(Color.WHITE);
        messageField.setForeground(new Color(50, 50, 50));
        messageField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(new Color(0, 120, 215));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Send message action
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void sendMessage() {
        try {
            String message = messageField.getText().trim();
            if (!message.isEmpty() && selectedUser != null) {
                chatService.sendMessage(username, selectedUser, message);
                messageField.setText("");
                updateChatMessages(); // Refresh the chat area after sending a message
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePeriodically() {
        while (true) {
            try {
                // Fetch the list of connected users
                List<String> users = chatService.getClients();

                // Update the UI
                SwingUtilities.invokeLater(() -> {
                    updateUserList(users);
                    if (selectedUser != null) {
                        updateChatMessages(); // Update chat messages for the selected user
                    }
                });

                Thread.sleep(3000); // Update every 3 seconds
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateUserList(List<String> users) {
        leftPanel.removeAll();
        for (String user : users) {
            if (!user.equals(username)) { // Don't show the current user in the list
                JButton userButton = new JButton(user);
                userButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                userButton.setBackground(new Color(240, 240, 240));
                userButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                userButton.setFocusPainted(false);
                userButton.addActionListener(e -> {
                    selectedUser = user; // Set the selected user
                    updateChatMessages(); // Update chat messages for the selected user
                });
                leftPanel.add(userButton);
            }
        }
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    private void updateChatMessages() {
        try {
            if (selectedUser != null) {
                List<String> messages = chatService.getMessages(username);
                chatArea.setText(String.join("\n", messages));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatAppUI::new);
    }
}