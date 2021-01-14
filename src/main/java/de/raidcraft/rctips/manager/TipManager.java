package de.raidcraft.rctips.manager;

import de.raidcraft.rctips.Messages;
import de.raidcraft.rctips.PluginConfig;
import de.raidcraft.rctips.RCTips;
import de.raidcraft.rctips.reward.Reward;
import de.raidcraft.rctips.tables.TAcceptedTip;
import de.raidcraft.rctips.tip.Tip;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class TipManager implements Runnable {

    private final static int TICKS_PER_SECOND = 20;
    private static int SEC_TO_MS(int sec) { return sec * 1000; }

    private final RCTips plugin;
    private final Map<UUID, Long> lastTip = new HashMap<>();
    private final Map<UUID, Set<String>> acceptedTips = new HashMap<>();
    private final List<Tip> configuredTips = new ArrayList<>();

    public TipManager(RCTips plugin) {
        this.plugin = plugin;

        reload();

        // Start processing task asynchronously
        // due to:
        // https://www.spigotmc.org/threads/sending-chat-messages-asynchronously.207379/
        // it is safe to get online players and send chat messages in asynchronous tasks
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this,
                TICKS_PER_SECOND, TICKS_PER_SECOND);
    }

    public void reload() {

        // Clear caches
        lastTip.clear();
        acceptedTips.clear();
        configuredTips.clear();

        // Load tips from configuration
        configuredTips.addAll(plugin.getPluginConfig().getTips());
    }

    private Set<String> getAcceptedTipIds(UUID player) {

        if(!acceptedTips.containsKey(player)) {

            Set<String> acceptedTipIds = TAcceptedTip.getAcceptedTips(player);

            acceptedTips.put(player, acceptedTipIds);
        }

        return acceptedTips.get(player);
    }

    private boolean hasAccepted(Tip tip, UUID player) {

        Set<String> acceptedTipIds = getAcceptedTipIds(player);

        return acceptedTipIds.contains(tip.getId());
    }

    public int getLoadedTipCount() {

        return configuredTips.size();
    }

    public Tip getTip(String tipId) {

        for(Tip tip : configuredTips) {
            if(!tip.getId().equalsIgnoreCase(tipId)) continue;
            return tip;
        }

        return null;
    }

    public boolean acceptTip(Tip tip, Player player) {

        Set<String> acceptedTipIds = getAcceptedTipIds(player.getUniqueId());

        // Check if already accepted
        if(hasAccepted(tip, player.getUniqueId())) {
            return false;
        }

        // Save in database
        TAcceptedTip tAcceptedTip = new TAcceptedTip(player.getUniqueId(), tip.getId());
        tAcceptedTip.save();

        // Save in cache
        acceptedTipIds.add(tip.getId());

        // Credit reward to player
        Reward reward = tip.getReward();
        if(reward != null) {
            reward.credit(tip, player);
        }

        return true;
    }

    private Tip getNextTip(UUID player) {

        for(Tip tip : configuredTips) {
            if(hasAccepted(tip, player)) continue;
            return tip;
        }

        return null;
    }

    private void sendNextTip(Player player) {

        Tip tip = getNextTip(player.getUniqueId());

        if(tip == null) {
            plugin.getLogger().info("No tip left for " + player.getName());
            return;
        }

        lastTip.put(player.getUniqueId(), System.currentTimeMillis());

        Messages.tip(player, tip);
    }

    private boolean isTipTime(Player player) {

        // Add player to cache
        if(!lastTip.containsKey(player.getUniqueId())) {
            lastTip.put(player.getUniqueId(), 0L);
        }

        // Get time of last tip
        long playersLastTip = lastTip.get(player.getUniqueId());

        PluginConfig pluginConfig = plugin.getPluginConfig();

        // Calculate next tip delay
        int delaySeconds = pluginConfig.getMinimumTipDelay() +
                pluginConfig.getAdditionalAcceptedTipDelay() * TAcceptedTip.getAcceptedTipCount(player.getUniqueId());

        // Upper limit
        if(delaySeconds > pluginConfig.getMaximumTipDelay()) {
            delaySeconds = pluginConfig.getMaximumTipDelay();
        }

        plugin.getLogger().info("Tip delay for " + player.getName() + ": " + delaySeconds + "s");

        // Check if tip delay not reached
        if(System.currentTimeMillis() - playersLastTip < SEC_TO_MS(delaySeconds)) {
            return false;
        }

        return true;
    }

    @Override
    public void run() {

        for(Player player : Bukkit.getOnlinePlayers()) {

            if(!isTipTime(player)) {
                continue;
            }

            sendNextTip(player);
        }
    }
}
