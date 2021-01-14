package de.raidcraft.rctips.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import de.raidcraft.rctips.RCTips;
import de.raidcraft.rctips.tip.Tip;
import org.bukkit.entity.Player;

@CommandAlias("rcta|rctips:admin")
@CommandPermission("rctips.admin.*")
public class AdminCommands extends BaseCommand {

    private final RCTips plugin;

    public AdminCommands(RCTips plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("reload")
    @Description("Läd die Config neu")
    @CommandPermission("rctips.admin.reload")
    public void reload(Player player) {

        plugin.reload();
        player.sendMessage("RCTips Reload: Es wurden " + plugin.getTipManager().getLoadedTipCount()
                + " Tipps geladen!");
    }
}
