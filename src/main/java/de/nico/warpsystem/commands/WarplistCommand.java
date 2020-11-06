package de.nico.warpsystem.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import de.nico.warpsystem.WarpSystem;

public class WarplistCommand extends Command {
    private final WarpSystem plugin;

    public WarplistCommand(WarpSystem plugin) {
        super("wlist", "Get a list of all warppoints", "/wlist");
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(this.plugin.prefix + "You can only use this command ingame.");
            return true;
        }

        this.plugin.getWarpList((Player)sender);

        return false;
    }
}
