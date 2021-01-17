package de.raidcraft.rctips;

import de.raidcraft.rctips.commands.PlayerCommands;
import de.raidcraft.rctips.reward.CommandReward;
import de.raidcraft.rctips.reward.Reward;
import de.raidcraft.rctips.tip.Tip;
import de.raidcraft.rctips.util.CommandParser;
import de.raidcraft.rctips.util.UrlParser;
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
import java.util.regex.Pattern;

import static de.raidcraft.rctips.Messages.Colors.*;
import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.event.ClickEvent.*;
import static net.kyori.adventure.text.event.HoverEvent.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

public final class Messages {

    public static final class Colors {

        public static final TextColor PREFIX = LIGHT_PURPLE;
        public static final TextColor TIPP_TEXT = AQUA;
        public static final TextColor ACCEPT_BUTTON = RED;
        public static final TextColor REWARD_TOOLTIP_DESC = YELLOW;
        public static final TextColor REWARD_TOOLTIP_DESC_ACCENT = GREEN;
        public static final TextColor ACCEPT_MESSAGE = GREEN;
        public static final TextColor ALREADY_ACCEPTED = RED;
        public static final TextColor LIST_HEADING = DARK_AQUA;
        public static final TextColor HIGHLIGHT = AQUA;
    }

    private Messages() {}

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
                .append(text("nicht mehr anzuzeigen", REWARD_TOOLTIP_DESC));

        if(reward != null) {
            builder.append(newline()).append(newline());
            builder.append(
                    text("Erhalte als Belohnung ", REWARD_TOOLTIP_DESC_ACCENT))
                    .append(newline());
            builder.append(
                    text(reward.getDescription(), REWARD_TOOLTIP_DESC_ACCENT, BOLD));
        }

        return builder.build();
    }

    public static void tipAccepted(Player player, Tip tip) {

        Reward reward = tip.getReward();


        TextComponent.Builder builder = text();

        if(reward != null) {
            builder.append(text("Du hast als Tipp-Belohnung ", ACCEPT_MESSAGE));
            builder.append(text(tip.getReward().getDescription(), ACCEPT_MESSAGE, BOLD));
            builder.append(text(" erhalten", ACCEPT_MESSAGE));
        } else {
            builder.append(text("Du hast den Tipp akzeptiert"));
        }

        Component component = builder.build();
        send(player, component);
    }

    private static Component tipContent(Tip tip) {

        TextComponent.Builder builder = text();

        builder.append(text("Tipp: ", PREFIX));

        // TODO: Generic parsers

        // Detect URL
        UrlParser urlParser = new UrlParser(tip.getText());
        // Detect command
        CommandParser cmdParser = new CommandParser(tip.getText());

        if(urlParser.containsUrl()) {
            builder.append(text(urlParser.getPreUrl(), TIPP_TEXT))
                    .append(
                            text(urlParser.getUrl(), TIPP_TEXT, ITALIC, UNDERLINED)
                                    .hoverEvent(showText(text("Besuche " + urlParser.getUrl())))
                                    .clickEvent(openUrl(urlParser.getUrl())))
                    .append(text(urlParser.getPostUrl(), TIPP_TEXT));
        } else if(cmdParser.containsCommand()) {
            builder.append(text(cmdParser.getPreCommand(), TIPP_TEXT))
                    .append(
                            text(cmdParser.getCommand(), TIPP_TEXT, ITALIC, UNDERLINED)
                                    .hoverEvent(showText(
                                            text("Führe " + cmdParser.getCommand() + " aus")))
                                    .clickEvent(runCommand(cmdParser.getCommand())))
                    .append(text(cmdParser.getPostCommand(), TIPP_TEXT));
        } else {
            builder.append(text(tip.getText(), TIPP_TEXT));
        }

        builder.append(text(" "));

        return builder.build();
    }

    private static Component tipState(UUID player, Tip tip) {

        TextComponent.Builder builder = text();

        if(RCTips.instance().getTipManager().hasAccepted(tip, player)) {
            builder.append(text("\u2713", ACCEPT_MESSAGE, BOLD)
                .hoverEvent(
                    showText(text("Du hast diesen Tipp bereits akzeptiert", REWARD_TOOLTIP_DESC))));
        } else {
            builder.append(text("X", ALREADY_ACCEPTED, BOLD)
                .hoverEvent(
                    showText(text("Du hast diesen Tipp noch nicht akzeptiert", REWARD_TOOLTIP_DESC))));
        }

        return builder.build();
    }

    public static void tip(Player player, Tip tip) {

        TextComponent.Builder builder = text();

        // Append main tip content
        builder.append(tipContent(tip));

        Reward reward = tip.getReward();
        if(!RCTips.instance().getTipManager().hasAccepted(tip, player.getUniqueId())) {
            TextComponent textComponent = text("OK", ACCEPT_BUTTON, ITALIC, BOLD, UNDERLINED)
                    .hoverEvent(rewardInfo(reward));

            ClickEvent acceptEvent = runCommand(
                    PlayerCommands.acceptTip() + " " + tip.getId() + " " + player.getName());
            builder.append(textComponent.clickEvent(acceptEvent));
        } else {
            builder.append(text("OK", ACCEPT_BUTTON, ITALIC, BOLD, STRIKETHROUGH)
                    .hoverEvent(
                            showText(text("Du hast diesen Tipp bereits akzeptiert", ALREADY_ACCEPTED))));
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
                                .append(tipState(player.getUniqueId(), tip))
                                .build());
                    }
                }, p -> PlayerCommands.listTipps() + " " + p)
                .render(tips, page);

        components.forEach(component -> send(player, component));
    }
}
