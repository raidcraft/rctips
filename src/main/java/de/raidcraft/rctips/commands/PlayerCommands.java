package de.raidcraft.rctips.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import de.raidcraft.rctips.RCTips;
import de.raidcraft.rctips.tip.Tip;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandAlias("rct")
public class PlayerCommands extends BaseCommand {

    private final RCTips plugin;

    public final static String acceptTip() {
        return "/rct accept";
    }

    public PlayerCommands(RCTips plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("accept")
    @Description("Akzeptiert einen Tipp und verteilt die Belohung")
    public void acceptTip(String tipId, Player player) {

        Tip tip = plugin.getTipManager().getTip(tipId);
        if(tip == null) {
            return;
        }
        if(plugin.getTipManager().acceptTip(tip, player)) {
            // TODO message du hast den tipp akzeptiert mit hover und co
        } else {
            // TODO fehler mehrmals akzeptieren
        }
    }
}
