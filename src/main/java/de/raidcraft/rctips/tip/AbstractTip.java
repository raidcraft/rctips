package de.raidcraft.rctips.tip;

import de.raidcraft.rctips.reward.Reward;

public abstract class AbstractTip implements Tip {

    private final String id;
    private final int weight;
    private final String text;
    private final Reward reward;

    public AbstractTip(String id,int weight, String text, Reward reward) {

        this.id = id;
        this.weight = weight;
        this.text = text;
        this.reward = reward;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Reward getReward() {
        return reward;
    }
}
