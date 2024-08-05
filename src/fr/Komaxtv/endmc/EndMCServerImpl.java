package fr.Komaxtv.endmc;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class EndMCServerImpl extends UnicastRemoteObject implements EndMCServerInterface {

    protected EndMCServerImpl() throws RemoteException {
        super();
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
