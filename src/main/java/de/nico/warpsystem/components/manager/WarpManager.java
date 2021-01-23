package de.nico.warpsystem.components.manager;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.level.Location;
import cn.nukkit.utils.Config;
import de.nico.warpsystem.WarpSystem;
import de.nico.warpsystem.components.forms.custom.CustomForm;
import de.nico.warpsystem.components.forms.simple.SimpleForm;
import de.nico.warpsystem.components.language.Language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarpManager {
    private final WarpSystem plugin;
    private final Config warpDB;
    private final Map<String, Location> warps = new HashMap<>();

    public WarpManager(final WarpSystem plugin) {
        this.plugin = plugin;
        this.warpDB = new Config(plugin.getDataFolder() + "/warps.yml", Config.YAML);
        this.warpDB.getAll().forEach((name, loc) -> {
            List pos = warpDB.getList(name);

            warps.put(name, new Location((Double) pos.get(0), (Double) pos.get(1), (Double) pos.get(2), (Double) pos.get(3),
                                         (Double) pos.get(4), Server.getInstance().getLevelByName((String) pos.get(5))));
        });
    }

    public void save(final boolean async) {
        this.warps.forEach((key, value) -> warpDB.set(key, new Object[]{value.x, value.y, value.z, value.pitch, value.yaw, value.level.getName()}));
        this.warpDB.save(async);
    }

    public void openSelectOptionForm(Player p) {
        SimpleForm simpleForm;

        if(p.isOp())  {
            simpleForm = new SimpleForm.Builder(Language.getNP("warp-menu-title"), Language.getNP("warp-menu-description"))
                    .addButton(new ElementButton(Language.getNP("warp-menu-warps")), e -> openWarpsForm(p))
                    .addButton(new ElementButton(Language.getNP("warp-menu-create")), e -> openCreateWarpForm(p))
                    .addButton(new ElementButton(Language.getNP("warp-menu-delete")), e -> openDeleteWarpFrom(p)).build();
            simpleForm.send(p);
            return;
        }

        if(p.hasPermission("warp.delete") && p.hasPermission("warp.create")) {
            simpleForm = new SimpleForm.Builder(Language.getNP("warp-menu-title"), Language.getNP("warp-menu-description"))
                    .addButton(new ElementButton(Language.getNP("warp-menu-warps")), e -> openWarpsForm(p))
                    .addButton(new ElementButton(Language.getNP("warp-menu-create")), e -> openCreateWarpForm(p))
                    .addButton(new ElementButton(Language.getNP("warp-menu-delete")), e -> openDeleteWarpFrom(p)).build();
            simpleForm.send(p);
            return;
        }

        if (p.hasPermission("warp.create")) {
            simpleForm = new SimpleForm.Builder(Language.getNP("warp-menu-title"), Language.getNP("warp-menu-description"))
                    .addButton(new ElementButton(Language.getNP("warp-menu-warps")), e -> openWarpsForm(p))
                    .addButton(new ElementButton(Language.getNP("warp-menu-create")), e -> openCreateWarpForm(p)).build();
            simpleForm.send(p);
            return;
        }

        if(p.hasPermission("warp.delete")) {
            simpleForm = new SimpleForm.Builder(Language.getNP("warp-menu-title"), Language.getNP("warp-menu-description"))
                    .addButton(new ElementButton(Language.getNP("warp-menu-warps")), e -> openWarpsForm(p))
                    .addButton(new ElementButton(Language.getNP("warp-menu-delete")), e -> openDeleteWarpFrom(p)).build();
            simpleForm.send(p);
            return;
        }

        openWarpsForm(p);
    }

    private void openCreateWarpForm(Player p) {
        CustomForm customForm = new CustomForm.Builder(Language.getNP("warp-menu-title"))
                .addElement(new ElementInput(Language.getNP("warp-menu-create-name"), Language.getNP("warp-menu-create-placeholder")))
                .onSubmit((e, r) -> {
                    if(r.getInputResponse(0).isEmpty()) {
                        p.sendMessage(Language.get("warp-menu-create-invalid"));
                        return;
                    }

                    String warp = r.getInputResponse(0);

                    if(warps.containsKey(warp)) {
                        p.sendMessage(Language.get("warp-exist", warp));
                        return;
                    }

                    if(warp.length() >= 15) {
                        p.sendMessage(Language.get("warp-name-limit"));
                        return;
                    }

                    this.warps.put(warp, p.getLocation());
                    this.saveWarp(warp, p.getLocation());

                    p.sendMessage(Language.get("warp-created", warp));
                }).build();

        customForm.send(p);
    }

    private void openDeleteWarpFrom(Player p) {
        if(warps.size() <= 0) {
            p.sendMessage(Language.get("no-warps-existing"));
            return;
        }

        CustomForm customForm = new CustomForm.Builder(Language.getNP("warp-menu-title"))
                .addElement(new ElementDropdown(Language.getNP("warp-menu-delete-name"), new ArrayList<>(warps.keySet())))
                .onSubmit((e, r) -> {
                    if(r.getDropdownResponse(0).getElementContent().isEmpty()) {
                        p.sendMessage(Language.get("warp-menu-delete-invalid"));
                        return;
                    }

                    String warp = r.getDropdownResponse(0).getElementContent();

                    this.warps.remove(warp);
                    this.warpDB.remove(warp);
                    this.save(true);
                    p.sendMessage(Language.get("warp-deleted", warp));
                }).build();

        customForm.send(p);
    }

    private void openWarpsForm(Player p) {
        if(warps.size() <= 0) {
            p.sendMessage(Language.get("no-warps-existing"));
            return;
        }

        CustomForm customForm = new CustomForm.Builder(Language.getNP("warp-menu-title"))
                .addElement(new ElementDropdown(Language.getNP("warp-menu-warps-name"), new ArrayList<>(warps.keySet())))
                .onSubmit((e, r) -> {
                    if(r.getDropdownResponse(0).getElementContent().isEmpty()) {
                        p.sendMessage(Language.get("warp-menu-delete-invalid"));
                        return;
                    }

                    String warp = r.getDropdownResponse(0).getElementContent();

                    try {
                        p.teleport(getWarp(warp));
                        p.sendMessage(Language.get("warp-success", warp));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        p.sendMessage(Language.get("warp-failed"));
                    }
                }).build();

        customForm.send(p);
    }

    public Location getWarp(String warp) {
        return warps.getOrDefault(warp, null);
    }

    public void saveWarp(String warp, Location location) {
        this.warps.put(warp, new Location(location.x, location.y, location.z, location.yaw, location.pitch, Server.getInstance().getLevelByName(location.level.getName())));
        this.save(true);
    }
}
