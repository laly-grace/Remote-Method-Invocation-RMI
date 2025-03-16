import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import java.util.List;

public class ChatAppUI {
    private JFrame frame;
    private JPanel leftPanel;
    private ChatService chatService;
    private String username;

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

            // Start a thread to periodically update the user list
            new Thread(this::updateUserListPeriodically).start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error connecting to the chat server.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void initializeUI() {
        frame = new JFrame("Chat App - " + username);
        frame.setSize(400, 600);
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
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane centerScrollPane = new JScrollPane(centerPanel);
        centerScrollPane.setBorder(BorderFactory.createEmptyBorder());
        frame.add(centerScrollPane, BorderLayout.CENTER);

        // Bottom panel with message input
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 240));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField messageField = new JTextField("Type your message here...");
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageField.setBackground(Color.WHITE);
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
        sendButton.addActionListener(e -> sendMessage(messageField));
        messageField.addActionListener(e -> sendMessage(messageField));

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void sendMessage(JTextField messageField) {
        try {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                chatService.sendMessage(username, message);
                messageField.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUserListPeriodically() {
        while (true) {
            try {
                // Fetch the list of connected users from the server
                List<String> users = chatService.getClients(); // Assuming getClients() is added to ChatService
                updateUserList(users);
                Thread.sleep(3000); // Update every 3 seconds
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateUserList(List<String> users) {
        SwingUtilities.invokeLater(() -> {
            leftPanel.removeAll();
            for (String user : users) {
                JLabel userLabel = new JLabel(user);
                userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                userLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                leftPanel.add(userLabel);
            }
            leftPanel.revalidate();
            leftPanel.repaint();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatAppUI::new);
    }
}