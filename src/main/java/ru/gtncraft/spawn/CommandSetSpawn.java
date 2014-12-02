package ru.gtncraft.spawn;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

class CommandSetSpawn implements CommandExecutor, TabExecutor {
    private final Spawn plugin;

    public CommandSetSpawn(final Spawn plugin) {
        Bukkit.getServer().getPluginCommand("setspawn").setExecutor(this);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location location = player.getLocation();
            try {
                plugin.setSpawn(location);
                player.sendMessage(ChatColor.GREEN + "Spawnpoint set at you current location.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return ImmutableList.of();
    }
}
