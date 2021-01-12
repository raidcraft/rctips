package de.raidcraft.template.reward;

public abstract class AbstractReward implements Reward {

    private String description;

    public AbstractReward(String description) {
        this.description = description;
    }

    @Override
    public final String getDescription() {
        return description;
    }
}
