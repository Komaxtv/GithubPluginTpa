package fr.Komaxtv.endmc;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EndMCServerInterface extends Remote {
    String sayHello() throws RemoteException;
    Block getBlock(String world, int x, int y, int z) throws RemoteException;
    int getMaxPlayers() throws RemoteException;
}
