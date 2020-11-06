package de.nico.warpsystem;

import cn.nukkit.Player;
import cn.nukkit.command.SimpleCommandMap;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import de.nico.warpsystem.commands.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WarpSystem extends PluginBase
{
    public final String prefix = "§l§5WarpSystem §l§7| ";

    Config config;

    @Override
    public void onEnable()
    {
        this.getLogger().info("Plugin Geladen....");
        this.getLogger().info("You can change the messages in language.yml");
        this.saveResource("language.yml");
        this.createWarps();
        this.load();
    }

    public void load()
    {
        SimpleCommandMap map = this.getServer().getCommandMap();
        map.register("w", new WarpCommand(this));
        map.register("wset", new WarpsetCommand(this));
        map.register("whelp", new WarphelpCommand(this));
        map.register("wremove", new WarpremoveCommand(this));
        map.register("wlist", new WarplistCommand(this));
    }

    // Create config
    public void createWarps() {
        File f = new File(this.getDataFolder() + "/warps.yml");

        if(!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }

        if(!f.exists()) {
            config = new Config(this.getDataFolder() + "/warps.yml", Config.YAML);
            config.save();
            config.reload();
        }
    }

    // Set a warp
    public void setWarp(String warp, Player p) {
        double[] location = new double[]{p.getLocation().x, p.getLocation().y, p.getLocation().z, p.getLocation().yaw, p.getLocation().pitch};

        config.load(this.getDataFolder() + "/warps.yml", Config.YAML);
        config.set(warp, location);
        config.save();
        config.reload();
        p.sendMessage(this.prefix + "§l§7Warp §l§c" + warp + " §l§7was created.");
    }

    // remove Warp
    public void removeWarp(String warp, Player p) {
        ArrayList<String> warps = new ArrayList<String>();

        config.load(this.getDataFolder() + "/warps.yml", Config.YAML);

        config.getAll().forEach((name, location) -> {
            warps.add(name);
        });

        if (warps.contains(warp)) {
            config.remove(warp);
            config.save();
            config.reload();
            p.sendMessage(this.prefix + "§l§7The warp §l§c" + warp + " §l§7was removed.");
        } else {
            p.sendMessage(this.prefix + "§l§c" + warp + " §l§7is not existing.");
        }
    }

    // Teleport to a warppoint
    public void getWarp(String warp, Player p) {
        ArrayList<String> warps = new ArrayList<String>();

        config.load(this.getDataFolder() + "/warps.yml", Config.YAML);

        config.getAll().forEach((name, location) -> {
            warps.add(name);
        });

        if(warps.contains(warp)) {
            List location = config.getList(warp);
            p.teleport(new Location((double) location.get(0), (double) location.get(1), (double) location.get(2), (double) location.get(3), (double) location.get(4)));
            p.sendMessage(this.prefix + "§l§7You were teleported to §l§c" + warp + "§l§7.");
        } else {
            p.sendMessage(this.prefix + "§l§c" + warp + " §l§7is not existing.");
        }
    }

    // Get all warppoints
    public void getWarpList(Player p) {
        ArrayList<String> warps = new ArrayList<String>();

        config.load(this.getDataFolder() + "/warps.yml", Config.YAML);

        config.getAll().forEach((name, location) -> {
            warps.add(name);
        });

        p.sendMessage("§l§5Warps: §l§7" + warps.toString());
    }
}