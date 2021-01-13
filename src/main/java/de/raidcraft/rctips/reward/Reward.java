package de.raidcraft.rctips.reward;

import de.raidcraft.rctips.tip.Tip;
import org.bukkit.entity.Player;

public interface Reward {

    String getDescription();

    void credit(Tip tip, Player player);
}
