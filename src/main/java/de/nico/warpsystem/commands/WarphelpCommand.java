package de.nico.warpsystem.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import de.nico.warpsystem.WarpSystem;

public class WarphelpCommand extends Command {
    private final WarpSystem plugin;

    public WarphelpCommand(WarpSystem plugin) {
        super("whelp", "Warp help Command", "/whelp");
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(this.plugin.prefix + "You can only use this command ingame.");
            return true;
        }

        sender.sendMessage("§l§7<=====§l§5WarpSystem§l§7=====>");
        sender.sendMessage("§l§7/wset <warp>");
        sender.sendMessage("§l§7/wremove <warp>");
        sender.sendMessage("§l§7/wlist");
        sender.sendMessage("§l§7/whelp");
        sender.sendMessage("§l§7/w <warp>");
        sender.sendMessage("§l§7<=====§l§5WarpSystem§l§7=====>");

        return false;
    }
}
