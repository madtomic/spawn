package ru.gtncraft.spawn;

import com.google.common.collect.ImmutableMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Spawn extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginCommand("setspawn").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    Location location = player.getLocation();
                    try {
                        setSpawn(location);
                        player.sendMessage(ChatColor.GREEN + "Spawnpoint set at you current location.");
                    } catch (IOException ex) {
                        getLogger().severe(ex.getMessage());
                    }
                }
                return true;
            }
        });
        getServer().getPluginCommand("spawn").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    World world = player.getWorld();
                    player.teleport(getSpawn(world));
                }
                return true;
            }
        });
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.teleport(getSpawn(player.getWorld()));
    }

    private Location getSpawn(World world) {
        ConfigurationSection config = getConfig().getConfigurationSection(world.getName());
        if (config == null) {
            return world.getSpawnLocation();
        } else {
            return new Location(world, config.getDouble("x"), config.getDouble("y"), config.getDouble("z"),
                    Float.valueOf(config.getString("yaw")),
                    Float.valueOf(config.getString("pitch"))
            );
        }
    }

    private void setSpawn(Location location) throws IOException {
        World world = location.getWorld();
        world.setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        getConfig().createSection(world.getName(), ImmutableMap.of(
                "x", location.getBlockX(),
                "y", location.getBlockY(),
                "z", location.getBlockZ(),
                "yaw", location.getYaw(),
                "pitch", location.getPitch()
        ));
        getConfig().save(new File(getDataFolder(), "config.yml"));
    }
}
