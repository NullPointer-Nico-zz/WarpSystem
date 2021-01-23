package de.nico.warpsystem.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import de.nico.warpsystem.WarpSystem;
import de.nico.warpsystem.components.language.Language;

public class WarpUICommand extends PluginCommand<WarpSystem> {
    private final WarpSystem plugin;

    public WarpUICommand(WarpSystem plugin) {
        super(Language.getNP("command-name"), plugin);
        this.setUsage(Language.getNP("command-usage"));
        this.setDescription(Language.getNP("command-description"));
        this.setAliases(new String[] {Language.getNP("command-alias"), "warps", "wui", "wpui"});
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return true;
        }

        Player p = (Player) sender;

        this.plugin.getWarpManager().openSelectOptionForm(p);
        return false;
    }
}
