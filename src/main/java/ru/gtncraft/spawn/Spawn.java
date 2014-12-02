package ru.gtncraft.spawn;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Spawn extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        new Listeners(this);
        new CommandSetSpawn(this);
        new CommandSpawn(this);
    }

    public Location getSpawn(World world) {
        ConfigurationSection config = getConfig().getConfigurationSection(world.getName());

        if (config == null) {
            return world.getSpawnLocation();
        } else {
            return new Location(world,
                    config.getDouble("x"),
                    config.getDouble("y"),
                    config.getDouble("z"),
                    Float.valueOf(config.getString("yaw")),
                    Float.valueOf(config.getString("pitch"))
            );
        }
    }

    public void setSpawn(Location location) throws IOException {
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
