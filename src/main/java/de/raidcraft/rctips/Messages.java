package de.raidcraft.rctips;

import de.raidcraft.rctips.tip.Tip;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Formatter;
import java.util.UUID;
import java.util.function.Consumer;

import static net.kyori.adventure.text.Component.text;

public final class Messages {

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

    public static void tip(Player player, Tip tip) {

        TextComponent.Builder builder = text();

        String tipText = RCTips.instance().getPluginConfig().getTipPrefix() + tip.getText();
        tipText = parseColor(tipText);

        builder.append(text(tipText));

         // TODO
    }
}
