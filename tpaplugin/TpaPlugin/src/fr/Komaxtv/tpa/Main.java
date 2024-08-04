package fr.Komaxtv.tpa;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Main extends JavaPlugin implements CommandExecutor {

    private final Map<UUID, UUID> pendingRequests = new HashMap<>();
    private final Map<UUID, ScheduledFuture<?>> requestTimers = new HashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final long REQUEST_TIMEOUT_SECONDS = 30;

    @Override
    public void onEnable() {
        getCommand("tpa").setExecutor(this);
        getCommand("tpahere").setExecutor(this);
        getCommand("tpconfirm").setExecutor(this);
        getCommand("tprefu").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Seules les joueurs peuvent utiliser ces commandes.");
            return true;
        }

        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("tpa")) {
            if (args.length != 1) {
                player.sendMessage(ChatColor.RED + "Usage: /tpa <pseudo>");
                return true;
            }

            Player target = getServer().getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Le joueur n'est pas en ligne.");
                return true;
            }

            if (pendingRequests.containsKey(target.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "Une demande de téléportation est déjà en attente avec ce joueur.");
                return true;
            }

            pendingRequests.put(target.getUniqueId(), player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "Demande de téléportation envoyée à " + target.getName() + ".");
            target.sendMessage(ChatColor.YELLOW + player.getName() + " souhaite vous téléporter. Tapez /tpconfirm pour accepter ou /tprefu pour refuser.");

            // Start a timer to automatically cancel the request after a timeout
            ScheduledFuture<?> timer = scheduler.schedule(() -> cancelRequest(player.getUniqueId(), target.getUniqueId(), "temps écoulé"), REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            requestTimers.put(player.getUniqueId(), timer);
            requestTimers.put(target.getUniqueId(), timer);

        } else if (cmd.getName().equalsIgnoreCase("tpahere")) {
            if (args.length != 1) {
                player.sendMessage(ChatColor.RED + "Usage: /tpahere <pseudo>");
                return true;
            }

            Player target = getServer().getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Le joueur n'est pas en ligne.");
                return true;
            }

            if (pendingRequests.containsKey(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "Vous avez déjà une demande de téléportation en cours.");
                return true;
            }

            pendingRequests.put(player.getUniqueId(), target.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "Demande de téléportation envoyée à " + target.getName() + ".");
            target.sendMessage(ChatColor.YELLOW + player.getName() + " souhaite vous téléporter vers lui. Tapez /tpconfirm pour accepter ou /tprefu pour refuser.");

            // Start a timer to automatically cancel the request after a timeout
            ScheduledFuture<?> timer = scheduler.schedule(() -> cancelRequest(target.getUniqueId(), player.getUniqueId(), "temps écoulé"), REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            requestTimers.put(player.getUniqueId(), timer);
            requestTimers.put(target.getUniqueId(), timer);

        } else if (cmd.getName().equalsIgnoreCase("tpconfirm")) {
            UUID requester = pendingRequests.get(player.getUniqueId());
            if (requester == null) {
                player.sendMessage(ChatColor.RED + "Aucune demande de téléportation en attente.");
                return true;
            }

            Player requesterPlayer = getServer().getPlayer(requester);
            if (requesterPlayer == null) {
                player.sendMessage(ChatColor.RED + "Le joueur ayant fait la demande n'est plus en ligne.");
                pendingRequests.remove(player.getUniqueId());
                return true;
            }

            requesterPlayer.teleport(player.getLocation());
            player.sendMessage(ChatColor.GREEN + "Vous avez accepté la demande de téléportation de " + requesterPlayer.getName() + ".");
            requesterPlayer.sendMessage(ChatColor.GREEN + "Vous avez été téléporté à " + player.getName() + ".");
            pendingRequests.remove(player.getUniqueId());
            cancelRequest(requesterPlayer.getUniqueId(), player.getUniqueId(), null);

        } else if (cmd.getName().equalsIgnoreCase("tprefu")) {
            UUID requester = pendingRequests.get(player.getUniqueId());
            if (requester == null) {
                player.sendMessage(ChatColor.RED + "Aucune demande de téléportation en attente.");
                return true;
            }

            Player requesterPlayer = getServer().getPlayer(requester);
            if (requesterPlayer == null) {
                player.sendMessage(ChatColor.RED + "Le joueur ayant fait la demande n'est plus en ligne.");
                pendingRequests.remove(player.getUniqueId());
                return true;
            }

            player.sendMessage(ChatColor.RED + "Vous avez refusé la demande de téléportation de " + requesterPlayer.getName() + ".");
            requesterPlayer.sendMessage(ChatColor.RED + player.getName() + " a refusé votre demande de téléportation.");
            pendingRequests.remove(player.getUniqueId());
            cancelRequest(requesterPlayer.getUniqueId(), player.getUniqueId(), null);
        }

        return true;
    }

    private void cancelRequest(UUID player, UUID target, String reason) {
        if (reason != null) {
            Player p = getServer().getPlayer(player);
            Player t = getServer().getPlayer(target);
            if (p != null) p.sendMessage(ChatColor.RED + "La demande de téléportation a été annulée : " + reason);
            if (t != null) t.sendMessage(ChatColor.RED + "La demande de téléportation a été annulée : " + reason);
        }

        pendingRequests.remove(player);
        pendingRequests.remove(target);
        ScheduledFuture<?> timer = requestTimers.remove(player);
        if (timer != null) timer.cancel(false);
        timer = requestTimers.remove(target);
        if (timer != null) timer.cancel(false);
    }
}
