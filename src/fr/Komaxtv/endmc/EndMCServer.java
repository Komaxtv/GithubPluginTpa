package fr.Komaxtv.endmc;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class EndMCServer extends UnicastRemoteObject implements EndMCServerInterface {

    protected EndMCServer() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        try {
            EndMCServer obj = new EndMCServer();
            LocateRegistry.createRegistry(1980);
            Naming.rebind("//localhost/EndMCServer", obj);
            System.out.println("Serveur RMI prêt.");
        } catch (Exception e) {
            System.err.println("Erreur du serveur : " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public String sayHello() throws RemoteException {
        return "Hello, world!";
    }

    @Override
    public Block getBlock(String world, int x, int y, int z) throws RemoteException {
        // Implémentation de la méthode getBlock
        return null;
    }

    @Override
    public int getMaxPlayers() throws RemoteException {
        // Implémentation de la méthode getMaxPlayers
        return 0;
    }
}
