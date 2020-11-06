package de.nico.warpsystem.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import de.nico.warpsystem.WarpSystem;

public class WarpremoveCommand extends Command {
    private final WarpSystem plugin;

    public WarpremoveCommand(WarpSystem plugin) {
        super("wremove", "Remove a Warppoint", "/wremove <warp>");
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(this.plugin.prefix + "You can only use this command ingame.");
            return true;
        }

        if(!(sender.hasPermission("warps.remove"))) {
            sender.sendMessage(this.plugin.prefix + "§l§cYou don`t have permission for this command.");
            return true;
        }

        if(!(args.length == 1)) {
            sender.sendMessage(this.plugin.prefix + "§l§7Please use /wremove <warp>");
            return true;
        }

        this.plugin.removeWarp(args[0], (Player)sender);

        return false;
    }
}
