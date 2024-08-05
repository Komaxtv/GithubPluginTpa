package fr.Komaxtv.endmc;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

public class EndMCPlayerImpl extends UnicastRemoteObject implements EndMCPlayer {

    private final Player bukkitPlayer;

    protected EndMCPlayerImpl(Player player) throws RemoteException {
        super();
        this.bukkitPlayer = player;
    }

    @Override
    public UUID getUniqueId() throws RemoteException {
        return bukkitPlayer.getUniqueId();
    }

    @Override
    public String getName() throws RemoteException {
        return bukkitPlayer.getName();
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        bukkitPlayer.sendMessage(message);
    }

    @Override
    public void teleport(String worldName, double x, double y, double z) throws RemoteException {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            bukkitPlayer.teleport(new Location(world, x, y, z));
        }
    }

    @Override
    public void spawnParticle(String particleType, String worldName, int x, int y, int z) throws RemoteException {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            // Utiliser une méthode alternative pour les particules si nécessaire
        }
    }
}
