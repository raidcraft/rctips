package de.raidcraft.rctips;

import de.raidcraft.rctips.commands.PlayerCommands;
import de.raidcraft.rctips.reward.Reward;
import de.raidcraft.rctips.tip.Tip;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.feature.pagination.Pagination;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.*;
import java.util.function.Consumer;

import static de.raidcraft.rctips.Messages.Colors.*;
import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.event.ClickEvent.*;
import static net.kyori.adventure.text.event.HoverEvent.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

public final class Messages {

    public static final class Colors {

        public static final TextColor ACCEPT_BUTTON = GREEN;
        public static final TextColor REWARD_TOOLTIP_DESC = YELLOW;
        public static final TextColor REWARD_TOOLTIP_DESC_ACCENT = GREEN;
        public static final TextColor ACCEPT_MESSAGE = GREEN;
        public static final TextColor ALREADY_ACCEPTED = RED;
        public static final TextColor LIST_HEADING = DARK_AQUA;
        public static final TextColor HIGHLIGHT = AQUA;
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

        builder.append(text("Klicke hier um diesen Tipp", REWARD_TOOLTIP_DESC))
                .append(newline())
                .append(text("nicht mehr anzuzeigen", REWARD_TOOLTIP_DESC))
                .append(newline()).append(newline());
        builder.append(
                text("Erhalte als Belohnung ", REWARD_TOOLTIP_DESC_ACCENT))
                .append(newline());
        builder.append(
                text(reward.getDescription(), REWARD_TOOLTIP_DESC_ACCENT, BOLD));

        return builder.build();
    }

    public static void tipAccepted(Player player, Tip tip) {

        TextComponent.Builder builder = text();

        builder.append(text("Du hast als Tipp-Belohnung ", ACCEPT_MESSAGE));
        builder.append(text(tip.getReward().getDescription(), ACCEPT_MESSAGE, BOLD));
        builder.append(text(" erhalten", ACCEPT_MESSAGE));
        Component component = builder.build();

        send(player, component);
    }

    private static Component tipContent(Tip tip) {

        TextComponent.Builder builder = text();

        String tipText = RCTips.instance().getPluginConfig().getTipPrefix() + tip.getText() + " ";
        tipText = parseColor(tipText);

        // Detect URL
        // TODO
        builder.append(text(tipText));

        return builder.build();
    }

    public static void tip(Player player, Tip tip, boolean rewardable) {

        TextComponent.Builder builder = text();

        // Append main tip content
        builder.append(tipContent(tip));

        Reward reward = tip.getReward();
        if(rewardable && reward != null) {
            if(!RCTips.instance().getTipManager().hasAccepted(tip, player.getUniqueId())) {
                ClickEvent acceptEvent = runCommand(
                        PlayerCommands.acceptTip() + " " + tip.getId() + " " + player.getName());
                builder.append(text("OK", ACCEPT_BUTTON, ITALIC, BOLD, UNDERLINED)
                        .clickEvent(acceptEvent)
                        .hoverEvent(rewardInfo(reward)));
            } else {
                builder.append(text("OK", ACCEPT_BUTTON, ITALIC, BOLD, STRIKETHROUGH)
                        .hoverEvent(
                                showText(text("Du hast diesen Tipp bereits akzeptiert", ALREADY_ACCEPTED))));
            }
        }

        Component component = builder.build();

        send(player, component);
    }

    public static void tipList(Player player, List<Tip> tips, int page) {

        List<Component> components = Pagination.builder()
                .resultsPerPage(10)
                .build(text("Alle Tipps", LIST_HEADING), new Pagination.Renderer.RowRenderer<Tip>() {
                    @Override
                    public @NonNull Collection<Component> renderRow(@Nullable Tip tip, int index) {

                        if (tip == null) return Collections.singleton(empty());

                        return Collections.singleton(text()
                                .append(text((index + 1) + ". ", HIGHLIGHT))
                                .append(tipContent(tip))
                                .build());
                    }
                }, p -> PlayerCommands.listTipps() + " " + p)
                .render(tips, page);

        components.forEach(component -> send(player, component));
    }
}
