import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ChatServer extends UnicastRemoteObject implements ChatService {
    private List<String> messages;
    private List<String> clients;

    protected ChatServer() throws RemoteException {
        super();
        messages = new ArrayList<>();
        clients = new ArrayList<>();
    }

    @Override
    public synchronized void sendMessage(String username, String message) throws RemoteException {
        String formattedMessage = username + ": " + message;
        messages.add(formattedMessage);
        System.out.println("New Message: " + formattedMessage);
    }

    @Override
    public synchronized List<String> getMessages() throws RemoteException {
        return new ArrayList<>(messages);
    }

    @Override
    public synchronized void registerClient(String username) throws RemoteException {
        if (!clients.contains(username)) {
            clients.add(username);
            System.out.println(username + " has joined the chat.");
        }
    }

    @Override
    public synchronized List<String> getClients() throws RemoteException {
        return new ArrayList<>(clients); // Return the list of connected clients
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