package de.raidcraft.template;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TipManager implements Runnable {

    private final static int TICKS_PER_SECOND = 20;
    private static int SEC_TO_MS(int sec) { return sec * 1000; }

    private final RCTips plugin;
    private final Map<UUID, Long> lastTip = new HashMap<>();

    public TipManager(RCTips plugin) {
        this.plugin = plugin;

        // Start processing task asynchronously
        // due to:
        // https://www.spigotmc.org/threads/sending-chat-messages-asynchronously.207379/
        // it is safe to get online players and send chat messages in asynchronous tasks
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this,
                TICKS_PER_SECOND, TICKS_PER_SECOND);
    }

    private void sendNextTip(Player player) {

        // TODO
    }

    private boolean isTipTime(Player player) {

        // Add player to cache
        if(!lastTip.containsKey(player.getUniqueId())) {
            lastTip.put(player.getUniqueId(), 0L);
        }

        // Get time of last tip
        long playersLastTip = lastTip.get(player.getUniqueId());

        // Check if minimum tip delay not reached
        if(System.currentTimeMillis() - playersLastTip < SEC_TO_MS(plugin.getPluginConfig().getMinimumTipDelay())) {
            return false;
        }

        // TODO
    }

    @Override
    public void run() {

        for(Player player : Bukkit.getOnlinePlayers()) {

            if(!isTipTime(player)) continue;

            sendNextTip(player);
        }
    }
}
