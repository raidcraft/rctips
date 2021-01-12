package de.raidcraft.template.reward;

import de.raidcraft.template.tip.Tip;
import org.bukkit.entity.Player;

public interface Reward {

    String getDescription();

    void credit(Tip tip, Player player);
}
