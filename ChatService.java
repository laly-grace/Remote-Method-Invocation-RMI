import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

// Define the RMI Interface for Chat Service
public interface ChatService extends Remote {
    
    // Method for a client to send a message to the server
    void sendMessage(String username, String message) throws RemoteException;
    
    // Method for a client to retrieve chat history
    List<String> getMessages() throws RemoteException;
    
    // Method for a client to register and notify other users
    void registerClient(String username) throws RemoteException;
}
