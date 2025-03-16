import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatServer extends UnicastRemoteObject implements ChatService {
    private Map<String, List<String>> userMessages; // Stores messages for each user
    private List<String> clients;

    protected ChatServer() throws RemoteException {
        super();
        userMessages = new HashMap<>();
        clients = new ArrayList<>();
    }

    @Override
    public synchronized void sendMessage(String sender, String receiver, String message) throws RemoteException {
        String formattedMessage = sender + ": " + message;
        
        // Add the message to the receiver's message list
        userMessages.computeIfAbsent(receiver, k -> new ArrayList<>()).add(formattedMessage);
        
        // Also add the message to the sender's message list (optional, for two-way communication)
        userMessages.computeIfAbsent(sender, k -> new ArrayList<>()).add(formattedMessage);
        
        System.out.println("New Message from " + sender + " to " + receiver + ": " + message);
    }

    @Override
    public synchronized List<String> getMessages(String username) throws RemoteException {
        return userMessages.getOrDefault(username, new ArrayList<>());
    }

    @Override
    public synchronized void registerClient(String username) throws RemoteException {
        if (!clients.contains(username)) {
            clients.add(username);
            userMessages.put(username, new ArrayList<>()); // Initialize message list for the user
            System.out.println(username + " has joined the chat.");
        }
    }

    @Override
    public synchronized List<String> getClients() throws RemoteException {
        return new ArrayList<>(clients);
    }

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