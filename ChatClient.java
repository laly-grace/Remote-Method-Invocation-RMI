import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;

// RMI Client for Chat Application
public class ChatClient {
    public static void main(String[] args) {
        try {
            // Lookup the remote ChatService
            ChatService chatService = (ChatService) Naming.lookup("//10.232.77.162/ChatService");
            
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();
            
            // Register client
            chatService.registerClient(username);
            System.out.println("Connected to chat server!");
            
            // Start a separate thread to fetch messages
            new Thread(() -> {
                while (true) {
                    try {
                        List<String> messages = chatService.getMessages();
                        System.out.println("\nChat History:");
                        for (String msg : messages) {
                            System.out.println(msg);
                        }
                        Thread.sleep(5000); // Fetch new messages every 5 seconds
                    } catch (Exception e) {
                        System.err.println("Error fetching messages: " + e.getMessage());
                    }
                }
            }).start();
            
            // Sending messages loop
            while (true) {
                System.out.print("Enter message: ");
                String message = scanner.nextLine();
                chatService.sendMessage(username, message);
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
