import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatService extends Remote {
    void sendMessage(String sender, String receiver, String message) throws RemoteException;
    List<String> getMessages(String username) throws RemoteException;
    void registerClient(String username) throws RemoteException;
    List<String> getClients() throws RemoteException;
}