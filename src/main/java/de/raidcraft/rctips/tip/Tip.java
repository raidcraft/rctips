package de.raidcraft.rctips.tip;

import de.raidcraft.rctips.reward.Reward;

public interface Tip {

    String getId();

    int getWeight();

    String getName();

    String getText();

    Reward getReward();
}
