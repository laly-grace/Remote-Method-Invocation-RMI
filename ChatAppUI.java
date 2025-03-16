import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChatAppUI {
    private JFrame frame;
    private JPanel leftPanel;
    private List<String> connectedUsers = new ArrayList<>();

    public ChatAppUI() {
        initializeUI();
        simulateUserConnections(); // Simulate users connecting to the server
    }

    private void initializeUI() {
        frame = new JFrame("Chat App");
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

        String[] messages = {
            "Hello, I'm Russell. How can I help you today?",
            "I need more information about Developer Plan.",
            "Hi, Russell",
            "Are we meeting today? Project has been already finished and I have results to show you.",
            "We'll am not sure. I have results to show you.",
            "The rest of the team is not here yet. Maybe in an hour or so?",
            "Have you faced any problems at the last phase of the project?",
            "Actually everything was fine. I'm very excited to show this to our team."
        };

        for (String message : messages) {
            JLabel messageLabel = new JLabel(message);
            messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            centerPanel.add(messageLabel);
        }

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

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void simulateUserConnections() {
        // Simulate users connecting to the server
        Timer timer = new Timer(2000, e -> {
            String newUser = "User " + (connectedUsers.size() + 1);
            connectedUsers.add(newUser);
            updateUserList();
        });
        timer.start();
    }

    private void updateUserList() {
        leftPanel.removeAll();
        for (String user : connectedUsers) {
            JLabel userLabel = new JLabel(user);
            userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            userLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            leftPanel.add(userLabel);
        }
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatAppUI::new);
    }
}