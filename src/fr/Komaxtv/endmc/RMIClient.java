package fr.Komaxtv.endmc;

import java.rmi.Naming;

public class RMIClient {
    public static void main(String[] args) {
        try {
            EndMCServerInterface stub = (EndMCServerInterface) Naming.lookup("//localhost/EndMCServer");
            String response = stub.sayHello();
            System.out.println("Réponse du serveur : " + response);
        } catch (Exception e) {
            System.err.println("Erreur de communication RMI : " + e.toString());
            e.printStackTrace();
        }
    }
}
