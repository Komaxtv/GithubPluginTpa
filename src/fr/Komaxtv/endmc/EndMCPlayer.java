package fr.Komaxtv.endmc;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface EndMCPlayer extends Remote {
    UUID getUniqueId() throws RemoteException;
    String getName() throws RemoteException;
    void sendMessage(String message) throws RemoteException;
    void teleport(String worldName, double x, double y, double z) throws RemoteException;
    void spawnParticle(String particleType, String worldName, int x, int y, int z) throws RemoteException;
}
