package de.nico.warpsystem.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import de.nico.warpsystem.WarpSystem;

public class WarpsetCommand extends Command {
    private final WarpSystem plugin;

    public WarpsetCommand(WarpSystem plugin) {
        super("wset", "Set a new Warppoint", "/wset <warp>");
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(this.plugin.prefix + "You can only use this command ingame.");
            return true;
        }

        if(!(sender.hasPermission("warps.add"))) {
            sender.sendMessage(this.plugin.prefix + "§l§cYou don`t have permission for this command.");
            return true;
        }

        if(!(args.length == 1)) {
            sender.sendMessage(this.plugin.prefix + "§l§7Please use /wset <warp>");
            return true;
        }

        this.plugin.setWarp(args[0], (Player)sender);

        return false;
    }
}
