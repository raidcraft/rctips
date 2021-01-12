package de.raidcraft.template.tip;

import de.raidcraft.template.reward.Reward;

public abstract class AbstractTip implements Tip {

    private final String id;
    private final String name;
    private final String text;
    private final Reward reward;

    public AbstractTip(String id, String name, String text, Reward reward) {

        this.id = id;
        this.name = name;
        this.text = text;
        this.reward = reward;
    }

    @Override
    public String getId() {

        return id;
    }

    @Override
    public String getName() {
        return name;
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
