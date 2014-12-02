package ru.gtncraft.spawn;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

class Listeners implements Listener {
    private final Spawn plugin;
    private final boolean forceSpawn;

    public Listeners(final Spawn plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        this.forceSpawn = plugin.getConfig().getBoolean("forceSpawn", false);
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (forceSpawn || !player.hasPlayedBefore()) {
            player.teleport(plugin.getSpawn(player.getWorld()), PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        if (forceSpawn) {
            event.setRespawnLocation(plugin.getSpawn(player.getWorld()));
        }
    }

}
