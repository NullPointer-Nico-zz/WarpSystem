package de.nico.warpsystem.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import de.nico.warpsystem.WarpSystem;

public class WarpCommand extends Command {
    private final WarpSystem plugin;

    public WarpCommand(WarpSystem plugin) {
        super("w", "Warp to a warppoint", "/w <warp>");
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(this.plugin.prefix + "You can only use this command ingame.");
            return true;
        }

        Player p = (Player)sender;

        if(!(args.length == 1)) {
            sender.sendMessage(this.plugin.prefix + "§l§7Please use /w <warp>");
        } else {
            this.plugin.getWarp(args[0], p);
        }

        return false;
    }
}
