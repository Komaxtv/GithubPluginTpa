package fr.Komaxtv.endmc;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIManager {

    private static boolean isExported = false;

    public static synchronized void exportObject(EndMCServerImpl serverImpl, int port) throws RemoteException {
        if (!isExported) {
            UnicastRemoteObject.exportObject(serverImpl, port);
            isExported = true;
        }
    }

    public static synchronized void unexportObject(EndMCServerImpl serverImpl) throws RemoteException {
        if (isExported) {
            UnicastRemoteObject.unexportObject(serverImpl, true);
            isExported = false;
        }
    }
}
