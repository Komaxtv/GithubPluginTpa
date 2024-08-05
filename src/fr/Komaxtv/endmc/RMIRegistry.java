package fr.Komaxtv.endmc;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RMIRegistry {

    public static void main(String[] args) {
        try {
            // Démarrer le registre RMI
            LocateRegistry.createRegistry(1099);

            // Créer et enregistrer l'instance du serveur
            EndMCServerImpl server = new EndMCServerImpl();
            Naming.rebind("rmi://localhost/EndMCServer", server);

            System.out.println("Serveur RMI prêt.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
