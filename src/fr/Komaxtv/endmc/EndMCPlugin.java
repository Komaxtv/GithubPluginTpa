package fr.Komaxtv.endmc;

import org.bukkit.plugin.java.JavaPlugin;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class EndMCPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        try {
            // Définir un gestionnaire de sécurité avec le fichier de politique
            String policyPath = "file:" + System.getProperty("user.dir") + "/policyfile.policy";
            getLogger().info("Chargement de la politique de sécurité depuis : " + policyPath);
            System.setProperty("java.security.policy", policyPath);
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }

            // Créer le registre RMI si nécessaire
            try {
                LocateRegistry.createRegistry(1980);
                getLogger().info("Registre RMI créé sur le port 1980.");
            } catch (RemoteException e) {
                getLogger().severe("Erreur lors de la création du registre RMI : " + e.toString());
                e.printStackTrace();
            }

            // Lier l'objet distant
            EndMCServerImpl obj = new EndMCServerImpl();
            Naming.rebind("//localhost:1980/EndMCServer", obj);
            getLogger().info("Objet EndMCServer lié dans le registre RMI.");

        } catch (Exception e) {
            getLogger().severe("Erreur de communication RMI : " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        try {
            // Défaire l'objet distant du registre RMI
            Naming.unbind("//localhost:1980/EndMCServer");
            getLogger().info("Objet EndMCServer défait du registre RMI.");
        } catch (Exception e) {
            getLogger().severe("Erreur lors du défait de l'objet RMI : " + e.toString());
            e.printStackTrace();
        }
    }
}
