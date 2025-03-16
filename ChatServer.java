import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

// Implementing the RMI Chat Service
public class ChatServer extends UnicastRemoteObject implements ChatService {
    private List<String> messages;
    private List<String> clients;

    // Constructor
    protected ChatServer() throws RemoteException {
        super();
        messages = new ArrayList<>();
        clients = new ArrayList<>();
    }

    // Method to send a message
    @Override
    public synchronized void sendMessage(String username, String message) throws RemoteException {
        String formattedMessage = username + ": " + message;
        messages.add(formattedMessage);
        System.out.println("New Message: " + formattedMessage);
    }

    // Method to retrieve chat history
    @Override
    public synchronized List<String> getMessages() throws RemoteException {
        return new ArrayList<>(messages);
    }

    // Method to register a new client
    @Override
    public synchronized void registerClient(String username) throws RemoteException {
        if (!clients.contains(username)) {
            clients.add(username);
            System.out.println(username + " has joined the chat.");
        }
    }

    // Main method to start the server
    public static void main(String[] args) {
        try {
            ChatServer server = new ChatServer();
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            java.rmi.Naming.rebind("ChatService", server);
            System.out.println("Chat Server is running...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
