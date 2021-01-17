package de.raidcraft.rctips.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
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
    public void reload() {

        plugin.reload();
        getCurrentCommandIssuer().sendMessage("RCTips Reload: Es wurden " + plugin.getTipManager().getLoadedTipCount()
                + " Tipps geladen!");
    }

    @CommandAlias("purge")
    @CommandCompletion("@players")
    @Description("Löscht alle akzeptierten Tipps eines Spielers")
    @CommandPermission("rctips.admin.purge")
    public void purge(Player targetPlayer) {

        int acceptedTips = plugin.getTipManager().purge(targetPlayer.getUniqueId());

        if(!getCurrentCommandIssuer().getUniqueId().equals(targetPlayer.getUniqueId())) {
            getCurrentCommandIssuer().sendMessage("Alle akzeptierten Tipps (" + acceptedTips + ") von "
                    + targetPlayer.getName() + " wurden zurückgesetzt!");
        }

        targetPlayer.sendMessage("Deine akzeptierten Tipps (" + acceptedTips + ") wurden zurückgesetzt!");
    }
}
