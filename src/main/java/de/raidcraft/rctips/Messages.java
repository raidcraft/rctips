package de.raidcraft.rctips;

import de.raidcraft.rctips.commands.PlayerCommands;
import de.raidcraft.rctips.reward.Reward;
import de.raidcraft.rctips.tip.Tip;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Formatter;
import java.util.UUID;
import java.util.function.Consumer;

import static de.raidcraft.rctips.Messages.Colors.*;
import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.event.ClickEvent.runCommand;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

public final class Messages {

    public static final class Colors {

        public static final TextColor ACCEPT = RED;
        public static final TextColor REWARD_TOOLTIP_DESC = YELLOW;
    }

    private Messages() {}

    public static String parseColor(String line) {

        String regex = "&(?<!&&)(?=%c)";
        Formatter fmt;
        for (ChatColor clr : ChatColor.values()) {
            char code = clr.getChar();
            fmt = new Formatter();
            line = line.replaceAll(fmt.format(regex, code).toString(), "\u00A7");
        }
        return line.replace("&&", "&");
    }

    public static void send(UUID playerId, Component message) {
        if (RCTips.isTesting()) return;
        BukkitAudiences.create(RCTips.instance())
                .player(playerId)
                .sendMessage(message);
    }

    public static void send(UUID playerId, Consumer<TextComponent.Builder> message) {

        TextComponent.Builder builder = text();
        message.accept(builder);
        send(playerId, builder.build());
    }

    public static void send(Player player, Component message) {
        send(player.getUniqueId(), message);
    }

    private static Component rewardInfo(Reward reward) {

        TextComponent.Builder builder = text();

        builder.append(
                text("Klicke hier um diesen Tipp nicht mehr anzuzeigen", REWARD_TOOLTIP_DESC))
                .append(newline());
        builder.append(
                text("Erhalte als Belohnung ", REWARD_TOOLTIP_DESC));
        builder.append(
                text(reward.getDescription(), REWARD_TOOLTIP_DESC, BOLD));

        return builder.build();
    }

    public static void tip(Player player, Tip tip) {

        TextComponent.Builder builder = text();

        String tipText = RCTips.instance().getPluginConfig().getTipPrefix() + tip.getText() + " ";
        tipText = parseColor(tipText);

        builder.append(text(tipText));

        Reward reward = tip.getReward();
        if(reward != null) {
            ClickEvent acceptEvent = runCommand(PlayerCommands.acceptTip() + " " + tip.getId() + " " + player.getName());
            builder.append(text("OK", ACCEPT, ITALIC, BOLD, UNDERLINED)
                    .clickEvent(acceptEvent)
                    .hoverEvent(rewardInfo(reward)));
        }

        Component component = builder.build();

        send(player, component);
    }
}
