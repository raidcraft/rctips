package de.raidcraft.rctips.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import de.raidcraft.rctips.Messages;
import de.raidcraft.rctips.RCTips;
import de.raidcraft.rctips.tip.Tip;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("rct|tipps|tipp")
public class PlayerCommands extends BaseCommand {

    private final RCTips plugin;

    public final static String acceptTip() {
        return "/rct accept";
    }
    public final static String listTipps() { return "/rct list"; }

    public PlayerCommands(RCTips plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("list")
    @Default
    @Description("Zeigt alle Tipps")
    public void showAll(Player player, @Optional Integer page) {

        if(page == null) page = 1;
        List<Tip> tips = plugin.getTipManager().getAllTips();

        Messages.tipList(player, tips, page);
    }

    @CommandAlias("accept")
    @Description("Akzeptiert einen Tipp und verteilt die Belohnung")
    public void acceptTip(String tipId, Player player) {

        Tip tip = plugin.getTipManager().getTip(tipId);
        if(tip == null) {
            return;
        }
        if(plugin.getTipManager().acceptTip(tip, player)) {
            Messages.tipAccepted(player, tip);
        } else {
           player.sendMessage(ChatColor.RED + "Du hast diesen Tipp bereits akzeptiert!");
        }
    }
}
