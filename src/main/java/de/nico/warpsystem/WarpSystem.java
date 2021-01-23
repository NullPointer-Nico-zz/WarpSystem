package de.nico.warpsystem;

import cn.nukkit.command.SimpleCommandMap;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import de.nico.warpsystem.commands.WarpUICommand;
import de.nico.warpsystem.components.forms.FormListener;
import de.nico.warpsystem.components.language.Language;
import de.nico.warpsystem.components.manager.WarpManager;
import lombok.Getter;

public class WarpSystem extends PluginBase {

    @Getter
    private WarpManager warpManager;

    @Override
    public void onEnable() {
        this.warpManager = new WarpManager(this);
        Language.init(this);
        this.saveDefaultConfig();
        this.registerCommands(this.getServer().getCommandMap());
        this.registerEvents(this.getServer().getPluginManager());
    }

    @Override
    public void onDisable() {
        this.warpManager.save(false);
    }

    public void registerCommands(SimpleCommandMap commandMap) {
        commandMap.register("warpui", new WarpUICommand(this));
    }

    public void registerEvents(PluginManager manager) {
        manager.registerEvents(new FormListener(), this);
    }
}