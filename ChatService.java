import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatService extends Remote {
    void sendMessage(String username, String message) throws RemoteException;
    List<String> getMessages() throws RemoteException;
    void registerClient(String username) throws RemoteException;
    List<String> getClients() throws RemoteException; // Add this method
}